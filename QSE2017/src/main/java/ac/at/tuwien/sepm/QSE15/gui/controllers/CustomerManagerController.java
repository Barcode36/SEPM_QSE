package main.java.ac.at.tuwien.sepm.QSE15.gui.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Customer;

import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.customerService.CustomerServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;


import java.sql.Date;
import java.time.LocalDate;

import static javafx.scene.control.Alert.AlertType.CONFIRMATION;

/**
 * Created by Stefan Puhalo on 5/22/2017.
 */

@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class CustomerManagerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerManagerController.class);

    @FXML
    TableView<Customer> tableView;

    @FXML
    TableColumn<Customer, Integer> pidColumn;

    @FXML
    TableColumn<Customer, String> nameColumn;

    @FXML
    TableColumn<Customer, String> surnameColumn;

    @FXML
    TableColumn<Customer, Date> birthDateColumn;

    @FXML
    TableColumn<Customer, Integer> isCustomerColumn;

    @FXML
    TableColumn<Customer, String> countryColumn;

    @FXML
    ChoiceBox statusChoiceBox;

    @FXML
    ChoiceBox filterChoiceBox;

    @FXML
    TextField fromTextField;

    @FXML
    TextField toTextField;

    @FXML
    private Button editButton;

    @FXML
    private Button cancelButton;

    @FXML
    DatePicker fromDatePicker;

    @FXML
    DatePicker toDatePicker;


    private static ObservableList<Customer> customers = FXCollections.observableArrayList();

    private AnnotationConfigApplicationContext context;

    CustomerServiceIMPL customerService;

    private String selectedFilter = "None";

    protected static Customer selectedCustomer;


    public void initialize() {
        context = new AnnotationConfigApplicationContext(this.getClass());
        customerService = context.getBean(CustomerServiceIMPL.class);
        logger.info("INFO: Entering Customer Manager stage.");

        fromDatePicker.setVisible(false);
        fromDatePicker.valueProperty().setValue(LocalDate.of(1985,1,1));
        toDatePicker.setVisible(false);
        toDatePicker.valueProperty().setValue(LocalDate.of(1985,1,1));

        editButton.setDisable(true);

        statusChoiceBox.getItems().add("All");
        statusChoiceBox.getItems().add("Customer");
        statusChoiceBox.getItems().add("Guest");

        statusChoiceBox.getSelectionModel().select("All");

        filterChoiceBox.getItems().add("None");
        filterChoiceBox.getItems().add("Name");
        filterChoiceBox.getItems().add("Surname");
        filterChoiceBox.getItems().add("Date of Birth");
        filterChoiceBox.getItems().add("Country");

        filterChoiceBox.getSelectionModel().select("None");

        pidColumn.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("Pid"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("Name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("Surname"));
        birthDateColumn.setCellValueFactory(new PropertyValueFactory<Customer, Date>("Bdate"));
        isCustomerColumn.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("Rid"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("Country"));


        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(
                event -> {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setName(event.getNewValue().substring(0, 1).toUpperCase() + event.getNewValue().substring(1));
                    editButton.setDisable(false);
                }
        );

        surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameColumn.setOnEditCommit(
                event -> {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setSurname(event.getNewValue().substring(0, 1).toUpperCase() + event.getNewValue().substring(1));
                    editButton.setDisable(false);
                }
        );

        countryColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        countryColumn.setOnEditCommit(
                event -> {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setCountry(event.getNewValue().substring(0, 1).toUpperCase() + event.getNewValue().substring(1));
                    editButton.setDisable(false);
                }
        );


        birthDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Date>() {
            @Override
            public String toString(Date object) {
                if(object != null) {
                    return object.toString();
                }else {
                    return "";
                }
            }

            @Override
            public Date fromString(String string) {
                try {
                    return Date.valueOf(string);
                }catch (Exception e) {
                    Utility.showError("Please add check-in date in format YYYY-MM-DD");
                    return null;
                }
            }
        }));

        birthDateColumn.setOnEditCommit(
                event -> {
                    if(event.getNewValue() != null && !event.getNewValue().equals("")) {
                        if(Date.valueOf(event.getNewValue().toString()).before(Date.valueOf(LocalDate.now().minusYears(12)))) {
                            (event.getTableView().getItems().get(event.getTablePosition().getRow())).setBdate(Date.valueOf(event.getNewValue().toString()));
                            editButton.setDisable(false);
                        }else {
                            Utility.showError("Customer must be older then 12 years");
                            editButton.setDisable(true);
                        }
                    }else {
                        editButton.setDisable(true);

                    }
                }
        );

        isCustomerColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                if(object.toString().equals("0"))
                    return "Customer";
                else return "Guest";
            }

            @Override
            public Integer fromString(String string) {
                return null;
            }
        }));

        isCustomerColumn.setEditable(false);

        try {
            customers = customerService.getAllCustomers();
        } catch (ServiceException e) {
            logger.error("Get all customers failed.");
        }

        tableView.setItems(customers);
        tableView.setEditable(true);

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Customer>() {
            @Override
            public void changed(ObservableValue<? extends Customer> observable, Customer oldValue, Customer newValue) {
                selectedCustomer = newValue;
            }
        });

        filterChoiceBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                selectedFilter = newValue.toString();
                if(selectedFilter.equals("Date of Birth")) {
                    selectedFilter = "BDATE";
                    fromDatePicker.setVisible(true);
                    toDatePicker.setVisible(true);
                    fromDatePicker.valueProperty().setValue(LocalDate.of(1985,1,1));
                    toDatePicker.valueProperty().setValue(LocalDate.of(1985,1,1));
                    fromTextField.setVisible(false);
                    toTextField.setVisible(false);
                }else {
                    fromDatePicker.setVisible(false);
                    toDatePicker.setVisible(false);
                    fromTextField.setVisible(true);
                    toTextField.setVisible(true);
                }
            }
        });
    }

    public void clickOnApply() {

        Object from;
        Object to;
        String status = statusChoiceBox.getSelectionModel().getSelectedItem().toString();

        if(fromTextField.isVisible() && toTextField.isVisible()) {
            from = fromTextField.getText();
            to = toTextField.getText();
        }else {
            from = fromDatePicker.getValue();
            to = toDatePicker.getValue();

            if(Date.valueOf(from.toString()).after(Date.valueOf(to.toString()))) {
                Utility.showError("Please select From date before To date");
                return;
            }
        }

        if(!selectedFilter.equals("None") && (from.equals("") || to.equals(""))) {
            Utility.showError("Please insert filter values in fields From and To or chose dates");
            return;
        }

        if(selectedFilter.equals("BDATE")) {

            if(from == null || to == null) {
                Utility.showError("Please choose Date range.");
                return;
            }
        }

        if(!status.equals("All") && !selectedFilter.equals("None")) {

            try {
                customers = customerService.getAllCustomersForTypeFromTo(selectedFilter, status, from,to);
            } catch (ServiceException e) {
            }

        }

        if(status.equals("All") && !selectedFilter.equals("None")) {
            try {
                customers = customerService.getAllCustomersFromTo(selectedFilter, from, to);
            } catch (ServiceException e) {

            }
        }

        if(!status.equals("All") && selectedFilter.equals("None")) {
            try {
                customers = customerService.getAllCustomersForType(status);

            } catch (ServiceException e) {
                logger.error("Get all customers for type failed.");
            }
        }

        if(status.equals("All") && selectedFilter.equals("None")) {
            try {
                customers = customerService.getAllCustomers();
            } catch (ServiceException e) {
                logger.error("Get all customers failed.");
            }

        }

        tableView.setItems(customers);

    }


    public void clickOnEdit() {
        try {
            customerService.updateAllCustomers(customers);
            Utility.showAlert("Customers successfully updated",CONFIRMATION);
            editButton.setDisable(true);
            logger.info("Customers updated");
        } catch (ServiceException e) {
            logger.error("Update all customers failed.");

        }
    }

    public void clickOnDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to permanent delete this customer?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if(alert.getResult() == ButtonType.YES) {
            if(selectedCustomer != null) {
                try {
                    customerService.deleteCustomer(selectedCustomer);
                    logger.info("Customer with ID = " + selectedCustomer.getPid() + " successfully deleted");
                    clickOnClearFilters();
                } catch (ServiceException e) {
                    logger.error("Delete customer failed.");
                }
            }else {
                Utility.showError("Please select a customer from table which you want to delete.");
            }
        }else {
            alert.close();
        }
    }


    public void clickOnAdd() {
        Stage stage = new Stage();
        stage.setTitle("Add Customer");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/layouts/customer/AddNewCustomer.fxml"));
            AnchorPane anchorPane = loader.load();
            Scene scene = new Scene(anchorPane);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            logger.error("Add customer stage can not be opened.");
        }
    }

    public void clickOnShowDetails() {

        Scene scene;

        if(selectedCustomer == null) {
            Utility.showError("Please select customer from the table.");
            return;
        }

        Stage stage = new Stage();
        stage.setTitle("Customer Details");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/layouts/customer/CustomerDetails.fxml"));
            AnchorPane anchorPane = loader.load();
            if(selectedCustomer.getRid() == 0) {
                scene = new Scene(anchorPane,646.0,802.0);
            }else {
                scene = new Scene(anchorPane,646.0,712.0);
            }
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            logger.error("Customer Details stage can not be opened");
        }
    }

    public void clickCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        logger.info("INFO: Customer manager stage canceled.");
        selectedCustomer = null;
        stage.close();
    }

    public void clickOnClearFilters() {
        statusChoiceBox.getSelectionModel().select("All");
        filterChoiceBox.getSelectionModel().select("None");
        fromTextField.clear();
        toTextField.clear();
        try {
            customers = customerService.getAllCustomers();
        } catch (ServiceException e) {
            logger.error("Get all customers failed.");
        }
        tableView.setItems(customers);
    }

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }


}