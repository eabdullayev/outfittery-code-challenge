package com.outfittery.challenge.integration;

import com.outfittery.challenge.models.Customer;
import com.outfittery.challenge.rest.dto.CustomerResponse;
import com.outfittery.challenge.rest.dto.ExceptionResponse;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CustomerControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void test_getAll() throws JSONException {
        String response = restTemplate.getForObject("/api/v1.0/customer/all", String.class);

        JSONAssert.assertEquals("[{id:1,name:John},{id:2,name:Tom}]", response, false);
    }

    @Test
    public void test_getById_ok() throws Exception {
        String response = restTemplate.getForObject("/api/v1.0/customer/1", String.class);

        JSONAssert.assertEquals("{id:1,name:John}", response, false);
    }

    @Test
    public void test_getById_ko() throws Exception {
        String response = restTemplate.getForObject("/api/v1.0/customer/3", String.class);

        JSONAssert.assertEquals("{\"errors\":[{\"field\":null,\"rejectedValue\":3,\"message\":\"Customer not found in DB\"}],\"details\":\"uri=/api/v1.0/customer/3\"}", response, false);
    }

    @Test
    public void test_createCustomer_ok() throws Exception {
        ResponseEntity<CustomerResponse> responseEntity = restTemplate
                .postForEntity("/api/v1.0/customer/add", new Customer("Elchin"), CustomerResponse.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Elchin", responseEntity.getBody().getName());
    }

    @Test
    public void test_createCustomer_nameBlank(){
        ResponseEntity<ExceptionResponse> responseEntity = restTemplate
                .postForEntity("/api/v1.0/customer/add", new Customer(" "), ExceptionResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(2, responseEntity.getBody().getErrors().size());
    }

    @Test
    public void test_createCustomer_sizeLessThanTwo(){
        ResponseEntity<ExceptionResponse> responseEntity = restTemplate
                .postForEntity("/api/v1.0/customer/add", new Customer("a"), ExceptionResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().getErrors().size());
        assertEquals("name length should be min 2 and max 50", responseEntity.getBody().getErrors().get(0).getMessage());
    }

    @Test
    public void test_deleteCustomer_ok(){
        ResponseEntity responseEntity = restTemplate.exchange("/api/v1.0/customer/remove/2", HttpMethod.DELETE,RequestEntity.EMPTY, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void test_deleteCustomer_ko(){
        ResponseEntity<ExceptionResponse> responseEntity = restTemplate.exchange("/api/v1.0/customer/remove/0", HttpMethod.DELETE,RequestEntity.EMPTY, ExceptionResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().getErrors().size());
        assertEquals("should be positive non zero value", responseEntity.getBody().getErrors().get(0).getMessage());
    }

    //todo should be implemented proper way in service layer
    @Test
    public void test_deleteCustomer_constraintViolation(){
        ResponseEntity<ExceptionResponse> responseEntity = restTemplate.exchange("/api/v1.0/customer/remove/1", HttpMethod.DELETE,RequestEntity.EMPTY, ExceptionResponse.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}
