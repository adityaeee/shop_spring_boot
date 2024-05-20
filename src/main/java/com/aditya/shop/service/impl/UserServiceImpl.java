package com.aditya.shop.service.impl;

import com.aditya.shop.entity.UserAccount;
import com.aditya.shop.repository.UserAccountRepository;
import com.aditya.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    @Override
    // bukan kita yg manggil
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public UserAccount getByUserId(String id) {
        return userAccountRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found")
        );

    }
}
