package com.outfittery.challenge.rest.dto;

import com.outfittery.challenge.models.LeaveType;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

public class LeaveRequest {

    @NotNull(message = "stylistId can not be null")
    @Positive(message = "should be positive number")
    private Long stylistId;

    @FutureOrPresent(message = "should be present or future")
    private LocalDate begin;

    @Future(message = "should be future")
    private LocalDate end;

    @NotNull(message = "leaveType can not be null")
    private LeaveType leaveType;

    public Long getStylistId() {
        return stylistId;
    }

    public void setStylistId(Long stylistId) {
        this.stylistId = stylistId;
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

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LeaveRequest{");
        sb.append("stylistId=").append(stylistId);
        sb.append(", begin=").append(begin);
        sb.append(", end=").append(end);
        sb.append(", leaveType=").append(leaveType);
        sb.append('}');
        return sb.toString();
    }
}
