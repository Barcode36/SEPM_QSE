package main.java.ac.at.tuwien.sepm.QSE15.dao.paymentDAO;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.entity.creditCard.CreditCard;
import main.java.ac.at.tuwien.sepm.QSE15.entity.payment.Payment;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;

import java.util.List;

/**
 * Created by Bajram Saiti on 02.05.17.
 */
public interface PaymentDAO {

    /**
     * Creates a new payment
     *
     * @param payment Payment that is going to be created
     * @return created payment
     * @throws DAOException - (1) in case a SQL Exception occurred
     *                      (2) in case the given employee is a null pointer
     */
    Payment create(Payment payment) throws DAOException;
    //  Payment get(Payment payment) throws DAOException;

    /**
     * Searches for a payment or payments
     *
     * @param payment Conditions or payment that we are searching for
     * @return List of payments
     * @throws DAOException - (1) in case a SQL Exception occurred
     *                      (2) in case the given employee is a null pointer
     */
    List<Payment> search(Payment payment) throws DAOException;

    /**
     * Calculates a sum that has already been paid for one reservation
     *
     * @param reservation reservation for wich we want to count how much is already paid
     * @return Payment with a SUM as paid parameter
     * @throws DAOException - (1) in case a SQL Exception occurred
     *                      (2) in case the given employee is a null pointer
     */
    Payment getPaidDifference(Reservation reservation) throws DAOException;

    /**
     * Gives a list with all Payments
     *
     * @return list with Payments
     * @throws DAOException - (1) in case a SQL Exception occurred
     *                      (2) in case the given employee is a null pointer
     */
    List<Payment> getAll() throws DAOException;

    /**
     *
     * @param reservation holds customerID
     * @return CreditCard from a specific customer
     * @throws DAOException in case there is no such reservation
     */
    CreditCard getCreditCardFromReservation(Reservation reservation) throws DAOException;

    /**
     *
     * @param reservation which status is calculated
     * @return Integer number that corresponds with each status
     * @throws DAOException if reservation is not found
     */
    Integer getReservationStatus(Reservation reservation) throws DAOException;

    /**
     *
     * @param reservation in which paid is set to true
     * @throws DAOException if reservation is not found
     */
    void setPaid(Reservation reservation) throws DAOException;

    /**
     *
     * @param reservation in which canceled is set to true
     * @throws DAOException if reservation is not found
     */
    void setCanceled(Reservation reservation) throws DAOException;

    /**
     *
     * @param reservation in which isArrived is set to true
     * @throws DAOException if reservation is not found
     */
    void setIsArrived(Reservation reservation) throws DAOException;

    /**
     *
     * @param reservation in which checkedOut is set to true
     * @throws DAOException if reservation is not found
     */
    void setCheckedOut(Reservation reservation) throws DAOException;
}
