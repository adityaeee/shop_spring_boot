package com.aditya.shop.service;

import com.aditya.shop.entity.TransactionDetail;

import java.util.List;

public interface TransactionDetailService {
    List<TransactionDetail> createBulk (List<TransactionDetail> transactionDetails);
}
