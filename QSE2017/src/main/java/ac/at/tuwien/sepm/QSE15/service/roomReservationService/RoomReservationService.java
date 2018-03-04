package main.java.ac.at.tuwien.sepm.QSE15.service.roomReservationService;

import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.RoomReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Room;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;

import java.util.List;

/**
 * Created by Bajram Saiti on 07.05.17.
 */
public interface RoomReservationService {

    RoomReservation create(RoomReservation reservation) throws ServiceException;

    void update(RoomReservation reservation) throws ServiceException;

    List<RoomReservation> search(RoomReservation r1) throws ServiceException;

    RoomReservation get(Integer rid) throws ServiceException;

    void cancel(Reservation reservation) throws ServiceException;

    List<RoomReservation> searchReservationsOnSpecifiedDate(RoomReservation reservation) throws ServiceException;
}
