package com.outfittery.challenge.controllers;

import com.outfittery.challenge.exceptions.ResourceNotFoundException;
import com.outfittery.challenge.models.Customer;
import com.outfittery.challenge.rest.dto.CustomerResponse;
import com.outfittery.challenge.services.CustomerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    public void test_getAll() throws Exception {
        when(customerService.getAll())
                .thenReturn(Arrays.asList(new CustomerResponse(1L, "Elchin")));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1.0/customer/all");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("[{id: 1, name: Elchin}]"))
                .andReturn();
    }

    @Test
    public void test_getById_ok() throws Exception {
        when(customerService.getById(anyLong()))
                .thenReturn(new CustomerResponse(1L, "Elchin"));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1.0/customer/1");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("{id: 1, name: Elchin}"))
                .andReturn();
    }

    @Test
    public void test_getById_ko() throws Exception {
        when(customerService.getById(anyLong()))
                .thenThrow(new ResourceNotFoundException("Customer not found in DB", 5L));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1.0/customer/5");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errors\":[{\"field\":null,\"rejectedValue\":5,\"message\":\"Customer not found in DB\"}],\"details\":\"uri=/api/v1.0/customer/5\"}"))
                .andReturn();
    }

    @Test
    public void test_createCustomer_ok() throws Exception {
        when(customerService.save(any(Customer.class)))
                .thenReturn(new CustomerResponse(3L, "Elchin"));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1.0/customer/add")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Elchin\"}")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().json("{id: 3, name: Elchin}"))
                .andReturn();
    }

    @Test
    public void test_createCustomer_nameBlank() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1.0/customer/add")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\" \"}")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errors\":[{\"field\":\"name\",\"rejectedValue\":\" \",\"message\":\"Name can not be blank\"},{\"field\":\"name\",\"rejectedValue\":\" \",\"message\":\"name length should be min 2 and max 50\"}],\"details\":\"uri=/api/v1.0/customer/add\"}"))
                .andReturn();
    }

    @Test
    public void test_createCustomer_sizeLessThanTwo() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1.0/customer/add")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"a\"}")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errors\":[{\"field\":\"name\",\"rejectedValue\":\"a\",\"message\":\"name length should be min 2 and max 50\"}],\"details\":\"uri=/api/v1.0/customer/add\"}"))
                .andReturn();
    }

    @Test
    public void test_deleteCustomer_ok() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1.0/customer/remove/2");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    public void test_deleteCustomer_ko() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1.0/customer/remove/0");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errors\":[{\"field\":\"deleteCustomer.id\",\"rejectedValue\":0,\"message\":\"should be positive non zero value\"}],\"details\":\"uri=/api/v1.0/customer/remove/0\"}"))
                .andReturn();
    }
}