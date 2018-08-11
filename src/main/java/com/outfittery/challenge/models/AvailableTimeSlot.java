package com.outfittery.challenge.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "AVAILABLE_TIME_SLOTS")
public class AvailableTimeSlot {

    @Id
    @SequenceGenerator(name = "SEQ_AVAIABLE_STYLIST_ID", sequenceName = "SEQ_AVAIABLE_STYLIST_ID", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AVAIABLE_STYLIST_ID")
    private Long id;
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name="time_index", referencedColumnName = "time_index")
    private TimeSlot timeSlot;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "AVAILABLE_STYLISTS")
    private List<Long> availableStylists = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public List<Long> getAvailableStylists() {
        return availableStylists;
    }

    public void setAvailableStylists(List<Long> availableStylists) {
        this.availableStylists = availableStylists;
    }
}
