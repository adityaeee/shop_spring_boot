package com.aditya.shop.controller;

import com.aditya.shop.constant.APIUrl;
import com.aditya.shop.entity.Customer;
import com.aditya.shop.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = APIUrl.CUSTOMER_API)
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public Customer createNewCustomer(@RequestBody Customer product) {
        return customerService.create(product);
    }

    @GetMapping(path = APIUrl.PATH_VAR_ID)
    public Customer getCustomerById(@PathVariable String id) {
        return customerService.getById(id);
    }

    @GetMapping
    public List<Customer> getAllCustomer() {
        return customerService.getAll();
    }

    @PutMapping
    public Customer updateCustomer(@RequestBody Customer product) {
        return customerService.update(product);
    }

    @DeleteMapping(path = APIUrl.PATH_VAR_ID)
    public String deleteById(@PathVariable String id) {
        customerService.delete(id);
        return "OK Succes Delete Customer";
    }

}
