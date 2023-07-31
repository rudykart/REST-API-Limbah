package com.rudykart.limbah.services;

import java.util.HashMap;
import java.util.List;
// import java.util.logging.Logger;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.rudykart.limbah.dto.CustomerDto;
import com.rudykart.limbah.dto.DataResponse;
import com.rudykart.limbah.dto.PagingResponse;
import com.rudykart.limbah.entities.Customer;
import com.rudykart.limbah.exceptions.DataNotFoundException;
import com.rudykart.limbah.repositories.CustomerRepository;

@Service
public class CustomerService {

    // private static final Logger LOGGER =
    // Logger.getLogger(CustomerService.class.getName());
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public DataResponse<List<Customer>> findAllCustomers() {
        List<Customer> customers = customerRepository.findAll(Sort.by(Direction.DESC, "createdAt"));
        emptyDataCheck(customers);
        return new DataResponse<>(200, customers);
    }

    public PagingResponse<Customer> findAllCustomerWithPaging(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Direction.DESC, "createdAt"));
        Page<Customer> customers = customerRepository.findAll(pageable);
        List<Customer> listOfCustomer = customers.getContent();
        emptyDataCheck(listOfCustomer);
        return new PagingResponse<>(200, listOfCustomer, customers.getNumber(), customers.getSize(),
                customers.getTotalElements(), customers.getTotalPages(), customers.isLast());
    }

    public PagingResponse<Customer> findAllCustomerWithPagingAndSearch(int pageNo, int pageSize, String name) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Direction.DESC, "createdAt"));
        Page<Customer> customers = customerRepository.findByNameContains(name, pageable);
        List<Customer> listOfCustomer = customers.getContent();
        emptyDataCheck(listOfCustomer);
        return new PagingResponse<>(200, listOfCustomer, customers.getNumber(), customers.getSize(),
                customers.getTotalElements(), customers.getTotalPages(), customers.isLast());
    }

    private void emptyDataCheck(List<Customer> data) {
        if (data.isEmpty()) {
            throw new DataNotFoundException("Data is empty");

        }
    }

    public DataResponse<Customer> saveCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setName(customerDto.getName());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setAddress(customerDto.getAddress());
        return new DataResponse<Customer>(201, customerRepository.save(customer));
    }

    public DataResponse<Customer> findCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Customer not found"));
        return new DataResponse<Customer>(200, customer);
    }

    public DataResponse<Customer> updateCustomer(Long id, CustomerDto customerDto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Customer not found"));
        customer.setName(customerDto.getName());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setAddress(customerDto.getAddress());
        return new DataResponse<Customer>(200, customerRepository.save(customer));
    }

    public DataResponse<Map<String, String>> deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Customer not found"));
        Map<String, String> data = new HashMap<>();
        data.put("name", customer.getName());
        data.put("phone_number", customer.getPhoneNumber());
        data.put("address", customer.getAddress());
        data.put("status", "Deleted");
        customerRepository.deleteById(id);
        return new DataResponse<>(200, data);
    }
}
