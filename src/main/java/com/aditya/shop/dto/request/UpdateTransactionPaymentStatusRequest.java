package com.aditya.shop.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTransactionPaymentStatusRequest {
    private String orderId;
    private String transactionStatus;
}