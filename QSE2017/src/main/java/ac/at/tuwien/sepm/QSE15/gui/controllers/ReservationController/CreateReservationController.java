package main.java.ac.at.tuwien.sepm.QSE15.gui.controllers.ReservationController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.java.ac.at.tuwien.sepm.QSE15.entity.creditCard.CreditCard;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Customer;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.RoomReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.ServiceReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Category;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Room;
import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;
import main.java.ac.at.tuwien.sepm.QSE15.gui.controllers.AddNewCustomerController;
import main.java.ac.at.tuwien.sepm.QSE15.gui.controllers.Main;
import main.java.ac.at.tuwien.sepm.QSE15.gui.controllers.MainWindowController;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.customerService.CustomerService;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomReservationService.RoomReservationService;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomService.CategoryService;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomService.RoomService;
import main.java.ac.at.tuwien.sepm.QSE15.service.serviceReservationService.ServiceReservationService;
import main.java.ac.at.tuwien.sepm.QSE15.service.serviceService.ServiceService;
import main.java.ac.at.tuwien.sepm.QSE15.utility.Utility;
import org.controlsfx.control.textfield.TextFields;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import org.h2.table.Table;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javafx.scene.input.KeyCode.BACK_SPACE;

/**
 * Created by ivana on 21.5.2017.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class CreateReservationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateReservationController.class);

    @FXML
    public ImageView fromICON;
    @FXML
    public ImageView untilICON;
    @FXML
    public ImageView birthdayICON;
    @FXML
    public ImageView roomICON;
    @FXML
    public ImageView priceICON;
    @FXML
    public ImageView tickCrossICON;

    public AnchorPane pane;

    private Stage primaryStage;

    // FIELDS and BOXES
    @FXML
    private TextField nameTF, surnameTF, idTF, roomIDTF, priceUpToTF, guestNameTF, guestSurnameTF;
    @FXML
    private DatePicker birthdayDP, fromDP, untilDP;
    @FXML
    private CheckBox breakfast_CheckBox;
    @FXML
    private ComboBox<String> category_ComboBox = new ComboBox<>();
    @FXML
    private Integer customerID;
    @FXML
    private Button addCustomerBTN = new Button();
    @FXML
    private Button createBTN = new Button();


    // TABLES and TABLE COLUMNS
    @FXML
    private TableView<Room> freeRoomsTable = new TableView<>();
    @FXML
    private TableColumn<Room, String> rnrColumn, categoryColumn, bedsColumn, priceColumn, extrasColumn;
    @FXML
    private TableView<Customer> addGuestTable = new TableView<>();
    @FXML
    private TableColumn<Customer, String> guestNameColumn, guestSurnameColumn;


    // SERVICES needed in order to develop functionality
    private AnnotationConfigApplicationContext context;
    private RoomService roomService;
    private RoomReservationService roomReservationService;
    private ServiceReservationService serviceReservationService;
    private CustomerService customerService;
    private CategoryService categoryService;

    private Customer guest;
    private Customer customerFound;
    private boolean found;
    private boolean customerSuccessfullyCreated;

    private List<RoomReservation> roomReservationList;
    private List<Room> roomList;
    private List<Service> serviceList;
    private List<Category> categoryNames;
    private List<Customer> customerList;
    private List<Customer> guestList = new ArrayList<Customer>();

    private ObservableList<String> categoriesList;

    private LocalDate currentDate = LocalDate.now();

    private String tick = "res/icons/tick.png";
    private String cross = "res/icons/cross.png";
    private static String regexNum = "^\\d+$";
    private static String regexAlpha = "[a-zA-Z]+";


    public void initialize(){

        context = new AnnotationConfigApplicationContext(this.getClass());
        roomService = context.getBean(RoomService.class);
        roomReservationService = context.getBean(RoomReservationService.class);
        serviceReservationService = context.getBean(ServiceReservationService.class);
        customerService = context.getBean(CustomerService.class);
        categoryService = context.getBean(CategoryService.class);

        categoriesList = FXCollections.observableArrayList("Any");

        try {
            categoryNames = categoryService.getAll();
            customerList = customerService.getAllCustomers();

            for (int i = 0; i < categoryNames.size(); i++){

                categoriesList.add(categoryNames.get(i).getName());
            }

        } catch (ServiceException e) {
            LOGGER.info("Unable to get all Customer/Categories");
        }

        category_ComboBox.setItems(categoriesList);
        roomIDTF.setEditable(false);
        addCustomerBTN.setDisable(true);
        birthdayDP.valueProperty().setValue(LocalDate.of(1990,1,1));
        birthdayDP.setEditable(false);
        fromDP.setEditable(false);
        untilDP.setEditable(false);
        addGuestTable.setPlaceholder(new Label("No Travelers added yet"));
        freeRoomsTable.setPlaceholder(new Label("No free rooms searched yet"));
        setIcons();
        setUpAutoFillTextField();
        fromDP.setDayCellFactory(fromDayCellFactory);
        untilDP.setDayCellFactory(untilDayCellFactory);

        Utility.changeFormatDatePicker(fromDP);
        Utility.changeFormatDatePicker(untilDP);
        Utility.changeFormatDatePicker(birthdayDP);

        fromDP.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {

                if (untilDP.getValue() != null && !untilDP.getValue().equals("") && newValue.isBefore(untilDP.getValue())){

                    fillTableFreeRooms();
                }
            }
        });

        untilDP.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {

                if (fromDP.getValue() != null && !fromDP.getValue().equals("") && newValue.isAfter(fromDP.getValue())){

                    fillTableFreeRooms();
                }
            }
        });

        category_ComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {

                if (fromDP.getValue() != null && untilDP.getValue() != null && fromDP.getValue().isBefore(untilDP.getValue())) {

                    fillTableFreeRooms();
                }
            }
        });

        freeRoomsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Room>() {
            public void changed(ObservableValue<? extends Room> observable, Room oldValue, Room newValue) {

                if (newValue != null ) {

                    roomIDTF.setText(String.valueOf(newValue.getRnr()));

                } else {

                    roomIDTF.clear();
                }
            }
        });

        birthdayDP.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {

                if (!surnameTF.getText().equals("") && !nameTF.getText().equals("") && newValue != null){

                    try {
                        customerFound = customerService.checkCustomer(nameTF.getText(), surnameTF.getText(), Date.valueOf(newValue));

                        found = (customerFound == null ? false : true);

                        if (!found){
                            addCustomerBTN.setDisable(false);
                            tickCrossICON.setImage(null);

                        } else {
                            addCustomerBTN.setDisable(true);
                            customerSuccessfullyCreated = true;
                            tickCrossICON.setImage(new Image(tick));
                        }

                    } catch (ServiceException e) {
                        LOGGER.error("An error occurred while checking the Customer and searching his birthday");
                    }

                }
            }
        });

        nameTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                if (!newValue.equals("") && !surnameTF.getText().equals("") && birthdayDP.getValue() != null){

                    try {
                        customerFound = customerService.checkCustomer(newValue, surnameTF.getText(), Date.valueOf(birthdayDP.getValue()));

                        found = (customerFound == null ? false : true);

                        if (!found){
                            addCustomerBTN.setDisable(false);
                            tickCrossICON.setImage(null);
                        } else {
                            addCustomerBTN.setDisable(true);
                            customerSuccessfullyCreated = true;
                            tickCrossICON.setImage(new Image(tick));
                        }

                    } catch (ServiceException e) {
                        LOGGER.error("An error occurred while checking the Customer and checking his name");
                    }

                }
            }
        });

        surnameTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                if (!newValue.equals("") && !nameTF.getText().equals("") && birthdayDP.getValue() != null){

                    try {
                        customerFound = customerService.checkCustomer(nameTF.getText(), newValue, Date.valueOf(birthdayDP.getValue()));

                        found = (customerFound == null ? false : true);

                        if (!found){
                            addCustomerBTN.setDisable(false);
                            tickCrossICON.setImage(null);
                        } else {
                            addCustomerBTN.setDisable(true);
                            customerSuccessfullyCreated = true;
                            tickCrossICON.setImage(new Image(tick));
                        }

                    } catch (ServiceException e) {
                        LOGGER.error("An error occurred while checking the Customer");
                    }
                }
            }
        });
    }


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

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


    private void fillTableFreeRooms(){

        rnrColumn.setCellValueFactory(new PropertyValueFactory<Room, String>("rnr"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<Room, String>("category"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Room, String>("price"));
        extrasColumn.setCellValueFactory(new PropertyValueFactory<Room, String>("extras"));

        try {

            if (category_ComboBox.getValue() == null || category_ComboBox.getValue().equals("Any")){

                roomList = roomService.searchFree(new Room(), new Category(null, priceUpToTF.getText().equals("") || !priceUpToTF.getText().matches(regexNum) ? null : Long.valueOf(priceUpToTF.getText())*100, null), Date.valueOf(fromDP.getValue()), Date.valueOf(untilDP.getValue()));
            }else {

                roomList = roomService.searchFree(new Room(), new Category(category_ComboBox.getValue().equals("") ? null : String.valueOf(category_ComboBox.getValue()), priceUpToTF.getText().equals("") || !priceUpToTF.getText().matches(regexNum)? null : Long.valueOf(priceUpToTF.getText())*100, null), Date.valueOf(fromDP.getValue()), Date.valueOf(untilDP.getValue()));
            }

            roomList = roomPriceToDouble(roomList);

            if (roomList != null) {
                freeRoomsTable.setItems(FXCollections.observableArrayList(roomList));
                freeRoomsTable.getSelectionModel().selectFirst();
            }

        } catch (ServiceException e) {
            LOGGER.error("Unable to get list of all Rooms.");
        }
    }

    private void fillTableGuests(){

        guestNameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        guestSurnameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("surname"));

        if (guestList != null) {
            addGuestTable.setItems(FXCollections.observableArrayList(guestList));
            addGuestTable.getSelectionModel().selectFirst();
        }
    }

    public void onCreateReservationClicked(){

        RoomReservation rr;

        if (!roomIDTF.getText().equals("") && fromDP.getValue() != null && untilDP != null && !surnameTF.getText().equals("") && !nameTF.getText().equals("") && birthdayDP.getValue() != null) {

            if (validateAndShowAlert(nameTF.getText().matches(regexAlpha), "name") && validateAndShowAlert(surnameTF.getText().matches(regexAlpha), "surname")) {

                try {

                    customerFound = customerService.checkCustomer(nameTF.getText(), surnameTF.getText(), Date.valueOf(birthdayDP.getValue()));

                    if (customerFound != null) {

                        addCustomerBTN.setDisable(true);
                        customerSuccessfullyCreated = true;

                    } else {
                        showAlertDialog("No Customer", "This reservation countains no customer", "Please add a customer in order to continue!", Alert.AlertType.ERROR);
                        return;
                    }

                } catch (ServiceException e) {
                    showAlertDialog("Creating Reservation Failed", "Please fill all fields", "", Alert.AlertType.ERROR);
                }

                if (customerSuccessfullyCreated) {
                    try {

                        rr = roomReservationService.create(new RoomReservation(null, customerFound.getPid(), Date.valueOf(fromDP.getValue()), Date.valueOf(untilDP.getValue()), null, false, false, Integer.valueOf(roomIDTF.getText()), null, breakfast_CheckBox.isSelected()));

                        Room room = new Room();

                        room.setRnr(Integer.valueOf(roomIDTF.getText()));



                        Integer rid = rr.getRid();

                        if (!guestList.isEmpty()) {

                            for (int i = 0; i < guestList.size(); i++) {

                                Customer c = guestList.get(i);
                                c.setRid(rid);
                                customerService.createCustomer(c);
                            }
                        }

                        Stage stage = (Stage) createBTN.getScene().getWindow();

                        Alert alertService = new Alert(Alert.AlertType.CONFIRMATION);
                        alertService.setTitle("Optional");
                        alertService.setHeaderText("Would You like to book also one of our services ?");
                        alertService.setContentText("We offer plenty of Services such as Wellness, Transport etc. \nChoose your option.");

                        ButtonType buttonTypeYes = new ButtonType("Yes");
                        ButtonType buttonTypeNo = new ButtonType("No");

                        alertService.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

                        Optional<ButtonType> result = alertService.showAndWait();

                        if (result.get() == buttonTypeYes) {

                            try {
                                FXMLLoader loader = new FXMLLoader();
                                AnchorPane root = null;
                                root = loader.load(getClass().getResource("/res/layouts/CreateServiceReservationView.fxml").openStream());

                                CreateServiceReservationController createServiceReservationController = (CreateServiceReservationController) loader.getController();
                                createServiceReservationController.setCreateOnlyServiceReservation(false);
                                createServiceReservationController.setPreviousReservation(rr);
                                createServiceReservationController.setNameTF(nameTF.getText());
                                createServiceReservationController.setSurnameTF(surnameTF.getText());
                                createServiceReservationController.setBirthdayDP(birthdayDP.getValue());

                                Scene scene = new Scene(root);
                                Stage newStage = new Stage();
                                newStage.setResizable(false);
                                newStage.initModality(Modality.WINDOW_MODAL);
                                newStage.initOwner(primaryStage);
                                newStage.setScene(scene);
                                newStage.show();

                            } catch (IOException e) {
                                LOGGER.error("Unable to load Create Reservation Window");
                            }

                        } else if (result.get() == buttonTypeNo) {
                            stage.close();
                            Main.mainWindowController.setNewRow(room);
                            LOGGER.info("Add new Reservation stage closed.");
                        }

                        LOGGER.info("Add new Reservation stage closed.");
                        stage.close();
                        Main.mainWindowController.setNewRow(room);

                    } catch (ServiceException e) {
                        LOGGER.error("Can't create Reservation", e);
                    }
                }
            }
        } else {
            showAlertDialog("Creating Reservation Failed", "Please fill all fields", "", Alert.AlertType.ERROR);
        }
    }


    protected Optional showAlertDialog(String title, String headerText, String contentText, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }


    public void onWritingPriceUpTo(KeyEvent keyEvent) {

        if (!priceUpToTF.getText().equals("") && priceUpToTF.getText().matches(regexNum) && fromDP.getValue() != null && untilDP.getValue() != null && fromDP.getValue().isBefore(untilDP.getValue())){

            fillTableFreeRooms();
        }

        if (keyEvent.getCode() == BACK_SPACE){

            fillTableFreeRooms();
        }
    }

    public void onAddCustomerClicked(ActionEvent actionEvent) {

        try {

            if (validateAndShowAlert(nameTF.getText().matches(regexAlpha), "name") && validateAndShowAlert(surnameTF.getText().matches(regexAlpha), "surname")) {
                FXMLLoader loader = new FXMLLoader();
                AnchorPane root = loader.load(getClass().getResource("/res/layouts/customer/AddNewCustomer.fxml").openStream());

                AddNewCustomerController addNewCustomerController = (AddNewCustomerController) loader.getController();
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
            }

        } catch (IOException e) {
            LOGGER.error("Unable to load Create Customer Window.");
        }
    }

    public void onAddGuestClicked(ActionEvent actionEvent) {

        if (!guestNameTF.getText().equals("") && !guestSurnameTF.getText().equals("")) {

            if (validateAndShowAlert(guestNameTF.getText().matches(regexAlpha), "name") && validateAndShowAlert(guestSurnameTF.getText().matches(regexAlpha), "surname")) {

                guestList.add(new Customer(null, guestNameTF.getText(), guestSurnameTF.getText(), null, null, null, null, null, null, null, null, null, new CreditCard(null, null, null, null, null, null), null, false));
                fillTableGuests();

                guestNameTF.clear();
                guestSurnameTF.clear();
            }

        } else {
            showAlertDialog("Not all fields have been filled out","Please make sure you wrote the name and the surname of the guest!","", Alert.AlertType.ERROR);
        }
    }


    public void onRemoveGuestClicked(ActionEvent actionEvent) {

        if (!guestList.isEmpty()){

            int index = addGuestTable.getSelectionModel().getFocusedIndex();
            guestList.remove(index);

            fillTableGuests();

        } else {
            showAlertDialog("Travelers table is empty","There is no traveler that can be removed!","", Alert.AlertType.ERROR);
        }
    }


    public void onBirtdayKeyReleased(KeyEvent keyEvent) {

        if (!surnameTF.equals("") && !nameTF.getText().equals("") && birthdayDP.getValue() != null){

            try {
                customerFound = customerService.checkCustomer(nameTF.getText(), surnameTF.getText(), Date.valueOf(birthdayDP.getValue()));

                found = (customerFound == null ? false : true);

                if (!found){
                    addCustomerBTN.setDisable(false);

                } else {
                    customerSuccessfullyCreated = true;
                }

            } catch (ServiceException e) {
                LOGGER.error("An error occurred while checking the Customer");
            }
        }
    }

    private void setIcons() {

        roomICON.setImage(new Image("/res/icons/room.png"));
        birthdayICON.setImage(new Image("/res/icons/birthday.png"));
        fromICON.setImage(new Image("res/icons/untilDate.png"));
        untilICON.setImage(new Image("res/icons/fromDate.png"));
        priceICON.setImage(new Image("res/icons/euro.png"));

    }

    final Callback<DatePicker, DateCell> untilDayCellFactory =
            new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);

                            if (fromDP.getValue() != null) {
                                if (item.isBefore(
                                        fromDP.getValue().plusDays(1))
                                        ) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
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
                                    currentDate)
                                    ) {
                                setDisable(true);
                                setStyle("-fx-background-color: #ffc0cb;");
                            }
                        }
                    };
                }
            };

    public List<Room> roomPriceToDouble(List<Room> reservations){
        for (int i = 0; i<reservations.size(); i++){
            Long total = reservations.get(i).getPrice()/100;
            reservations.get(i).setPrice(total);
        }
        return reservations;
    }

    public boolean validateAndShowAlert(boolean valid, String type){

        if (valid == false) {

            switch (type) {
                case "name":
                    Utility.showAlert("", "Only letters are allowed in the name field !", "Input error", Alert.AlertType.ERROR);
                    return valid;
                case "surname":
                    Utility.showAlert("", "Only letters are allowed in the surname field !", "Input error", Alert.AlertType.ERROR);
                    return valid;
                case "price":
                    Utility.showAlert("", "Only numbers are allowed in the price field !", "Input error", Alert.AlertType.ERROR);
                    return valid;
                default:
                    return valid;
            }
        }

        return valid;
    }


}