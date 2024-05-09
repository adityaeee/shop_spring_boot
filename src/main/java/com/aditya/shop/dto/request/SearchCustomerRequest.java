package com.aditya.shop.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder //memungkinkan untuk membuat objek dari class ini tanpa harus instance
public class SearchCustomerRequest {
    private String name;
    private String phone;
    private String birthDate;
    private Boolean status;
}
