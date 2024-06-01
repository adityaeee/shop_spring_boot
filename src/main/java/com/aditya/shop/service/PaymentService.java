package com.aditya.shop.service;

import com.aditya.shop.entity.Payment;
import com.aditya.shop.entity.Transaction;

public interface PaymentService {
    Payment createPayment(Transaction transaction);
}
