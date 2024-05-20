package com.aditya.shop.service.impl;

import com.aditya.shop.constant.UserRole;
import com.aditya.shop.entity.Role;
import com.aditya.shop.repository.RoleRepository;
import com.aditya.shop.service.RoleService;
import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleSeriveImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getOrSave(UserRole role) {
        return roleRepository.findByRole(role)
                .orElseGet(()-> roleRepository.saveAndFlush(Role.builder().role(role).build()));
    }
}
