package com.aditya.shop.service.impl;

import com.aditya.shop.constant.UserRole;
import com.aditya.shop.dto.request.AuthRequest;
import com.aditya.shop.dto.response.LoginResponse;
import com.aditya.shop.dto.response.RegisterResponse;
import com.aditya.shop.entity.Customer;
import com.aditya.shop.entity.Role;
import com.aditya.shop.entity.UserAccount;
import com.aditya.shop.repository.UserAccountRepository;
import com.aditya.shop.service.AuthService;
import com.aditya.shop.service.CustomerService;
import com.aditya.shop.service.JwtService;
import com.aditya.shop.service.RoleService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserAccountRepository userAccountRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;
//    jadikan bean
    private final AuthenticationManager authenticationManager;

//    service untuk claims token
    private final JwtService jwtService;

    @Value("${shop.superadmin.username}")
    private String superAdminUsername;

    @Value("${shop.superadmin.password}")
    private String superAdminPassword;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    public void initSuperAdmin(){
        Optional<UserAccount> currentUser = userAccountRepository.findByUsername(superAdminUsername);
        if (currentUser.isPresent()) {
            return; //kalau ada di return aja
        }
        Role superAdmin = roleService.getOrSave(UserRole.ROLE_SUPER_ADMIN);
        Role admin = roleService.getOrSave(UserRole.ROLE_ADMIN);
        Role customer = roleService.getOrSave(UserRole.ROLE_CUSTOMER);

        UserAccount account = UserAccount.builder()
                .username(superAdminUsername)
                .password(passwordEncoder.encode(superAdminPassword))
                .role(List.of(superAdmin, admin, customer))
                .isEnable(true)
                .build();
        userAccountRepository.save(account);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse register(AuthRequest request) {
        Role role = roleService.getOrSave(UserRole.ROLE_CUSTOMER);

        String hasPassword = passwordEncoder.encode(request.getPassword());

        UserAccount userAccount = UserAccount.builder()
                .username(request.getUsername())
                .password(hasPassword)
                .role(List.of(role))
                .isEnable(true)
                .build();

        userAccountRepository.saveAndFlush(userAccount);

        Customer customer = Customer.builder()
                .status(true)
                .userAccount(userAccount)
                .build();
        customerService.create(customer);

        return RegisterResponse.builder()
                .username(userAccount.getUsername())
                .roles(userAccount.getAuthorities().stream().map
                        (GrantedAuthority::getAuthority).toList())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse registerAdmin(AuthRequest request) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public LoginResponse login(AuthRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );
        Authentication authenticated = authenticationManager.authenticate(authentication);

        UserAccount userAccount = (UserAccount) authenticated.getPrincipal();
        String token = jwtService.generateToken(userAccount);
        return LoginResponse.builder()
                .token(token)
                .username(userAccount.getUsername())
                .roles(userAccount.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
    }
}
