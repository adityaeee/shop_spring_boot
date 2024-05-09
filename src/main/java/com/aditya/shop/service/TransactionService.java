package com.aditya.shop.service;

import com.aditya.shop.dto.request.TransactionRequest;
import com.aditya.shop.dto.response.TransactionResponse;
import com.aditya.shop.entity.Transaction;

public interface TransactionService {
    TransactionResponse create(TransactionRequest request);
}
