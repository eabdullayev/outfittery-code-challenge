package com.outfittery.challenge.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "STYLISTS")
//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Stylist {
    @Id
    @SequenceGenerator(name = "SEQ_STYLIST_ID", sequenceName = "SEQ_STYLIST_ID", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STYLIST_ID")
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private StylistState stylistState = StylistState.ROOKIE;

    @JsonBackReference(value = "stylistLeaves")
    @OneToMany(mappedBy = "stylist")
    private List<Leave> leaves;

    @JsonBackReference(value = "stylistReservation")
    @OneToMany(mappedBy = "stylist")
    private List<Reservation> reservations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StylistState getStylistState() {
        return stylistState;
    }

    public void setStylistState(StylistState stylistState) {
        this.stylistState = stylistState;
    }

    public List<Leave> getLeaves() {
        return leaves;
    }

    public void setLeaves(List<Leave> leaves) {
        this.leaves = leaves;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
