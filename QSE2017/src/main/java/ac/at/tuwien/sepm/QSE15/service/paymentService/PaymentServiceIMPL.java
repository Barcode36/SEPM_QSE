package main.java.ac.at.tuwien.sepm.QSE15.service.paymentService;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;


import main.java.ac.at.tuwien.sepm.QSE15.dao.paymentDAO.JDBCPaymentDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.paymentDAO.PaymentDAO;

import main.java.ac.at.tuwien.sepm.QSE15.entity.creditCard.CreditCard;
import main.java.ac.at.tuwien.sepm.QSE15.entity.payment.Payment;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Bajram Saiti on 02.05.17.
 */

@Service
public class PaymentServiceIMPL implements PaymentService {

    private PaymentDAO paymentDAO;
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceIMPL.class);

    @Autowired
    private JDBCPaymentDAO JDBCPaymentDAO;


    @Override
    public Payment create(Payment payment) throws ServiceException {
        try {
            return JDBCPaymentDAO.create(payment);
        } catch (DAOException e) {
            LOGGER.error("Error in Create Payment/Service");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Payment> search(Payment payment) throws ServiceException {

        try {
            return JDBCPaymentDAO.search(payment);

        } catch (DAOException e) {
            LOGGER.error("Error in search payment/Service");
            throw new ServiceException(e.getMessage());

        }

    }

    @Override
    public Payment getPaidDifference(Reservation reservation) throws ServiceException {
        try {
            return JDBCPaymentDAO.getPaidDifference(reservation);
        } catch (DAOException e) {
            LOGGER.error("Error in getPaidDifference/Service");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Payment> getAll() throws ServiceException {
        try {
            return JDBCPaymentDAO.getAll();
        } catch (DAOException e) {
            LOGGER.error("Error in getAll/ Payment Service");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Integer getReservationStatus(Reservation reservation) throws ServiceException {

        try {
            return JDBCPaymentDAO.getReservationStatus(reservation);
        } catch (DAOException e) {
            LOGGER.error("Error in getReservationStatus in Payment service.");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public CreditCard getCreditCardFromReservation(Reservation reservation) throws ServiceException {

        try {
            return JDBCPaymentDAO.getCreditCardFromReservation(reservation);
        } catch (DAOException e) {
            LOGGER.error("Error in getCreditCardFromReservation in PaymentServiceIMPL.");
            throw new ServiceException();
        }
    }

    @Override
    public void setPaid(Reservation reservation) throws ServiceException {

        try {
            JDBCPaymentDAO.setPaid(reservation);
        } catch (DAOException e) {
            LOGGER.error("Error in setPaid in PaymentService");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void setCanceled(Reservation reservation) throws ServiceException {

        try {
            JDBCPaymentDAO.setCanceled(reservation);
        } catch (DAOException e) {
            LOGGER.error("Error in setCanceled in PaymentService");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void setIsArrived(Reservation reservation) throws ServiceException {

        try {
            JDBCPaymentDAO.setIsArrived(reservation);
        } catch (DAOException e) {
            LOGGER.error("Error in setIsArrived in PaymentService");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void setCheckedOut(Reservation reservation) throws ServiceException {

        try {
            JDBCPaymentDAO.setCheckedOut(reservation);
        } catch (DAOException e) {
            LOGGER.error("Error in setCheckedOut in PaymentService");
            throw new ServiceException(e.getMessage());
        }
    }
}
