package com.outfittery.challenge.services;

import com.outfittery.challenge.exceptions.ResourceNotFoundException;
import com.outfittery.challenge.models.Customer;
import com.outfittery.challenge.repositories.CustomerRepo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceImplTest {
    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepo customerRepo;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test_getAll() {
        List<Customer> customers = mock(List.class);
        when(customerRepo.findAll()).thenReturn(customers);
        customerService.getAll();

        //verify
        verify(customerRepo, times(1)).findAll();
    }

    @Test
    public void test_getById_ok() {
        //prepare
        Customer customer = mock(Customer.class);
        Optional<Customer> optional = Optional.of(customer);
        when(customerRepo.findById(anyLong())).thenReturn(optional);
        //call
        customerService.getById(5L);

        //verify
        verify(customerRepo, times(1)).findById(anyLong());
    }

    @Test
    public void test_getById_ko() {
        //prepare
        expectedException.expect(ResourceNotFoundException.class);
        expectedException.expectMessage("Customer not found in DB");
        when(customerRepo.findById(anyLong())).thenThrow(new ResourceNotFoundException("Customer not found in DB", 5L));
        //call
        customerService.getById(5L);

        //verify
        verify(customerRepo, times(1)).findById(anyLong());
    }

    @Test
    public void save() {
        //prepare
        Customer customer = mock(Customer.class);
        when(customerRepo.save(any(Customer.class))).thenReturn(customer);
        //call
        customerService.save(customer);
        //verify
        verify(customerRepo, times(1)).save(any(Customer.class));
    }

    @Test
    public void delete() {
        customerService.delete(5L);
        //verify
        verify(customerRepo, times(1)).deleteById(anyLong());
    }
}