package com.aditya.shop.repository;

import com.aditya.shop.constant.UserRole;
import com.aditya.shop.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRole(UserRole role);
}
