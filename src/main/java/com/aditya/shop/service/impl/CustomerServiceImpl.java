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
        return customerRepository.saveAndFlush(customer);
    }

    @Override
    public Customer getById(String id) {
        return findByIdOrThrowNotFound(id);
    }



    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer update(Customer customer) {
        findByIdOrThrowNotFound(customer.getId());
        return customerRepository.saveAndFlush(customer);
    }

    @Override
    public void delete(String id) {
        Customer customer = findByIdOrThrowNotFound(id);
        customerRepository.delete(customer);
    }

    public Customer findByIdOrThrowNotFound(String id) {
        // artinya, kita findById, kalau enggak ada dilempar atau di Throw,
        // jadi kan sebenernya di bungkus sama optional dan kita pakai.get maka datanya menjadi Customer kayak kemaren. tapi dengan .orElseThrow kita juga gunakan .get ketika ada datanya dan otomatis di lempat throw ketika data null
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("customer not found"));
    }


}
