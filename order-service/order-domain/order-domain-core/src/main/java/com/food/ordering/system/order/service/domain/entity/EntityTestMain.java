package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.vo.Money;
import com.food.ordering.system.domain.vo.ProductId;
import com.food.ordering.system.order.service.domain.entity.OrderItem.Builder;
import com.food.ordering.system.order.service.domain.vo.OrderItemId;
import java.math.BigDecimal;
import java.util.UUID;

public class EntityTestMain {

    public static void main(String[] args) {
        ProductId productId = new ProductId(UUID.randomUUID());
        Product product = new Product(productId, "물건", new Money(BigDecimal.ZERO));
        System.out.println(product.getId());  // ProductId 출력됨!

        OrderItem orderItem = Builder.builder()
                .id(new OrderItemId(1L))
                .product(product)
                .quantity(1)
                .price(new Money(BigDecimal.ZERO))
                .subTotal(new Money(BigDecimal.ZERO))
                .build();


    }
}
