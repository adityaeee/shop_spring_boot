package com.aditya.shop.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Product {
    private String id;
    private String name;
    private Long price;
    private Integer stock;
}
