package com.aditya.shop.service;

import com.aditya.shop.dto.request.TransactionRequest;
import com.aditya.shop.dto.request.UpdateTransactionPaymentStatusRequest;
import com.aditya.shop.dto.response.TransactionResponse;
import com.aditya.shop.entity.Transaction;

import java.util.List;

public interface TransactionService {
    TransactionResponse create(TransactionRequest request);

    List<TransactionResponse> getAll();

    void updateStatus (UpdateTransactionPaymentStatusRequest updateStatusRequest);
}
