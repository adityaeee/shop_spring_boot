package com.aditya.shop.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentItemDetailRequest {

	@JsonProperty("quantity")
	private Integer quantity;

	@JsonProperty("price")
	private Long price;

	@JsonProperty("name")
	private String name;

}