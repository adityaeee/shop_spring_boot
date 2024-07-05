package com.aditya.shop.dto.request;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {

		@JsonProperty("transaction_details")
		private PaymentDetailRequest paymentDetail;

		@JsonProperty("item_details")
		private List<PaymentItemDetailRequest> paymentItemDetails;

		@JsonProperty("enabled_payments")
		private List<String> paymentMethod;
		// ini nanti kita enggak izinkan semua metode payment, yg kita izinkan hanay gopay dan shopeepay

		@JsonProperty("customer_details")
		private PaymentCustomerRequest customer;
}
