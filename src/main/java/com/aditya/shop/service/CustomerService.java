package com.aditya.shop.service;

import com.aditya.shop.dto.request.SearchCustomerRequest;
import com.aditya.shop.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer create(Customer customer);
    Customer getById(String id);
    List<Customer> getAll(SearchCustomerRequest customerRequest);
    Customer update(Customer customer);
    void delete(String id);

    void updateStatusById(String id, Boolean status);
}
