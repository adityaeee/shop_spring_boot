package com.aditya.shop.controller;

import com.aditya.shop.constant.APIUrl;
import com.aditya.shop.dto.request.TransactionRequest;
import com.aditya.shop.dto.request.UpdateTransactionPaymentStatusRequest;
import com.aditya.shop.dto.response.CommonResponse;
import com.aditya.shop.dto.response.TransactionResponse;
import com.aditya.shop.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.TRANSACTION)
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<CommonResponse<TransactionResponse>> createNewTransaction(
            @RequestBody TransactionRequest request
    ) {
        // logic untuk manggil service
        TransactionResponse transaction = transactionService.create(request);
        CommonResponse<TransactionResponse> response = CommonResponse.<TransactionResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Transaction Success")
                .data(transaction)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<TransactionResponse> getAllTransaction(){
        return transactionService.getAll();
    };

//    "/api/v1/transactions/status"
    @PostMapping(path = "/status")
    public ResponseEntity<CommonResponse<?>> updateStatus (
            @RequestBody Map<String, Object> request
            ) {

        UpdateTransactionPaymentStatusRequest updateStatus = UpdateTransactionPaymentStatusRequest.builder()
                .orderId(request.get("order_id").toString())
                .transactionStatus(request.get("transaction_status").toString())
                .build();

        transactionService.updateStatus(updateStatus);

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully update status")
                        .build()

        );
    }


}
