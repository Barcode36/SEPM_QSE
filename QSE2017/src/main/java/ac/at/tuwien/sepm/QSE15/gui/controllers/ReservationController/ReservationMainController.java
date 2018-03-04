package main.java.ac.at.tuwien.sepm.QSE15.gui.controllers.ReservationController;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.customerDAO.JDBCCustomerDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Customer;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.RoomReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.ServiceReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Room;
import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.CustomerNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.gui.controllers.Main;
import main.java.ac.at.tuwien.sepm.QSE15.gui.controllers.PaymentController.PaymentController;
import main.java.ac.at.tuwien.sepm.QSE15.gui.controllers.Service_Controllers.ServiceMainController;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.customerService.CustomerServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.service.paymentService.PaymentService;
import main.java.ac.at.tuwien.sepm.QSE15.service.paymentService.PaymentServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomReservationService.RoomReservationIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomService.RoomServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.service.serviceReservationService.ServiceReservationServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.service.serviceService.ServiceServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.utility.Utility;
import org.controlsfx.control.decoration.Decorator;
import org.controlsfx.control.decoration.StyleClassDecoration;
import org.controlsfx.control.table.TableRowExpanderColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.controlsfx.control.textfield.TextFields;

import javax.naming.Binding;
import javax.script.Bindings;
import javax.swing.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Date;

