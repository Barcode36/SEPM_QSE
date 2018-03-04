package main.java.ac.at.tuwien.sepm.QSE15.dao.invoiceDAO;

import javafx.scene.control.Alert;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.entity.invoice.Invoice;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.ServiceReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;
import main.java.ac.at.tuwien.sepm.QSE15.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luka on 5/8/17.
 */
@Repository
public class JDBCInvoiceDAO implements InvoiceDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCInvoiceDAO.class);

    private Connection connection;

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

    //Luka Veljkovic
    @Override
    public Invoice generateInvoice(Reservation reservation) throws DAOException {

        Invoice invoice = new Invoice();
        Boolean isPaid;
        Boolean hasServices;
        List<Service> services = new ArrayList<>();
        List<ServiceReservation> serviceReservations = new ArrayList<>();
        String checkIfPaidQuery = "SELECT is_paid FROM reservation WHERE rid = ";
        String hotelName = "SELECT name FROM hotel ";
        String checkIfHasServicesQuery = "SELECT rid FROM service_reservation WHERE rid = ";
        String invoiceQueryWithoutService = "SELECT r.rid, c.name, c.surname, r.from_date, r.until_date, rr.room_price, " +
                "r.is_paid, r.is_canceled, c.address, c.zip, c.place, c.country, c.phone, c.email, c.identification, " +
                "room.room_category FROM customer c JOIN reservation r ON c.pid = r.customerid " +
                "JOIN room_reservation rr ON rr.rid = r.rid JOIN room ON room.rnr = rr.roomid WHERE r.rid = ";
        String invoiceQueryWithService = "SELECT r.rid, c.name, c.surname, r.from_date, r.until_date, rr.room_price, r.is_paid, " +
                "r.is_canceled, s.service_type, s.description, s.price, sr.on_date, c.address, c.zip, c.place, " +
                "c.country, c.phone, c.email, c.identification, room.room_category FROM customer c JOIN reservation r " +
                "ON c.pid = r.customerid JOIN service_reservation sr ON r.rid = sr.rid JOIN service s " +
                "ON s.srid = sr.srid JOIN room_reservation rr ON rr.rid = r.rid JOIN room ON room.rnr = rr.roomid " +
                "WHERE r.rid = ";

        LOGGER.info("Generating invoice.");

        if (reservation == null){

          //  Utility.showAlert("Reservation ID field is empty.", Alert.AlertType.ERROR);
            LOGGER.error("Reservation is not initialized (null).");
            throw new DAOException();
        }

        try {
            PreparedStatement ps = connection.prepareStatement(hotelName);
            ResultSet rs = ps.executeQuery();

            rs.next();
            invoice.setHotelName(rs.getString(1));

        } catch (SQLException e) {
            LOGGER.error("There is no hotel name.");
            throw new DAOException();
        }

        try {
            PreparedStatement ps = connection.prepareStatement(checkIfPaidQuery + reservation.getRid());
            ResultSet rs = ps.executeQuery();

            rs.next();
            isPaid = rs.getBoolean(1);

        } catch (SQLException e) {
            LOGGER.error("An error occurred while generating invoice. Reservation ID not found."); //maybe can check if rid exists??
            throw new DAOException();
        }

        try {
            PreparedStatement ps = connection.prepareStatement(checkIfHasServicesQuery + reservation.getRid());
            ResultSet rs = ps.executeQuery();

            hasServices = rs.next();

        } catch (SQLException e) {
            LOGGER.error("An error occurred while generating invoice. Reservation ID not found.");
            throw new DAOException();
        }

        if (isPaid){
            LOGGER.info("This reservation is already paid.");
        }

        else {
            LOGGER.info("Reservation is not paid yet.");
        }

        try {
            PreparedStatement ps;
            try {

                if (hasServices) {

                    ps = connection.prepareStatement(invoiceQueryWithService + reservation.getRid());
                } else{

                    ps = connection.prepareStatement(invoiceQueryWithoutService + reservation.getRid());

                }
            } catch (SQLException e) {
                LOGGER.error("An error occurred while generating invoice. Reservation ID not found.");
                throw new DAOException();
            }

            if (ps != null) {

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        do {

                            if (hasServices) {
                                invoice.setReservationId(rs.getInt(1));
                                invoice.setName(rs.getString(2));
                                invoice.setSurname(rs.getString(3));
                                invoice.setFromDate(rs.getDate(4));
                                invoice.setUntilDate(rs.getDate(5));
                                invoice.setRoomPrice(rs.getLong(6));
                                services.add(new Service(0, rs.getString(9), rs.getString(10),
                                        rs.getLong(11))); //sid, type, desc, price, serviceDate
                                ServiceReservation serviceReservation = new ServiceReservation();
                                serviceReservation.setDate(rs.getDate(12));
                                serviceReservations.add(serviceReservation);
                                invoice.setAddress(rs.getString(13));
                                invoice.setZip(rs.getString(14));
                                invoice.setPlace(rs.getString(15));
                                invoice.setCountry(rs.getString(16));
                                invoice.setPhone(rs.getString(17));
                                invoice.setEmail(rs.getString(18));
                                invoice.setIdentification(rs.getString(19));
                                invoice.setRoomType(rs.getString(20));
                            } else{

                                invoice.setReservationId(rs.getInt(1));
                                invoice.setName(rs.getString(2));
                                invoice.setSurname(rs.getString(3));
                                invoice.setFromDate(rs.getDate(4));
                                invoice.setUntilDate(rs.getDate(5));
                                invoice.setRoomPrice(rs.getLong(6));
                                invoice.setAddress(rs.getString(9));
                                invoice.setZip(rs.getString(10));
                                invoice.setPlace(rs.getString(11));
                                invoice.setCountry(rs.getString(12));
                                invoice.setPhone(rs.getString(13));
                                invoice.setEmail(rs.getString(14));
                                invoice.setIdentification(rs.getString(15));
                                invoice.setRoomType(rs.getString(16));
                            }

                        } while (rs.next());
                    }
                }
            }
        } catch (SQLException e) {

            LOGGER.error("Error with executing invoice query.");
            throw new DAOException();
        }

        invoice.setServices(services);
        invoice.setServiceReservations(serviceReservations);
        LOGGER.info("Invoice is generated successfully.");

        return invoice;
    }
}
