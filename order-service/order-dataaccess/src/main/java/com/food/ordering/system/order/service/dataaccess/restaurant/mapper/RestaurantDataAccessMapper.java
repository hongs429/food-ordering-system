package com.food.ordering.system.order.service.dataaccess.restaurant.mapper;


import com.food.ordering.system.domain.vo.Money;
import com.food.ordering.system.domain.vo.ProductId;
import com.food.ordering.system.domain.vo.RestaurantId;
import com.food.ordering.system.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.order.service.dataaccess.restaurant.exception.RestaurantDataAccessException;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class RestaurantDataAccessMapper {

    public List<UUID> restaurantToRestaurantProductIds(Restaurant restaurant) {
        return restaurant.getProducts().stream()
                .map(product -> product.getId().getValue())
                .collect(Collectors.toList());
    }

    public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {
        RestaurantEntity restaurantEntity =
                restaurantEntities.stream().findFirst().orElseThrow(() ->
                        new RestaurantDataAccessException("Restaurant could not be found!"));

        List<Product> restaurantProducts = restaurantEntities.stream()
                .map(entity ->
                        new Product(
                                new ProductId(entity.getProductId()),
                                entity.getProductName(),
                                new Money(entity.getProductPrice())
                        )
                )
                .toList();

        return Restaurant.Builder.builder()
                .id(new RestaurantId(restaurantEntity.getRestaurantId()))
                .products(restaurantProducts)
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }
}
