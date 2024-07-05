package com.aditya.shop.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDetailRequest {

	@JsonProperty("gross_amount")
	private Long amount;

	@JsonProperty("order_id")
	private String orderId;

}