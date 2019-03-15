package com.outfittery.challenge.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "TIME_SLOTS")
public class TimeSlot implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_TIME_SLOT_ID", sequenceName = "SEQ_TIME_SLOT_ID", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TIME_SLOT_ID")
    private Long id;
    @Column(name = "time_index")
    private Integer idxTime;
    private String time;

    @OneToMany(mappedBy = "timeSlot")
    List<Reservation> reservations;

    @OneToMany(mappedBy = "timeSlot")
    List<AvailableTimeSlot> availableTimeSlots;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdxTime() {
        return idxTime;
    }

    public void setIdxTime(Integer idxTime) {
        this.idxTime = idxTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<AvailableTimeSlot> getAvailableTimeSlots() {
        return availableTimeSlots;
    }

    public void setAvailableTimeSlots(List<AvailableTimeSlot> availableTimeSlots) {
        this.availableTimeSlots = availableTimeSlots;
    }
}
