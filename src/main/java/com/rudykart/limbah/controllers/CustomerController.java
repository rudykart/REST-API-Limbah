package com.rudykart.limbah.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rudykart.limbah.dto.CustomerDto;
import com.rudykart.limbah.dto.DataResponse;
import com.rudykart.limbah.dto.PagingResponse;
import com.rudykart.limbah.entities.Customer;
import com.rudykart.limbah.services.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/customers")
public class CustomerController {
    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PreAuthorize("hasAuthority('EMPLOYEE') OR hasAuthority('BOSS')")
    @GetMapping
    public ResponseEntity<DataResponse<List<Customer>>> findAllCustomers() {
        return ResponseEntity.ok().body(customerService.findAllCustomers());
    }

    @PreAuthorize("hasAuthority('EMPLOYEE') OR hasAuthority('BOSS')")
    @GetMapping("/{pageSize}/{pageNo}")
    public ResponseEntity<PagingResponse<Customer>> findAllCustomerWithPaging(@PathVariable int pageNo,
            @PathVariable int pageSize) {
        return ResponseEntity.ok().body(customerService.findAllCustomerWithPaging(pageNo, pageSize));
    }

    @PreAuthorize("hasAuthority('EMPLOYEE') OR hasAuthority('BOSS')")
    @GetMapping("/{search}/{pageSize}/{pageNo}")
    public ResponseEntity<PagingResponse<Customer>> findAllCustomerWithPagingAndSearch(@PathVariable int pageNo,
            @PathVariable int pageSize, @PathVariable String search) {
        return ResponseEntity.ok().body(customerService.findAllCustomerWithPagingAndSearch(pageNo, pageSize, search));
    }

    @PreAuthorize("hasAuthority('EMPLOYEE') OR hasAuthority('BOSS')")
    @PostMapping
    public ResponseEntity<DataResponse<Customer>> saveCustomer(@RequestBody @Valid CustomerDto customerDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.saveCustomer(customerDto));
    }

    @PreAuthorize("hasAuthority('EMPLOYEE') OR hasAuthority('BOSS')")
    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<Customer>> findCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok().body(customerService.findCustomerById(id));
    }

    @PreAuthorize("hasAuthority('BOSS')")
    @PutMapping("/{id}/update")
    public ResponseEntity<DataResponse<Customer>> updateCustomer(@PathVariable Long id,
            @Valid @RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok().body(customerService.updateCustomer(id, customerDto));
    }

    @PreAuthorize("hasAuthority('BOSS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable Long id) {
        return ResponseEntity.ok().body(customerService.deleteCustomer(id));
    }
}
