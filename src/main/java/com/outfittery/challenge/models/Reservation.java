package com.outfittery.challenge.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "RESERVATIONS",
uniqueConstraints = @UniqueConstraint(columnNames = {"time_index", "date", "customer_id"}, name = "UK_Composite_Reservation"))
//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Reservation {
    @Id
    @SequenceGenerator(name = "SEQ_RESERVATION_ID", sequenceName = "SEQ_RESERVATION_ID", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RESERVATION_ID")
    private Long id;

    @JsonManagedReference(value = "reservationTimeSlot")
    @ManyToOne
    @JoinColumn(name="time_index", referencedColumnName = "time_index")
    private TimeSlot timeSlot;

    @JsonManagedReference(value = "reservationStylist")
    @ManyToOne
    @JoinColumn(name = "stylist_id")
    private Stylist stylist;

    private LocalDate date;

    @JsonManagedReference(value = "reservationCustomer")
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Stylist getStylist() {
        return stylist;
    }

    public void setStylist(Stylist stylist) {
        this.stylist = stylist;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
