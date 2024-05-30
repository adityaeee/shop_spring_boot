package com.aditya.shop.dto.response;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class TransactionResponse {
    private String id;
//    private String customerId;
    private Date transDate;
    private List<TransactionDetailResponse> transactionDetails;

    private CustomerResponse customer;
    private PaymentResponse payment;

}
