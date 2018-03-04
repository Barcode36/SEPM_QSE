package main.java.ac.at.tuwien.sepm.QSE15.gui.controllers.ReservationController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Customer;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.RoomReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.ServiceReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;
import main.java.ac.at.tuwien.sepm.QSE15.gui.controllers.AddNewCustomerController;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.customerService.CustomerService;
import main.java.ac.at.tuwien.sepm.QSE15.service.serviceReservationService.ServiceReservationService;
import main.java.ac.at.tuwien.sepm.QSE15.service.serviceService.ServiceService;
import main.java.ac.at.tuwien.sepm.QSE15.utility.Utility;
import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivana on 17.6.2017.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class CreateServiceReservationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateServiceReservationController.class);

    private Stage primaryStage;

    @FXML
    private TextField nameTF, surnameTF, serviceIDTF;

    @FXML
    private DatePicker birthdayDP, fromDP, untilDP, onDP;

    @FXML
    private CheckBox entireStay_CheckBox;

    @FXML
    private ComboBox<String> serviceType_ComboBox = new ComboBox<>();

    @FXML
    private Button addCustomerBT = new Button();

    @FXML
    private Button removeBT = new Button();

    @FXML
    private Button addBT = new Button();

    @FXML
    private Button createBT = new Button();

    @FXML
    private Button cancelBT = new Button();

    @FXML
    private ImageView fromIM, untilIM, onIM, birthdayIM, idIM, tickCrossICON;

    @FXML
    private TableView<Service> serviceList_Table = new TableView<>();
    @FXML
    private TableColumn<Service, String> serviceIDColumn, typeColumn, descriptionColumn, priceColumn;
    @FXML
    private TableView<ServiceReservation> toBeReserved_Table = new TableView<>();
    @FXML
    private TableColumn<ServiceReservation, String> serviceIDReservedColumn, fromColumn, untilColumn;

    private AnnotationConfigApplicationContext context;
    private ServiceReservationService serviceReservationService;
    private ServiceService serviceService;
    private CustomerService customerService;

    private List<Service> serviceList;
    private List<ServiceReservation> serviceReservationList = new ArrayList<>();
    private ObservableList<String> serviceTypesList_Combo;
    private List<Service> serviceTypesList;

    private Customer customerFound;
    private boolean found;
    private boolean customerSuccessfullyCreated;
    private List<Customer> customerList;

    private RoomReservation previousReservation;

    private String tick = "res/icons/tick.png";

    private boolean createOnlyServiceReservation = true;

    public void initialize(){

        context = new AnnotationConfigApplicationContext(this.getClass());
        serviceReservationService = context.getBean(ServiceReservationService.class);
        serviceService = context.getBean(ServiceService.class);
        customerService = context.getBean(CustomerService.class);

        createOnlyServiceReservationView(createOnlyServiceReservation);
        serviceIDTF.setEditable(false);

        toBeReserved_Table.setPlaceholder(new Label("Add here the services you would \nlike to reserve"));

        onDP.setDayCellFactory(onDayCellFactory);
        fromDP.setDayCellFactory(fromDayCellFactory);
        untilDP.setDayCellFactory(untilDayCellFactory);

        serviceTypesList_Combo = FXCollections.observableArrayList("Any");

        try {
            customerList = customerService.getAllCustomers();
        } catch (ServiceException e) {
            LOGGER.error("Unable to get all Customers !");
        }

        try {
            serviceTypesList = serviceService.getAllServicesFromReservation(new ServiceReservation(-1,null,null,null,null,null,null,null,null));

            for (int i = 0; i < serviceTypesList.size(); i++){

                if (!serviceTypesList_Combo.contains(serviceTypesList.get(i).getType())) {

                    serviceTypesList_Combo.add(serviceTypesList.get(i).getType());
                }
            }

        } catch (ServiceException e) {
            LOGGER.info("Unable to get all Service types");
        }
        serviceType_ComboBox.setItems(serviceTypesList_Combo);
        fillTableServices();
        setUpAutoFillTextField();

        Utility.changeFormatDatePicker(fromDP);
        Utility.changeFormatDatePicker(untilDP);
        Utility.changeFormatDatePicker(birthdayDP);
        Utility.changeFormatDatePicker(onDP);

        birthdayDP.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {

                if (createOnlyServiceReservation) {
                    if (!surnameTF.getText().equals("") && !nameTF.getText().equals("") && newValue != null) {

                        try {
                            customerFound = customerService.checkCustomer(nameTF.getText(), surnameTF.getText(), Date.valueOf(newValue));

                            found = (customerFound == null ? false : true);

                            if (!found) {
                                addCustomerBT.setDisable(false);
                                tickCrossICON.setImage(null);

                            } else {
                                addCustomerBT.setDisable(true);
                                customerSuccessfullyCreated = true;
                                tickCrossICON.setImage(new Image(tick));
                            }

                        } catch (ServiceException e) {
                            LOGGER.error("An error occurred while checking the Customer");
                        }

                    }
                }
            }
        });

        nameTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                if (createOnlyServiceReservation) {
                    if (!newValue.equals("") && !surnameTF.getText().equals("") && birthdayDP.getValue() != null) {

                        try {
                            customerFound = customerService.checkCustomer(newValue, surnameTF.getText(), Date.valueOf(birthdayDP.getValue()));

                            found = (customerFound == null ? false : true);

                            if (!found) {
                                addCustomerBT.setDisable(false);
                                tickCrossICON.setImage(null);

                            } else {
                                addCustomerBT.setDisable(true);
                                customerSuccessfullyCreated = true;
                                tickCrossICON.setImage(new Image(tick));
                            }

                        } catch (ServiceException e) {
                            LOGGER.error("An error occurred while checking the Customer");
                        }

                    }
                }
            }
        });

        surnameTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                if(createOnlyServiceReservation) {
                    if (!newValue.equals("") && !nameTF.getText().equals("") && birthdayDP.getValue() != null) {

                        try {
                            customerFound = customerService.checkCustomer(nameTF.getText(), newValue, Date.valueOf(birthdayDP.getValue()));

                            found = (customerFound == null ? false : true);

                            if (!found) {
                                addCustomerBT.setDisable(false);
                                tickCrossICON.setImage(null);

                            } else {
                                addCustomerBT.setDisable(true);
                                customerSuccessfullyCreated = true;
                                tickCrossICON.setImage(new Image(tick));
                            }

                        } catch (ServiceException e) {
                            LOGGER.error("An error occurred while checking the Customer and writing his the surname");
                        }
                    }
                }
            }
        });

        serviceList_Table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Service>() {
            public void changed(ObservableValue<? extends Service> observable, Service oldValue, Service newValue) {

                if (newValue != null ) {

                    serviceIDTF.setText(String.valueOf(newValue.getSrid()));

                } else {

                    serviceIDTF.clear();
                }
            }
        });

        serviceType_ComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {

                fillTableServices();
            }
        });
    }

    private void createOnlyServiceReservationView(boolean disable){

        birthdayDP.setEditable(disable);

        if (disable){
            birthdayDP.valueProperty().setValue(LocalDate.of(1990,1,1));
        }

        fromDP.setEditable(disable);
        fromDP.setVisible(disable);

        untilDP.setEditable(disable);
        untilDP.setVisible(disable);

        onDP.setEditable(!disable);
        onDP.setVisible(!disable);

        nameTF.setEditable(disable);
        surnameTF.setEditable(disable);

        fromIM.setVisible(disable);
        untilIM.setVisible(disable);
        onIM.setVisible(!disable);

        birthdayIM.setImage(new Image("/res/icons/birthday.png"));
        idIM.setImage(new Image("/res/icons/id2.png"));

        if (!disable){

            onIM.setImage(new Image("/res/icons/onDate.png"));

        }else {

            fromIM.setImage(new Image("res/icons/untilDate.png"));
            untilIM.setImage(new Image("res/icons/fromDate.png"));
        }

        entireStay_CheckBox.setVisible(!disable);
        entireStay_CheckBox.setDisable(disable);

        addCustomerBT.setVisible(disable);
        addCustomerBT.setDisable(true);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void fillTableToBeReserved() {

        serviceIDReservedColumn.setCellValueFactory(new PropertyValueFactory<ServiceReservation, String>("serviceID"));
        fromColumn.setCellValueFactory(new PropertyValueFactory<ServiceReservation, String>("from"));
        untilColumn.setCellValueFactory(new PropertyValueFactory<ServiceReservation, String>("until"));

        if (serviceReservationList != null) {
            toBeReserved_Table.setItems(FXCollections.observableArrayList(serviceReservationList));
            toBeReserved_Table.getSelectionModel().selectFirst();
        }
    }

    private void fillTableServices() {

        serviceIDColumn.setCellValueFactory(new PropertyValueFactory<Service, String>("srid"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Service, String>("type"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Service, String>("price"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Service, String>("description"));

        try {

            if(serviceType_ComboBox.getValue() == null || serviceType_ComboBox.getValue().equals("Any")) {

                serviceList = serviceService.getAllServicesFromReservation(new ServiceReservation(-1, null, null, null, null, null, null, null, null));

            }else {

                serviceList = serviceService.search(new Service(null, serviceType_ComboBox.getValue(), null, null));
            }

            serviceList =  servicePriceToDouble(serviceList);

            if (serviceList != null) {
                serviceList_Table.setItems(FXCollections.observableArrayList(serviceList));
                serviceList_Table.getSelectionModel().selectFirst();
            }

        } catch (ServiceException e) {
            LOGGER.error("Could not fill Table Services");
        }
    }

    public void setNameTF(String name) {
        this.nameTF.setText(name);
    }

    public void setSurnameTF(String surname) {
        this.surnameTF.setText(surname);
    }

    public void setBirthdayDP(LocalDate birth) {
        this.birthdayDP.setValue(birth);
    }

    public void setPreviousReservation(RoomReservation r){
        this.previousReservation = r;
    }

    public void setCreateOnlyServiceReservation(boolean t){
        this.createOnlyServiceReservation = t;
        createOnlyServiceReservationView(t);
    }

    public void onAddCustomerClicked(ActionEvent actionEvent) {

        try {

            FXMLLoader loader = new FXMLLoader();
            AnchorPane root = loader.load(getClass().getResource("/res/layouts/customer/AddNewCustomer.fxml").openStream());

            AddNewCustomerController addNewCustomerController = (AddNewCustomerController)loader.getController();
            addNewCustomerController.setFirstNameField(nameTF.getText());
            addNewCustomerController.setLastNameField(surnameTF.getText());
            addNewCustomerController.setDateOfBirth(birthdayDP.getValue());

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            LOGGER.error("Unable to load Create Customer Window.");
        }
    }

    // adding Services that a customer wishes to reserve in the table
    public void onAddClicked(ActionEvent actionEvent) {

        if (createOnlyServiceReservation) {

            if (fromDP.getValue() != null && untilDP.getValue() != null && (fromDP.getValue().isBefore(untilDP.getValue()) || fromDP.getValue().equals(untilDP.getValue())) && !serviceIDTF.getText().equals("")) {

                ServiceReservation srr = new ServiceReservation(null, null, Date.valueOf(fromDP.getValue()), fromDP.getValue().equals(untilDP.getValue()) ? Date.valueOf(untilDP.getValue().plusDays(1)) : Date.valueOf(untilDP.getValue()), null, null, null, new Service(Integer.valueOf(serviceIDTF.getText()),null,null,null), null);
                srr.setServiceID(Integer.valueOf(serviceIDTF.getText()));
                serviceReservationList.add(srr);
                fillTableToBeReserved();

                //fromDP.setValue(null);
                //untilDP.setValue(null);

            } else {
                Utility.showAlert("", "Please select a service and from/until dates you want to reserve it!", "Not all fields have been filled out", Alert.AlertType.ERROR);
            }

        }else {

            if ((entireStay_CheckBox.isSelected() ^ onDP.getValue() != null) && !serviceIDTF.getText().equals("")) {

                ServiceReservation srr = new ServiceReservation(null, null, entireStay_CheckBox.isSelected() ? previousReservation.getFrom() : Date.valueOf(onDP.getValue()), entireStay_CheckBox.isSelected() ? previousReservation.getUntil() : Date.valueOf(onDP.getValue().plusDays(1)), null, null, null, new Service(Integer.valueOf(serviceIDTF.getText()),null,null,null), null);
                srr.setServiceID(Integer.valueOf(serviceIDTF.getText()));
                serviceReservationList.add(srr);
                fillTableToBeReserved();

                onDP.setValue(null);
                entireStay_CheckBox.setSelected(false);

            } else {
                Utility.showAlert("", "Please select a service and a date when you want to reserve it!", "Not all fields have been filled out", Alert.AlertType.ERROR);
            }
        }
        serviceType_ComboBox.setValue(null);
    }

    // removing services from the table "to be reserved"
    public void onRemoveClicked(ActionEvent actionEvent) {

        if (!serviceReservationList.isEmpty()){

            int index = toBeReserved_Table.getSelectionModel().getFocusedIndex();
            serviceReservationList.remove(index);

            fillTableToBeReserved();

        } else {
            Utility.showAlert("","There is no services that can be removed!","Services to be reserved table is empty", Alert.AlertType.ERROR);
        }
    }

    public void onCancelClicked(ActionEvent actionEvent){

        Stage stage = (Stage) cancelBT.getScene().getWindow();
        LOGGER.info("INFO: Create Service Reservation stage canceled.");
        stage.close();
    }

    public void onCreateClicked(ActionEvent actionEvent) {

        ServiceReservation sr;

        if (!surnameTF.getText().equals("") && !nameTF.getText().equals("") && birthdayDP.getValue() != null && !serviceReservationList.isEmpty()) {

            try {

                customerFound = customerService.checkCustomer(nameTF.getText(), surnameTF.getText(), Date.valueOf(birthdayDP.getValue()));

                if (customerFound != null) {

                    addCustomerBT.setDisable(true);
                    customerSuccessfullyCreated = true;

                } else {
                    Utility.showAlert("Please add a customer in order to continue!No Customer", "This reservation contains no customer", "No Customer", Alert.AlertType.ERROR);
                    return;
                }

            } catch (ServiceException e) {
                Utility.showAlert("", "Please fill all fields", "Creating Service Reservation Failed", Alert.AlertType.ERROR);
            }

            if (customerSuccessfullyCreated){

                Integer ridFromFirst = null;

                for (int i = 0; i < serviceReservationList.size(); i++){

                    sr = serviceReservationList.get(i);

                    try {

                        // Service Reservation for only 1 day
                        if (sr.getFrom().toLocalDate().isEqual(sr.getUntil().toLocalDate().minusDays(1))) {

                            // if its new service reservation
                            if (createOnlyServiceReservation) {

                                ServiceReservation local4;
                                local4 = serviceReservationService.create(new ServiceReservation(i == 0 ? null : ridFromFirst, customerFound.getPid(), sr.getFrom(), sr.getUntil(), null, false, false, new Service(Integer.valueOf(sr.getServiceID()), null, null, null), sr.getFrom()));
                                ridFromFirst = local4.getRid();

                                // if its part of a room reservation
                            } else {

                                serviceReservationService.create(new ServiceReservation(previousReservation.getRid(), previousReservation.getCostumerId(), previousReservation.getFrom(), previousReservation.getUntil(), null, null, false, new Service(Integer.valueOf(sr.getServiceID()), null, null, null), sr.getFrom()));

                            }
                            // Service Reservation for more days
                        } else {

                            List<LocalDate> periodAllDates = getSingleDatesBetweenTwoDates(sr.getFrom().toLocalDate(), sr.getUntil().toLocalDate());

                            for (int j = 0; j < periodAllDates.size(); j++) {

                                // if its new service reservation
                                if (createOnlyServiceReservation) {

                                    if (i == 0 && j == 0){

                                        ServiceReservation local1;
                                        local1 = serviceReservationService.create(new ServiceReservation(null, customerFound.getPid(), Date.valueOf(periodAllDates.get(i)), Date.valueOf(periodAllDates.get(i)), null, false, false, new Service(Integer.valueOf(sr.getServiceID()), null, null, null), Date.valueOf(periodAllDates.get(j))));
                                        ridFromFirst = local1.getRid();

                                    } else {

                                        serviceReservationService.create(new ServiceReservation(ridFromFirst, customerFound.getPid(), Date.valueOf(periodAllDates.get(i)), Date.valueOf(periodAllDates.get(i)), null, false, false, new Service(Integer.valueOf(sr.getServiceID()), null, null, null), Date.valueOf(periodAllDates.get(j))));
                                    }

                                    // if its part of a room reservation
                                } else {

                                    ServiceReservation local2;
                                    local2 = serviceReservationService.create(new ServiceReservation(previousReservation.getRid(), previousReservation.getCostumerId(), previousReservation.getFrom(), previousReservation.getUntil(), null, false, false, new Service(Integer.valueOf(serviceIDTF.getText()), null, null, null), Date.valueOf(periodAllDates.get(j))));
                                }
                            }
                        }

                    }catch (ServiceException e){
                        Utility.showAlert("", "Please check your input data and try again.", "Cannot create Service Reservation", Alert.AlertType.ERROR);
                    }
                }
            }

            Stage stage = (Stage) createBT.getScene().getWindow();
            LOGGER.info("INFO: Create Service Reservation stage canceled.");
            stage.close();

        } else {
            Utility.showAlert("Please select a service and dates you want to reserve it and fill the following fields: customer name, customer surname and customer birthday!", "Not all required fields have been filled out!", "Creating Service Reservation failed!", Alert.AlertType.ERROR);
        }
    }

    public List<LocalDate> getSingleDatesBetweenTwoDates(LocalDate start, LocalDate end){

        List<LocalDate> dates = new ArrayList<>();

        while(start.isBefore(end)){
            dates.add(start);
            start = start.plusDays(1);
        }

        return dates;
    }

    final Callback<DatePicker, DateCell> untilDayCellFactory =
            new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item.isBefore(
                                    fromDP.getValue().plusDays(1))
                                    ) {
                                setDisable(true);
                                setStyle("-fx-background-color: #ffc0cb;");
                            }
                        }
                    };
                }
            };

    final Callback<DatePicker, DateCell> fromDayCellFactory =
            new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item.isBefore(
                                    LocalDate.now())
                                    ) {
                                setDisable(true);
                                setStyle("-fx-background-color: #ffc0cb;");
                            }
                        }
                    };
                }
            };

    final Callback<DatePicker, DateCell> onDayCellFactory =
            new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item.isBefore(previousReservation.getFrom().toLocalDate())) {
                                setDisable(true);
                                setStyle("-fx-background-color: #ffc0cb;");
                            }

                            if (item.isAfter(previousReservation.getUntil().toLocalDate())){
                                setDisable(true);
                                setStyle("-fx-background-color: #ffc0cb;");
                            }
                        }
                    };
                }
            };

    public void setUpAutoFillTextField(){

        if (customerList != null) {

            List<String> names = new ArrayList<>();
            List<String> surnames = new ArrayList<>();

            for (Customer c : customerList) {

                if (c.getRid() == 0) {

                    names.add(c.getName());
                    surnames.add(c.getSurname());
                }
            }

            TextFields.bindAutoCompletion(nameTF,names);
            TextFields.bindAutoCompletion(surnameTF,surnames);
        }
    }

    public List<Service> servicePriceToDouble(List<Service> reservations){
        for (int i = 0; i<reservations.size(); i++){
            Long total = reservations.get(i).getPrice()/100;
            reservations.get(i).setPrice(total);
        }
        return reservations;
    }
}