package com.outfittery.challenge.models;


import org.hibernate.annotations.Immutable;

import javax.persistence.*;

/**
 * Temp entity for generating cache data
 */
@Entity
@Immutable
@NamedNativeQueries(
        {@NamedNativeQuery(
                name = "VAvailableTimeSlot.findAllFreeTimeSlotsByDate",
                query = "select ts.time, res.busy_stylist_ids, " +
                        "( select string_agg(s.id, ',') " +
                        "from stylists s " +
                        "where s.state='READY_TO_STYLE' " +
                        "and not exists (select l.stylist_id from leaves l where ?1 between l.begin and l.end and l.stylist_id =s.id)) as all_stylist_ids " +
                        "from time_slots ts left outer join " +
                        "( " +
                        "select r.date, r.time_index, count(r.stylist_id) as total_stylist, string_agg(r.stylist_id, ',') as busy_stylist_ids, " +
                        "(select count(s.id) " +
                        "from stylists s " +
                        "where s.state='READY_TO_STYLE' " +
                        "and not exists (select l.stylist_id from leaves l where r.date between l.begin and l.end and l.stylist_id =s.id)) as total_count " +
                        "from reservations r " +
                        "where r.date=?1 " +
                        "group by r.date, r.time_index, total_count " +
                        ") as res " +
                        "on ts.time_index=res.time_index " +
                        "where res.total_stylist is null " +
                        "or res.total_stylist < res.total_count"
                , resultClass = VAvailableTimeSlot.class),
                @NamedNativeQuery(
                        name = "VAvailableTimeSlot.findAllFreeTimeSlotsByDateAndTime",
                        query = "select ts.time, res.busy_stylist_ids, " +
                                "( select string_agg(s.id, ',') " +
                                "from stylists s " +
                                "where s.state='READY_TO_STYLE' " +
                                "and not exists (select l.stylist_id from leaves l where ?1 between l.begin and l.end and l.stylist_id =s.id)) as all_stylist_ids " +
                                "from time_slots ts left outer join " +
                                "( " +
                                "select r.date, r.time_index, count(r.stylist_id) as total_stylist, string_agg(r.stylist_id, ',') as busy_stylist_ids, " +
                                "(select count(s.id) " +
                                "from stylists s " +
                                "where s.state='READY_TO_STYLE' " +
                                "and not exists (select l.stylist_id from leaves l where r.date between l.begin and l.end and l.stylist_id =s.id)) as total_count " +
                                "from reservations r " +
                                "where r.date=?1 " +
                                "group by r.date, r.time_index, total_count " +
                                ") as res " +
                                "on ts.time_index=res.time_index " +
                                "where (res.total_stylist is null " +
                                "or res.total_stylist < res.total_count) " +
                                "and time=?2",
                        resultClass = VAvailableTimeSlot.class
                )
        }
)
public class VAvailableTimeSlot {
    @Id
    private String time;

    @Column(name = "busy_stylist_ids")
    private String busyStylistIds;

    @Column(name = "all_stylist_ids")
    private String allStylistIds;

    public VAvailableTimeSlot() {
    }

    public VAvailableTimeSlot(String time, String busyStylistIds, String allStylistIds) {
        this.time = time;
        this.busyStylistIds = busyStylistIds;
        this.allStylistIds = allStylistIds;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBusyStylistIds() {
        return busyStylistIds;
    }

    public void setBusyStylistIds(String busyStylistIds) {
        this.busyStylistIds = busyStylistIds;
    }

    public String getAllStylistIds() {
        return allStylistIds;
    }

    public void setAllStylistIds(String allStylistIds) {
        this.allStylistIds = allStylistIds;
    }
}
