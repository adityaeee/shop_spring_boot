package com.aditya.shop.service.impl;

import com.aditya.shop.dto.request.TransactionRequest;
import com.aditya.shop.dto.response.TransactionDetailResponse;
import com.aditya.shop.dto.response.TransactionResponse;
import com.aditya.shop.entity.Customer;
import com.aditya.shop.entity.Product;
import com.aditya.shop.entity.Transaction;
import com.aditya.shop.entity.TransactionDetail;
import com.aditya.shop.repository.TransactionRepository;
import com.aditya.shop.service.CustomerService;
import com.aditya.shop.service.ProductService;
import com.aditya.shop.service.TransactionDetailService;
import com.aditya.shop.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionDetailService transactionDetailService;
    private final CustomerService customerService;
    private final ProductService productService;


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

        return TransactionResponse.builder()
                .id(trx.getId())
                .customerId(trx.getCustomer().getId())
                .transDate(trx.getTransDate())
                .transactionDetails(trxDetailResponse)
                .build();
    }
}
