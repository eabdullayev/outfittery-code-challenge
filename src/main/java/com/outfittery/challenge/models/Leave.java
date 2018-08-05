package com.outfittery.challenge.models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "LEAVES")
public class Leave {
    @Id
    @SequenceGenerator(name = "SEQ_LEAVE_ID", sequenceName = "SEQ_LEAVE_ID", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_LEAVE_ID")
    private Long id;

    private LocalDate begin;

    private LocalDate end;

    @ManyToOne
    @JoinColumn(name = "stylist_id")
    private Stylist stylist;

    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBegin() {
        return begin;
    }

    public void setBegin(LocalDate begin) {
        this.begin = begin;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public Stylist getStylist() {
        return stylist;
    }

    public void setStylist(Stylist stylist) {
        this.stylist = stylist;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }
}
