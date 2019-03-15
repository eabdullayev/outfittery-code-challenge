package com.outfittery.challenge.controllers;

import com.outfittery.challenge.rest.dto.ManyReservationResponse;
import com.outfittery.challenge.rest.dto.ReservationRequest;
import com.outfittery.challenge.rest.dto.ReservationResponse;
import com.outfittery.challenge.services.ReservationService;
import org.apache.tomcat.jni.Local;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Test
    public void getAvailableTimeSlot_ok() throws Exception {
        when(reservationService.getTimeSlots(any(LocalDate.class)))
                .thenReturn(Arrays.asList("10:00", "10:30", "11:00"));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1.0/reservation/time-slots/available?date=" + LocalDate.now().plusDays(3).toString());
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("[\"10:00\", \"10:30\", \"11:00\"]"))
                .andReturn();
    }

    @Test
    public void getAvailableTimeSlot_ko() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1.0/reservation/time-slots/available?date="+ LocalDate.now().minusDays(3).toString());
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errors\":[{\"field\":\"getAvailableTimeSlot.date\"," +
                        "\"message\":\"Date should be present or future\"}]," +
                        "\"details\":\"uri=/api/v1.0/reservation/time-slots/available\"}"))
                .andReturn();
    }

    @Test
    public void makeReservation_ok() throws Exception {
        when(reservationService.makeReservation(any(ReservationRequest.class)))
                .thenReturn(new ReservationResponse(3L, "Elchin", "11:00", LocalDate.now(), "Antonio"));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1.0/reservation/make-reservation")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"customerId\": 3, \"timeSlot\":\"11:00\", \"date\":\"" +LocalDate.now().toString() +"\" }")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"customerId\":3,\"customerName\":\"Elchin\",\"timeSlot\":\"11:00\",\"stylist\":\"Antonio\"}"))
                .andReturn();
    }

    @Test
    public void makeReservation_validationException() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1.0/reservation/make-reservation")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"customerId\": 3, \"timeSlot\":\"11:00\", \"date\":\"" +LocalDate.now().minusDays(3).toString() +"\" }")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errors\":[{\"field\":\"date\"," +
                        "\"message\":\"Date should be present or future\"}]," +
                        "\"details\":\"uri=/api/v1.0/reservation/make-reservation\"}"))
                .andReturn();
    }

    @Test
    public void updateReservation_ok() throws Exception {
        when(reservationService.updateReservation(any(ReservationRequest.class)))
                .thenReturn(new ReservationResponse(3L, "Elchin", "11:00", LocalDate.now(), "Antonio"));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1.0/reservation/update-reservation")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"reservationId\":1, \"customerId\": 3, \"timeSlot\":\"11:00\", \"date\":\"" +LocalDate.now().plusDays(3).toString() +"\" }")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"customerId\":3,\"customerName\":\"Elchin\",\"timeSlot\":\"11:00\",\"stylist\":\"Antonio\"}"))
                .andReturn();
    }

    @Test
    public void updateReservation_validationException() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1.0/reservation/update-reservation")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"reservationId\":1, \"customerId\": 3, \"timeSlot\":\"11:00\", \"date\":\"" +LocalDate.now().minusDays(3).toString() +"\" }")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errors\":[{\"field\":\"date\",\"message\":\"Date should be present or future\"}],\"details\":\"uri=/api/v1.0/reservation/update-reservation\"}"))
                .andReturn();
    }

    @Test
    public void addManyReservations_ok() throws Exception {
        ManyReservationResponse response = new ManyReservationResponse();
        response.getProcessedReservations().add(new ReservationResponse(3L, "Elchin", "11:30", LocalDate.now(), "Antonio"));
        when(reservationService.makeManyReservations(any(List.class)))
                .thenReturn(response);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1.0/reservation/add-many-reservations")
                .accept(MediaType.APPLICATION_JSON)
                .content("[{\"reservationId\":1, \"customerId\": 3, \"timeSlot\":\"11:00\", \"date\":\"" +LocalDate.now().plusDays(3).toString() +"\" }]")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"processedReservations\":[{\"customerId\":3,\"customerName\":\"Elchin\",\"timeSlot\":\"11:30\",\"stylist\":\"Antonio\"}],\"failedReservations\":[]}"))
                .andReturn();
    }

    @Test
    public void addManyReservations_validationException() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1.0/reservation/add-many-reservations")
                .accept(MediaType.APPLICATION_JSON)
                .content("[{\"reservationId\":1, \"customerId\": 3, \"timeSlot\":\"11:00\", \"date\":\"" +LocalDate.now().minusDays(3).toString() +"\" }]")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errors\":[{\"field\":\"addManyReservations.request[0].date\",\"message\":\"Date should be present or future\"}],\"details\":\"uri=/api/v1.0/reservation/add-many-reservations\"}"))
                .andReturn();
    }
}