/**
 * Created by Bajram Saiti on 20.05.17.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class ReservationMainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationMainController.class);
    public Label nameLB;
    public Label surnameLB;
    public Label addressLB;
    public Label zipLB;
    public Label placeLB;
    public Label countryLB;
    public Label phoneLB;
    public Label emailLB;
    public Label bdateLB;
    public Label sexLB;
    public Label idLB;
    public Label creditCardLB;


    public Label rnrLB;
    public Label priceLB;
    public Label breakfastLB;
    public Label categoryLB;
    public Label bedsLB;
    public Label extrasLB;


    public TableView<Service> serviceTableView;
    public TableColumn<Service,String> sridColumn;
    public TableColumn<Service,String> typeColumn;
    public TableColumn<Service,String> servicePriceColumn;
    public TableColumn<Service,String> descriptionColumn;
    public Button newReservationBTN;
    public Slider totalSlider;
    public ImageView priceIcon;
    public Button searchBTN;
    public ImageView customerIcon2;
    public TextField customerNameTF;
    public DatePicker fromDatePicker;
    public DatePicker untilDatePicker;
    public ImageView fromDateIcon;
    public ImageView untilDateIcon;
    public Label sliderTF;
    public Button roomManagerBTN;
    public Button clearBTN;
    public Button serviceManagerBTN;
    public Button payButton;
    public Button cancelButton;
    public Button checkInButton;
    public Button checkOutButton;
    public Text statusText;

    private AnnotationConfigApplicationContext context;
    private RoomReservationIMPL roomReservationService;
    private ServiceReservationServiceIMPL serviceReservationServiceIMPL;
    private CustomerServiceIMPL customerServiceIMPL;
    private RoomServiceIMPL roomServiceIMPL;
    private ServiceServiceIMPL serviceServiceIMPL;
    private PaymentServiceIMPL paymentServiceIMPL;


    private JDBCCustomerDAO customerDAO;
    private Customer currentCustomer;
    private Room currentRoom;
    private RoomReservation currentRoomReservation;
    private ServiceReservation currentServiceReservation;

    private Scene scene;

    private List<RoomReservation> roomReservationList;
    private List<ServiceReservation> serviceReservations;
    private List<Customer> customers;

    public Label customerLabel;
    public ImageView customerIcon;
    public ImageView roomIcon;
    public ImageView serviceIcon;
    public Stage primaryStage;
    private TableRowExpanderColumn<Reservation> expander;
    public TableView<Reservation> reservationTableView;
    public TableColumn<Reservation, String> CustomerIdColumn;
    public TableColumn<Reservation, String> RidColumn;
    public TableColumn<Reservation, String> fromDateColumn;
    public TableColumn<Reservation, String> untilDateColumn;
    public TableColumn<Reservation, String> paidColumn;
    public TableColumn<Reservation, String> totalColumn;
    public TableColumn<Reservation, String> canceledColumn;

    DatePicker from;
    DatePicker until;

    public void initialize() {

        context = new AnnotationConfigApplicationContext(this.getClass());
        roomReservationService = context.getBean(RoomReservationIMPL.class);
        serviceReservationServiceIMPL = context.getBean(ServiceReservationServiceIMPL.class);
        customerDAO = context.getBean(JDBCCustomerDAO.class);
        roomServiceIMPL = context.getBean(RoomServiceIMPL.class);
        serviceServiceIMPL = context.getBean(ServiceServiceIMPL.class);
        customerServiceIMPL = context.getBean(CustomerServiceIMPL.class);
        paymentServiceIMPL = context.getBean(PaymentServiceIMPL.class);

        try {
            roomReservationList = roomReservationService.search(new RoomReservation());
            serviceReservations = serviceReservationServiceIMPL.search(new ServiceReservation());
            customers = customerServiceIMPL.getAllCustomers();
        } catch (ServiceException e) {
            LOGGER.error("Unable to get all Reservations.");
        }
        setUpExpander();
        setIcons();
        setUpAndFillTable();
        reservationTableView.getColumns().add(expander);
        setUpSlider(reservationTableView.getItems());
        setUpAutoFillTextField();
        Utility.changeFormatDatePicker(fromDatePicker);
        Utility.changeFormatDatePicker(untilDatePicker);
        reservationTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Reservation>() {
            public void changed(ObservableValue<? extends Reservation> observable, Reservation oldValue, Reservation newValue) {
                changeSelectionModel(oldValue,newValue);

                if (newValue!=null && oldValue!=null && expander.getExpandedProperty(oldValue).getValue()){
                    expander.toggleExpanded(reservationTableView.getItems().indexOf(oldValue));

                }

                Integer status = 0;

                try {
                    status = paymentServiceIMPL.getReservationStatus(currentRoomReservation);
                } catch (ServiceException e) {

                    LOGGER.error("Error while getting status in ReservationMainController.");
                }

                changeButtonFromStatus();
                setStatusText(status);
            }
        });
        reservationTableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    expander.toggleExpanded(reservationTableView.getItems().indexOf(reservationTableView.getSelectionModel().getSelectedItem()));
                }
            }
        });
        totalSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                sliderTF.setText(String.format("%.2f", new_val));
            }
        });



    }

    public void setUpAutoFillTextField(){

        if (customers!=null) {
            List<String> names = new ArrayList<>();
            for (Customer c : customers) {
                names.add(c.getName() + " " + c.getSurname());
            }
            TextFields.bindAutoCompletion(customerNameTF,names);
        }

    }

    public void changeSelectionModel(Reservation oldValue, Reservation newValue){
        try {
            if (newValue != null) {

                currentCustomer = customerDAO.find(newValue.getCostumerId());
                nameLB.setText(currentCustomer.getName());
                surnameLB.setText(currentCustomer.getSurname());
                addressLB.setText(currentCustomer.getAddress());
                zipLB.setText(currentCustomer.getZip());
                placeLB.setText(currentCustomer.getPlace());
                countryLB.setText(currentCustomer.getCountry());
                phoneLB.setText(currentCustomer.getPhone());
                emailLB.setText(currentCustomer.getEmail());
                bdateLB.setText(currentCustomer.getBdate().toString());
                sexLB.setText(currentCustomer.getSex());
                idLB.setText(currentCustomer.getIdentification());
                creditCardLB.setText(currentCustomer.getCreditCard().getCnr());

                currentRoomReservation = roomReservationService.get(newValue.getRid());
                currentRoom = null;
                if(currentRoomReservation!=null){
                    currentRoom = roomServiceIMPL.get(currentRoomReservation.getRoomId());
                }

                if (currentRoom != null) {
                    currentRoomReservation = roomReservationService.get(newValue.getRid());
                    currentRoom = roomServiceIMPL.get(currentRoomReservation.getRoomId());
                    rnrLB.setText(currentRoom.getRnr() + "");
                    priceLB.setText(currentRoom.getPrice()/100 + "");
                    breakfastLB.setText(currentRoomReservation.getBreakfast() + "");
                    categoryLB.setText(currentRoom.getCategory());
                    extrasLB.setText(currentRoom.getExtras());
                } else {

                    rnrLB.setText("");
                    priceLB.setText("");
                    breakfastLB.setText("");
                    categoryLB.setText("");
                    bedsLB.setText("");
                    extrasLB.setText("");
                }

                ServiceReservation serviceReservation = new ServiceReservation();
                serviceReservation.setRid(newValue.getRid());
                List<Service> serviceList = serviceServiceIMPL.getAllServicesFromReservation(serviceReservation);
                serviceList = servicePriceToDouble(serviceList);

                serviceTableView.setItems(FXCollections.observableArrayList(serviceList));
                serviceTableView.getSelectionModel().selectFirst();


            }
        }catch (DAOException e) {
            LOGGER.error("Unable to find customer");
        } catch (CustomerNotFoundException e) {
            LOGGER.error("Unable to find customer");
        } catch (ServiceException e) {
            LOGGER.error("Unable to get room for selected reservation.");
        }
    }

    public List<Service> servicePriceToDouble(List<Service> reservations){
        for (int i = 0; i<reservations.size(); i++){
            Long total = reservations.get(i).getPrice()/100;
            reservations.get(i).setPrice(total);
        }
        return reservations;
    }


    private void setUpAndFillTable() {

        RidColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("rid"));


        CustomerIdColumn.setCellValueFactory(celldata-> {
            Customer customer = null;
            try {
                customer = customerServiceIMPL.getCustomer(celldata.getValue().getCostumerId());
            } catch (ServiceException e) {
                LOGGER.error(e.getMessage());
            }
            if(customer != null) {
                return new ReadOnlyStringWrapper(customer.getName());
            }else {
                return new ReadOnlyStringWrapper("No customer");
            }


        });


        fromDateColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("from"));
        untilDateColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("until"));
        paidColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("Paid"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("total"));
        canceledColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("Canceled"));
        reservationTableView.setPlaceholder(new Label("There is (no such) a Reservation in Database."));

        sridColumn.setCellValueFactory(new PropertyValueFactory<Service, String>("srid"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Service, String>("type"));
        servicePriceColumn.setCellValueFactory(new PropertyValueFactory<Service, String>("price"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Service, String>("description"));
        serviceTableView.setPlaceholder(new Label("This Reservation has no Services."));

        reservationTableView.setItems(FXCollections.observableArrayList(roomReservationList));
        ObservableList<Reservation> allReservations = reservationTableView.getItems();
        reservationTableView.setItems(FXCollections.observableArrayList(serviceReservations));
        allReservations.addAll(reservationTableView.getItems());

        reservationTableView.setItems(priceToDouble(findDuplicates(allReservations)));
        RidColumn.setSortType(TableColumn.SortType.DESCENDING);
        reservationTableView.getSortOrder().add(RidColumn);
        reservationTableView.getSelectionModel().selectFirst();

        changeSelectionModel(null, reservationTableView.getSelectionModel().getSelectedItem());
    }

    private void setUpExpander(){
        expander = new TableRowExpanderColumn<>(param -> {
            GridPane editor = new GridPane();
            editor.setPadding(new Insets(10));
            editor.setHgap(10);
            editor.setVgap(5);

            Reservation reservation = param.getValue();

            from = new DatePicker(reservation.getFrom().toLocalDate());
            until = new DatePicker(reservation.getUntil().toLocalDate());
            from.setDayCellFactory(fromDayCellFactory);
            until.setDayCellFactory(untilDayCellFactory);
            Utility.changeFormatDatePicker(from);
            Utility.changeFormatDatePicker(until);


            editor.addRow(0, new Label("From Date"), from, new Label("Until Date"), until);
            Button saveButton = new Button("Save");
            saveButton.setOnAction(event -> {
                if (Utility.checkIfDateValid(from, until)){
                    onUpdateClicked(reservation);
                }

                param.toggleExpanded();
            });

            Button storno = new Button("Storno");
            storno.setOnAction(event -> {
                onStornoClicked(reservation);
                param.toggleExpanded();
            });

            editor.addRow(1, saveButton, storno);







            return editor;
        });

    }

    final Callback<DatePicker, DateCell> fromDayCellFactory =
            new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item.isBefore(
                                    LocalDate.now().plusDays(2))
                                    ) {
                                setDisable(true);
                                setStyle("-fx-background-color: #ffc0cb;");
                            }
                        }
                    };
                }
            };

    final Callback<DatePicker, DateCell> untilDayCellFactory =
            new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item.isBefore(
                                    from.getValue().plusDays(1))
                                    ) {
                                setDisable(true);
                                setStyle("-fx-background-color: #ffc0cb;");
                            }
                        }
                    };
                }
            };


    private void onUpdateClicked(Reservation reservation){

        if (rnrLB!=null && !rnrLB.getText().equals("")){
            try {
                Room room = roomServiceIMPL.get(Integer.parseInt(rnrLB.getText()));
                //RoomReservation r = roomReservationService.get(reservation.getRid());
               // Long curtTotal = r.getTotal();
              //  long difference = ChronoUnit.DAYS.between(reservation.getFrom().toLocalDate(), reservation.getUntil().toLocalDate());
              //  Long temp = curtTotal-(difference*room.getPrice());
              //  curtTotal = curtTotal<=0?0:curtTotal;
                RoomReservation res = new RoomReservation();
                res.setRoomId(room.getRnr());
                res.setRid(reservation.getRid());
                res.setFrom(java.sql.Date.valueOf(from.getValue()));
                res.setUntil(java.sql.Date.valueOf(until.getValue()));
                roomReservationService.update(res);
                Utility.showAlert("Reservation is updated successfully", Alert.AlertType.CONFIRMATION);
                roomReservationList = roomReservationService.search(new RoomReservation());
                serviceReservations = serviceReservationServiceIMPL.search(new ServiceReservation());
                setUpAndFillTable();
            } catch (ServiceException e) {
                LOGGER.error("Unable to get reservation.");
                Utility.showAlert("Error while update Reservation. Please check the input and try again.", Alert.AlertType.CONFIRMATION);
            }
        }
        else {
            Utility.showAlert("Only RoomReservation can be updated.", Alert.AlertType.WARNING);
        }
    }

    private void onStornoClicked(Reservation reservation){
        try {

            roomReservationService.cancel(reservation);
            serviceReservationServiceIMPL.cancel(reservation);
            roomReservationList = roomReservationService.search(new RoomReservation());
            serviceReservations = serviceReservationServiceIMPL.search(new ServiceReservation());

            onClearClicked(null);
            Utility.showAlert("Reservation is Canceled successfully", Alert.AlertType.CONFIRMATION);

        } catch (ServiceException e) {
            LOGGER.error("Unable to get Reservation.");
        }

    }

    private void setUpSlider(ObservableList<Reservation> reservations){
        Long max = 0L;
        Long min = Long.MAX_VALUE;

        for (Reservation r : reservations){
            Long total = r.getTotal();
            if (total>max){
                max = total;
            }
            if (total<min){
                min = total;
            }
        }

        String s = min + "";
        Double dmin = Double.parseDouble(s);
        s = max + "";
        Double dmax = Double.parseDouble(s);
        totalSlider.setMin(dmin);
        totalSlider.setMax(dmax);
        totalSlider.setShowTickLabels(true);
        totalSlider.setShowTickMarks(true);
        totalSlider.setMajorTickUnit(200);
        totalSlider.setMinorTickCount(3);
        totalSlider.setValue(totalSlider.getMax());

    }

    private ObservableList<Reservation> findDuplicates(ObservableList<Reservation> list) {
        List<Integer> rids = new ArrayList<>();
        for (int i = list.size()-1; i>=0; i--) {

            if (rids.contains(list.get(i).getRid())) {
                list.remove(i);
            }
            else {
                rids.add(list.get(i).getRid());
            }
        }

        return list;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void setIcons() {
        try {
            customerIcon.setImage(new Image("/res/icons/customer.png"));
            customerIcon2.setImage(new Image("/res/icons/customer.png"));
            roomIcon.setImage(new Image("/res/icons/room.png"));
            serviceIcon.setImage(new Image("/res/icons/settings.png"));
            fromDateIcon.setImage(new Image("res/icons/untilDate.png"));
            untilDateIcon.setImage(new Image("res/icons/fromDate.png"));
            priceIcon.setImage(new Image("res/icons/euro.png"));
        }catch (Exception e){
            LOGGER.error("Could't load the icons.");
        }


    }

    public ObservableList<Reservation> priceToDouble(ObservableList<Reservation> reservations){
        for (int i = 0; i<reservations.size(); i++){
            Long total = reservations.get(i).getTotal()/100;
            reservations.get(i).setTotal(total);
        }
        return reservations;
    }

    public void onSearchClicked(ActionEvent actionEvent) {

        RoomReservation roomReservation = new RoomReservation();
        ServiceReservation serviceReservation = new ServiceReservation();


        if(fromDatePicker.getValue()!=null && untilDatePicker.getValue()!=null){
            if (Utility.checkIfDateValid(fromDatePicker,untilDatePicker)){
                roomReservation.setFrom(java.sql.Date.valueOf(fromDatePicker.getValue()));
                roomReservation.setUntil(java.sql.Date.valueOf(untilDatePicker.getValue()));
                serviceReservation.setFrom(java.sql.Date.valueOf(fromDatePicker.getValue()));
                serviceReservation.setUntil(java.sql.Date.valueOf(untilDatePicker.getValue()));
            }
        }

        if (totalSlider!=null){
            Double dTotal = totalSlider.getValue() * 100;
            Long getSliderValue =dTotal.longValue();
            roomReservation.setTotal(getSliderValue);
            serviceReservation.setTotal(getSliderValue);
        }

        if (customerNameTF!=null && !customerNameTF.getText().equals("")){
            Integer customerID = -1;
            for (Customer c : customers ){
                String fullname = c.getName() + " " + c.getSurname();
                if (fullname.equals(customerNameTF.getText())){
                    customerID = c.getPid();
                }
            }
            if (customerID!=-1){
                roomReservation.setCostumerId(customerID);
                serviceReservation.setCostumerId(customerID);
            }
            else {
                String message = "Customer " + customerNameTF.getText() + " doesn't have any Resevations.";
                Utility.showAlert(message, Alert.AlertType.INFORMATION);
                customerNameTF.setText(null);
            }
        }

        try {
            roomReservationList = roomReservationService.search(roomReservation);
            serviceReservations = serviceReservationServiceIMPL.search(serviceReservation);
            setUpAndFillTable();
        } catch (ServiceException e) {
            LOGGER.error("Unable to get such a RoomReservation");
        }
    }

    public void onRoomManagerClicked(ActionEvent actionEvent) {

        Utility.loadNewStage("/res/layouts/RoomMainView.fxml","Room Manager", roomManagerBTN);
    }

    public void onClearClicked(ActionEvent actionEvent) {
        totalSlider.setValue(totalSlider.getMax());
        fromDatePicker.getEditor().clear();
        fromDatePicker.setValue(null);
        untilDatePicker.getEditor().clear();
        untilDatePicker.setValue(null);
        customerNameTF.setText("");
        try {
            roomReservationList = roomReservationService.search(new RoomReservation());
            serviceReservations = serviceReservationServiceIMPL.search(new ServiceReservation());

        } catch (ServiceException e) {
            LOGGER.error("Unable to get all Reservations.");
        }

        setUpAndFillTable();
        if (javafx.beans.binding.Bindings.isEmpty(reservationTableView.getItems()).getValue()){
            expander.toggleExpanded(reservationTableView.getSelectionModel().getSelectedIndex());
        }
    }

    public void onNewReservationClicked(ActionEvent actionEvent) {

        Utility.loadNewStage("/res/layouts/CreateRoomReservationView.fxml","Create Reservation", newReservationBTN);
    }

    public void onServiceManagerClicked(ActionEvent actionEvent) {

        Utility.loadNewStage("/res/layouts/ServiceMainView.fxml","Service Manager", serviceManagerBTN);
    }

    public void setPaid(){
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/res/layouts/payment/PaymentLayout.fxml"));
        AnchorPane anchorPane = null;
        try {
            anchorPane = fxmlLoader.load();
            PaymentController paymentController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setTitle("Payment");
            Scene scene = new Scene(anchorPane);
            stage.setScene(scene);
            paymentController.setReservation(currentRoomReservation);
            paymentController.setCurrentStage(stage);
            stage.show();
        } catch (IOException e) {
            LOGGER.error("Error in ReservationMainController while loading anchorPane.");
        }
        Room room = new Room();
        room.setRnr(currentRoomReservation.getRoomId());
        Main.mainWindowController.setNewRow(room);
        setUpAndFillTable();
    }
    public void setCanceled(){
        try {
            paymentServiceIMPL.setCanceled(currentRoomReservation);
            Utility.showAlert("Reservation " + currentRoomReservation.getRid() + " is canceled successfully.", Alert.AlertType.INFORMATION);
        } catch (ServiceException e) {
            LOGGER.error("Error in ReservationMainController in setCanceled.");
        }
        Room room = new Room();
        room.setRnr(currentRoom.getRnr());
        Main.mainWindowController.setNewRow(room);
        setUpAndFillTable();
    }
    public void setIsArrived(){
        try {
            paymentServiceIMPL.setIsArrived(currentRoomReservation);
            Utility.showAlert("Reservation " + currentRoomReservation.getRid() + " is checked-in successfully.", Alert.AlertType.INFORMATION);
        } catch (ServiceException e) {
            LOGGER.error("Error in ReservationMainController in setIsArrived.");
        }
        Room room = new Room();
        room.setRnr(currentRoom.getRnr());
        Main.mainWindowController.setNewRow(room);
        setUpAndFillTable();
    }
    public void setCheckedOut(){
        try {
            paymentServiceIMPL.setCheckedOut(currentRoomReservation);
            Utility.showAlert("Reservation " + currentRoomReservation.getRid() + " is checked-out successfully.", Alert.AlertType.INFORMATION);
        } catch (ServiceException e) {
            LOGGER.error("Error in ReservationMainController in setCheckedOut.");
        }
        Room room = new Room();
        room.setRnr(currentRoom.getRnr());
        Main.mainWindowController.setNewRow(room);
        setUpAndFillTable();
    }
    private void changeButtonFromStatus() {
        Integer status = 0;
        try {
            status = paymentServiceIMPL.getReservationStatus(currentRoomReservation);
        } catch (ServiceException e) {
            LOGGER.error("Error while getting status in ReservationMainController.");
        }
        switch (status){
            case 0:
                cancelButton.setDisable(false);
                checkInButton.setDisable(false);
                checkOutButton.setDisable(true);
                payButton.setDisable(false);
                break;
            case 10:
                cancelButton.setDisable(false);
                checkInButton.setDisable(true);
                checkOutButton.setDisable(true);
                payButton.setDisable(false);
                break;
            case 100:
                cancelButton.setDisable(false);
                checkInButton.setDisable(false);
                checkOutButton.setDisable(true);
                payButton.setDisable(true);
                break;
            case 110:
                cancelButton.setDisable(false);
                checkInButton.setDisable(true);
                checkOutButton.setDisable(false);
                payButton.setDisable(true);
                break;
            case 1110:
                cancelButton.setDisable(true);
                checkInButton.setDisable(true);
                checkOutButton.setDisable(true);
                payButton.setDisable(true);
                break;
            case 10000:
                cancelButton.setDisable(true);
                checkInButton.setDisable(true);
                checkOutButton.setDisable(true);
                payButton.setDisable(true);
                break;
        }
    }
    public void setStatusText(Integer statusInt){
        String status = "Status: ";
        switch (statusInt) {
            case 0: status += "Arriving [not paid]";
                break;
            case 10: status += "Checked-in [not paid]";
                break;
            case 100: status += "Arriving [paid]";
                break;
            case 110: status += "Checked-in [paid]";
                break;
            case 1110: status += "Checked-out";
                break;
            case 10000: status += "Canceled";
                break;
            default: status += "Error";
                break;
        }
        statusText.setText(status);
    }
}
