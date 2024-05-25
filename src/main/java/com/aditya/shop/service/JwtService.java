package com.aditya.shop.service;

import com.aditya.shop.dto.response.JwtClaims;
import com.aditya.shop.entity.UserAccount;

public interface JwtService {
    String generateToken(UserAccount userAccount);

    boolean verifyJwtToken(String token);

    JwtClaims getClaimsByToken (String token);

}
