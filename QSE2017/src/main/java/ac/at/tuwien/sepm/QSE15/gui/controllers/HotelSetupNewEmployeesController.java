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
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.UserNotValidException;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.authenticationService.MyAuthenticationService;
import main.java.ac.at.tuwien.sepm.QSE15.service.emailPasswordService.EmailPasswordService;
import main.java.ac.at.tuwien.sepm.QSE15.service.employeeService.EmployeeServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ervincosic on 17/05/2017.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class HotelSetupNewEmployeesController extends AddNewEmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddNewEmployeeController.class);

    @FXML
    public TableView<Employee> employeesTableView;
    @FXML
    public TableColumn<Employee, String> firstNameColumn;
    @FXML
    public TableColumn<Employee, String> lastNameColumn;
    @FXML
    public TableColumn<Employee, String> positionColumn;
    @FXML
    public Button removeButton;
    @FXML
    public Button confirmButton;
    @FXML
    public Button addButton;

    private MyAuthenticationService authenticationService;

    private EmployeeServiceIMPL employeeServiceIMPL;

    private ArrayList<Employee> employees;

    private EmailPasswordService emailPasswordService;

    @Override
    public void initialize() {

        AnnotationConfigApplicationContext context;

        context = new AnnotationConfigApplicationContext(this.getClass());

        authenticationService = context.getBean(MyAuthenticationService.class);

        employeeServiceIMPL = context.getBean(EmployeeServiceIMPL.class);

        emailPasswordService = context.getBean(EmailPasswordService.class);

        employees = new ArrayList<>();

        initSuperClass();


    }

    /**
     * use this method to initialize the super class
     */
    private void initSuperClass(){

        super.setAuthenticationService(authenticationService);

        super.setEmployeeService(employeeService);

        super.initialize();

    }

    @FXML
    private void onAddButtonClicked(){

        Employee employee = super.addEmployee();

        if(employee == null){
            return;
        }

        employees.add(employee);

        LOGGER.info("Employee added to table.");

        addEmployeeToTable();

        super.employeesImage.setImage(new Image("/res/images/no_image.png"));
    }

    private void addEmployeeToTable() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

        positionColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        employeesTableView.setItems(FXCollections.observableArrayList(employees));
    }

    @FXML
    private void onRemoveButtonClicked(){
        //todo: are you sure dialog
        int removeId = employeesTableView.getSelectionModel().getFocusedIndex();

        employees.remove(removeId);

        employeesTableView.setItems(FXCollections.observableArrayList(employees));

    }

    @FXML
    private void onConfirmButtonClicked(){
        //todo: are you sure dialog
        String adminPassword = getAdminPassword();

        if(!adminPassword.equals("25387da5-71a3-4871-9591-834ebe7ddff7")) { // if the input is empty

            Employee admin = Main.mainWindowController.getLoggedEmployee();

            admin.setPassword(authenticationService.encryptPassword(adminPassword));

            try {
                authenticationService.validateUsernameAndPassword(admin); // check if the password is correct
            } catch (UserNotValidException e) {
                Utility.showError("Wrong password.");
                return;
            }

            if (!employees.isEmpty()) {
                for (Employee employee : employees) {
                    try {

                        emailPasswordService.saveEmailPasswordForUser(employee, admin, adminPassword); //generate email password

                        employee.setPassword(authenticationService.encryptPassword(employee.getPassword())); // encrypt user password

                        employeeServiceIMPL.create(employee); // persist user
                    } catch (ServiceException e) {
                        LOGGER.error("Error while adding the employees.");
                    }
                }

                Utility.showAlert("You have successfuly added yout employees, " +
                        "only one more step left.", null, "Next step", Alert.AlertType.INFORMATION);

                Utility.loadNewStage("/res/layouts/hotelSetup/HotelSetupAddRooms.fxml", "Add rooms to your hotel.", addButton);
            } else {
                Utility.showAlert("You have to add some employees.", Alert.AlertType.ERROR);
            }
        }else{
            Utility.showError("Wrong password.");
        }
    }

    /**
     * This method shows and alert dialog with an input field for the user password
     *
     * @return (1) 25387da5-71a3-4871-9591-834ebe7ddff7 - if nothing was written
     *         (2) the actual user password
     */
    private String getAdminPassword(){
        String adminPassword = Utility.showPasswordInputDialog();
        if(adminPassword == null ){
            return "25387da5-71a3-4871-9591-834ebe7ddff7"; //Unique UUID
        }else if (adminPassword.length() == 0){
            return "25387da5-71a3-4871-9591-834ebe7ddff7";
        }
        return adminPassword;
    }


}
