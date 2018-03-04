package main.java.ac.at.tuwien.sepm.QSE15.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.UserNotValidException;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.authenticationService.MyAuthenticationService;
import main.java.ac.at.tuwien.sepm.QSE15.service.hotelService.MyHotelService;
import main.java.ac.at.tuwien.sepm.QSE15.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
/**
 * Implementation - Ervin Cosic
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class LoginViewController implements Initializable{

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginViewController.class);

    private Stage primaryStage;

    private AnnotationConfigApplicationContext context;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private MyAuthenticationService authenticationService;

    Employee loggedInEmployee;

    private MyHotelService hotelService;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        context = new AnnotationConfigApplicationContext(this.getClass());

        authenticationService = context.getBean(MyAuthenticationService.class);

        hotelService = context.getBean(MyHotelService.class);




    }

    /**
     * This method is called when the login button on the screen is clicked
     */
    public void onLoginButtonClicked(){
        Employee loginTry = parseLoginParameters();



        messageLabel.setText("");

        try {
            if(loginTry == null){
                throw new UserNotValidException();
            }

            loggedInEmployee = authenticationService.validateUsernameAndPassword(loginTry);



        } catch (UserNotValidException e) {


            messageLabel.setText("Invalid username or password.");
            return;
        }

        if(loggedInEmployee != null){
            openApplication();
        }

    }

    /**
     * This method takes the username and password field values and returns them in an employee object
     * @return employee with username adn password
     */
    private Employee parseLoginParameters(){

        Employee employeeToLogin = new Employee();

        messageLabel.setText("");

        if(usernameField.getText().equals("") || passwordField.getText().equals("")){

            messageLabel.setText("The username or password can't be empty.");

            return null;
        }else if(passwordField.getText().length() > 16){

            messageLabel.setText("The password cannot be longer than 16 characters.");

            return null;
        }

        employeeToLogin.setUsername(usernameField.getText());

        employeeToLogin.setPassword(authenticationService.encryptPassword(passwordField.getText()));

        return employeeToLogin;
    }

    /**
     * This method chooses which screen to load based on the rights the employee has
     */
    private void openApplication(){

        FXMLLoader loader;

        boolean hotelSet = false;

        try {
            hotelSet = hotelService.isHotelSet();
        } catch (ServiceException e) {
            Utility.showAlert("Error while loading the hotel.", Alert.AlertType.ERROR);

        }

        if(hotelSet) {

            loader = new FXMLLoader(this.getClass().getResource("/res/layouts/MainWindow.fxml"));

            LOGGER.info("Main screen loaded.");

        }else{

            loader = new FXMLLoader(this.getClass().getResource("/res/layouts/hotelSetup/HotelSetupLayout.fxml"));

            LOGGER.info("Hotel setup loading...");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Hotel setup.");
            alert.setHeaderText(null);
            alert.setContentText("You need to set up your hotel first!");

            alert.showAndWait();
        }


        try {
            AnchorPane pane = loader.load();

            Scene scene = new Scene(pane);

            //scene.getStylesheets().add(getClass().getResource("/res/layouts/style.css").toString());

            Stage hotelSetupStage = new Stage();

            Stage stg = primaryStage;

            if(hotelSet) {

                try {


                    MainWindowController mainWindowController = loader.getController();
                    Main.mainWindowController = mainWindowController;

                    mainWindowController.setLoggedEmployee(loggedInEmployee);

                    Stage loginstage = primaryStage;
                    primaryStage = hotelSetupStage;
                    mainWindowController.setUserNameText(loggedInEmployee);



                    primaryStage.setHeight(837);
                    primaryStage.setWidth(1272);


                    primaryStage.setScene(scene);
                    primaryStage.setResizable(false);
                    primaryStage.setTitle("HSM");

                    mainWindowController.setCurrentStage(primaryStage);

                    primaryStage.show();
                    loginstage.close();
                    mainWindowController.setScrollBarBinding();
                    mainWindowController.checkRights();


                    LOGGER.info("Opening screen.");

                    stg.close();

                } catch (ClassCastException e) {

                }

            }else {

                hotelSetupStage.setTitle("Hotel setup");
                hotelSetupStage.setScene(scene);
                Main.mainWindowController.setLoggedEmployee(loggedInEmployee);
                hotelSetupStage.show();
                stg.close();

            }


        } catch (IOException e) {

            LOGGER.error("IO Exception: " + e.getMessage());

            Utility.showError(e.getMessage());
        }


    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Autowired
    public void setAuthenticationService(MyAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
}
