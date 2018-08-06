package com.outfittery.challenge.rest.dto.builder;

import com.outfittery.challenge.models.Customer;
import com.outfittery.challenge.rest.dto.CustomerResponse;

public class CustomerBuilder {

    public static CustomerResponse buildCustomerResponse(Customer customer) {
        return new CustomerResponse(customer.getId(), customer.getName());
    }
}
