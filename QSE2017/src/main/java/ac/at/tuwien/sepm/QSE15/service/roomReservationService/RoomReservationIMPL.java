package main.java.ac.at.tuwien.sepm.QSE15.service.roomReservationService;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.customerDAO.CustomerDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.customerDAO.JDBCCustomerDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.roomReservationDAO.JDBCRoomReservationDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.roomReservationDAO.RoomReservationDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Customer;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Person;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.RoomReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.ServiceReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Room;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.CustomerNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

/**
 * Created by Bajram Saiti on 07.05.17.
 */
@Service
public class RoomReservationIMPL implements RoomReservationService{

    @Autowired
    JDBCRoomReservationDAO roomReservationDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomReservationIMPL.class);

    @Override
    public RoomReservation create(RoomReservation reservation) throws ServiceException {

        if (reservation == null){
            LOGGER.error("Cannot create RoomReservation! Input value is null!");
            throw new ServiceException("Input parameter is null");
        }
        try {

            RoomReservation rr = roomReservationDAO.create(reservation);
            sendEmail(reservation);
            return rr;
        } catch (DAOException e) {
            LOGGER.error("Unable to create RoomReservation.");
            throw new ServiceException(e.getMessage());
        }
    }
    @Override
    public void update(RoomReservation reservation) throws ServiceException {
        if (reservation == null){
            LOGGER.error("Cannot update RoomReservation! Input value is null!");
            throw new ServiceException("Input parameter is null");
        }
        try {
            roomReservationDAO.update(reservation);
        } catch (DAOException e) {
            LOGGER.error("Unable to update RoomReservation.");
            throw new ServiceException(e.getMessage());
        }
    }
    @Override
    public List<RoomReservation> search(RoomReservation reservation) throws ServiceException {
        try {
            return roomReservationDAO.search(reservation);
        } catch (DAOException e) {
            LOGGER.error("Unable to search RoomReservations.");
            throw new ServiceException(e.getMessage());
        }
    }
    @Override
    public RoomReservation get(Integer rid) throws ServiceException {
        if (rid == null){
            LOGGER.error("Cannot get RoomReservation! Input value is null or RID is missing!");
            throw new ServiceException("Input parameter is null");
        }
        try {
            return roomReservationDAO.get(rid);
        } catch (DAOException e) {
            LOGGER.error("Unable to get RoomReservation.");
            throw new ServiceException(e.getMessage());
        }
    }
    @Override
    public void cancel(Reservation reservation) throws ServiceException {
        if (reservation == null){
            LOGGER.error("Cannot cancel RoomReservation! Input value is null!");
            throw new ServiceException("Input parameter is null");
        }
        LocalDate start = reservation.getFrom().toLocalDate();
        LocalDate current = LocalDate.now();
        if (start.isBefore(current)) {
            LOGGER.error("Start of the reservation is before current time.");
            throw new ServiceException();
        }
        long difference = Math.abs(ChronoUnit.DAYS.between(current, start));
        if (difference > 2) {
            try {
                roomReservationDAO.cancel(reservation);
            } catch (DAOException e) {
                LOGGER.error("Unable to cancel RoomReservation.");
                throw new ServiceException(e.getMessage());
            }
        } else {
            LOGGER.error("Too late to cancel the reservation.");
            throw new ServiceException("Too late to cancel the reservation.");
        }
    }
    public List<RoomReservation> searchReservationsOnSpecifiedDate(RoomReservation reservation) throws ServiceException{
        if (reservation.getFrom() == null || reservation.getUntil() == null){
            LOGGER.error("Both dates need to be set in order to procced!");
            throw new ServiceException();
        }
        try {
            return roomReservationDAO.searchReservationsOnSpecifiedDate(reservation);
        } catch (DAOException e) {
            LOGGER.error("Unable to search Reservations On Specified Date.");
            throw new ServiceException(e.getMessage());
        }
    }

    public void sendEmail(RoomReservation reservation) throws ServiceException {
        final String username = "hmsqse15@gmail.com";
        final String password = "sepmqse15";
        Properties properties = new Properties();
        CustomerDAO customerDAO = new JDBCCustomerDAO();
        Person person = new Customer();
        person.setEmail("drmacak_4@yahoo.com");
        if (person != null) {
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            Message message = new MimeMessage(session);
            try {
                message.setFrom(new InternetAddress(username));
                InternetAddress[] myToList = InternetAddress.parse(person.getEmail());
                int a = 0;
                message.setRecipients(Message.RecipientType.TO, myToList);
                message.setSubject("Reservation confirmation for Reservation No: " + reservation.getRid());
                message.setContent("<h:body style=background-colour:white,font-family:verdana;color:#0000>" +
                        "Confirmation of reservation: " + reservation.getRid() + ".\n" +
                        "You have booked a room from: " + reservation.getFrom() + ", to: " + reservation.getUntil() + ". \n" +
                        "Value of reservation: " + reservation.getTotal()/100 + ",00 EUR.\n" +
                        "Thank you!" + "<br/><br/>" +
                        "</body>", "text/html; charset=utf-8");
                Transport.send(message);
            } catch (MessagingException e) {
                LOGGER.error("Error in Send Email");
                throw  new ServiceException(e.getMessage());
            }
        }
    }
}