package main.java.ac.at.tuwien.sepm.QSE15.service.paymentService;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.entity.creditCard.CreditCard;
import main.java.ac.at.tuwien.sepm.QSE15.entity.payment.Payment;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;

import java.util.List;

/**
 * Created by Bajram Saiti on 02.05.17.
 */
public interface PaymentService {
    /**
     * Creates a new payment
     * @param payment Payment that is going to be created
     * @return created payment
     * @throws ServiceException
     */
    Payment create(Payment payment) throws ServiceException;
    //  Payment get(Payment payment) throws DAOException;

    /**
     * Searches for a payment or payments
     * @param payment Conditions or payment that we are searching for
     * @return List of payments
     * @throws ServiceException
     */
    List<Payment> search(Payment payment) throws ServiceException;

    /**
     * Calculates a sum that has already been paid for one reservation
     * @param reservation reservation for wich we want to count how much is already paid
     * @return Payment with a SUM as paid parameter
     * @throws ServiceException -
     */
    Payment getPaidDifference(Reservation reservation) throws ServiceException;

    /**
     * Gives a list with all Payments
     *
     * @return list with Payments
     * @throws ServiceException - (1) in case a SQL Exception occurred
     *                      (2) in case the given employee is a null pointer
     */
    List<Payment> getAll() throws ServiceException;

    /**
     *
     * @param reservation which status is calculated
     * @return Integer number that corresponds with each status
     * @throws ServiceException if reservation is not found
     */
    Integer getReservationStatus(Reservation reservation) throws ServiceException;

    /**
     *
     * @param reservation holds customerID
     * @return CreditCard from a specific customer
     * @throws ServiceException in case there is no such reservation
     */
    CreditCard getCreditCardFromReservation(Reservation reservation) throws ServiceException;

    /**
     *
     * @param reservation in which paid is set to true
     * @throws ServiceException if reservation is not found
     */
    void setPaid(Reservation reservation) throws ServiceException;

    /**
     *
     * @param reservation in which canceled is set to true
     * @throws ServiceException if reservation is not found
     */
    void setCanceled(Reservation reservation) throws ServiceException;

    /**
     *
     * @param reservation in which isArrived is set to true
     * @throws ServiceException if reservation is not found
     */
    void setIsArrived(Reservation reservation) throws ServiceException;

    /**
     *
     * @param reservation in which checkedOut is set to true
     * @throws ServiceException if reservation is not found
     */
    void setCheckedOut(Reservation reservation) throws ServiceException;
}
