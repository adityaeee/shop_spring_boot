package com.aditya.shop.service.impl;

import com.aditya.shop.dto.request.PaymentCustomerRequest;
import com.aditya.shop.dto.request.PaymentDetailRequest;
import com.aditya.shop.dto.request.PaymentItemDetailRequest;
import com.aditya.shop.dto.request.PaymentRequest;
import com.aditya.shop.entity.Payment;
import com.aditya.shop.entity.Transaction;
import com.aditya.shop.repository.PaymentRepository;
import com.aditya.shop.service.PaymentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final RestClient restClient;
    
    private final String SECRET_KEY;
    private final String BASE_URL_SNAP;

    @Autowired
    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            RestClient restClient,
            @Value("${midtrans.api.key}") String SECRET_KEY,
            @Value("${midtrans.api.snap-url}") String BASE_URL_SNAP
    ) {
        this.paymentRepository = paymentRepository;
        this.restClient = restClient;
        this.SECRET_KEY = SECRET_KEY;
        this. BASE_URL_SNAP = BASE_URL_SNAP;
    }

    @Transactional(rollbackOn = Exception.class)

    @Override
    public Payment createPayment(Transaction transaction) {

        Long amount = transaction.getTransactionDetails().stream()
                .mapToLong(trxDetail -> (trxDetail.getQty() * trxDetail.getProductPrice()))
                .reduce(0, Long::sum);
//              .reduce(0,(((left, right) -> left + right)));

        PaymentDetailRequest paymentDetailRequest = PaymentDetailRequest.builder()
                .amount(amount)
                .orderId(transaction.getId())
                .build();

        List<PaymentItemDetailRequest> paymentItemDetailRequests = transaction.getTransactionDetails().stream()
                .map(trxDetail -> PaymentItemDetailRequest.builder()
                        .name(trxDetail.getProduct().getName())
                        .price(trxDetail.getProductPrice())
                        .quantity(trxDetail.getQty())
                        .build()
                ).toList();

        PaymentCustomerRequest paymentCustomerRequest = PaymentCustomerRequest.builder()
                .name(transaction.getCustomer() != null ? transaction.getCustomer().getName() : "Guest")
                .build();

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .paymentDetail(paymentDetailRequest)
                .paymentItemDetails(paymentItemDetailRequests)
                .paymentMethod(List.of("gopay", "shopeepay"))
                .customer(paymentCustomerRequest)
                .build();

//        kirim apa kebutuhan dari midtrans
        ResponseEntity<Map<String, String>> responseFromMidTrans = restClient.post()
                .uri(BASE_URL_SNAP)
                .body(paymentRequest)
                //Authorization: Basic AUTH_STRING
                .header(HttpHeaders.AUTHORIZATION, "Basic " + SECRET_KEY)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {});

        // ambil data from response midtrans
        Map<String, String> body = responseFromMidTrans.getBody();

        if (body == null) {
            return null;
        }

        Payment payment = Payment.builder()
                .token(body.get("token"))
                .redirectUrl(body.get("redirect_url"))
                .transactionStatus("ordered")
                .build();

        paymentRepository.saveAndFlush(payment);

        return payment;
    }
}
