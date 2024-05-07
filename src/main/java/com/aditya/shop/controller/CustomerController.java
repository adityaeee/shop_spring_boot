package com.aditya.shop.controller;

import com.aditya.shop.entity.Customer;
import com.aditya.shop.entity.Product;
import com.aditya.shop.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public Customer createNewCustomer(@RequestBody Customer customer) {

        return customerService.create(customer);
    }

    @GetMapping
    public List<Customer> getAllCustomer(){
        return customerService.getAll();
    }

    @GetMapping(path = "/{id}")
    public Customer getByid(@PathVariable String id) {
        return customerService.getById(id);
    }

    @PutMapping
    public Customer updateCustomer(@RequestBody Customer customer) {
        return customerService.update(customer);
    }

    @DeleteMapping(path = "/{id}")
    public String deleteById (@PathVariable String id) {
        customerService.delete(id);
        return "Ok, success delete product with id "+ id;
    }

}
