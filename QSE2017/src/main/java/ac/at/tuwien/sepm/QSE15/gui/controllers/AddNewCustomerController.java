package main.java.ac.at.tuwien.sepm.QSE15.gui.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.java.ac.at.tuwien.sepm.QSE15.entity.creditCard.CreditCard;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Customer;
import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.creditCardService.CreditCardServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.service.customerService.CustomerServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;

/**
 * Created by Stefan Puhalo on 5/22/2017.
 */

@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class AddNewCustomerController {

    @FXML
    TextField firstNameField;

    @FXML
    TextField lastNameField;

    @FXML
    TextField phoneField;

    @FXML
    TextField emailField;

    @FXML
    TextField streetField;

    @FXML
    TextField cityField;

    @FXML
    TextField countryField;

    @FXML
    TextField postalCodeField;

    @FXML
    ChoiceBox typeChoiceBox;

    @FXML
    ChoiceBox titleChoiceBox;

    @FXML
    DatePicker birthDatePicker;

    @FXML
    TextField creditCardNumberField;

    @FXML
    TextField identificationField;

    @FXML
    ChoiceBox creditCardTypeChoiceBox;

    @FXML
    ChoiceBox monthChoiceBox;

    @FXML
    ChoiceBox yearChoiceBox;

    @FXML
    ChoiceBox inHotelWithChoiceBox;

    @FXML
    TextField holderField;

    @FXML
    TextField cvvField;

    @FXML
    Button cancelButton;

    @FXML
    CheckBox newsletterCheckBox;

    @FXML
    TextArea notesArea;

    @FXML
    Label inHotelWithLabel;

    @FXML
    Label creditCardDetailsLabel;

    @FXML
    Label creditCardTypeLabel;

    @FXML
    Label holderLabel;

    @FXML
    Label cardNumberLabel;

    @FXML
    Label expirationDateLabel;

    @FXML
    Label slashLabel;

    @FXML
    Label cvvLabel;

    @FXML
    ImageView creditCardImageView;

    @FXML
    Label notesLabel;

    @FXML
    Separator separator;

    @FXML
    Button addButton;

    @FXML
    ImageView addImageView;

    @FXML
    ImageView cancelImageView;

    @FXML
    AnchorPane anchorPane;



    private AnnotationConfigApplicationContext context;
    private Stage primaryStage;

    private static final Logger logger = LoggerFactory.getLogger(AddNewCustomerController.class);

    CustomerServiceIMPL customerService;
    CreditCardServiceIMPL creditCardService;

    CreditCard creditCard;

    private Date dateOfBirth = Date.valueOf(LocalDate.of(1985,1,1));

    String sex;
    Integer rid = 0;
    Integer month = 0, year = 0;
    String cardType = "";
    Boolean newsletter = false;


    public void initialize() throws ServiceException {

        context = new AnnotationConfigApplicationContext(this.getClass());
        customerService = context.getBean(CustomerServiceIMPL.class);
        creditCardService = context.getBean(CreditCardServiceIMPL.class);

        logger.info("INFO: Entering Add Customer stage.");

        HashMap<String, Integer> customersMap = customerService.getReservationsOfAllCustomers();

        for(String fullName : customersMap.keySet()) {

            inHotelWithChoiceBox.getItems().add(fullName);
        }

        titleChoiceBox.getItems().add("Mr");
        titleChoiceBox.getItems().add("Mrs");
        titleChoiceBox.getItems().add("Miss");

        typeChoiceBox.getItems().add("Customer");
        typeChoiceBox.getItems().add("Guest");

        typeChoiceBox.getSelectionModel().select("Customer");

        inHotelWithChoiceBox.setVisible(false);
        inHotelWithLabel.setVisible(false);

        for(int i=1; i<=12; i++) {
            monthChoiceBox.getItems().add(i);
        }

        for(int i = LocalDate.now().getYear(); i < LocalDate.now().getYear() + 10; i++) {
            yearChoiceBox.getItems().add(i);
        }

        creditCardTypeChoiceBox.getItems().add("Visa");
        creditCardTypeChoiceBox.getItems().add("Mastercard");
        creditCardTypeChoiceBox.getItems().add("Maestro");
        creditCardTypeChoiceBox.getItems().add("American Express");

        birthDatePicker.valueProperty().setValue(LocalDate.of(1985,1,1));

        birthDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                dateOfBirth = Date.valueOf(newValue);
            }
        });

        typeChoiceBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue.toString().equals("Guest")) {
                    inHotelWithChoiceBox.setVisible(true);
                    inHotelWithLabel.setVisible(true);
                    creditCardTypeChoiceBox.setVisible(false);
                    monthChoiceBox.setVisible(false);
                    yearChoiceBox.setVisible(false);
                    creditCardNumberField.setVisible(false);
                    holderField.setVisible(false);
                    cvvField.setVisible(false);
                    creditCardDetailsLabel.setVisible(false);
                    cardNumberLabel.setVisible(false);
                    creditCardTypeLabel.setVisible(false);
                    holderLabel.setVisible(false);
                    expirationDateLabel.setVisible(false);
                    slashLabel.setVisible(false);
                    cvvLabel.setVisible(false);
                    creditCardImageView.setVisible(false);
                    separator.setVisible(false);
                    notesLabel.setLayoutY(507.0);
                    notesArea.setLayoutY(542.0);
                    newsletterCheckBox.setLayoutY(542.0);
                    addButton.setLayoutY(626.0);
                    cancelButton.setLayoutY(661.0);
                    addImageView.setLayoutY(623.0);
                    cancelImageView.setLayoutY(658.0);
                    anchorPane.getScene().getWindow().setHeight(751.0);
                    rid = 0;
                }else {
                    inHotelWithChoiceBox.setVisible(false);
                    inHotelWithLabel.setVisible(false);
                    creditCardTypeChoiceBox.setVisible(true);
                    monthChoiceBox.setVisible(true);
                    yearChoiceBox.setVisible(true);
                    creditCardNumberField.setVisible(true);
                    holderField.setVisible(true);
                    cvvField.setVisible(true);
                    creditCardDetailsLabel.setVisible(true);
                    cardNumberLabel.setVisible(true);
                    creditCardTypeLabel.setVisible(true);
                    holderLabel.setVisible(true);
                    expirationDateLabel.setVisible(true);
                    slashLabel.setVisible(true);
                    cvvLabel.setVisible(true);
                    creditCardImageView.setVisible(true);
                    separator.setVisible(true);
                    notesLabel.setLayoutY(681.0);
                    notesArea.setLayoutY(710.0);
                    newsletterCheckBox.setLayoutY(686.0);
                    addButton.setLayoutY(721.0);
                    cancelButton.setLayoutY(756.0);
                    addImageView.setLayoutY(718.0);
                    cancelImageView.setLayoutY(753.0);
                    anchorPane.getScene().getWindow().setHeight(841.0);
                }
            }
        });

        titleChoiceBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(newValue != null) { // when clearing the choice box this method is invoked and throws a null pointer exception
                    if (newValue.equals("Mr")) {
                        sex = "male";
                    } else {
                        sex = "female";
                    }
                }
            }
        });

        inHotelWithChoiceBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                rid = customersMap.get(newValue.toString());
            }
        });

        creditCardTypeChoiceBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                cardType = newValue.toString();
            }
        });

        monthChoiceBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                month = Integer.parseInt(newValue.toString());
            }
        });

        yearChoiceBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                year = Integer.parseInt(newValue.toString());
            }
        });

        newsletterCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                newsletter = newValue;
            }
        });

    }

    public void clickOnAdd() {

        Customer customer = null;

        if(titleChoiceBox.getSelectionModel().isEmpty() || firstNameField.getText().equals("") ||
                lastNameField.getText().equals("") || phoneField.getText().equals("") || emailField.getText().equals("") ||
                identificationField.getText().equals("")) {
            Utility.showError("Please fill in all personal details of new customer.");
            return;
        }

        if(cityField.getText().equals("") || streetField.getText().equals("") || countryField.getText().equals("") ||
                postalCodeField.getText().equals("")) {
            Utility.showError("Please fill in all address details of new customer.");
            return;
        }

        if(typeChoiceBox.getSelectionModel().getSelectedItem().equals("Customer")) {

            if(creditCardTypeChoiceBox.getSelectionModel().isEmpty() || monthChoiceBox.getSelectionModel().isEmpty()
                    || yearChoiceBox.getSelectionModel().isEmpty() || cvvField.getText().isEmpty()
                    || holderField.getText().isEmpty() || creditCardNumberField.getText().isEmpty()) {
                Utility.showError("Please fill in all credit card details of new customer.");
                return;
            } else {
                try {
                    creditCard = creditCardService.createCreditCard(new CreditCard(creditCardNumberField.getText(), holderField.getText(),
                            cardType,month,year,cvvField.getText()));
                    customer = new Customer(null, firstNameField.getText(), lastNameField.getText(),
                            streetField.getText(), postalCodeField.getText(), cityField.getText(), countryField.getText(),
                            phoneField.getText(), emailField.getText(), dateOfBirth, sex,identificationField.getText(),
                            creditCard, notesArea.getText(), newsletter, rid);
                    customerService.createCustomer(customer);
                    logger.info("Credit Card created");
                    logger.error("Customer created");
                }catch (ServiceException e) {
                    logger.error("Credit Card not created");
                    logger.error("Customer creation failed");
                }
            }
        }else {
            try {
                customer = new Customer(null, firstNameField.getText(), lastNameField.getText(),
                        streetField.getText(), postalCodeField.getText(), cityField.getText(), countryField.getText(),
                        phoneField.getText(), emailField.getText(), dateOfBirth, sex,identificationField.getText(),
                        notesArea.getText(), newsletter, rid);
                customerService.createCustomer(customer);
                logger.info("Guest created");
            } catch (ServiceException e) {
                logger.error("Customer creation failed");
            }

        }
        Utility.showAlert("Customer with ID = " + customer.getPid() + " successfully added.", Alert.AlertType.CONFIRMATION);
        clickCancel();
    }

    public void clickCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        logger.info("INFO: Add new Customer stage canceled.");
        stage.close();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public void setFirstNameField(String firstNameField) {
        logger.info("Name is " + firstNameField);
        this.firstNameField.setText(firstNameField);
    }
    public void setLastNameField(String lastNameField) {
        logger.info("Name is " + lastNameField);
        this.lastNameField.setText(lastNameField);
    }
    public void setDateOfBirth(LocalDate birth) {
        this.birthDatePicker.setValue(birth);
    }




}
