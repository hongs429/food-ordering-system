package com.food.ordering.system.domain.entity;

import lombok.EqualsAndHashCode;


@EqualsAndHashCode
public abstract class BaseEntity<ID> {
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }


}
