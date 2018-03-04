package main.java.ac.at.tuwien.sepm.QSE15.service.NewsletterService;
import main.java.ac.at.tuwien.sepm.QSE15.entity.hotel.Hotel;
import main.java.ac.at.tuwien.sepm.QSE15.entity.newsletter.Newsletter;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Person;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.emailPasswordService.EmailPasswordService;
import main.java.ac.at.tuwien.sepm.QSE15.service.hotelService.MyHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
/**
 * Created by ervincosic on 26/06/2017.
 */
@Service
public class NewsletterService {
    @Autowired
    private EmailPasswordService passwordService;
    @Autowired
    private MyHotelService hotelService;
    /**
     * Use this method to send a newsletter from the controller this
     * should be the only public method in this service
     *
     * @param newsletter - Newsletter with set subject, message, employee that
     *                  has their password as plain text
     * @return (1) true on success
     *         (2) false on failure
     */
    public boolean sendNewsletter(Newsletter newsletter) throws ServiceException {
        Hotel hotel = null;
        try {
            hotel = getHotel();
        } catch (ServiceException e) {
        }
        final String username = "hmsqse15@gmail.com";
        final String password = "sepmqse15";
        Properties properties = new Properties();
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
            Person person = new Employee();
            person.setEmail("drmacak_4@yahoo.com");
            InternetAddress[] myToList = InternetAddress.parse(person.getEmail());
            message.setRecipients(Message.RecipientType.TO, myToList);
            message.setSubject(newsletter.getSubject());
            message.setContent("<h:body style=background-colour:white,font-family:verdana;color:#0000>" +
                    newsletter.getMessage() +
                    "</body>", "text/html; charset=utf-8");
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            // LOGGER.error("Error in Send Email");
        }
        return false;
    }
    /**
     * This method retrievs the email password in from the database for given user
     * @param newsletter - newsletter with set employee that is using the newsletter
     * @return  password as plaintext
     * @throws ServiceException (1) in case an error in the password service occures
     *                          (2) if newsletter is null
     *                          (3) if employee is not set
     */
    public String getEmailPassword(Newsletter newsletter) throws ServiceException {
        if(newsletter == null) {
            throw  new ServiceException("Newsletter is a null pointer.");
        }
        if(newsletter.getEmployee() == null){
            throw new ServiceException("Employee is a null pointer.");
        }
        return passwordService.getEmailPassword(newsletter.getEmployee());
    }
    /**
     * This method is used to get the hotel which has the port and host set for the e-mail
     * @return Hotel object with set host and port
     * @throws ServiceException - in case the DAO doesn't work how it is supposed to
     */
    public Hotel getHotel()throws ServiceException{
        Hotel hotel;
        try {
            hotel = hotelService.get();
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
        return hotel;
    }
}