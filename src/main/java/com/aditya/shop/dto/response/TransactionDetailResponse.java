package com.aditya.shop.dto.response;

import lombok.*;

@Data
@Builder
public class TransactionDetailResponse {
    private String id;
    private String productId;
    private Long productPrice;
    private Integer quantity;
}
