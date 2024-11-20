package com.shane.esdtask.service;

import com.shane.esdtask.dto.CustomerRequest;
import com.shane.esdtask.dto.CustomerResponse;
import com.shane.esdtask.dto.LoginRequest;
import com.shane.esdtask.dto.UpdateCustomerRequest;
import com.shane.esdtask.entity.Customer;
import com.shane.esdtask.exception.CustomerNotFoundException;
import com.shane.esdtask.helper.EncryptionService;
import com.shane.esdtask.helper.JWTHelper;
import com.shane.esdtask.mapper.CustomerMapper;
import com.shane.esdtask.repo.CustomerRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor

public class CustomerService {
    private final CustomerRepo customerRepo;
    private final EncryptionService encryptionService;
    private final JWTHelper jwtHelper;
    private final CustomerMapper customerMapper;


    public Customer getCustomer(String email) {
        return customerRepo.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException(
                        format("Cannot update Customer:: No customer found with the provided ID:: %s", email)
                ));
    }
    public String login(LoginRequest request) {

            Customer customer = getCustomer(request.email());
            if(!encryptionService.validates(request.password(), customer.getPassword())) {
                return "Wrong Password or Email";
            }

            return jwtHelper.generateToken(request.email());
    }

    public String createCustomer(@Valid CustomerRequest request) {
        Customer customer = customerMapper.toCustomer(request);
        customer.setPassword(encryptionService.encode(customer.getPassword()));
        customerRepo.save(customer);
        return "Customer Created Successfully";

    }

    public void deleteCustomer(String email) {
        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer with email " + email + " not found"));
        customerRepo.delete(customer);
    }

    public CustomerResponse updateCustomer(String email, UpdateCustomerRequest request) {
        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer with email " + email + " not found"));

        customer.setFirstName(request.f_name());
        customer.setLastName(request.l_name());

        customerRepo.save(customer);
        return new CustomerResponse(
                customer.getFirstName() , customer.getLastName(),
                customer.getEmail()
        );
    }

}
