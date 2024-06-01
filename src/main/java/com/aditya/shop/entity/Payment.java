package com.aditya.shop.entity;

import com.aditya.shop.constant.ConstantTable;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = ConstantTable.PAYMENT)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "redirect_url", nullable = false)
    private String redirectUrl;

    @Column(name = "transaction_status", nullable = false)
    private String transactionStatus;
//    setllement
//    deny
//    pending
//    cancel
//    expire
//    failure

}
