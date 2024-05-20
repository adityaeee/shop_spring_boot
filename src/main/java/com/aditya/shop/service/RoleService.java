package com.aditya.shop.service;

import com.aditya.shop.constant.UserRole;
import com.aditya.shop.entity.Role;

public interface RoleService {
    Role getOrSave (UserRole role);
}
