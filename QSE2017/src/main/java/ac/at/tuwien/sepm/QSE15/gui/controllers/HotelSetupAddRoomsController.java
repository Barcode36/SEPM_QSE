package main.java.ac.at.tuwien.sepm.QSE15.gui.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Room;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomService.RoomServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ervincosic on 18/05/2017.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class HotelSetupAddRoomsController extends RoomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HotelSetupAddRoomsController.class);

    @FXML
    private TableView<Room> roomsTable;
    @FXML
    private TableColumn<Room, String> roomNumberColumn;
    @FXML
    private TableColumn<Room, String> categoryColumn;
    @FXML
    private TableColumn<Room, String> extrasColumn;

    private AnnotationConfigApplicationContext context;

    private RoomServiceIMPL roomServiceIMPL;

    private ArrayList<Room> rooms;
    @FXML
    private Button confirmbutton;

    @Override
    public void initialize() {

        context = new AnnotationConfigApplicationContext(this.getClass());

        roomServiceIMPL = context.getBean(RoomServiceIMPL.class);

        rooms = new ArrayList<>();

        super.setRoomServiceIMPL(roomServiceIMPL);

        super.initialize();
    }

    @FXML
    private void onAddButtonClicked(){

        Room room = super.parseInputToRoom();

        if(room == null){
            return;
        }else if(!checkIfRoomIsFree(room.getRnr())){

            Utility.showError("Room is already in the list.");

            return;
        }else if(room.getRnr() == null){
            return;
        }

        rooms.add(room);

        addRoomToTable();

    }

    private void addRoomToTable(){
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("rnr"));

        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        extrasColumn.setCellValueFactory(new PropertyValueFactory<>("extras"));

        roomsTable.setItems(FXCollections.observableArrayList(rooms));

        LOGGER.info("Room added.");
    }

    @FXML
    private void onRemoveButtonClicked(){
        if(!rooms.isEmpty()) {
            int id = roomsTable.getSelectionModel().getFocusedIndex();

            rooms.remove(id);

            roomsTable.setItems(FXCollections.observableArrayList(rooms));

        }else{
            Utility.showAlert("There are no rooms to delete.",null, "No rooms", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onConfirmButtonClicked(){
        FXMLLoader loader;

        loader = new FXMLLoader(this.getClass().getResource("/res/layouts/MainWindow.fxml"));

        AnchorPane anchorPane = null;
        try {
            anchorPane = loader.load();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        Scene scene = new Scene(anchorPane);

        MainWindowController mainWindowController = loader.getController();

        Employee loggedInEmployee = Main.mainWindowController.getLoggedEmployee();

        Main.mainWindowController = mainWindowController;

        mainWindowController.setLoggedEmployee(loggedInEmployee);

        Stage loginstage = (Stage) confirmbutton.getScene().getWindow();

        primaryStage = loginstage;
        mainWindowController.setUserNameText(loggedInEmployee);



        primaryStage.setHeight(837);
        primaryStage.setWidth(1272);


        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("HSM");

        mainWindowController.setCurrentStage(primaryStage);

        primaryStage.show();

        mainWindowController.setScrollBarBinding();


        LOGGER.info("Opening screen.");



        if(!rooms.isEmpty()){
            for(Room room : rooms){
                try {
                    roomServiceIMPL.create(room);
                     Main.mainWindowController.updateTableAfterRoomInsert(room);


                } catch (ServiceException e) {
                    LOGGER.error("Unable to create room.");
                    Utility.showAlert("Unable to create room", e.getMessage(), "Error", Alert.AlertType.ERROR);
                }
            }
        }else{
            Utility.showAlert("No rooms.", null, "Error", Alert.AlertType.ERROR);
        }


    }

    /**
     * This method checks if the room id is in the rooms list
     * @param id - id to check
     * @return (1) true - in case there is no room with such an id
     *         (2) false - in case there is a room with that id
     */
    private  boolean checkIfRoomIsFree(int id){
        for(Room room : rooms){
            if(room.getRnr() == id){
                return false;
            }
        }
        return true;
    }
}
