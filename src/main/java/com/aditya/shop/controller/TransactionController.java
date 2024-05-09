package com.aditya.shop.controller;

import com.aditya.shop.constant.APIUrl;
import com.aditya.shop.dto.request.TransactionRequest;
import com.aditya.shop.dto.response.TransactionResponse;
import com.aditya.shop.entity.Transaction;
import com.aditya.shop.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.TRANSACTION)
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public TransactionResponse createNewTransaction(
            @RequestBody TransactionRequest request
    ) {
       return transactionService.create(request);
    }

    @GetMapping
    public List<TransactionResponse> getAllTransaction(){
        return transactionService.getAll();
    };

}
