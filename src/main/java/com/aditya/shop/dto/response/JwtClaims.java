package com.aditya.shop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtClaims {
    private String userAccountId;
    private List<String> roles;
}


//{
// "sub": "dfew3r2-23423-userAccoundId",
// "roles" : [
//		"ROLE_CUSTOMER"
//	]
