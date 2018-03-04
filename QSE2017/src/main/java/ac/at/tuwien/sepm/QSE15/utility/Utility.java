package main.java.ac.at.tuwien.sepm.QSE15.utility;

import com.sun.istack.internal.Nullable;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.StringConverter;
import main.java.ac.at.tuwien.sepm.QSE15.gui.controllers.HotelSetupController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ervincosic on 17/05/2017.
 */
public class Utility {

    private static final Logger LOGGER = LoggerFactory.getLogger(HotelSetupController.class);
    private static String regexAlpha = "[a-zA-Z]+";
    private static String regexNum = "^\\d+$";
    private static String regexAlphaNum = "[a-zA-Z0-9]+";
    static String passwordString;
    /**
     * use this method to simply show an allert dialog
     * @param message
     * @param header - can be null
     * @param title
     * @param type - Alert.AlertType
     */
    public static void showAlert(String message, @Nullable String header, String title, Alert.AlertType type){
        Alert alert = new Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        alert.showAndWait();
    }

    public static void showAlert(String message, Alert.AlertType type){

        String title;

        switch (type){
            case CONFIRMATION:
                title = "Confirmation";
                break;
            case ERROR:
                title = "Error";
                break;
            case INFORMATION:
                title = "Information";
                break;
            case WARNING:
                title = "Warning";
                break;
            default:
                title = "";
                break;
        }
        showAlert(message, null, title, type);

    }

    public static void showError(String message){
        showAlert(message, Alert.AlertType.ERROR);
    }



    public static void loadNewStage(String resourceLocation, String title, @Nullable Stage currentStage){
        FXMLLoader fxmlLoader = new FXMLLoader(Utility.class.getResource(resourceLocation));

        LOGGER.info("Loading new layout: " + resourceLocation);

        try {
            AnchorPane anchorPane = fxmlLoader.load();

            Scene scene = new Scene(anchorPane);

            Stage addEmployeesStage = new Stage();



            addEmployeesStage.setScene(scene);
            addEmployeesStage.setTitle(title);

            addEmployeesStage.show();

            if(currentStage != null ) {
                currentStage.close();
            }

        } catch (IOException e) {

            LOGGER.info(e.getMessage());

            Utility.showAlert("Unable to open stage.", e.getMessage(), "Error", Alert.AlertType.ERROR);
            Thread.dumpStack();
        }
    }

    public static void loadNewStage(String resourceLocation, String title, Button button){
        if (((Stage) button.getScene().getWindow()).getTitle().equals("HSM")){
            loadNewStage(resourceLocation, title, new Stage());

        }else {
            loadNewStage(resourceLocation, title, (Stage) button.getScene().getWindow());
        }
    }


    /**
     * Use this method to check if a email has a valid regex
     * @param email
     * @return
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean checkIfDateValid(DatePicker d1, DatePicker d2){
        if (d1.getValue().isBefore(d2.getValue())){
            return true;
        }
        else {
            Utility.showAlert("From date is after until date. Please enter the valid dates.", Alert.AlertType.WARNING);
            return false;
        }
    }

    public static boolean checkIfValidNumbersOnly(String price){
        if(price.matches(regexNum)){
            return true;
        }
        else {
            showAlert("Input is not valid. Please use only numbers.", Alert.AlertType.WARNING);
            return false;
        }
    }


    public static boolean checkIfValidAlphaNumeric(String s){
        if(s.matches(regexAlphaNum)){
            return true;
        }
        else {
            showAlert("Please use only Letters and Numbers.", Alert.AlertType.WARNING);
            return false;
        }
    }

    public static boolean checkIfValidAlpha(String s){
        if(s.matches(regexAlpha)){
            return true;
        }
        else {
            showAlert("Please use only Letters not Numbers or some Characters or Whitespace.", Alert.AlertType.WARNING);
            return false;
        }
    }

    public static boolean checkIfTextFieldValidNotEmpty(TextField textField){
        if (!textField.equals("")){
            return true;
        }
        else {
            showAlert("Please fill all TextFields.", Alert.AlertType.WARNING);
            return false;
        }
    }

    public static boolean checkIfTextFieldValidNotEmpty(List<TextField> textFields){


        for (TextField t : textFields){
            if (t.equals("")){
                showAlert("Please fill all TextFields.", Alert.AlertType.WARNING);
                return false;
            }
        }
        return true;
    }


    public static void changeFormatDatePicker(DatePicker datePicker){

        datePicker.setEditable(false);
        String pattern = "yyyy-MM-dd";

        datePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }

    public static boolean checkIfDateIsNotNull(DatePicker date){
        if (date.getValue()==null){
            showAlert("Please choose date.", Alert.AlertType.WARNING);
            return false;
        }
        else{
            return true;
        }
    }

    public static String showPasswordInputDialog(){
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Confirm");
        dialog.setHeaderText("Please enter you password.");
// Set the button types.
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>("username", password.getText());
            }
            return null;
        });
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(usernamePassword -> {
            passwordString = usernamePassword.getValue().toString();
        });
        return passwordString;
    }

}
