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
import main.java.ac.at.tuwien.sepm.QSE15.service.authenticationService.MyAuthenticationService;
import main.java.ac.at.tuwien.sepm.QSE15.service.employeeService.EmployeeServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

/**
 * Created by Stefan Puhalo on 5/15/2017.
 */

@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class EmployeeDetailsController {

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

    String position;
    String imagePath;
    String sex;
    Integer rights = 0;

    private Date dateOfBirth = null;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeDetailsController.class);

    private AnnotationConfigApplicationContext context;

    MyAuthenticationService authenticationService;

    Employee employee;

    EmployeeServiceIMPL employeeService;


    public void initialize() {
        context = new AnnotationConfigApplicationContext(this.getClass());
        employeeService = context.getBean(EmployeeServiceIMPL.class);
        authenticationService = context.getBean(MyAuthenticationService.class);
        logger.info("INFO: Entering Employee details stage.");

        employee = EmployeeManagerController.selectedEmployee;
        if(employee.getRights() != null) {
            rights = employee.getRights();
        }
        dateOfBirth = employee.getBdate();
        imagePath = employee.getPicture();

        ObservableList<String> positions = FXCollections.observableArrayList();

        try {
            positions.addAll(employeeService.getAllPositions());
        } catch (ServiceException e) {
            logger.error("Get all positions failed");
        }

        positionChoiceBox.getItems().addAll(positions);

        otherPositionLabel.setVisible(false);
        otherPositionTextField.setVisible(false);

        titleChoiceBox.getItems().add("Mr");
        titleChoiceBox.getItems().add("Mrs");
        titleChoiceBox.getItems().add("Miss");

        positionChoiceBox.getSelectionModel().select(employee.getRole());

        if(employee.getSex().equals("male")) {
            titleChoiceBox.getSelectionModel().select("Mr");
        }else {
            titleChoiceBox.getSelectionModel().select("Mrs");
        }
        birthDatePicker.valueProperty().setValue(employee.getBdate().toLocalDate());
        firstNameField.setText(employee.getName());
        lastNameField.setText(employee.getSurname());
        phoneField.setText(employee.getPhone());
        emailField.setText(employee.getEmail());
        streetField.setText(employee.getAddress());
        cityField.setText(employee.getPlace());
        countryField.setText(employee.getCountry());
        postalCodeField.setText(employee.getZip());
        svnrField.setText(employee.getSvnr());
        ibanField.setText(employee.getIban());
        bicField.setText(employee.getBic());


        NumberFormat n = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        String salary = n.format(employee.getSalary() / 100.0);
        salaryField.setText(salary);

        salaryField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    Long salaryLong = getLongSalary(newValue);
                    employee.setSalary(salaryLong);
                }catch (Exception e) {
                    Utility.showError("Please insert value in form of decimal number");

                }
            }
        });

        if(employee.getRole().equals("Manager")) {
            usernameField.setVisible(true);
            passwordField.setVisible(true);
            usernameField.setText(employee.getUsername());
            passwordField.setText(employee.getPassword());
            accountDetailsLabel.setVisible(true);
            usernameLabel.setVisible(true);
            passwordLabel.setVisible(true);
            accountImageView.setVisible(true);
            otherPositionLabel.setVisible(false);
            otherPositionTextField.setVisible(false);
            rights = 1;
        }else if (employee.getRole().equals("Receptionist")) {
            usernameField.setVisible(true);
            passwordField.setVisible(true);
            usernameField.setText(employee.getUsername());
            passwordField.setText(employee.getPassword());
            accountDetailsLabel.setVisible(true);
            usernameLabel.setVisible(true);
            passwordLabel.setVisible(true);
            accountImageView.setVisible(true);
            otherPositionLabel.setVisible(false);
            otherPositionTextField.setVisible(false);
            rights = 2;
        }else {
            usernameField.setVisible(false);
            passwordField.setVisible(false);
            accountDetailsLabel.setVisible(false);
            accountImageView.setVisible(false);
            usernameLabel.setVisible(false);
            passwordLabel.setVisible(false);


        }

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
                        accountImageView.setVisible(false);
                        usernameLabel.setVisible(false);
                        passwordLabel.setVisible(false);
                        usernameField.setVisible(false);
                        passwordField.setVisible(false);
                        rights = 0;
                    } else {
                        usernameField.setVisible(false);
                        passwordField.setVisible(false);
                        accountDetailsLabel.setVisible(false);
                        accountImageView.setVisible(false);
                        usernameLabel.setVisible(false);
                        passwordLabel.setVisible(false);
                        rights = 0;
                    }
                }
            }
        });

        titleChoiceBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(newValue.equals("Mr")) {
                    sex = "male";
                } else {
                    sex = "female";
                }
            }
        });


        if(imagePath != null) {
            Path basePath = Paths.get(new File("").getAbsolutePath().concat("\\QSE2017\\src"));
            Path absolutePath = Paths.get(basePath.toString().concat("\\" + imagePath));
            File file = new File(absolutePath.toString());
            if(file!=null) {
                BufferedImage bufferedImage = null;
                try {
                    bufferedImage = ImageIO.read(file);
                } catch (IOException e) {
                }
                if (bufferedImage != null) {
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                    employeesImage.setImage(image);
                }
            }

        }


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

    public void clickOnEdit() {

        employee.setRole(positionChoiceBox.getSelectionModel().getSelectedItem().toString());
        employee.setBdate(dateOfBirth);

        if(titleChoiceBox.getSelectionModel().getSelectedItem().equals("Mr")) {
            employee.setSex("male");
        }else {
            employee.setSex("female");
        }

        employee.setName(firstNameField.getText());
        employee.setSurname(lastNameField.getText());
        employee.setEmail(emailField.getText());
        employee.setPhone(phoneField.getText());
        employee.setAddress(streetField.getText());
        employee.setPlace(cityField.getText());
        employee.setCountry(countryField.getText());
        employee.setZip(postalCodeField.getText());
        employee.setSvnr(svnrField.getText());
        employee.setIban(ibanField.getText());
        employee.setBic(bicField.getText());
        employee.setPicture(imagePath);
        employee.setRights(rights);

        if(usernameField.isVisible()) {
            employee.setUsername(usernameField.getText());
            employee.setPassword(authenticationService.encryptPassword(passwordField.getText()));
        }

        try {
            employeeService.updateEmployee(employee);
        } catch (ServiceException e) {
            logger.error("Updated employee failed");
        }

        Utility.showAlert("Employee successfully updated.", Alert.AlertType.CONFIRMATION);

        clickCancel();
    }

    public void clickCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        logger.info("INFO: Employe Details stage canceled.");
        stage.close();
    }

    public Long getLongSalary(String price) {
        Double doubleValue = Double.parseDouble(price) * 100;
        return doubleValue.longValue();

    }

}
