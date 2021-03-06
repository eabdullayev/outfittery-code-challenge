package com.outfittery.challenge.services;

import com.outfittery.challenge.exceptions.ResourceNotFoundException;
import com.outfittery.challenge.models.Customer;
import com.outfittery.challenge.repositories.CustomerRepo;
import com.outfittery.challenge.rest.dto.CustomerResponse;
import com.outfittery.challenge.rest.dto.builder.CustomerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * used for performing CRUD operation on customer object
 */
@Service
public class CustomerServiceImpl implements CustomerService{
    private Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
    @Autowired
    private CustomerRepo customerRepo;

    @Override
    public List<CustomerResponse> getAll() {
        return customerRepo.findAll().stream()
                .map(customer -> CustomerBuilder.buildCustomerResponse(customer))
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse getById(Long id) {
        logger.info("Customer findById: "+ id);
        return CustomerBuilder.buildCustomerResponse(customerRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Customer not found in DB", id)));
    }

    @Override @Transactional
    public CustomerResponse save(Customer customer) {
        logger.info("Customer.save: " + customer);
        return CustomerBuilder.buildCustomerResponse(customerRepo.save(customer));
    }

    @Override @Transactional
    public void delete(Long id) {
        logger.info("Customer.delete id: " + id);
        customerRepo.deleteById(id);
    }
}
