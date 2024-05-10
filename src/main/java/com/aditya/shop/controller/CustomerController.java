package com.aditya.shop.controller;

import com.aditya.shop.constant.APIUrl;
import com.aditya.shop.dto.request.SearchCustomerRequest;
import com.aditya.shop.entity.Customer;
import com.aditya.shop.service.CustomerService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.query.Jpa21Utils;
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
    public List<Customer> getAllCustomer(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "mobilePhoneNo", required = false) String phone,
            @RequestParam(name = "birthDate", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String birthDate,
            @RequestParam(name = "status", required = false) Boolean status
    ) {

        SearchCustomerRequest request = SearchCustomerRequest.builder()
                .name(name)
                .phone(phone)
                .birthDate(birthDate)
                .status(status)
                .build(); //ini sama saja seperti new


        return customerService.getAll(request);
    }

    @PutMapping
    public Customer updateCustomer(@RequestBody Customer customer) {
        return customerService.update(customer);
    }

    @PutMapping(path = APIUrl.PATH_VAR_ID)
    public String updateStatus(
            @PathVariable String id,
            @RequestParam(name = "status") Boolean status
    ) {
        customerService.updateStatusById(id, status);
        return "Ok, Success Update status customer";
    }

    @DeleteMapping(path = APIUrl.PATH_VAR_ID)
    public String deleteById(@PathVariable String id) {
        customerService.delete(id);
        return "OK Succes Delete Customer";
    }

}
