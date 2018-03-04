package main.java.ac.at.tuwien.sepm.QSE15.gui.controllers;

import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.ac.at.tuwien.sepm.QSE15.dao.roomDAO.CategoryDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Category;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Room;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomService.CategoryServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomService.RoomServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luka Veljkovic on 5/17/17.
 */

@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class RoomController {

    public Stage primaryStage;

    public ComboBox<String> categoryComboBox;
    public TextArea extrasTextArea;
    public TextField roomNameTextField;

    private RoomServiceIMPL roomServiceIMPL;
    private static final Logger LOGGER = LoggerFactory.getLogger(RoomController.class);
    private AnnotationConfigApplicationContext context;
    private CategoryServiceIMPL categoryServiceIMPL;

    public void initialize(){

        List<Category> list = new ArrayList<>();
        List<String> categoryNames = new ArrayList<>();

        if (categoryServiceIMPL == null){

            context = new AnnotationConfigApplicationContext(this.getClass());
            categoryServiceIMPL = context.getBean(CategoryServiceIMPL.class);
        }

        try {
            list = categoryServiceIMPL.getAll();
        } catch (ServiceException e) {
            LOGGER.error("Error while getting all categories.");
        }

        for (Category c : list){

            categoryNames.add(c.getName());
        }

        /**
         * used for checking if the variable is preset
         */
        if(roomServiceIMPL == null) {
            context = new AnnotationConfigApplicationContext(this.getClass());
            roomServiceIMPL = context.getBean(RoomServiceIMPL.class);
        }

        categoryComboBox.setItems(FXCollections.observableArrayList(categoryNames));

        if (!categoryNames.isEmpty()) {
            categoryComboBox.getSelectionModel().select(0);
        }
    }

    protected Room parseInputToRoom() {

        Room room = new Room();

        if (categoryComboBox.getValue()!= null ){

            room.setCategory(categoryComboBox.getValue());
        }else{
            Utility.showAlert("Please choose a category.", Alert.AlertType.ERROR);
            LOGGER.info("No category is selected.");
        }

        if (extrasTextArea.getText() != null) {

            room.setExtras(extrasTextArea.getText());
        } else{

            room.setExtras("");
        }

        if (roomNameTextField.getText().matches("[0-9]+")) {

            room.setRnr(Integer.parseInt(roomNameTextField.getText()));
        } else{

            LOGGER.error("Entered value is not an integer number.");

            Utility.showAlert("Room number must be integer number.", Alert.AlertType.ERROR);
            LOGGER.error("Room number is not integer number.");
            return null;
        }

        room.setPrice(getPriceFromRoom());

        /**
         * This is already instanced in the initialize method
         */
        // context = new AnnotationConfigApplicationContext(this.getClass());
        //roomServiceIMPL = context.getBean(RoomServiceIMPL.class);
        resetFields();
        return room;

    }

    public void createButtonClicked() {
        Room room = parseInputToRoom();
        if(room != null) {
            if (room.getCategory() != null && room.getExtras() != null && room.getRnr() != null) {

                try {
                    roomServiceIMPL.create(room);
                    Main.mainWindowController.updateTableAfterRoomInsert(room);
                    Utility.showAlert("Room is created successfully.", Alert.AlertType.INFORMATION);
                    LOGGER.info("Room is created successfully.");
                } catch (ServiceException e) {
                    LOGGER.error("Error while creating a room in room controller.");
                }
            } else {

                Utility.showAlert("Room number and category are mandatory.", Alert.AlertType.ERROR);
                LOGGER.error("Some values are missing.");
            }
        }
    }

    private void resetFields(){
        categoryComboBox.getSelectionModel().select(0);
        extrasTextArea.setText("");
        roomNameTextField.setText("");
    }


    private long getPriceFromRoom(){
        return 200;
    }

    protected void setRoomServiceIMPL(RoomServiceIMPL roomServiceIMPL) {
        this.roomServiceIMPL = roomServiceIMPL;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}