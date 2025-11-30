package com.food.ordering.system.domain.vo;

import java.util.UUID;


public class OrderId extends BaseId<UUID> {
    protected OrderId(UUID value) {
        super(value);
    }
}
