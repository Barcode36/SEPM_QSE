package main.java.ac.at.tuwien.sepm.QSE15.gui.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.authenticationService.AuthenticationService;
import main.java.ac.at.tuwien.sepm.QSE15.service.authenticationService.MyAuthenticationService;
import main.java.ac.at.tuwien.sepm.QSE15.service.employeeService.EmployeeServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.imageio.ImageIO;
import javax.rmi.CORBA.Util;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;


/**
 * Created by Stefan Puhalo on 5/14/2017.
 */

@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class AddNewEmployeeController {

    @FXML
    TextField firstNameField;

    @FXML
    TextField lastNameField;

    @FXML
    TextField phoneField;

    @FXML
    TextField emailField;

    @FXML
    TextField streetField;

    @FXML
    TextField cityField;

    @FXML
    TextField countryField;

    @FXML
    TextField postalCodeField;

    @FXML
    TextField ibanField;

    @FXML
    TextField bicField;

    @FXML
    TextField salaryField;

    @FXML
    TextField svnrField;

    @FXML
    TextField usernameField;

    @FXML
    PasswordField passwordField;

    @FXML
    ChoiceBox positionChoiceBox;

    @FXML
    ChoiceBox titleChoiceBox;

    @FXML
    DatePicker birthDatePicker;

    @FXML
    ImageView employeesImage;

    @FXML
    private Button cancelButton;

    @FXML
    private Label otherPositionLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label accountDetailsLabel;

    @FXML
    private TextField otherPositionTextField;

    @FXML
    private ImageView accountImageView;


    private AnnotationConfigApplicationContext context;

    private static final Logger logger = LoggerFactory.getLogger(AddNewEmployeeController.class);

    EmployeeServiceIMPL employeeService;

    MyAuthenticationService authenticationService;

    private Date dateOfBirth = Date.valueOf(LocalDate.of(1985,1,1));;

    String position = "";
    String imagePath = "";
    String sex;
    Integer rights = 0;
    String username = null;
    String password = null;

    public void setEmployeeService(EmployeeServiceIMPL employeeService) {
        this.employeeService = employeeService;
    }

    public void setAuthenticationService(MyAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public void initialize() {
        context = new AnnotationConfigApplicationContext(this.getClass());
        /**
         * Checking if these components were already set in the extending class
         * if not fetch them from <Code> private AnnotationConfigApplicationContext context; </Code>
         */
        if(employeeService == null) {
            employeeService = context.getBean(EmployeeServiceIMPL.class);
        }

        if(authenticationService == null){
            authenticationService = context.getBean(MyAuthenticationService.class);
        }
        logger.info("INFO: Entering Add Employee stage.");

        ObservableList<String> positions = FXCollections.observableArrayList();

        otherPositionLabel.setVisible(false);
        otherPositionTextField.setVisible(false);

        try {
            positions.addAll(employeeService.getAllPositions());
            positions.add("Other");
        } catch (ServiceException e) {
            logger.error("Get all positions failed");
        }

        positionChoiceBox.getItems().addAll(positions);

        titleChoiceBox.getItems().add("Mr");
        titleChoiceBox.getItems().add("Mrs");
        titleChoiceBox.getItems().add("Miss");

        birthDatePicker.valueProperty().setValue(LocalDate.of(1985,1,1));
        dateOfBirth = Date.valueOf(birthDatePicker.getValue());

        birthDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                dateOfBirth = Date.valueOf(newValue);
            }
        });

        positionChoiceBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) { // when clearing the choice box this method is invoked and throws a null pointer exception
                    if (newValue.equals("Manager")) {
                        otherPositionLabel.setVisible(false);
                        otherPositionTextField.setVisible(false);
                        usernameField.setVisible(true);
                        passwordField.setVisible(true);
                        accountDetailsLabel.setVisible(true);
                        usernameLabel.setVisible(true);
                        passwordLabel.setVisible(true);
                        accountImageView.setVisible(true);
                        otherPositionLabel.setVisible(false);
                        otherPositionTextField.setVisible(false);
                        position = newValue.toString();
                        rights = 1;
                    } else if (newValue.equals("Receptionist")) {
                        otherPositionLabel.setVisible(false);
                        otherPositionTextField.setVisible(false);
                        usernameField.setVisible(true);
                        passwordField.setVisible(true);
                        accountDetailsLabel.setVisible(true);
                        accountImageView.setVisible(true);
                        usernameLabel.setVisible(true);
                        passwordLabel.setVisible(true);
                        otherPositionLabel.setVisible(false);
                        otherPositionTextField.setVisible(false);
                        position = newValue.toString();
                        rights = 2;
                    } else if (newValue.equals("Other")) {
                        otherPositionLabel.setVisible(true);
                        otherPositionTextField.setVisible(true);
                        accountDetailsLabel.setVisible(false);
                        usernameLabel.setVisible(false);
                        passwordLabel.setVisible(false);
                        accountImageView.setVisible(false);
                        usernameField.setVisible(false);
                        passwordField.setVisible(false);
                    } else {
                        otherPositionLabel.setVisible(false);
                        otherPositionTextField.setVisible(false);
                        usernameField.setVisible(false);
                        passwordField.setVisible(false);
                        accountDetailsLabel.setVisible(false);
                        accountImageView.setVisible(false);
                        usernameLabel.setVisible(false);
                        passwordLabel.setVisible(false);
                    }
                }
            }
        });

        titleChoiceBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(newValue != null) { // when clearing the choice box this method is invoked and throws a null pointer exception
                    if (newValue.equals("Mr")) {
                        sex = "male";
                    } else {
                        sex = "female";
                    }
                }
            }
        });
    }

    public void loadImage() throws IOException {
        Stage stage = new Stage();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");

        //Set extension filter
        FileChooser.ExtensionFilter extFilterAll = new FileChooser.ExtensionFilter("All Images", "*.*");
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG", "*.jpg");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG", "*.png");
        fileChooser.getExtensionFilters().addAll(extFilterAll, extFilterJPG, extFilterPNG);

        Path basePath = Paths.get(new File("").getAbsolutePath().concat("\\QSE2017\\src"));


        fileChooser.setInitialDirectory(
                new File(basePath.toString().concat("\\res\\images")));


        //Show dialog
        File file = fileChooser.showOpenDialog(stage);

        try {
            if(file!=null) {
                Path absolutePath = Paths.get(file.getAbsolutePath());
                imagePath = basePath.relativize(absolutePath).toString();
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                employeesImage.setImage(image);
            }

        } catch (IOException ex) {
        }

    }

    protected Employee addEmployee() {

        /**
         * this is the employee that will be returned
         */
        Employee createdEmployee = null;

        if(positionChoiceBox.getSelectionModel().isEmpty() || (positionChoiceBox.getSelectionModel().equals("Other") &&
                otherPositionTextField.getText().equals(""))) {
            Utility.showError("Please choose a position of new employee or insert the other one.");
            return null;
        }

        if(titleChoiceBox.getSelectionModel().isEmpty() || firstNameField.getText().equals("") ||
                lastNameField.getText().equals("") || phoneField.getText().equals("") || emailField.getText().equals("")) {
            Utility.showError("Please fill in all personal details of new employee.");
            return null;
        }

        if(cityField.getText().equals("") || streetField.getText().equals("") || countryField.getText().equals("") ||
                postalCodeField.getText().equals("")) {
            Utility.showError("Please fill in all address details of new employee.");
            return null;
        }

        if(svnrField.getText().equals("") || ibanField.getText().equals("") || bicField.getText().equals("") || salaryField.equals("")) {
           Utility.showError("Please fill in all employment details of new employee.");
           return null;
        }

        if(!checkNumeric(salaryField.getText())) {
            Utility.showError("Please insert salaray of new employee as a numeric value.");
            return null;
        }

        if(usernameField.isVisible() && (usernameField.equals("") || (passwordField.equals("")))) {
            Utility.showError("Please fill in all account details of new employe.");
            return null;
        }

        if(otherPositionTextField.isVisible()) {
            position = otherPositionTextField.getText();
        }else{
            position = positionChoiceBox.getSelectionModel().getSelectedItem().toString();
        }

        if(usernameField.isVisible()) {
            username = usernameField.getText();
            password = authenticationService.encryptPassword(passwordField.getText());
        }

        createdEmployee = new Employee(firstNameField.getText(), lastNameField.getText(),
                streetField.getText(), postalCodeField.getText(), cityField.getText(), countryField.getText(),
                phoneField.getText(), emailField.getText(), dateOfBirth, sex,
                svnrField.getText(), ibanField.getText(), bicField.getText(), getLongSalary(salaryField.getText()), position, imagePath,
                false, username, password, rights);


        firstNameField.clear();
        lastNameField.clear();
        phoneField.clear();
        emailField.clear();
        streetField.clear();
        countryField.clear();
        postalCodeField.clear();
        cityField.clear();
        svnrField.clear();
        ibanField.clear();
        bicField.clear();
        salaryField.clear();
        usernameField.clear();
        passwordField.clear();
        birthDatePicker.valueProperty().setValue(LocalDate.of(1985,1,1));
        titleChoiceBox.getSelectionModel().clearSelection();
        positionChoiceBox.getSelectionModel().clearSelection();

        return createdEmployee;
    }

    /**
     * EDIT : ERVIN COSIC
     * I wrote this method to persist the employee dao because i needed the code for gathering all the fields
     *  in a employee object, so this method calls now the <Code>addEmployee()</Code> method, when the add button is pressed.
     *  The add new employee returns now a employee object.
     *
     *  EDIT 2 : ERVIN COSIC
     *  I moved the password hashing to the persisting, so i can use the password
     *  to generate an email encryption for this employee
     */
    public void persistEmployee(){
        Employee employee;


        try {
            employee = addEmployee();//generate employee from form

            if(employee != null && employee.getPassword() != null) {
                employee.setPassword(authenticationService.encryptPassword(employee.getPassword())); //encrypt the password of the employee
            }

            if(employee != null) {
                employee = employeeService.create(employee); // persist the employee
                Utility.showAlert("Employee with ID = " + employee.getPid() + " successfully added.", Alert.AlertType.CONFIRMATION);
                clickCancel();
            }

            //todo save email password for new employee
        } catch (ServiceException e) {
            logger.error("Unable to save employee");
            return;
        }


    }

    public void clickCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        logger.info("INFO: Add new Employee stage canceled.");
        stage.close();
    }

    public Long getLongSalary(String salary ) {
        Double doubleValue = Double.parseDouble(salary) * 100;
        return doubleValue.longValue();

    }

    public boolean checkNumeric(String value) {
        try {
            Double.parseDouble(value);
        }
        catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

}
