package com.aditya.shop.service;

import com.aditya.shop.entity.UserAccount;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserAccount getByUserId(String id);

    UserAccount getByContent();
}
