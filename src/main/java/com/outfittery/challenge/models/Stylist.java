package com.outfittery.challenge.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "STYLISTS")
public class Stylist {
    @Id
    @SequenceGenerator(name = "SEQ_STYLIST_ID", sequenceName = "SEQ_STYLIST_ID", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STYLIST_ID")
    private Long id;
    @NotBlank(message = "name can not be blank")
    @Size(message = "name length should be min 2 and max 50", min = 2, max = 50)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private StylistState stylistState = StylistState.ROOKIE;

    @OneToMany(mappedBy = "stylist")
    private List<Leave> leaves;

    @OneToMany(mappedBy = "stylist")
    private List<Reservation> reservations;

    public Stylist() {
    }

    public Stylist(Long id) {
        this.id = id;
    }

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Stylist{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", stylistState=").append(stylistState);
        sb.append('}');
        return sb.toString();
    }
}
