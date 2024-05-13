package com.aditya.shop.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
//@NoArgsConstructor
@Builder
public class ProductResponse {
    private String name;
    private Long price;
}
