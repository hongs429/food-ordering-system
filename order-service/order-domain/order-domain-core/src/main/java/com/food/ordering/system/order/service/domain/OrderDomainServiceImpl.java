package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.vo.ProductId;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreateEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

    private static final ZonedDateTime UTC_NOW_TIME = ZonedDateTime.now(ZoneId.of("UTC"));

    @Override
    public OrderCreateEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with id: {} is initiated", order.getId());
        return new OrderCreateEvent(order, UTC_NOW_TIME);
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        Map<ProductId, OrderItem> orderItemToMap = order.getItems().stream().collect(Collectors.toMap(
                orderItem -> orderItem.getProduct().getId(), Function.identity()
        ));

        restaurant.getProducts().forEach(orderItem -> {
            if (orderItemToMap.containsKey(orderItem.getId())) {
                OrderItem selectedOrderItem = orderItemToMap.get(orderItem.getId());
                selectedOrderItem.getProduct().updateWithConfirmedNameAndPrice(orderItem.getName(), orderItem.getPrice());
            }
        });
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException("Restaurant is not active. restaurant id: " + restaurant.getId());
        }
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("Order with id: {} has been paid", order.getId());
        return new OrderPaidEvent(order, UTC_NOW_TIME);
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with id: {} has been approved", order.getId());

    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        log.info("Order payment is cancelling for order with id: {}", order.getId());
        return new OrderCancelledEvent(order, UTC_NOW_TIME);
    }

    @Override
    public void cancelOrder(Order order) {
        order.cancel();
        log.info("Order with id: {} has been cancelled", order.getId());

    }
}
