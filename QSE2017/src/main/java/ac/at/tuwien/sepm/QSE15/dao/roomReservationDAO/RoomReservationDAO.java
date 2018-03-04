package main.java.ac.at.tuwien.sepm.QSE15.dao.roomReservationDAO;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.RoomReservation;

import java.sql.Date;
import java.util.List;

/**
 * Created by Bajram Saiti on 07.05.17.
 */
public interface RoomReservationDAO {
    /**
     * Description: This method create a new Room Reservation.
     * @param reservation This is the reservation we want to create.
     * @return If the Reservation is created we will Return it back.
     * @throws DAOException
     */
    RoomReservation create(RoomReservation reservation) throws DAOException;

    /**
     * Description: This method will update some already existing Reservation
     * @param reservation This is the Reservation we want do edit ( with new data).
     * @throws DAOException
     */
    void update(RoomReservation reservation) throws DAOException;

    /**
     * Description: This method searches RoomReservations by some parameters.
     * @param reservation Here are saved the parameters from filter.
     * @return Returns List of Reservations Objects found in Database.
     * @throws DAOException
     */
    List<RoomReservation> search(RoomReservation reservation) throws DAOException;

    /**
     * Description: This method will give us back the object Reservation by giving only the Reservation ID.
     * @param rid Reservation ID we want to get.
     * @return Returns Reservation Object.
     * @throws DAOException
     */
    RoomReservation get(Integer rid) throws DAOException;

    /**
     * Description: This method will cancel Reservation if its possible (if it didn't started).
     * @param reservation Reservation we would like to cancel.
     * @throws DAOException
     */
    void cancel(Reservation reservation) throws DAOException;

    /**
     * Description: This method searches RoomReservations on some specific date.
     * @param reservation Here are saved the parameters from filter.
     * @return Returns List of Reservations Objects found in Database.
     * @throws DAOException
     */
    List<RoomReservation> searchReservationsOnSpecifiedDate(RoomReservation reservation) throws DAOException;
}
