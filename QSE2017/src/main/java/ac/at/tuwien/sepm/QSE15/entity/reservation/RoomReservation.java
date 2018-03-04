package main.java.ac.at.tuwien.sepm.QSE15.entity.reservation;

import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Room;

import java.sql.Date;

/**
 * Created by Bajram Saiti on 28.04.17.
 */
public class RoomReservation extends Reservation {

    private Integer roomId;
    private Long roomPrice;
    private Boolean breakfast;

    public RoomReservation(){

    }

    public RoomReservation(Integer rid, Integer costumerId, Date from, Date until, Long total, Boolean isPaid, Boolean isCancled, Integer roomId, Long roomPrice, Boolean breakfast) {
        super(rid,costumerId,from,until,total,isPaid,isCancled);
        this.roomId = roomId;
        this.roomPrice = roomPrice;
        this.breakfast = breakfast;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Long getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(Long roomPrice) {
        this.roomPrice = roomPrice;
    }

    public Boolean getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(Boolean breakfast) {
        this.breakfast = breakfast;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomReservation that = (RoomReservation) o;

        if (roomId != null ? !roomId.equals(that.roomId) : that.roomId != null) return false;
        if (roomPrice != null ? !roomPrice.equals(that.roomPrice) : that.roomPrice != null) return false;
        return breakfast != null ? breakfast.equals(that.breakfast) : that.breakfast == null;
    }

    @Override
    public int hashCode() {
        int result = roomId != null ? roomId.hashCode() : 0;
        result = 31 * result + (roomPrice != null ? roomPrice.hashCode() : 0);
        result = 31 * result + (breakfast != null ? breakfast.hashCode() : 0);
        return result;
    }
}
