package com.aditya.shop.controller;

import com.aditya.shop.constant.APIUrl;
import com.aditya.shop.dto.request.SearchCustomerRequest;
import com.aditya.shop.entity.Customer;
import com.aditya.shop.service.CustomerService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.query.Jpa21Utils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = APIUrl.CUSTOMER_API)
public class CustomerController {
    private final CustomerService customerService;

//    @PostMapping
//	public ResponseEntity<CommonResponse<Customer>> createNewCustomer(@RequestBody Customer product) {
//		Customer newCustomer = customerService.create(product);
//		CommonResponse<Customer> response = CommonResponse.<Customer>builder()
//						.statusCode(HttpStatus.CREATED.value())
//						.message(ResponseMessage.SUCCESS_SAVE_DATA)
//						.data(newCustomer)
//						.build();
//		return ResponseEntity
//						.status(HttpStatus.CREATED)
//						.body(response);
//	}


    @GetMapping(path = APIUrl.PATH_VAR_ID)
    public Customer getCustomerById(@PathVariable String id) {
        return customerService.getById(id);
    }

    // hasAnyRole() -> multi role
    // hasRole() -> single role
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
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
                .build();


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
