package main.java.ac.at.tuwien.sepm.QSE15.service.serviceReservationService;

import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.ServiceReservation;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;

import java.util.List;

/**
 * Created by Bajram Saiti on 02.05.17.
 */
public interface ServiceReservationService {

    ServiceReservation create(ServiceReservation reservation) throws ServiceException;

    void update(ServiceReservation reservation) throws ServiceException;

    List<ServiceReservation> search(ServiceReservation reservation) throws  ServiceException;

    ServiceReservation get(ServiceReservation reservation) throws ServiceException;

    void cancel(Reservation reservation) throws ServiceException;


    /**
     * Sends an confirmation email to a customer
     * @param reservation new created reservation
     *
     */
    public void sendEmail(ServiceReservation reservation) throws ServiceException;


}
