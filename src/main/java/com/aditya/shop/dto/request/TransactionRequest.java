package com.aditya.shop.dto.request;

import lombok.*;

import java.util.List;

@Data
@Builder
public class TransactionRequest {
    private String customerId;
    private List<TransactionDetailRequest> transactionDetails;
}
