package com.food.ordering.system.order.service.dataaccess.restaurant.entity;


import java.math.BigDecimal;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"restaurantId", "productId"})
@IdClass(RestaurantEntityId.class)
@Entity
@Table(name = "order_restaurant_m_view", schema = "restaurant")
public class RestaurantEntity {
    @Id
    private UUID restaurantId;
    @Id
    private UUID productId;

    private String restaurantName;
    private Boolean restaurantActive;
    private String productName;
    private BigDecimal productPrice;
    private Boolean productAvailable;
}
