package main.java.ac.at.tuwien.sepm.QSE15.gui.controllers;

import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.java.ac.at.tuwien.sepm.QSE15.entity.hotel.Hotel;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.HotelNotValidException;

import main.java.ac.at.tuwien.sepm.QSE15.exceptions.NoLogoException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.UserNotValidException;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.authenticationService.MyAuthenticationService;
import main.java.ac.at.tuwien.sepm.QSE15.service.emailPasswordService.EmailPasswordService;
import main.java.ac.at.tuwien.sepm.QSE15.service.hotelService.MyHotelService;
import main.java.ac.at.tuwien.sepm.QSE15.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by ervincosic on 14/05/2017.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class HotelSetupController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(HotelSetupController.class);

    /**
     * This variable stores the selected image
     */
    private BufferedImage bufferedImage;

    @FXML
    public TextField nameTextField;

    @FXML
    public Button saveButton;

    @FXML
    public TextField  addressTextField;

    @FXML
    public TextField  ibanTextField;

    @FXML
    public TextField  bicTextField;

    @FXML
    public TextField  gmailTextField;

    @FXML
    public TextField  passwordTextField;

    @FXML
    public ImageView hotelLogoImageView;

    @FXML
    public ChoiceBox<String> hostSpinner;

    @FXML
    public javafx.scene.control.TextField smtpTextField;

    @FXML
    public javafx.scene.control.TextField portTextField;

    @FXML
    public javafx.scene.control.CheckBox tlsCheckBox;

    private MyHotelService hotelService;

    private AnnotationConfigApplicationContext context;

    private EmailPasswordService emailPasswordService;

    private MyAuthenticationService authenticationService;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateHostSpinner();

        context = new AnnotationConfigApplicationContext(this.getClass());

        hotelService = context.getBean(MyHotelService.class);

        authenticationService = context.getBean(MyAuthenticationService.class);

        emailPasswordService = context.getBean(EmailPasswordService.class);

        /**
         * Spinner on change listener changes visibility of smtp host and port
         */
        hostSpinner.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.intValue() == 3){
                smtpTextField.setVisible(true);
                portTextField.setVisible(true);
                tlsCheckBox.setVisible(true);
            }else{
                smtpTextField.setVisible(false);
                portTextField.setVisible(false);
                tlsCheckBox.setVisible(false);
            }
        });

        /**
         * On click listener for the image
         */
        hotelLogoImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onPictureClickedListener());

        /**
         * On click listener for the save button
         */
        saveButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onSaveButtonClicked());

       // saveButton.getScene().getStylesheets().add(getClass().getResource("/res/style.css").toString());

    }

    private void populateHostSpinner(){
        ArrayList<String> hosts = new ArrayList<>();

        hosts.add("Gmail");
        hosts.add("Hotmail");
        hosts.add("Yahoo");
        hosts.add("Custom");

        hostSpinner.setItems(FXCollections.observableArrayList(hosts));
        hostSpinner.getSelectionModel().select(0);
    }


    /**
     * This method is invoked while clicking on the save button.
     * Its sett as a handler in the initialize method
     */
    private void onSaveButtonClicked(){

        Hotel hotel = createHotelFromForm();

        if(hotel == null){
            return;
        }

        try {


            String adminPassword = getAdminPassword();


            if(adminPassword.equals("25387da5-71a3-4871-9591-834ebe7ddff7")){// If nothing was entered
                return;
            }

            Employee admin = Main.mainWindowController.getLoggedEmployee();
            admin.setPassword(authenticationService.encryptPassword(adminPassword));

            try {

                authenticationService.validateUsernameAndPassword(admin);

            } catch (UserNotValidException e) {
                Utility.showError("Wrong password.");
                return;
            }

            hotelService.create(hotel);

            emailPasswordService.saveEmailPasswordForAdmin(admin, adminPassword);

        } catch (ServiceException e) {

            LOGGER.error("Error while creating hotel.");

            Utility.showAlert("Error while saving the hotel.", null, "Error", Alert.AlertType.ERROR);

        } catch (HotelNotValidException e) {

            Utility.showAlert("The hotel is not valid.", null, "Error", Alert.AlertType.ERROR);
        }

        try {

            saveHotelLogo();

        } catch (NoLogoException e) {
           return;
        }

        Utility.loadNewStage("/res/layouts/hotelSetup/HotelSetupNewEmployees.fxml", "Add your emoloyees", (Stage) saveButton.getScene().getWindow());

    }

    private String getAdminPassword(){

        String password = Utility.showPasswordInputDialog();

        if(password == null ){
            return "25387da5-71a3-4871-9591-834ebe7ddff7"; //Unique UUID
        }else if (password.length() == 0){
            return "25387da5-71a3-4871-9591-834ebe7ddff7";
        }

        return password;
    }

    /**
     * this method is invoked when the selectpicture button is clicked
     */
    private void onPictureClickedListener(){

        bufferedImage = selectPicture();

        if(bufferedImage == null){
            return;
        }

        javafx.scene.image.Image image = SwingFXUtils.toFXImage(bufferedImage, null);

        hotelLogoImageView.setImage(image);

    }

    /**
     * This method is used to save the buffered picture
     */
    private void saveHotelLogo() throws NoLogoException{

        final String FILE_URL = "QSE2017/src/res/images/logo.jpg";

        if(bufferedImage == null){
            Utility.showAlert("Please set an image as your logo.",null, "Missing logo", Alert.AlertType.ERROR);
            throw  new NoLogoException();
        }

        try {

            FileOutputStream outputStream = new FileOutputStream(FILE_URL);

            ImageIO.write(bufferedImage,".jpg", outputStream);

        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        Utility.showAlert("Hotel saved", null, "Hotel setup", Alert.AlertType.INFORMATION);
    }

    /**
     * this method takes loads the picture and returns a buffered image
     *
     *
     * @return (1) selected buffered image
     *         (2) if nothing is loaded
     */
    private BufferedImage selectPicture(){

        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files","*.jpg", "*.gif"));

        Stage stage = (Stage) main.java.ac.at.tuwien.sepm.QSE15.gui.controllers.Main.getStage().getScene().getWindow();

        File selectedFile = chooser.showOpenDialog(stage);


        if(selectedFile == null){
            return null;
        }

        BufferedImage bufferedImage;

        try {

            bufferedImage = ImageIO.read(selectedFile);

        } catch (IOException e) {

            Utility.showAlert("Error while loading the picture.", null, "Error", Alert.AlertType.ERROR);

            return null;
        }


        return bufferedImage;

    }

    /**
     * this method is used to parse al the inputs to a hotel object
     * @return Hotel object filled with given data
     */
    private Hotel createHotelFromForm(){

        Hotel hotel = new Hotel();

        String message;

        String name = nameTextField.getText();

        String address = addressTextField.getText();

        String iban = ibanTextField.getText();

        String bic = bicTextField.getText();

        String email = gmailTextField.getText();

        String password = passwordTextField.getText();

        String host = "";

        String port = "";

        //TODO Implement custom host and other mail services
        if(hostSpinner.getSelectionModel().getSelectedIndex() == 3){

            host = smtpTextField.getText();

            port = portTextField.getText();

        }else if(hostSpinner.getSelectionModel().getSelectedIndex() == 0){ // Gmail

            host = "smtp.gmail.com";

            port = "465";

        }else if(hostSpinner.getSelectionModel().getSelectedIndex() == 1){ // Hotmail

            host = "smtp.live.com";

            port = "25"; // 25, really microsoft ?

        }else if(hostSpinner.getSelectionModel().getSelectedIndex() == 2){ // Yahoo

            host ="smtp.mail.yahoo.com";

            port ="465";

        }


        StringBuilder stringBuilder = new StringBuilder();

        if(!isStringSet(name)){
            stringBuilder.append("No Hotel name \n");
            nameTextField.getStylesheets().add("error");
        }else if(!isStringSet(address)){
            stringBuilder.append("NO address \n");
            addressTextField.getStylesheets().add("error");
        }else if(!isStringSet(iban)){
            stringBuilder.append("No Iban \n");
            ibanTextField.getStylesheets().add("error");
        }else if(!isStringSet(bic)){
            stringBuilder.append("No Bic \n");
            bicTextField.getStylesheets().add("error");
        }else if(!isStringSet(email)){
            stringBuilder.append("No e-mail address\n");
            gmailTextField.getStylesheets().add("error");
        }else if(!isStringSet(password)){
            stringBuilder.append("No password\n");
            passwordTextField.getStylesheets().add("error");
        }else if(!Utility.isEmailValid(email)){
            stringBuilder.append("Please write a valid E-Mail Address.");
        }

        if(hostSpinner.getSelectionModel().getSelectedIndex() == 3){
            if(!isStringSet(host)){
                stringBuilder.append("Please write the host name.");
            }else if(!isStringSet(port)){
                stringBuilder.append("Plese write the port.");
            }
        }

        message = stringBuilder.toString();

        if(!message.equals("")){

            Utility.showAlert(message, null, "Please fill the form.", Alert.AlertType.ERROR);
            return null;
        }

        hotel.setName(name);
        hotel.setAddress(address);
        hotel.setEmail(email);
        hotel.setPassword(password);
        hotel.setIban(iban);
        hotel.setBic(bic);
        hotel.setPort(port);
        hotel.setHost(host);

        return hotel;

    }

    /**
     * Check if a string is null or empty
     * @param string - to be checked
     * @return true if its a valid string
     */
    private boolean isStringSet(String string){
        if(string == null){
            return false;
        }else if (string.equals("")){
            return false;
        }
        return true;
    }

}
