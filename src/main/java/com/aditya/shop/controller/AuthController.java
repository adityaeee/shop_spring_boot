package com.aditya.shop.controller;

import com.aditya.shop.constant.APIUrl;
import com.aditya.shop.constant.ResponseMessage;
import com.aditya.shop.dto.request.AuthRequest;
import com.aditya.shop.dto.response.CommonResponse;
import com.aditya.shop.dto.response.LoginResponse;
import com.aditya.shop.dto.response.RegisterResponse;
import com.aditya.shop.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = APIUrl.AUTH_API)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Transactional(rollbackFor = Exception.class)
    @PostMapping(path = "/register")
    public ResponseEntity<CommonResponse<?>> registerUser(@RequestBody AuthRequest request) {
        RegisterResponse register =  authService.register((request));

        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(register)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<CommonResponse<?>> login(@RequestBody AuthRequest request) {
        LoginResponse loginResponse = authService.login(request);

        CommonResponse<LoginResponse> response = CommonResponse.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Login Successfully")
                .data(loginResponse)
                .build();

        return ResponseEntity.ok(response);
    }
}
