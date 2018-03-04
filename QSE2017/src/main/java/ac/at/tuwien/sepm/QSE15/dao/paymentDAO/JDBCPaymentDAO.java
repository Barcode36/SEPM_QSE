package main.java.ac.at.tuwien.sepm.QSE15.dao.paymentDAO;

import com.sun.org.apache.regexp.internal.RE;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.entity.creditCard.CreditCard;
import main.java.ac.at.tuwien.sepm.QSE15.entity.payment.Payment;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sun.rmi.runtime.Log;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bajram Saiti on 02.05.17.
 */
@Repository
public class JDBCPaymentDAO implements PaymentDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCPaymentDAO.class);
    private Connection connection;

    private String addReservationQuerry;
    private String searchForReservationQuerry;
    private String getPaidAmountQuerry;
    private String getAllPayments;

    @Autowired
    private JDBCSingletonConnection jdbcSingletonConnection;

    @PostConstruct
    public void init(){
        try {
            connection = jdbcSingletonConnection.getConnection();
        } catch (DAOException e) {
            LOGGER.error("Unable to get connection.");
        }
    }

    @Override
    public Payment create(Payment payment) throws DAOException {

        LOGGER.info("Creating payment!");
        if (payment == null){
            throw new DAOException("Wrong input");
        }

        addReservationQuerry = "INSERT INTO Payments(Reservation, Paid_Amount, Payment_Method, Payment_date) VALUES (?,?,?,?)";
        try {

            PreparedStatement ps = connection.prepareStatement(addReservationQuerry);
            ps.setInt(1, payment.getReservation());
            ps.setLong(2, payment.getPaidAmount());
            ps.setString(3, payment.getPaymentMethod());
            ps.setDate(4, payment.getDate());

            ps.executeUpdate();
            connection.commit();


        } catch (SQLException e) {
            LOGGER.error("Error in Create Payment!");
            throw new DAOException(e.getMessage());
        }
        LOGGER.info("Payment successfully created!");
        return payment;
    }

    @Override
    public Payment getPaidDifference(Reservation reservation) throws DAOException {

        LOGGER.info("Getting paid value!");
        if (reservation==null){
            throw  new DAOException("Wrong input!");
        }
        if (reservation == null){
            throw new DAOException("Wrong input");
        }
        getPaidAmountQuerry = "SELECT SUM(paid_amount) FROM PAYMENTS WHERE RESERVATION="+reservation.getRid();

        long resPrice = 0;
        long price = 0;
        if (reservation.getTotal() != 0) {

            String getResTotalQuerry = "SELECT TOTAL FROM RESERVATION WHERE RID = " + reservation.getRid();
            try {
                PreparedStatement ps1 = connection.prepareStatement(getResTotalQuerry);
                ResultSet rs1 = ps1.executeQuery();

                rs1.next();
                resPrice = rs1.getLong("TOTAL");


            } catch (SQLException e) {
            }
        } else {

            resPrice = reservation.getTotal();

        }
        Payment payment = new Payment();

        try {

            PreparedStatement ps = connection.prepareStatement(getPaidAmountQuerry);
            ResultSet rs = ps.executeQuery();

            rs.next();
            price = rs.getLong(1);
            payment.setPaidAmount(resPrice - price);
            if (resPrice - price > 0){
                reservation.setNeedsTObePaid(resPrice - price);
            } else{

                reservation.setNeedsTObePaid(0L);
            }

        } catch (SQLException e) {
            LOGGER.info("Error in getPaidValue!");

            throw new DAOException(e.getMessage());
        }
        LOGGER.info("Getting paid value successful!");
        return payment;
    }

    @Override
    public List<Payment> search(Payment payment) throws DAOException {
        LOGGER.info("Searching payments!");
        if (payment == null){
            throw new DAOException("Wrong input");
        }
        List<Payment> list = new ArrayList<>();
        searchForReservationQuerry = "SELECT * FROM PAYMENTS WHERE";
        if (payment.getReservation() != null) {
            if (searchForReservationQuerry.indexOf('=') != -1) {
                searchForReservationQuerry += " and reservation=" + payment.getReservation();
            } else {
                searchForReservationQuerry += " reservation= " + payment.getReservation();
            }
        }
        if (payment.getDate() != null) {
            if (searchForReservationQuerry.indexOf('=') != -1) {
                searchForReservationQuerry += " and payment_date='" + payment.getDate() + "'";
            } else {
                searchForReservationQuerry += " payment_date='" + payment.getDate() + "'";
            }
        }
        if (payment.getPaymentMethod()!=null) {
            if (!payment.getPaymentMethod().equals("Cash") || !payment.getPaymentMethod().equals("Credit Card")) {
                if (searchForReservationQuerry.indexOf('=') != -1) {
                    searchForReservationQuerry += " and payment_method='" + payment.getPaymentMethod() + "'";
                } else {
                    searchForReservationQuerry += " payment_method='" + payment.getPaymentMethod() + "'";
                }
            }
        }
            try {
                PreparedStatement ps = connection.prepareStatement(searchForReservationQuerry);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Payment payment1 = new Payment();
                    payment1.setPaidAmount(rs.getLong("Paid_Amount"));
                    payment1.setDate(rs.getDate("Payment_date"));
                    payment1.setPayId(rs.getInt("PayID"));
                    payment1.setReservation(rs.getInt("Reservation"));
                    payment1.setPaymentMethod(rs.getString("Payment_Method"));

                    list.add(payment);
                }
            } catch (SQLException e) {
                LOGGER.error("Error in search Payment");
                throw new DAOException(e.getMessage());
            }


            return list;


    }
    @Override
    public List<Payment> getAll() throws DAOException{
        LOGGER.info("Getting all Payments");
        List<Payment> list = new ArrayList<>();
        getAllPayments = "SELECT * FROM PAYMENTS";
        try {
            PreparedStatement ps = connection.prepareStatement(getAllPayments);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Payment payment1 = new Payment();
                payment1.setPaidAmount(rs.getLong("Paid_Amount"));
                payment1.setDate(rs.getDate("Payment_date"));
                payment1.setReservation(rs.getInt("Reservation"));
                payment1.setPaymentMethod(rs.getString("Payment_Method"));
                payment1.setPayId(rs.getInt("PayID"));
                list.add(payment1);
            }
        } catch (SQLException e) {
            LOGGER.error("Error in getAll Payments");
            throw new DAOException(e.getMessage());
        }
        return list;
    }

    @Override
    public CreditCard getCreditCardFromReservation(Reservation reservation) throws DAOException {

        String query = "SELECT cc.cnr, cc.holder, cc.card_type, cc.exp_month, cc.exp_year, cc.cvv FROM creditcard cc " +
                "JOIN customer c ON cc.cnr = c.credit_card JOIN reservation r ON c.pid = r.customerid WHERE r.rid = ";
        CreditCard creditCard = new CreditCard();

        try {
            PreparedStatement ps = connection.prepareStatement(query + reservation.getRid());
            ResultSet rs = ps.executeQuery();
            rs.next();

            creditCard.setCnr(rs.getString(1));
            creditCard.setHolder(rs.getString(2));
            creditCard.setCardType(rs.getString(3));
            creditCard.setExpMonth(rs.getInt(4));
            creditCard.setExpYear(rs.getInt(5));
            creditCard.setCvv(rs.getString(6));
        } catch (SQLException e) {
            LOGGER.error("Error while getting credit card from reservation.");
            throw new DAOException();
        }

        return creditCard;
    }

    @Override
    public Integer getReservationStatus(Reservation reservation) throws DAOException {

        //0 - arriving
        //10 - checked-in
        //100 - paid
        //1000 - checked-out
        //10000 canceled

       // LOGGER.info("Getting reservation status.");

        String query = "SELECT is_paid, is_canceled, is_arrived, is_checkedout FROM reservation WHERE rid = ";
        Integer status = 0;
        Boolean isPaid;
        Boolean isCanceled;
        Boolean isArrived;
        Boolean isCheckedOut;

        try {
            PreparedStatement ps = connection.prepareStatement(query + reservation.getRid());
            ResultSet rs = ps.executeQuery();

            rs.next();

            isPaid = rs.getBoolean(1);
            isCanceled = rs.getBoolean(2);
            isArrived = rs.getBoolean(3);
            isCheckedOut = rs.getBoolean(4);

        } catch (SQLException e) {
            LOGGER.error("Error in getReservationStatus.");
            throw new DAOException(e.getMessage());
        }

        if (isArrived) status += 10;
        if (isPaid) status += 100;
        if (isCheckedOut) status += 1000;
        if (isCanceled) status = 10000;

        return status;
    }

    @Override
    public void setPaid(Reservation reservation) throws DAOException {

        String paidQuery = "UPDATE Reservation SET is_paid = TRUE WHERE rid = ";

        try {
            PreparedStatement ps = connection.prepareStatement(paidQuery + reservation.getRid());
            ps.execute();
        } catch (SQLException e) {
            LOGGER.error("Error in setPaid.");
        }
    }

    @Override
    public void setCanceled(Reservation reservation) throws DAOException {

        String canceledQuery = "UPDATE Reservation SET is_canceled = TRUE WHERE rid = ";

        try {
            PreparedStatement ps = connection.prepareStatement(canceledQuery + reservation.getRid());
            ps.execute();
        } catch (SQLException e) {
            LOGGER.error("Error in setCanceled.");
        }
    }

    @Override
    public void setIsArrived(Reservation reservation) throws DAOException {

        String arrivedQuery = "UPDATE Reservation SET is_arrived = TRUE WHERE rid = ";

        try {
            PreparedStatement ps = connection.prepareStatement(arrivedQuery + reservation.getRid());
            ps.execute();
        } catch (SQLException e) {
            LOGGER.error("Error in setIsArrived.");
        }
    }

    @Override
    public void setCheckedOut(Reservation reservation) throws DAOException {

        String checkedOutQuery = "UPDATE Reservation SET is_checkedout = TRUE WHERE rid = ";

        try {
            PreparedStatement ps = connection.prepareStatement(checkedOutQuery + reservation.getRid());
            ps.execute();
        } catch (SQLException e) {
            LOGGER.error("Error in setCheckedOut.");
        }
    }
}