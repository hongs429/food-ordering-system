package com.food.ordering.system.domain.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public abstract class BaseEntity<ID> {
    private ID id;

    protected BaseEntity() {
    }
}
