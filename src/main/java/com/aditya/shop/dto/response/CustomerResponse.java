package com.aditya.shop.dto.response;

import com.aditya.shop.entity.UserAccount;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CustomerResponse {
    private String id;
    private String name;
    private String mobilePhoneNo;
    private String address;
    private Date birthDate;
    private Boolean status;
    private UserAccount userAccount;
}
