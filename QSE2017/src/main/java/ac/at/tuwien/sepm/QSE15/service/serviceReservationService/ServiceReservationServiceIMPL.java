package main.java.ac.at.tuwien.sepm.QSE15.service.serviceReservationService;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.customerDAO.CustomerDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.customerDAO.JDBCCustomerDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.serviceReservationDAO.JDBCServiceReservationDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Person;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.ServiceReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.CustomerNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

/**
 * Created by Bajram Saiti on 02.05.17.
 */
@org.springframework.stereotype.Service
public class ServiceReservationServiceIMPL implements ServiceReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceReservationServiceIMPL.class);

    @Autowired
    JDBCServiceReservationDAO serviceReservationDAO;

    @Override
    public ServiceReservation create(ServiceReservation reservation) throws ServiceException {

        if (reservation == null){
            LOGGER.error("Cannot create Service Reservation! Input value in null");
            throw new ServiceException("Input parameter is null");
        }

        try {
            serviceReservationDAO.create(reservation);
            return reservation;

        } catch (DAOException e) {
            LOGGER.error("Unable to create ServiceReservation.");
            throw new ServiceException(e.getMessage());
        }

    }

    @Override
    public void update(ServiceReservation reservation) throws ServiceException {

        if (reservation == null){
            LOGGER.error("Cannot update Service Reservation! Input value in null");
            throw new ServiceException("Input parameter is null");
        }

        try {
            serviceReservationDAO.update(reservation);

        } catch (DAOException e) {
            LOGGER.error("Unable to update ServiceReservation.");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<ServiceReservation> search(ServiceReservation r1) throws ServiceException {

        try {
            return serviceReservationDAO.search(r1);

        } catch (DAOException e) {
            LOGGER.error("Unable to search ServiceReservations.");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public ServiceReservation get(ServiceReservation reservation) throws ServiceException {

        if (reservation == null || reservation.getRid() == null || reservation.getDate() == null || reservation.getSrid() == null){
            LOGGER.error("Cannot get Service Reservation! Input value in null");
            throw new ServiceException("Input parameter is null");
        }

        try {
            return serviceReservationDAO.get(reservation);

        } catch (DAOException e) {
            LOGGER.error("Unable to get ServiceReservation.");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void cancel(Reservation reservation) throws ServiceException {

        if (reservation == null){
            LOGGER.error("Cannot cancel Service Reservation! Input value in null");
            throw new ServiceException("Input parameter is null");
        }

        LocalDate start = reservation.getFrom().toLocalDate();
        LocalDate current = LocalDate.now();

        if (start.isBefore(current)) {
            LOGGER.error("Start of the Service reservation is before current time.");
            throw new ServiceException();
        }

        long difference = Math.abs(ChronoUnit.DAYS.between(current, start));

        if (difference > 2) {

            try {
                serviceReservationDAO.cancel(reservation);
            } catch (DAOException e) {
                LOGGER.error("Unable to cancel Service Reservation.");
                throw new ServiceException(e.getMessage());
            }

        } else {
            LOGGER.error("Too late to cancel the Service reservation.");
            throw new ServiceException("Too late to cancel the Service reservation.");
        }
    }

    @Override
    public void sendEmail(ServiceReservation reservation) throws ServiceException {

        if (reservation == null){
            throw new ServiceException("Input parameter is null");
        }
        final String username = "hmsqse15@gmail.com";
        final String password = "sepmqse15";

        Properties properties = new Properties();
        CustomerDAO customerDAO = new JDBCCustomerDAO();
        Person person = null;
        try {
            person = customerDAO.find(reservation.getCostumerId());
        } catch (DAOException | CustomerNotFoundException e) {
            LOGGER.error("Error in Send Email");

            throw new ServiceException(e.getMessage());
        }
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

                message.setRecipients(Message.RecipientType.TO, myToList);
                message.setSubject("Reservation confirmation for Reservation No: " + reservation.getRid());
                message.setContent("<h:body style=background-colour:white,font-family:verdana;color:#0000>" +
                        "Confirmation of reservation: " + reservation.getRid() + ".\n" +
                        "You have booked a room from: " + reservation.getFrom() + ", to: " + reservation.getUntil() + ". \n" +
                        "Value of reservation: " + reservation.getTotal() + "EUR.\n" +
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
