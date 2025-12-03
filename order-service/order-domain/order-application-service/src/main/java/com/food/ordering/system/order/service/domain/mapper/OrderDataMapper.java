package com.food.ordering.system.order.service.domain.mapper;


import com.food.ordering.system.domain.vo.CustomerId;
import com.food.ordering.system.domain.vo.Money;
import com.food.ordering.system.domain.vo.ProductId;
import com.food.ordering.system.domain.vo.RestaurantId;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Order.OrderBuilder;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.vo.StreetAddress;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OrderDataMapper {

    public Restaurant toRestaurant(CreateOrderCommand createOrderCommand) {
        return Restaurant.Builder.builder()
                .id(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(createOrderCommand.getItems().stream()
                        .map(orderItem -> new Product(new ProductId(orderItem.getProductId())))
                        .collect(Collectors.toList())
                )
                .build();
    }

    public Order toOrder(CreateOrderCommand createOrderCommand) {
        return OrderBuilder.builder()
                .customerId(new CustomerId(createOrderCommand.getCustomerId()))
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.getAddress()))
                .price(new Money(createOrderCommand.getPrice()))
                .items(orderItemsToOrderItemsEntities(createOrderCommand.getItems()))
                .build();
    }

    public CreateOrderResponse toCreateOrderResponse(Order order) {
        return CreateOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .message("")
                .build();
    }

    private List<OrderItem> orderItemsToOrderItemsEntities(
            List<com.food.ordering.system.order.service.domain.dto.create.OrderItem> items) {

        return items.stream()
                .map(orderItem -> OrderItem.Builder.builder()
                        .product(new Product(new ProductId(orderItem.getProductId())))
                        .price(new Money(orderItem.getPrice()))
                        .quantity(orderItem.getQuantity())
                        .subTotal(new Money(orderItem.getSubTotal()))
                        .build())
                .toList();
    }

    private StreetAddress orderAddressToStreetAddress(OrderAddress address) {
        return new StreetAddress(UUID.randomUUID(), address.getStreet(), address.getPostalCode(), address.getCity());
    }
}
