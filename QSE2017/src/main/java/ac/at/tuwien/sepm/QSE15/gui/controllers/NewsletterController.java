package main.java.ac.at.tuwien.sepm.QSE15.gui.controllers;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.ac.at.tuwien.sepm.QSE15.entity.newsletter.Newsletter;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Person;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.UserNotValidException;
import main.java.ac.at.tuwien.sepm.QSE15.service.NewsletterService.NewsletterService;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.authenticationService.MyAuthenticationService;
import main.java.ac.at.tuwien.sepm.QSE15.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.rmi.CORBA.Util;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
/**
 * Created by ervincosic on 26/06/2017.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class NewsletterController implements Initializable {
    @FXML
    private TextField subjectTextField;
    @FXML
    private TextArea messageTextArea;
    private String subject;
    private String message;
    private MyAuthenticationService authenticationService;
    private NewsletterService newsletterService;
    private AnnotationConfigApplicationContext context;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        context = new AnnotationConfigApplicationContext(this.getClass());
        authenticationService = context.getBean(MyAuthenticationService.class);
        newsletterService = context.getBean(NewsletterService.class);
    }
    /**
     * Saves the subject and message in the newsletter object
     * @return (1) null if subject or message is empty
     *         (2) Newsletter object if everything is ok
     */
    private Newsletter loadFields(){
        Newsletter  newsletter = new Newsletter();
        subject = subjectTextField.getText();
        message = messageTextArea.getText();
        if(subject.length() == 0 ){
            return null;
        }else if(message.length() == 0){
            return null;
        }
        newsletter.setMessage(message);
        newsletter.setSubject(subject);
        return newsletter;
    }
    /**
     * This method is invoked with the send button
     */
    public void sendNewsLetter(){
        String userPassword = Utility.showPasswordInputDialog();
        Employee employee = Main.mainWindowController.getLoggedEmployee();
        try {
            employee = authenticationService.validateUsernameAndPassword(employee);
            employee.setPassword(userPassword);
        } catch (UserNotValidException e) {
            Utility.showError("Invalid password");
            return;
        }
        employee.setPassword(userPassword);
        if(employee == null){
            return;
        }
        Newsletter newsletter = loadFields();
        if(newsletter == null){
            return;
        }
        newsletter.setEmployee(employee);
        boolean flag = false;
        try {
            flag = newsletterService.sendNewsletter(newsletter);
        } catch (ServiceException e) {
        }
        if(flag){
            Utility.showAlert("Newsletter successfully sent", Alert.AlertType.INFORMATION);

        }else{
            Utility.showError("Cannot send newsletter.");
        }
    }
}