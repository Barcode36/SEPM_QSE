package main.java.ac.at.tuwien.sepm.QSE15.gui.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.ColumnNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
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

import static javafx.scene.control.Alert.AlertType.CONFIRMATION;

/**
 * Created by Stefan Puhalo on 5/15/2017.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class EmployeeManagerController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeManagerController.class);

    @FXML
    TableView<Employee> tableView;

    @FXML
    TableColumn<Employee, Integer> pidColumn;

    @FXML
    TableColumn<Employee, String> positionColumn;

    @FXML
    TableColumn<Employee, String> nameColumn;

    @FXML
    TableColumn<Employee, String> surnameColumn;

    @FXML
    TableColumn<Employee, Long> salaryColumn;

    @FXML
    TableColumn<Employee, Date> birthDateColumn;

    @FXML
    ChoiceBox positionChoiceBox;

    @FXML
    ChoiceBox filterChoiceBox;

    @FXML
    TextField fromTextField;

    @FXML
    TextField toTextField;

    @FXML
    DatePicker fromDatePicker;

    @FXML
    DatePicker toDatePicker;

    @FXML
    private Button editButton;

    @FXML
    private ImageView employeeImageView;

    @FXML
    private Button cancelButton;


    private static ObservableList<Employee> employees = FXCollections.observableArrayList();

    private AnnotationConfigApplicationContext context;

    EmployeeServiceIMPL employeeService;

    private String selectedFilter = "None";

    protected static Employee selectedEmployee;


    public void initialize() {
        context = new AnnotationConfigApplicationContext(this.getClass());
        employeeService = context.getBean(EmployeeServiceIMPL.class);
        logger.info("INFO: Entering Employee Manager stage.");

        fromDatePicker.setVisible(false);
        toDatePicker.setVisible(false);


        editButton.setDisable(true);

        ObservableList<String> positions = FXCollections.observableArrayList();

        try {
            positions.addAll(employeeService.getAllPositions());
        } catch (ServiceException e) {
            logger.error("Get all positions failed");
        }


        positionChoiceBox.getItems().add("All");
        positionChoiceBox.getItems().addAll(positions);

        positionChoiceBox.getSelectionModel().select("All");

        filterChoiceBox.getItems().add("None");
        filterChoiceBox.getItems().add("Name");
        filterChoiceBox.getItems().add("Surname");
        filterChoiceBox.getItems().add("Date of Birth");
        filterChoiceBox.getItems().add("Salary");

        filterChoiceBox.getSelectionModel().select("None");

        pidColumn.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("Pid"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("Role"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("Name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("Surname"));
        birthDateColumn.setCellValueFactory(new PropertyValueFactory<Employee, Date>("Bdate"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<Employee, Long>("Salary"));


        positionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        positionColumn.setOnEditCommit(
                event -> {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setRole(event.getNewValue().substring(0, 1).toUpperCase() + event.getNewValue().substring(1));
                    editButton.setDisable(false);
                }
        );

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(
                event -> {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setName(event.getNewValue().substring(0, 1).toUpperCase() + event.getNewValue().substring(1));
                    editButton.setDisable(false);

                }
        );

        surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameColumn.setOnEditCommit(
                event -> {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setSurname(event.getNewValue().substring(0, 1).toUpperCase() + event.getNewValue().substring(1));
                    editButton.setDisable(false);

                }
        );


        birthDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Date>() {
            @Override
            public String toString(Date object) {
                if(object != null) {
                    return object.toString();
                }else {
                    return "";
                }
            }

            @Override
            public Date fromString(String string) {
                try {
                    return Date.valueOf(string);
                }catch (Exception e) {
                    return null;
                }
            }
        }));

        birthDateColumn.setOnEditCommit(
                event -> {
                    if(event.getNewValue() != null && !event.getNewValue().equals("")) {
                        if(Date.valueOf(event.getNewValue().toString()).before(Date.valueOf(LocalDate.now().minusYears(16)))) {
                            (event.getTableView().getItems().get(event.getTablePosition().getRow())).setBdate(Date.valueOf(event.getNewValue().toString()));
                            editButton.setDisable(false);
                        }else {
                            Utility.showError("Employee must be older then 16 years");
                            editButton.setDisable(true);
                        }
                    }else {
                        Utility.showError("Please insert the Date of Birth in format YYYY-MM-DD");
                        editButton.setDisable(true);

                    }
                }
        );

        salaryColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Long>() {
            @Override
            public String toString(Long object) {
                if(object != null) {
                    NumberFormat n = NumberFormat.getCurrencyInstance(Locale.GERMANY);
                    String s = n.format(object / 100.0);
                    return String.valueOf(s);
                }
                else return "";
            }

            @Override
            public Long fromString(String string) {
                try {

                    Long longValue = getLongSalary(string);
                    return longValue;
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }));

        salaryColumn.setOnEditCommit(
                event -> {
                    if (event.getNewValue() == null) {
                        Utility.showError("Please insert the salary as a numeric value");
                        logger.error("ERROR: No salary found");
                        editButton.setDisable(true);
                    } else if (event.getNewValue() <= 0) {
                        Utility.showError("Please insert the salary greater than 0.");
                        logger.error("ERROR: Salary < 0");
                        editButton.setDisable(true);
                    } else {
                        (event.getTableView().getItems().get(event.getTablePosition().getRow())).setSalary(event.getNewValue());
                        editButton.setDisable(false);
                    }
                }
        );


        try {
            employees = employeeService.getAllEmployees();
        } catch (ServiceException e) {
            logger.error("Get all Employees failed.");
        }

        tableView.setItems(employees);
        tableView.setEditable(true);

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Employee>() {
            @Override
            public void changed(ObservableValue<? extends Employee> observable, Employee oldValue, Employee newValue) {
                selectedEmployee = newValue;
                if(selectedEmployee != null) {
                    if(selectedEmployee.getPicture() != null) {
                        Path basePath = Paths.get(new File("").getAbsolutePath().concat("\\QSE2017\\src"));
                        Path absolutePath = Paths.get(basePath.toString().concat("\\" + selectedEmployee.getPicture()));
                        File file = new File(absolutePath.toString());
                        if(file!=null) {
                            BufferedImage bufferedImage = null;
                            try {
                                bufferedImage = ImageIO.read(file);
                            } catch (IOException e) {
                            }
                            if (bufferedImage != null) {
                                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                                employeeImageView.setImage(image);
                            }
                        }

                    }else {
                        employeeImageView.setImage(null);
                    }
                }
            }
        });

        filterChoiceBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                selectedFilter = newValue.toString();
                if(selectedFilter.equals("Date of Birth")) {
                    selectedFilter = "BDATE";
                    fromDatePicker.setVisible(true);
                    toDatePicker.setVisible(true);
                    fromDatePicker.valueProperty().setValue(LocalDate.of(1985,1,1));
                    toDatePicker.valueProperty().setValue(LocalDate.of(1985,1,1));
                    fromTextField.setVisible(false);
                    toTextField.setVisible(false);
                }

                else  {
                    fromDatePicker.setVisible(false);
                    toDatePicker.setVisible(false);
                    fromTextField.setVisible(true);
                    toTextField.setVisible(true);

                }
            }
        });

    }

    public void clickOnApply() {

        Object from;
        Object to;
        if(fromTextField.isVisible() && toTextField.isVisible()) {
            from = fromTextField.getText();
            to = toTextField.getText();
        }else {
            from = fromDatePicker.getValue();
            to = toDatePicker.getValue();

            if(Date.valueOf(from.toString()).after(Date.valueOf(to.toString()))) {
                Utility.showError("Please select From date before To date");
                return;
            }
        }

        String position = positionChoiceBox.getSelectionModel().getSelectedItem().toString();

        if(selectedFilter.equals("Salary")) {
            try {
                from = getLongSalary(from.toString());
            } catch (Exception e) {
                Utility.showError("Please insert value in form of decimal number.");
                return;
            }
            try {
                to = getLongSalary(to.toString());
            } catch (Exception e) {
                Utility.showError("Please insert value in form of decimal number.");
                return;
            }
        }


        if(selectedFilter.equals("BDATE")) {

            if(from == null || to == null) {
                Utility.showError("Please choose Date range.");
                return;
            }
        }


        if(!selectedFilter.equals("None") && (from.equals("") || to.equals(""))) {
            Utility.showError("Please insert filter values in fields From and To or chose dates.");
            return;
        }

        if(!position.equals("All") && !selectedFilter.equals("None")) {

            try {
                employees = employeeService.getAllEmployeesForPositionFromTo(selectedFilter, position, from,to);
            } catch (ServiceException e) {
                logger.error("Get all employees for position from to failed.");
            }

        }

        if(position.equals("All") && !selectedFilter.equals("None")) {
            try {
                employees = employeeService.getAllEmployeesFromTo(selectedFilter, from, to);
            } catch (ServiceException e) {
                logger.error("Get all employees from to failed.");
            }
        }

        if(!position.equals("All") && selectedFilter.equals("None")) {
            try {
                employees = employeeService.getAllEmployeesForPosition(position);
            } catch (ServiceException e) {
                logger.error("Get all employees for position failed.");
            }
        }

        if(position.equals("All") && selectedFilter.equals("None")) {
            try {
                employees = employeeService.getAllEmployees();
            } catch (ServiceException e) {
                logger.error("Get all employees failed.");

            }

        }

        tableView.setItems(employees);

    }

    public void clickOnEdit() {
        try {
            employeeService.updateAllEmployees(employees);
            Utility.showAlert("Employees successfully updated",CONFIRMATION);
            editButton.setDisable(true);
            logger.info("Employees updated");
        } catch (ServiceException e) {
        }
    }

    public void clickOnAdd() {
        Stage stage = new Stage();
        stage.setTitle("Add Employee");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/layouts/employee/AddNewEmployee.fxml"));
            AnchorPane anchorPane = loader.load();
            Scene scene = new Scene(anchorPane);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
        }
    }

    public void clickOnShowDetails() {
        if(getSelectedEmployee() == null) {
            Utility.showError("Please select an employee from the table.");
            return;
        }
        Stage stage = new Stage();
        stage.setTitle("Employee Details");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/layouts/employee/EmployeeDetails.fxml"));
            AnchorPane anchorPane = loader.load();
            Scene scene = new Scene(anchorPane);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            logger.error("Opening employee details stage failed.");
        }
    }

    public void clickCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        selectedEmployee = null;
        logger.info("Employee Manager stage canceled.");
        stage.close();
    }

    public void clickOnClearFilters() {
        positionChoiceBox.getSelectionModel().select("All");
        filterChoiceBox.getSelectionModel().select("None");
        fromTextField.clear();
        toTextField.clear();
        try {
            employees = employeeService.getAllEmployees();
        } catch (ServiceException e) {
            logger.error("Get all employees failed.");
        }
        tableView.setItems(employees);
    }

    public void clickOnDeleteEmployee() throws ServiceException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this employee?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if(alert.getResult() == ButtonType.YES) {
            if(selectedEmployee != null) {
                try {
                    employeeService.delete(selectedEmployee);
                    logger.info("Employee with ID = " + selectedEmployee.getPid() + " successfully deleted.");
                    clickOnClearFilters();
                } catch (ServiceException e) {
                    logger.error("Delete employee failed.");
                }
            }else {
                Utility.showError("Please select an employee from table which you want to delete.");
            }
        }else {
            alert.close();
        }
    }

    public Employee getSelectedEmployee() {
        return selectedEmployee;
    }

    public Long getLongSalary(String price) {
        Double doubleValue = Double.parseDouble(price) * 100;
        return doubleValue.longValue();

    }



}