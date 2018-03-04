package main.java.ac.at.tuwien.sepm.QSE15.test;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.paymentDAO.PaymentDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.payment.Payment;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.RoomReservation;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.util.List;

/**
 * Created by Pwnnz0r on 21/05/2017.
 */
abstract class AbstractPaymentDAOTest {

    private PaymentDAO paymentDAO;

    public void setPaymentDAO(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    @Test(expected = DAOException.class)
    public void createMethodWithInvalidParameter() throws DAOException {
        paymentDAO.create(null);
    }
    @Test
    public void createWithCorrectInput() throws DAOException {
        List<Payment> list = paymentDAO.getAll();
        paymentDAO.create(new Payment(null, 200, 100L,"VISA", new Date(200L)));
        List<Payment> list1 = paymentDAO.getAll();

        for (int i = 0; i<list1.size();i++){
        }
        Assert.assertTrue(list.size()+1==list1.size());

    }

    @Test(expected = DAOException.class)
    public void searchWithInvalidParameter() throws DAOException {
        paymentDAO.search(null);
    }
    @Test
    public void searchWithValidParameter() throws DAOException {
        Payment payment1 = new Payment(null, 2004, 100L,"Credit Card", new Date(200L));
        paymentDAO.create(payment1);
        Payment payment = new Payment();
        payment.setReservation(2004);

        List<Payment> payments = paymentDAO.search(payment);

        Assert.assertEquals(payment1.getReservation(), payments.get(0).getReservation());

    }
    @Test(expected = DAOException.class)
    public void getPaidDifWithInvalidParameter() throws DAOException {
        paymentDAO.getPaidDifference(null);
    }
   // @Test
    public void getPaidDIfWIthValidParameter() throws DAOException{
        Reservation reservation = new RoomReservation();
        reservation.setRid(2000);
        Payment payment1 = new Payment(null, 2000, 100L,"Credit Card", new Date(200L));

        paymentDAO.create(payment1);
        Payment payment = paymentDAO.getPaidDifference(reservation);
        Assert.assertTrue(payment.getPaidAmount()==29000);
    }



}
