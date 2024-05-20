package com.aditya.shop.service;

import com.aditya.shop.dto.request.AuthRequest;
import com.aditya.shop.dto.response.LoginResponse;
import com.aditya.shop.dto.response.RegisterResponse;
import jdk.jfr.Frequency;

public interface AuthService {
    RegisterResponse register(AuthRequest request);
    RegisterResponse registerAdmin(AuthRequest request);
    LoginResponse login(AuthRequest request);
}
