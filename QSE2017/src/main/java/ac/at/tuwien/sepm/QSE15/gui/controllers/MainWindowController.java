package main.java.ac.at.tuwien.sepm.QSE15.gui.controllers;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import main.java.ac.at.tuwien.sepm.QSE15.entity.InfoCell.InfoCell;
import main.java.ac.at.tuwien.sepm.QSE15.entity.hotel.Hotel;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Customer;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.RoomReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Category;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Room;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.customerService.CustomerService;
import main.java.ac.at.tuwien.sepm.QSE15.service.customerService.CustomerServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.service.hotelService.HotelService;
import main.java.ac.at.tuwien.sepm.QSE15.service.hotelService.MyHotelService;
import main.java.ac.at.tuwien.sepm.QSE15.service.paymentService.PaymentService;
import main.java.ac.at.tuwien.sepm.QSE15.service.paymentService.PaymentServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomReservationService.RoomReservationIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomReservationService.RoomReservationService;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomService.RoomService;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomService.RoomServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

/**
 * Created by Nemanja Vukoje on 15/05/2017.
 */

@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class MainWindowController {

    public AnchorPane paneID;
    public TableView<Row> datesTable;
    public TableView<Room> roomNoTable;
    public TableColumn<Room, Integer> roomNoColumn;
    public DatePicker datePicker;
    public Text userName;
    public Label timeLabel, dateLabel;
    public Button showOptionsButton, editRoomsButton, employeeButton, reservationsButton, roomsButton, customerButton,
                 paymentsButton, newResButton, openServiceRes;
    public MenuItem menuBarOpenReservationID, menuBarEditReservationID, menuBarShowReservationsID,
            menuBarDeleteReservationID, menuBarAddServicesID, menuBarEditServicesID, menuBarShowServicesID,
            menuBarDeleteServicesID, menuBarAddNewGuestID, menuBarAddNewCustomerID, menuBarShowAllGuestsID,
            menuBarEditGuestID, menuBarShowAllRoomsID;
    public ImageView imageClock;
    public ImageView imageDate;
    public ImageView arrowImageField;
    public Button serviceMainButton;
    public Button openStatictiscButton;
    public ImageView employImage;

    private int minute;
    private int hour;
    private int second;
    private Date hotelDate;
    private Stage currentStage;
    private RoomReservationService roomReservationService;
    private CustomerService customerService;
    private PaymentService paymentService;
    private RoomService roomService;
    private List<Room> allRoomsList;
    private Employee loggedEmployee;
    private ScrollBar scrollBar;
    private Row currentRow;

    private static final Logger LOGGER = LoggerFactory.getLogger(MainWindowController.class);

    Employee getLoggedEmployee() {
        return loggedEmployee;
    }

    void setLoggedEmployee(Employee loggedEmployee) {
        this.loggedEmployee = loggedEmployee;
    }

    void setUserNameText(Employee employee){
        userName.setText(employee.getName());
        loggedEmployee = employee;

    }

    void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
        currentStage.setResizable(false);
    }


    public void initialize() {

        LOGGER.info("Initializing TimeTable!");

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(this.getClass());
        customerService = context.getBean(CustomerServiceIMPL.class);
        roomReservationService = context.getBean(RoomReservationIMPL.class);
        roomService = context.getBean(RoomServiceIMPL.class);
        paymentService = context.getBean(PaymentServiceIMPL.class);
        HotelService hotelService = context.getBean(MyHotelService.class);

        editRoomsButton.setVisible(true);
        employeeButton.setVisible(true);
        setIcons();


        try {
            allRoomsList = roomService.search(new Room(), new Category());
        } catch (ServiceException e) {
            LOGGER.error(" All room List error");
        }

        scrollBar = new ScrollBar();
        LocalDate date = LocalDate.now();

        //userName.setText(loggedEmployee.getName());
        datePicker.setValue(date);
        Utility.changeFormatDatePicker(datePicker);

        roomNoColumn.setCellValueFactory(new PropertyValueFactory<>("rnr"));
        roomNoColumn.setMinWidth(104);

        /*
         * Adding columns
         */
        datesTable.setEditable(false);
        Hotel hotel = new Hotel();
        try {
            hotel = hotelService.get();
        } catch (ServiceException e) {
            LOGGER.error("Error in MainWindowController while getting hotel.");
        }

       // if (hotel.getDate() != null) date = hotel.getDate().toLocalDate(); //from creation date generate
        makeColumns(200, "" + date);

        /*
         * ***** Setting real time *****
         */
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {

            Calendar cal = Calendar.getInstance();
            LocalDate date1 = LocalDate.now();

            second = cal.get(Calendar.SECOND);
            minute = cal.get(Calendar.MINUTE);
            hour = cal.get(Calendar.HOUR);
            second = cal.get(Calendar.SECOND);

            timeLabel.setText("" + (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + " " + ((cal.get(Calendar.AM_PM) == Calendar.PM) ? "PM" : "AM"));
            dateLabel.setText("" + date1);
        }),
                new KeyFrame(Duration.seconds(60))
        );

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        /*
         * ******************************
         */
        for (int i = 0; i < datesTable.getColumns().size(); i++) {

            if (datesTable.getColumns().get(i).getText().equals("" + LocalDate.now())) {
                datesTable.scrollToColumn(datesTable.getColumns().get(i));
            }
        }

        datesTable.setEditable(false);
        roomNoTable.setFixedCellSize(82.0);
        datesTable.setFixedCellSize(82.0);
        populateTable("" + date);
        roomNoTable.setItems(FXCollections.observableArrayList(allRoomsList));
        setRowFactory(true,datesTable);
        LOGGER.info("Initializing TimeTable successful!");
    }

    /**
     * Filling table with rows
     *
     * @param date from that date is the table filled
     */
    private void populateTable(String date) {

        for (Room roomInAllRoomList : allRoomsList) {


            Row rows;
            String dateToString = date + "";
            List<InfoCell> infoCells = new ArrayList<>();
            List<Room> roomsThatAreNotAvailableOnThisDateList;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(simpleDateFormat.parse(dateToString));
            } catch (ParseException e) {
                LOGGER.error("Error while parsing date.");
            }

            //  calendar.add(Calendar.DATE, 1);

            dateToString = simpleDateFormat.format(calendar.getTime());

            for (int i = 0; i < 200; i++) {

                calendar.add(Calendar.DATE, 1);

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate;

                try {

                    startDate = dateFormat.parse(dateToString);
                    java.sql.Date sql = new java.sql.Date(startDate.getTime());
                    try {
                        RoomReservation roomReservation = new RoomReservation();
                        InfoCell infoCell = new InfoCell();
                        roomReservation.setUntil(sql);

                        roomReservation.setFrom(sql);
                        List<RoomReservation> roomReservations = roomReservationService.searchReservationsOnSpecifiedDate(roomReservation);

                        roomsThatAreNotAvailableOnThisDateList = roomService.searchFree(new Room(), new Category(), sql, sql);
                        if (!roomsThatAreNotAvailableOnThisDateList.contains(roomInAllRoomList)) {

                            for (RoomReservation roomReservation1 : roomReservations) {

                                //check if that's the current room (== doesn't work!!)
                                if (Objects.equals(roomReservation1.getRoomId(), roomInAllRoomList.getRnr())) {

                                    Customer customer = customerService.getCustomer(roomReservation1.getCostumerId());
                                    Reservation res = new RoomReservation();
                                    Integer status = paymentService.getReservationStatus(roomReservation1);

                                    res.setRid(roomReservation1.getRid());
                                    infoCell.setValues(res, customer, status);

                                }
                            }
                        }

                        infoCells.add(infoCell);

                    } catch (ServiceException e) {
                        LOGGER.error("Service error in MainWindowController.");
                    }

                } catch (ParseException e) {
                    LOGGER.error("Parsing error in MainWindowController.");
                }

                //calendar.add(Calendar.DATE, 1);  // number of days to add
                dateToString = simpleDateFormat.format(calendar.getTime());
            }

            rows = makeSampleData(infoCells);
            datesTable.getItems().add(rows);
            calendar.add(Calendar.DATE, 1);
        }

    }
    public void setNewRow(Room room){
        Row row = makeOneRow(room);
        for (int i =0; i<allRoomsList.size(); i++){
            if (allRoomsList.get(i).getRnr().equals(room.getRnr())){

                datesTable.getItems().remove(i);
                datesTable.getItems().add(i, row);
            }
        }
    }

    void updateTableAfterRoomInsert(Room room){
        try {
            allRoomsList = roomService.search(new Room(), new Category());
            roomNoTable.setItems(FXCollections.observableArrayList(allRoomsList));
            for (int i=0; i<allRoomsList.size();i++){
                if (allRoomsList.get(i).getRnr().equals(room.getRnr())){
                    datesTable.getItems().add(i, new Row());
                }
            }


        } catch (ServiceException e) {
            LOGGER.error(" All room List error");
        }
    }

    private Row  makeOneRow(Room room) {
        LOGGER.info("Making one Row/MainWindowController");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate;
        Row rows = null;
        List<InfoCell> infoCells = new ArrayList<>();

        for (int i = 0; i < datesTable.getColumns().size(); i++) {
            String dateToString = datesTable.getColumns().get(i).getText() + "";
            List<Room> roomsThatAreAvailableOnThisDateList;


            try {

                startDate = dateFormat.parse(dateToString);
                java.sql.Date sql = new java.sql.Date(startDate.getTime());
                try {
                    RoomReservation roomReservation = new RoomReservation();
                    InfoCell infoCell = new InfoCell();
                    roomReservation.setFrom(sql);
                    roomReservation.setUntil(sql);
                    List<RoomReservation> roomReservations = roomReservationService.searchReservationsOnSpecifiedDate(roomReservation);

                    roomsThatAreAvailableOnThisDateList = roomService.searchFree(new Room(), new Category(), sql, sql);

                    if (!roomsThatAreAvailableOnThisDateList.contains(room)) {

                        for (RoomReservation roomReservation1 : roomReservations) {

                            //check if that's the current room (== doesn't work!!)
                            if (Objects.equals(roomReservation1.getRoomId(), room.getRnr())) {
                                Reservation res = new RoomReservation();

                                Customer customer = customerService.getCustomer(roomReservation1.getCostumerId());

                                Integer status = paymentService.getReservationStatus(roomReservation1);
                                res.setRid(roomReservation1.getRid());
                                infoCell.setValues(res, customer, status);
                            }
                        }
                    }

                    infoCells.add(infoCell);

                } catch (ServiceException e) {
                    LOGGER.error("Service error in MainWindowController.");
                }

            } catch (ParseException e) {
                LOGGER.error("Parsing error in MainWindowController.");
            }
        }
        rows = makeSampleData(infoCells);
        LOGGER.info("Making row successful");
        return rows;
    }

    /**
     * Making columns in our table
     *
     * @param count how many columns
     * @param date  From that date columns would be made,
     *              and their text will set to it
     */
    private void makeColumns(int count, String date) {

        String dateToString = date + "";

        for (int m = 0; m < count; m++) {

            TableColumn<Row, String> column = new TableColumn<>(dateToString);
            column.setMinWidth(200);
            column.setSortable(false);

            column.setCellValueFactory(param -> {
                int index = param.getTableView().getColumns().indexOf(param.getTableColumn());
                List<InfoCell> cells = param.getValue().getCells();

                return new SimpleStringProperty(cells.size() > index ? cells.get(index).getDataForCell() : null);
            });
            datesTable.getColumns().add(column);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();

            try {
                calendar.setTime(simpleDateFormat.parse(dateToString));
                calendar.add(Calendar.DATE, 1);  // number of days to add
                dateToString = simpleDateFormat.format(calendar.getTime());

                calendar.setTime(simpleDateFormat.parse(dateToString));
            } catch (ParseException e) {
            }
        }
    }

    /**
     * Genereting a row for our table
     *
     * @param list List with values, if the room is reserved for
     *             some date span.
     * @return Row which is added to a table
     */
    private Row makeSampleData(List<InfoCell> list) {

        Row e = new Row();
        for (InfoCell aList : list) {

            e.getCells().add(aList);

        }
        return e;
    }




    /**
     * This class represents one row in our table
     * It contains a list of InfoCells(Information)
     */
    private static class Row {
        private final List<InfoCell> list = new ArrayList<>();

        private List<InfoCell> getCells() {
            return list;
        }
    }


    /**
     * Search method. Scrolls to a selected date, from a DatePicker
     *
     *
     */
    public void setTimeTable() {

        // fillDates(datePicker.getValue());

        for (int i = 0; i < datesTable.getColumns().size(); i++) {

            if (datesTable.getColumns().get(i).getText().equals("" + datePicker.getValue())) {

                datesTable.scrollToColumn(datesTable.getColumns().get(i));
                break;
            } else {
                if (i == datesTable.getColumns().size() - 1) {

                    Utility.showAlert("Wrong Date", Alert.AlertType.ERROR);
                }
            }
        }

        // datePicker.setValue(LocalDate.now());
    }


    /**
     * This method expands a window,
     * in order to show Buttons
     *
     *
     */
    public void expand() {

        if (currentStage.getWidth() != 1575) {

            currentStage.setWidth(1575);
            currentStage.setHeight(836);
//            showOptionsButton.setText("Close Options");
            arrowImageField.setImage(new Image("res/images/001-left-arrow.png"));
        } else {
            currentStage.setHeight(836);
            currentStage.setWidth(1272);
            arrowImageField.setImage(new Image("res/images/002-right-arrow.png"));

        }
    }

    /*
    public void expand() {

        if (currentStage.getWidth() != 1232) {

            currentStage.setWidth(1232);
            showOptionsButton.setText("Close Options");
        } else {
            currentStage.setWidth(968);
            showOptionsButton.setText("Show Options");
        }
    }
     */

   /* public void setValue(int row, int col, RoomReservation val) {
        // datesTable.getItems().add(val);
        final Reservation selectedRow = datesTable.getItems().get(row);
        final TableColumn<RoomReservation, ?> selectedColumn = datesTable.getColumns().get(col);
        // Lookup the propery name for this column
        final String propertyName = ((PropertyValueFactory) selectedColumn.getCellValueFactory()).getProperty();
        try {
            // Use reflection to get the property
            final Field f = Reservation.class.getField(propertyName);
            final Object o = f.get(selectedRow);

            // Modify the value based on the type of property
            if (o instanceof SimpleStringProperty) {
                ((SimpleStringProperty) o).setValue(val.toString());
            }

        } catch (Exception ex) {
        }
    }
    */

    /**
     * This method connects 2 tables. When User scrolls, the both of tables are scrolling together.
     *
     * @param tableView1  First table
     * @param tableView2  Second table
     * @param scrollBar   ScrollBar
     * @param orientation Left/Right or Up/Down
     */

    private void bindScrollBars(TableView<?> tableView1, TableView<?> tableView2,
                                ScrollBar scrollBar, Orientation orientation) {

        // Get the scrollbar of first table
        VirtualFlow virtualFlow = (VirtualFlow) tableView1.getChildrenUnmodifiable().get(1);
        ScrollBar scrollBar1 = null;

        for (final Node subNode : virtualFlow.getChildrenUnmodifiable()) {

            if (subNode instanceof ScrollBar && ((ScrollBar) subNode).getOrientation() == orientation) {
                scrollBar1 = (ScrollBar) subNode;
                scrollBar1.setVisible(false);
            }
        }

        // Get the scrollbar of second table
        virtualFlow = (VirtualFlow) tableView2.getChildrenUnmodifiable().get(1);

        ScrollBar scrollBar2 = null;

        for (final Node subNode : virtualFlow.getChildrenUnmodifiable()) {

            if (subNode instanceof ScrollBar && ((ScrollBar) subNode).getOrientation() == orientation) {
                scrollBar2 = (ScrollBar) subNode;
            }
        }

        // Set min/max of visible scrollbar to min/max of a table scrollbar
        if (scrollBar1 != null) {
            scrollBar.setMin(scrollBar1.getMin());
        }
        if (scrollBar1 != null) {
            scrollBar.setMax(scrollBar1.getMax());
        }

        // bind the hidden scrollbar valueProperty the visible scrollbar
        if (scrollBar1 != null) {
            scrollBar.valueProperty().bindBidirectional(scrollBar1.valueProperty());
        }
        if (scrollBar2 != null) {
            scrollBar.valueProperty().bindBidirectional(scrollBar2.valueProperty());
        }
    }

    /**
     * This method must be called in Application.start() after the stage is shown,
     * because the hidden scrollbars exist only when the tables are rendered
     */
    public void setScrollBarBinding() {
        bindScrollBars(roomNoTable, this.datesTable, this.scrollBar, Orientation.VERTICAL);
    }


    /**
     * Sets all Icons
     */
    private void setIcons(){
        imageDate.setImage(new Image("res/images/calendar.png"));
        imageClock.setImage(new Image("res/images/clock.png"));
        arrowImageField.setImage(new Image("res/images/002-right-arrow.png"));
       // employImage.setImage(new Image(loggedEmployee.getPicture()));

    }

    /**
     * ***** Methods for changing a window, when click on Button or MenuBar ********
     * todo change all FXML files, implement for MenuItems
     */
    public void openReservationWindow(ActionEvent actionEvent) {
        Utility.loadNewStage("/res/layouts/ReservationMainView.fxml", "Reservations", reservationsButton);

    }
    public void openNewRoomReservation(ActionEvent actionEvent) {
        Utility.loadNewStage("/res/layouts/CreateRoomReservationView.fxml", "Reservations",newResButton);

    }

    public void openServiceReservation(ActionEvent actionEvent) {
        Utility.loadNewStage("/res/layouts/CreateServiceReservationView.fxml", "Reservations",openServiceRes);

    }

    public void openRoomsWindow(ActionEvent actionEvent) {
        Utility.loadNewStage("/res/layouts/RoomMainView.fxml", "Rooms", roomsButton);

    }
    public void openServiceReservationMainWindow(ActionEvent actionEvent) {
        Utility.loadNewStage("/res/layouts/ServiceMainView.fxml", "Services",openStatictiscButton);

    }

    public void openStatsWindow(ActionEvent actionEvent) {
        Utility.loadNewStage("/res/layouts/Statistics.fxml", "Statistics",openStatictiscButton);

    }


    public void openPaymentsWindow(ActionEvent actionEvent) {
        Utility.loadNewStage("/res/layouts/InvoiceLayout.fxml", "Payments", paymentsButton);

    }

    public void openCustomerWindow(ActionEvent actionEvent) {
        Utility.loadNewStage("/res/layouts/customer/CustomerManager.fxml" +
                "", "Customers", customerButton);

    }


    public void editRoomsAdminOnlyWindow(ActionEvent actionEvent) {
        Utility.loadNewStage("/res/layouts/hotelSetup/HotelSetupAddRooms.fxml","Edit Rooms - Admin", editRoomsButton);

    }

    public void openEmployeesWindowAdminOnly(ActionEvent actionEvent) {
        Utility.loadNewStage("/res/layouts/employee/EmployeeManager.fxml", "Employees - Admin", employeeButton);

    }



    public void menuBarOpenReservation() {
        Utility.loadNewStage("/res/layouts/CreateRoomReservationView.fxml", "Create new Reservation", new Stage());

    }

    public void menuBarEditReservation() {
          Utility.loadNewStage("/res/layouts/ReservationMainView.fxml", "Reservations", new Stage(  ));

    }

    public void menuBarShowReservations() {
        Utility.loadNewStage("/res/layouts/ReservationMainView.fxml", "Reservations", new Stage());

    }

    public void menuBarDeleteReservation() {
        Utility.loadNewStage("/res/layouts/ReservationMainView.fxml", "Reservations", new Stage());

    }

    public void menuBarAddServices() {
        Utility.loadNewStage("/res/layouts/CreateServiceReservationView.fxml", "Add new Service", new Stage());

    }

    public void menuBarEditServices() {
        Utility.loadNewStage("/res/layouts/ServiceMainView.fxml", "Services", new Stage());

    }

    public void menuBarShowServices() {
        Utility.loadNewStage("/res/layouts/ServiceMainView.fxml", "Services", new Stage());

    }

    public void menuBarDeleteServices() {
        Utility.loadNewStage("/res/layouts/ServiceMainView.fxml", "Services", new Stage());

    }

    public void menuBarAddNewGuest() {
        //Utility.loadNewStage("main/java/resources/layouts/EmployeeManager.fxml", "Employees - Admin", employeeButton);

    }

    public void menuBarAddNewCustomer() {
               Utility.loadNewStage("/res/layouts/customer/AddNewCustomer.fxml", "Add new Customer", new Stage());

    }

    public void menuBarShowAllGuests() {
        Utility.loadNewStage("/res/layouts/customer/CustomerManager.fxml", "Customers", new Stage());

    }

    public void menuBarEditGuest() {
        Utility.loadNewStage("/res/layouts/customer/CustomerManager.fxml", "Customers", new Stage());

    }

    public void menuBarShowAllRooms() {
        Utility.loadNewStage("/res/layouts/RoomMainView.fxml", "Rooms", new Stage());

    }




    void checkRights(){

        if (loggedEmployee.getRights() != 1){

            employeeButton.setVisible(false);
            editRoomsButton.setVisible(false);
        }
    }
    protected void setRowFactory(final boolean edit,TableView<Row> tableView){

        tableView.setRowFactory(new Callback<TableView<Row>, TableRow<Row>>() {
            @Override
            public TableRow<Row> call(TableView<Row> tableView) {
                final TableRow<Row> row = new TableRow<Row>();
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem deleteMenuItem = new MenuItem("Show All");
                //final MenuItem editMenuItem = new MenuItem("Edit");
                deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        currentRow = tableView.getSelectionModel().getSelectedItem();

                                Utility.loadNewStage("/res/layouts/ReservationMainView.fxml", "Reservations", new Stage());
                                    //
                                 }
                            // ... user chose CANCEL or closed the dialog


                });
                /*editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        currentBox = tableView.getSelectionModel().getSelectedItem();
                        try {

                            FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(Controller.class.getResource("EditBoxWindow.fxml"));
                        AnchorPane boxFilter= fxmlLoader.load();

                        Stage dialogStage = new Stage();
                        dialogStage.setTitle("Edit Box");

                        Scene scene = new Scene(boxFilter);
                        dialogStage.setScene(scene);

                        EditBoxController controller = fxmlLoader.getController();
                        //controller.setService(service);
                        controller.setDialogStage(dialogStage);
                        controller.setBoxIDField(currentBox.getId()+"");
                        controller.setMain(this.);
                        dialogStage.show();
                        } catch (IOException e) {
                        }
                    }
                });*/
                contextMenu.getItems().addAll( deleteMenuItem);
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu)null)
                                .otherwise(contextMenu)
                );
                return row ;
            }
        });

    }

    public void openNewsLetter(){
        Utility.loadNewStage("/res/layouts/newsletter.fxml", "Newsletter", new Stage());
    }

}

