package com.aditya.shop.service.impl;

import com.aditya.shop.entity.Customer;
import com.aditya.shop.repository.CustomerRepository;
import com.aditya.shop.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer create(Customer customer) {
        customerRepository.saveAndFlush(customer);
        return customer;
    }

    @Override
    public Customer getById(String id) {
        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isEmpty()) {
            throw new RuntimeException("Data tidak ditemukan");
        }else {
            return customer.get();
        }
    }

    @Override
    public List<Customer> getAll() {
       return customerRepository.findAll();
    }

    @Override
    public Customer update(Customer customer) {
        getById(customer.getId());
        return customerRepository.saveAndFlush(customer);
    }

    @Override
    public void delete(String id) {
        Customer customer = getById(id);
        customerRepository.delete(customer);
    }
}
