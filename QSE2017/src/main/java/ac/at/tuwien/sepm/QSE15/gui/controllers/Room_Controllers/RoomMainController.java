package main.java.ac.at.tuwien.sepm.QSE15.gui.controllers.Room_Controllers;

import javafx.beans.binding.Bindings;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Category;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Lock;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Room;
import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;
import main.java.ac.at.tuwien.sepm.QSE15.gui.controllers.Main;
import main.java.ac.at.tuwien.sepm.QSE15.gui.controllers.ReservationController.ReservationMainController;
import main.java.ac.at.tuwien.sepm.QSE15.gui.controllers.RoomController;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomService.CategoryServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomService.RoomServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.utility.Utility;
import org.controlsfx.control.table.TableRowExpanderColumn;
import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivana on 22.5.2017.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class RoomMainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomMainController.class);

    public Stage primaryStage;
    public Button reservationMainBTN1;
    public Button newRoomBTN;
    public Slider priceSlider;
    public Button searchBTN;
    public ImageView categoryIcon;
    public ImageView priceIcon;
    public Button clearBTN;
    public ComboBox<String > categoryCBox;

    private TableRowExpanderColumn<Room> expander;

    public TableView<Room> roomsTableView;
    public TableColumn<Room,String> rnrColumn;
    public TableColumn<Room,String> priceColumn;
    public TableColumn<Category,String> bedsColumn;
    public TableColumn<Room,String> categoryColumn;
    public TableColumn<Room,String> extrasColumn;

    public ImageView roomImage;
    public TextArea extrasTextArea;
    public Button updateBTN;
    public ComboBox<String >  categoryCBox1;
    public Label roomIdLB;
    public ImageView imageIcon;
    public ImageView editRoomIcon;
    public ImageView lockIcon;
    public ImageView bedsIcon;
    public AnchorPane mainPane;
    public TextField rnrTF;
    public Label priceLB;

    public TextField roomNrTF;
    public ImageView roomNrIcon;

    public TableView<Lock> locksTableView;
    public TableColumn<Lock,String> lidColumn, reasonColumn, fromColumn, untilColumn;
    private List<Lock> lockList;

    private List<Integer> roomNrList;
    private List<Room> rooms;
    private List<Category> categories;
    private AnnotationConfigApplicationContext context;
    private RoomServiceIMPL roomService;
    private Room currentRoom;
    private CategoryServiceIMPL categoryService;


    public void initialize(){

        context = new AnnotationConfigApplicationContext(this.getClass());
        roomService = context.getBean(RoomServiceIMPL.class);
        categoryService = context.getBean(CategoryServiceIMPL.class);

        getCategories();
        setUpComboBox();

        getRooms(new Room(),new Category());
        setUpAutoFillTextField();
        setUpSlider(roomsTableView.getItems());
        setUpIcons();
        roomsTableView.getSelectionModel().selectFirst();
        changeRoomImage();



        roomsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Room>() {
            public void changed(ObservableValue<? extends Room> observable, Room oldValue, Room newValue) {
                currentRoom = roomsTableView.getSelectionModel().getSelectedItem();
                changeSelectedRoom(newValue);
                changeRoomImage();

                if (newValue!=null && oldValue!=null && expander.getExpandedProperty(oldValue).getValue()){
                    expander.toggleExpanded(roomsTableView.getItems().indexOf(oldValue));

                }
            }
        });

        roomsTableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    expander.toggleExpanded(roomsTableView.getItems().indexOf(roomsTableView.getSelectionModel().getSelectedItem()));
                }
            }
        });


        priceSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                priceLB.setText(String.format("%.2f", new_val));
            }
        });


    }

    public List<Room> priceToDouble(List<Room> rooms){
        for (int i = 0; i<rooms.size(); i++){
            Long total = rooms.get(i).getPrice()/100;
            rooms.get(i).setPrice(total);
        }
        return rooms;
    }

    private void setUpExpander(){
        expander = new TableRowExpanderColumn<>(param -> {
            GridPane editor = new GridPane();
            editor.setPadding(new Insets(10));
            editor.setHgap(10);
            editor.setVgap(5);

            Room room = param.getValue();

            DatePicker from = new DatePicker();
            from.setEditable(false);
            from.setPromptText("From Date");
            DatePicker until = new DatePicker();
            until.setEditable(false);
            until.setPromptText("Until Date");
            TextField reason = new TextField();
            reason.setPromptText("Enter Lock Reason");
            Utility.changeFormatDatePicker(from);
            Utility.changeFormatDatePicker(until);



            Button saveButton = new Button("Lock");
            saveButton.setOnAction(event -> {
                if (Utility.checkIfValidAlpha(reason.getText()) && Utility.checkIfDateIsNotNull(from) && Utility.checkIfDateIsNotNull(until) && Utility.checkIfDateValid(from, until)){
                    Lock lock = new Lock();
                    lock.setRnr(room.getRnr());
                    lock.setReason(reason.getText());
                    lock.setLocked_from(Date.valueOf(from.getValue()));
                    lock.setLocked_until(Date.valueOf(until.getValue()));
                    onLockClicked(lock);
                }

                param.toggleExpanded();
            });


            editor.addRow(0, reason,from, until,saveButton);


            return editor;
        });
    }

    private void setUpComboBox() {
        List<String> list = new ArrayList<>();
        for (Category c : categories){
            if (!list.contains(c.getName())){
                list.add(c.getName());
            }
        }


        categoryCBox1.setItems(FXCollections.observableArrayList(list));
        list.add("Any");
        categoryCBox.setItems(FXCollections.observableArrayList(list));
    }

    public void setUpAutoFillTextField(){

        if (rooms!=null) {
            roomNrList = new ArrayList<>();
            for (Room r : rooms) {
                roomNrList.add(r.getRnr());
            }
            TextFields.bindAutoCompletion(roomNrTF,roomNrList);
//            TextFields.bindAutoCompletion(rnrTF,roomNrList);
        }

    }

    public void changeRoomImage(){
        currentRoom = roomsTableView.getSelectionModel().getSelectedItem();
        if (currentRoom!=null){
            String cat = currentRoom.getCategory();
            String ext = cat.equals("Single Room")? "singleroom.jpg" : (cat.equals("Suite")? "suite.jpg" : "doubleroom.jpg");
            roomImage.setImage(new Image("res/images/" + ext));
        }
    }

    public void setUpIcons(){
        editRoomIcon.setImage(new Image("res/icons/edit.png"));
        imageIcon.setImage(new Image("res/icons/image.png"));
        categoryIcon.setImage(new Image("res/icons/categoryIcon.png"));
        roomNrIcon.setImage(new Image("res/icons/roomnr.png"));
        priceIcon.setImage(new Image("res/icons/euro.png"));
        lockIcon.setImage(new Image("res/icons/lock.png"));
    }


    public void changeSelectedRoom(Room newValue){
        if (newValue!=null) {
            currentRoom = newValue;
            roomIdLB.setText(newValue.getRnr() + "");
            extrasTextArea.setText(newValue.getExtras());
            getAllLockForSpecificRoom(newValue);
        //    rnrTF.setText(newValue.getRnr() + "");
        }
    }

    public void setUpSlider(ObservableList<Room> rooms){
        Long max = 0L;
        Long min = Long.MAX_VALUE;

        for (Room r : rooms){
            Long price = r.getPrice();

            if (price>max){
                max = price;
            }

            if (price<min){
                min = price;
            }

        }
        String s = min + "";
        Double dmin = Double.parseDouble(s);
        s = max + "";
        Double dmax = Double.parseDouble(s);
        priceSlider.setMin(dmin);
        priceSlider.setMax(dmax);
        priceSlider.setShowTickLabels(true);
        priceSlider.setShowTickMarks(true);
        priceSlider.setMajorTickUnit(20);
        priceSlider.setMinorTickCount(10);
        priceSlider.setValue(priceSlider.getMax());
        priceLB.setText(String.format("%.2f", priceSlider.getValue()));



    }


    public void setUpAndFillTable(){

        rnrColumn.setCellValueFactory(new PropertyValueFactory<Room, String>("rnr"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Room, String>("price"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<Room, String>("category"));
        extrasColumn.setCellValueFactory(new PropertyValueFactory<Room, String>("extras"));

        lidColumn.setCellValueFactory(new PropertyValueFactory<Lock,String>("lid"));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<Lock,String>("reason"));
        fromColumn.setCellValueFactory(new PropertyValueFactory<Lock,String>("locked_from"));
        untilColumn.setCellValueFactory(new PropertyValueFactory<Lock,String>("locked_until"));


        ObservableList<Room> list = FXCollections.observableArrayList(rooms);
        roomsTableView.setItems(list);
        roomsTableView.getSelectionModel().selectFirst();
        changeSelectedRoom((Room) roomsTableView.getSelectionModel().getSelectedItem());
    }

    public void getAllLockForSpecificRoom(Room room){
        try {
            lockList = roomService.getLocks(room.getRnr());
            locksTableView.setItems(FXCollections.observableArrayList(lockList));
            locksTableView.getSelectionModel().selectFirst();
        } catch (ServiceException e) {
            LOGGER.error("Unable to get Locks.");
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    public void onReservationMainClicked(ActionEvent actionEvent) {


        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("res/layouts/ReservationMainView.fxml"));
            AnchorPane pane;
            pane = loader.load();
            ((ReservationMainController)loader.getController()).setPrimaryStage(primaryStage);
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            LOGGER.error("Unable to load Create Room Window.");
        }
    }

    public void onNewRoomClicked(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/res/layouts/RoomLayout.fxml"));
            AnchorPane pane;
            pane = loader.load();
            ((RoomController)loader.getController()).setPrimaryStage(primaryStage);
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.setScene(scene);
            stage.show();

            //Utility.loadNewStage("/res/layouts/RoomLayout.fxml", "Add new Room", newRoomBTN);


        } catch (IOException e) {
            LOGGER.error("Unable to load Create Room Window.");
        }
    }

    public void onLockClicked(Lock lock) {

        try {
            roomService.lockRoom(lock);
            Utility.showAlert("The room : " + lock.getRnr() + " is locked from : " + lock.getLocked_from() + " until : " + lock.getLocked_until() + " successfully.", Alert.AlertType.CONFIRMATION);
            onClearClicked(null);
        } catch (ServiceException e) {
            LOGGER.error("Unable to lock room.");
        }

    }

    public void onClearClicked(ActionEvent actionEvent) {
        priceSlider.setValue(priceSlider.getMax());
        roomNrTF.setText("");
        categoryCBox.getSelectionModel().select(3);
        getRooms(new Room(),new Category());
        if (javafx.beans.binding.Bindings.isEmpty(roomsTableView.getItems()).getValue()){
            expander.toggleExpanded(roomsTableView.getSelectionModel().getSelectedIndex());
        }
    }

    public Integer checkIfRoomExistsTextFields(TextField textField){


        if (textField!=null && !textField.getText().equals("") && Utility.checkIfValidNumbersOnly(textField.getText())){
            Integer rnr = -1;

            if (roomNrList.contains(Integer.parseInt(textField.getText()))){
                rnr = Integer.parseInt(textField.getText());
            }
            if (rnr!=-1){
                return rnr;
            }
            else {
                Utility.showAlert("The room with nr: " + textField.getText() + " does not exists.", Alert.AlertType.WARNING);
            }

        }
        return null;
    }

    public void onSearchClicked(ActionEvent actionEvent) {

        Room room = new Room();
        Category category = new Category();

        room.setRnr(checkIfRoomExistsTextFields(roomNrTF));


        if (!categoryCBox.getSelectionModel().isEmpty() && !categoryCBox.getValue().equals("Any")){
            room.setCategory(categoryCBox.getValue());
            category.setName(categoryCBox.getValue());
        }

        Double dPrice = priceSlider.getValue()*100;
        Long longPrice = dPrice.longValue();
        room.setPrice(longPrice);
        category.setPrice(longPrice);

        getRooms(room,category);
    }

    public void onUpdateClicked(ActionEvent actionEvent) {
        try {
            Room r = roomService.get(roomsTableView.getSelectionModel().getSelectedItem().getRnr());

            if (!categoryCBox1.getSelectionModel().isEmpty()) {
                r.setCategory(categoryCBox1.getValue());
            }

            if (extrasTextArea.getText().equals("")){
                Utility.showAlert("Please fill the text area!", Alert.AlertType.WARNING);
            }

            else {
                if (extrasTextArea!=null && !extrasTextArea.getText().equals("")) {
                    r.setExtras(extrasTextArea.getText());
                    roomService.update(r);
                    getRooms(new Room(), new Category());
                    Utility.showAlert("Room is Updated.", Alert.AlertType.INFORMATION);
                }
            }

        } catch (ServiceException e) {
            LOGGER.error("Unable to update room");
        }
    }

    public void getRooms(Room room, Category category){
        try {
            rooms = roomService.search(room,category);
            rooms = priceToDouble(rooms);
            setUpExpander();
            setUpAndFillTable();
            roomsTableView.getColumns().add(expander);
        } catch (ServiceException e) {
            LOGGER.error("Unable to search rooms");
        }
    }

    public void getCategories() {
        try {
            categories = categoryService.getAll();
        } catch (ServiceException e) {
            LOGGER.error("Unable to get all Categories.");
        }
    }
}
