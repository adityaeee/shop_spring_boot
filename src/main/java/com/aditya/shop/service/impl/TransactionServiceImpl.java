package com.aditya.shop.service.impl;

import com.aditya.shop.dto.request.TransactionRequest;
import com.aditya.shop.dto.request.UpdateTransactionPaymentStatusRequest;
import com.aditya.shop.dto.response.CustomerResponse;
import com.aditya.shop.dto.response.PaymentResponse;
import com.aditya.shop.dto.response.TransactionDetailResponse;
import com.aditya.shop.dto.response.TransactionResponse;
import com.aditya.shop.entity.*;
import com.aditya.shop.repository.TransactionRepository;
import com.aditya.shop.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final TransactionDetailService transactionDetailService;
    private final CustomerService customerService;
    private final ProductService productService;

    private final PaymentService paymentService;

    @Transactional(rollbackOn = Exception.class) // ini akan auto commit, dan akan dirollback jika ada Exception
    @Override
    public TransactionResponse create(TransactionRequest request) {

//       cari customernya dan validasi
        Customer customer = customerService.getById(request.getCustomerId());

//        1.Save ke transaction
        Transaction trx = Transaction.builder()
                .customer(customer)
                .transDate(new Date())
                .build();
        transactionRepository.saveAndFlush(trx);


//        2.Save ke transactionDetail
        List<TransactionDetail> trxDetail = request.getTransactionDetails().stream()
                .map(detailRequest -> {
                    Product product = productService.getById(detailRequest.getProductId());

                    if (product.getStock() - detailRequest.getQty() < 0) {
                        throw new RuntimeException("Out of Stock");
                    }

                    product.setStock(product.getStock() - detailRequest.getQty());

                    return TransactionDetail.builder()
                            .transaction(trx)
                            .product(product)
                            .productPrice(product.getPrice())
                            .qty(detailRequest.getQty())
                            .build();
                }).toList();
        transactionDetailService.createBulk(trxDetail);
        trx.setTransactionDetails(trxDetail);


//        3.Return datanya
        List<TransactionDetailResponse> trxDetailResponse = trxDetail.stream()
                .map(detail -> {
                    return TransactionDetailResponse.builder()
                            .id(detail.getId())
                            .productId(detail.getProduct().getId())
                            .productPrice(detail.getProductPrice())
                            .quantity(detail.getQty())
                            .build();
                }).toList();

        Payment payment = paymentService.createPayment(trx);
        trx.setPayment(payment);

        CustomerResponse customerResponse = CustomerResponse.builder()
                .id(trx.getCustomer().getId())
                .name(trx.getCustomer().getName())
                .address(trx.getCustomer().getAddress())
                .birthDate(trx.getCustomer().getBirthDate())
                .mobilePhoneNo(trx.getCustomer().getMobilePhoneNo())
                .status(trx.getCustomer().getStatus())
                .userAccount(trx.getCustomer().getUserAccount())
                .build();

        PaymentResponse paymentResponse = PaymentResponse.builder()
                .id(payment.getId())
                .token(payment.getToken())
                .redirectUrl(payment.getRedirectUrl())
                .transactionStatus(payment.getTransactionStatus())
                .build();

        return TransactionResponse.builder()
                .id(trx.getId())
                .transDate(trx.getTransDate())
                .transactionDetails(trxDetailResponse)
                .customer(customerResponse)
                .payment(paymentResponse)
                .build();
    }

    @Override
    public List<TransactionResponse> getAll() {

        List<Transaction> transactions = transactionRepository.findAll();

        return transactions.stream()
                .map(transaction -> {
                    List<TransactionDetailResponse> transDetailResponse = transaction.getTransactionDetails().stream()
                            .map(detail -> {
                                return TransactionDetailResponse.builder()
                                        .id(detail.getId())
                                        .productId(detail.getProduct().getId())
                                        .productPrice(detail.getProductPrice())
                                        .quantity(detail.getQty())
                                        .build();
                            }).toList();


                  return TransactionResponse.builder()
                          .id(transaction.getId())
//                          .customerId(transaction.getCustomer().getId())
                          .transDate(transaction.getTransDate())
                          .transactionDetails(transDetailResponse)
                          .build();

                }).toList();
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void updateStatus(UpdateTransactionPaymentStatusRequest updateStatusRequest) {
        Transaction transaction = transactionRepository.findById(updateStatusRequest.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction Not Found"));

        Payment payment = transaction.getPayment();
        payment.setTransactionStatus(updateStatusRequest.getTransactionStatus());


    }


}
