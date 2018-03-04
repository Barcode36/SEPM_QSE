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
 * Created by Stefan Puhalo on 6/6/2017.
 */

@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")

public class CustomerDetailsController {

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
    ImageView cancelImageView;

    @FXML
    ImageView editImageView;

    @FXML
    AnchorPane anchorPane;

    private AnnotationConfigApplicationContext context;

    private static final Logger logger = LoggerFactory.getLogger(CustomerDetailsController.class);

    CustomerServiceIMPL customerService;
    CreditCardServiceIMPL creditCardService;

    Customer customer;

    CreditCard creditCard;

    private Date dateOfBirth;
    String sex;
    Integer rid;

    String customerType;

    HashMap<String, Integer> customersMap;


    public void initialize() throws ServiceException {

        context = new AnnotationConfigApplicationContext(this.getClass());
        customerService = context.getBean(CustomerServiceIMPL.class);
        creditCardService = context.getBean(CreditCardServiceIMPL.class);

        logger.info("INFO: Entering Customer Details stage.");

        customer = CustomerManagerController.selectedCustomer;
        creditCard = customer.getCreditCard();

        dateOfBirth = customer.getBdate();


        customersMap = customerService.getReservationsOfAllCustomers();

        for(String fullName : customersMap.keySet()) {

            inHotelWithChoiceBox.getItems().add(fullName);
        }

        titleChoiceBox.getItems().add("Mr");
        titleChoiceBox.getItems().add("Mrs");
        titleChoiceBox.getItems().add("Miss");

        typeChoiceBox.getItems().add("Customer");
        typeChoiceBox.getItems().add("Guest");

        for(int i=1; i<=12; i++) {
            monthChoiceBox.getItems().add(i);
        }

        for(int i = LocalDate.now().getYear(); i < LocalDate.now().getYear() + 10; i++) {
            yearChoiceBox.getItems().add(i);
        }

        creditCardTypeChoiceBox.getItems().add("Visa");
        creditCardTypeChoiceBox.getItems().add("Mastercard");
        creditCardTypeChoiceBox.getItems().add("American Express");
        creditCardTypeChoiceBox.getItems().add("Maestro");

        birthDatePicker.valueProperty().setValue(customer.getBdate().toLocalDate());
        firstNameField.setText(customer.getName());
        lastNameField.setText(customer.getSurname());
        phoneField.setText(customer.getPhone());
        emailField.setText(customer.getEmail());
        identificationField.setText(customer.getIdentification());
        streetField.setText(customer.getAddress());
        cityField.setText(customer.getPlace());
        countryField.setText(customer.getCountry());
        postalCodeField.setText(customer.getZip());
        notesArea.setText(customer.getNote());

        if(customer.getSex().equals("male")) {
            titleChoiceBox.getSelectionModel().select("Mr");
        }else {
            titleChoiceBox.getSelectionModel().select("Mrs");
        }


        if(customer.getNewsletter()) {
            newsletterCheckBox.selectedProperty().setValue(true);
        }else {
            newsletterCheckBox.selectedProperty().setValue(false);
        }

        rid = customer.getRid();


        typeChoiceBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                if (newValue.equals("Customer")) {
                    typeChoiceBox.getSelectionModel().select("Customer");
                    inHotelWithChoiceBox.setVisible(false);
                    inHotelWithLabel.setVisible(false);
                    creditCardTypeChoiceBox.setVisible(true);
                    creditCardTypeChoiceBox.getSelectionModel().select(customer.getCreditCard().getCardType());
                    monthChoiceBox.setVisible(true);
                    monthChoiceBox.getSelectionModel().select(customer.getCreditCard().getExpMonth());
                    yearChoiceBox.setVisible(true);
                    yearChoiceBox.getSelectionModel().select(customer.getCreditCard().getExpYear());
                    creditCardNumberField.setVisible(true);
                    creditCardNumberField.setText(customer.getCreditCard().getCnr());
                    holderField.setVisible(true);
                    holderField.setText(customer.getCreditCard().getHolder());
                    cvvField.setVisible(true);
                    cvvField.setText(customer.getCreditCard().getCvv());
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
                    editImageView.setLayoutY(718.0);
                    cancelImageView.setLayoutY(753.0);
                    //anchorPane.getScene().getWindow().setHeight(841.0);

                } else {

                    inHotelWithChoiceBox.setVisible(true);
                    inHotelWithLabel.setVisible(true);
                    inHotelWithChoiceBox.getSelectionModel().select(getKeyFromValue(customersMap, customer.getRid()));
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
                    editImageView.setLayoutY(623.0);
                    cancelImageView.setLayoutY(658.0);
                    //anchorPane.getScene().getWindow().setHeight(751.0);

                }
            }
        });

        if(rid == 0) {
            typeChoiceBox.getSelectionModel().select("Customer");
            customerType = "Customer";
        }else {
            typeChoiceBox.getSelectionModel().select("Guest");
            customerType = "Guest";
        }

        birthDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                dateOfBirth = Date.valueOf(newValue);

            }
        });

    }

    public void clickOnEdit() {

        customer.setName(firstNameField.getText());
        customer.setSurname(lastNameField.getText());
        customer.setEmail(emailField.getText());
        customer.setPhone(phoneField.getText());
        customer.setIdentification(identificationField.getText());
        customer.setAddress(streetField.getText());
        customer.setPlace(cityField.getText());
        customer.setCountry(countryField.getText());
        customer.setZip(postalCodeField.getText());
        customer.setBdate(dateOfBirth);
        customer.setNote(notesArea.getText());

        if(newsletterCheckBox.isSelected()) {
            customer.setNewsletter(true);
        }else {
            customer.setNewsletter(false);
        }

        if(titleChoiceBox.getSelectionModel().getSelectedItem().equals("Mr")) {
            customer.setSex("male");
        }else {
            customer.setSex("female");
        }

        if(creditCardTypeChoiceBox.isVisible()) {

            if(creditCardTypeChoiceBox.getValue() == null || creditCardNumberField.getText().equals("") ||
                    holderField.getText().equals("") || cvvField.getText().equals("") || monthChoiceBox.getValue() == null ||
                    yearChoiceBox.getValue() == null) {
                Utility.showError("Please fill in all credit card details");
                return;
            }else {
                creditCard.setCardType(creditCardTypeChoiceBox.getValue().toString());
                creditCard.setCnr(creditCardNumberField.getText());
                creditCard.setHolder(holderField.getText());
                creditCard.setCvv(cvvField.getText());
                creditCard.setExpMonth(Integer.parseInt(monthChoiceBox.getValue().toString()));
                creditCard.setExpYear(Integer.parseInt(yearChoiceBox.getValue().toString()));

                try {
                    if(customerType.equals("Customer")) {
                        creditCardService.updateCreditCard(creditCard);
                    }else {
                        creditCardService.createCreditCard(creditCard);
                    }

                } catch (ServiceException e) {
                    logger.error("Update credit card failed.");
                }

            }
        }

        if(typeChoiceBox.getSelectionModel().getSelectedItem().equals("Guest")) {
            customer.setRid(customersMap.get(inHotelWithChoiceBox.getSelectionModel().getSelectedItem().toString()));
        }else {
            customer.setRid(0);
        }

        try {
            customerService.updateCustomer(customer);
        } catch (ServiceException e) {
            logger.error("Update customer failed.");

        }

        Utility.showAlert("Customer successfully updated.", Alert.AlertType.CONFIRMATION);

        clickCancel();

    }

    public void clickCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        logger.info("INFO: Customer Details stage canceled.");
        stage.close();
    }

    public static Object getKeyFromValue(HashMap hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }


}
