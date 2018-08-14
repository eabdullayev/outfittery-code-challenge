package com.outfittery.challenge.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "AVAILABLE_TIME_SLOTS")
@NamedQueries({
        @NamedQuery(name = "AvailableTimeSlot.findByDateAndTimeSlot",
        query = "SELECT a " +
                "FROM AvailableTimeSlot a " +
                "WHERE a.date=?1 " +
                "AND a.timeSlot = ?2")
})
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
    private Set<Long> availableStylists = new LinkedHashSet<>();

    @Version
    private int version;

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

    public Set<Long> getAvailableStylists() {
        return availableStylists;
    }

    public void setAvailableStylists(Set<Long> availableStylists) {
        this.availableStylists = availableStylists;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
