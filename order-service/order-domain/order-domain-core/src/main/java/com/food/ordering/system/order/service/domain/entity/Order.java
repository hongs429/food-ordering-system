package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.vo.CustomerId;
import com.food.ordering.system.domain.vo.Money;
import com.food.ordering.system.domain.vo.OrderId;
import com.food.ordering.system.domain.vo.OrderStatus;
import com.food.ordering.system.domain.vo.RestaurantId;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.vo.OrderItemId;
import com.food.ordering.system.order.service.domain.vo.StreetAddress;
import com.food.ordering.system.order.service.domain.vo.TrackingId;
import java.util.List;
import java.util.UUID;
import lombok.Getter;


@Getter
public class Order extends AggregateRoot<OrderId> {
    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;

    // 비지니스로직에서 정의되므로 final 이지 않다.
    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    public void initializeOrder() {
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }

    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    public void pay() {
        if (orderStatus != OrderStatus.PENDING) {
            throw new OrderDomainException("Order is not in correct state for pay operation");
        }
        orderStatus = OrderStatus.PAID;
    }

    public void approve() {
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for approve operation");
        }
        orderStatus = OrderStatus.APPROVED;
    }

    public void initCancel(List<String> failureMessages) {
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for cancel operation");
        }
        orderStatus = OrderStatus.CANCELLED;

        updateFailureMessage(failureMessages);
    }

    private void updateFailureMessage(List<String> failureMessages) {
        List<String> processedFailureMessages = failureMessages.stream()
                .filter(failureMessage -> !failureMessage.isEmpty())
                .toList();

        if (failureMessages != null && !failureMessages.isEmpty()) {
            this.failureMessages.addAll(processedFailureMessages);
        }
        if (this.failureMessages.isEmpty()) {
            this.failureMessages = processedFailureMessages;
        }
    }

    public void cancel() {
        if (!(orderStatus == OrderStatus.CANCELLING || orderStatus == OrderStatus.PENDING)) {
            throw new OrderDomainException("Order is not in correct state for cancel operation");
        }
        orderStatus = OrderStatus.CANCELLED;
    }

    private void validateItemsPrice() {
        Money orderItemsTotal = items.stream().map(orderItem -> {
            validateItemPrice(orderItem);
            return orderItem.getSubTotal();
        }).reduce(Money.ZERO, Money::add);

        if (!price.equals(orderItemsTotal)) {
            throw new OrderDomainException("Total price: " + price.getAmount() + " is not equal to price for items: "
                    + orderItemsTotal.getAmount());
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        if (!orderItem.isPriceValid()) {
            throw new OrderDomainException(
                    "Order item price: " + orderItem.getPrice().getAmount() + "is not valid for product "
                            + orderItem.getProduct().getId().getValue());
        }
    }

    private void validateTotalPrice() {
        if (price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price must be greater than zero");
        }
    }

    private void validateInitialOrder() {
        if (orderStatus != null || getId() != null) {
            throw new OrderDomainException("Order is not in correct state for initialization!");
        }
    }

    private void initializeOrderItems() {
        long itemId = 1;
        for (OrderItem orderItem : items) {
            orderItem.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
        }
    }

    private Order(OrderBuilder orderBuilder) {
        super.setId(orderBuilder.id);
        customerId = orderBuilder.customerId;
        restaurantId = orderBuilder.restaurantId;
        deliveryAddress = orderBuilder.deliveryAddress;
        price = orderBuilder.price;
        items = orderBuilder.items;
        trackingId = orderBuilder.trackingId;
        orderStatus = orderBuilder.orderStatus;
        failureMessages = orderBuilder.failureMessages;
    }


    public static final class OrderBuilder {
        private OrderId id;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private OrderBuilder() {
        }

        public static OrderBuilder builder() {
            return new OrderBuilder();
        }

        public OrderBuilder id(OrderId val) {
            id = val;
            return this;
        }

        public OrderBuilder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public OrderBuilder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public OrderBuilder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public OrderBuilder price(Money val) {
            price = val;
            return this;
        }

        public OrderBuilder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public OrderBuilder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public OrderBuilder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public OrderBuilder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
