package main.java.ac.at.tuwien.sepm.QSE15.test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.employeeDAO.JDBCEmployeeDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.ColumnNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.EmployeeNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.NoEmployeesFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.employeeService.EmployeeServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.test.stubs.EmployeeDaoStub;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Date;

/**
 * Created by Stefan Puhalo on 6/21/2017.
 */
public class AbstractEmployeeServiceTest {

    protected JDBCEmployeeDAO employeeDAO;

    protected EmployeeServiceIMPL employeeService;

    protected EmployeeDaoStub employeeDAOStub;

    protected void setEmployeeDAO(JDBCEmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    protected void setEmployeeService(EmployeeServiceIMPL employeeService) { this.employeeService = employeeService; }

    @Test
    public void createWithValidParametersShouldPersist() {

        try {

        Employee employee = new Employee("Stefan","Puhalo","Address1", "1090",
                "Vienna","AT","066038381959",
                "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","34856124124144141",
                "561828000014958","BKAUATW",185000L, "Manager",
                "/stefan.jpg",false, "", "",1);


        Mockito.when(employeeDAO.create(employee)).thenReturn(employee);

        Employee createdEmployee = employeeService.create(employee);

        Assert.assertTrue(createdEmployee.getName().equals("Stefan"));

        }catch (ServiceException | DAOException e) {

        }

    }

    @Test(expected = ServiceException.class)
    public void createWithNullShouldThrowException() throws ServiceException {
        employeeService.create(null);
    }

    @Test
    public void updateAllEmployeesWithValidParametersShouldPersist() {

        try {

            Employee employee1 = new Employee("Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","34856124124144141",
                    "561828000014958","BKAUATW",185000L, "Manager",
                    "/stefan.jpg",false, "", "",1);

            Employee employee2 = new Employee("Stefan","Puhalovic","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","34856124124144141",
                    "561828000014958","BKAUATW",185000L, "Manager",
                    "/stefan.jpg",false, "", "",1);

            ObservableList<Employee> employees = FXCollections.observableArrayList();
            employees.addAll(employee1,employee2);

            employeeDAOStub = new EmployeeDaoStub();
            employeeService.setEmployeeDAO(employeeDAOStub);
            employeeService.updateAllEmployees(employees);


            Assert.assertTrue(employee2.getName().equals("John"));

        }catch (ServiceException e) {

        }

    }

    @Test(expected = ServiceException.class)
    public void updateAllWithNullShouldThrowException() throws ServiceException {
        employeeService.updateAllEmployees(null);
    }


    @Test
    public void updateWithValidParametersShouldPersist() {
        try {

            Employee employee = new Employee("Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","34856124124144141",
                    "561828000014958","BKAUATW",185000L, "Manager",
                    "/stefan.jpg",false, "", "",1);


            employeeDAOStub = new EmployeeDaoStub();
            employeeService.setEmployeeDAO(employeeDAOStub);
            employeeService.updateEmployee(employee);

            Assert.assertTrue(employee.getName() == "John");


        }catch (ServiceException e) {

        }

    }

    @Test(expected = ServiceException.class)
    public void updateWithNullShouldThrowException() throws ServiceException {
        employeeService.updateEmployee(null);
    }

    @Test
    public void deleteWithValidParametersShouldPersist() {
        try {

            Employee employee = new Employee("Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","34856124124144141",
                    "561828000014958","BKAUATW",185000L, "Manager",
                    "/stefan.jpg",false, "", "",1);


            employeeDAOStub = new EmployeeDaoStub();
            employeeService.setEmployeeDAO(employeeDAOStub);
            employeeService.delete(employee);

            Assert.assertTrue(employee.getDeleted());


        }catch (ServiceException e) {

        }

    }

    @Test(expected = ServiceException.class)
    public void deleteWithNullShouldThrowException() throws ServiceException {
        employeeService.delete(null);
    }

    @Test
    public void getAllEmployeesWithValidParameters() {

        try{

            Employee employee = new Employee("Stefan","Puhalo","Address1", "1090",
                "Vienna","AT","066038381959",
                "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","34856124124144141",
                "561828000014958","BKAUATW",185000L, "Manager",
                "/stefan.jpg",false, "", "",1);


            ObservableList<Employee> employees = FXCollections.observableArrayList();
            ObservableList<Employee> retrievedEmployees;

            employees.add(employee);
            Mockito.when(employeeDAO.findAll()).thenReturn(employees);
            employeeService.setEmployeeDAO(employeeDAO);
            retrievedEmployees = employeeService.getAllEmployees();

            Assert.assertTrue(retrievedEmployees.contains(employee));

        }catch (ServiceException | DAOException | NoEmployeesFoundException e) {

        }
    }

    @Test
    public void getEmployeeWithValidID() {

        try{

             Employee employee1 = new Employee("Stefan","Puhalo","Address1", "1090",
             "Vienna","AT","066038381959",
             "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","34856124124144141",
             "561828000014958","BKAUATW",185000L, "Manager",
             "/stefan.jpg",false, "", "",1);

             employee1.setPid(102);
             Mockito.when(employeeDAO.find(102)).thenReturn(employee1);
             employeeService.setEmployeeDAO(employeeDAO);
             Employee retrievedEmployee1;

             retrievedEmployee1 = employeeService.getEmployee(102);

             Assert.assertTrue(retrievedEmployee1.getName().equals("Stefan"));

        }catch (ServiceException | DAOException | EmployeeNotFoundException e) {

        }

    }

    @Test(expected = ServiceException.class)
    public void getEmployeeWithZeroShouldThrowException() throws ServiceException {
        employeeService.getEmployee(0);
    }

    @Test
    public void getAllEmployeesFromToWithValidParameters() {
        try{

            Employee employee = new Employee("Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","34856124124144141",
                    "561828000014958","BKAUATW",185000L, "Manager",
                    "/stefan.jpg",false, "", "",1);


            ObservableList<Employee> employees = FXCollections.observableArrayList();
            ObservableList<Employee> retrievedEmployees;

            employees.add(employee);
            Mockito.when(employeeDAO.findAllFromTo("BDATE","1980-01-01", "1984-01-01")).thenReturn(employees);
            employeeService.setEmployeeDAO(employeeDAO);
            retrievedEmployees = employeeService.getAllEmployeesFromTo("BDATE","1980-01-01", "1984-01-01");

            Assert.assertTrue(retrievedEmployees.contains(employee));

        }catch (ServiceException | DAOException | ColumnNotFoundException e) {

        }
    }

    @Test(expected = ServiceException.class)
    public void getAllEmployeesFromToWithNullShouldThrowException() throws ServiceException {
        employeeService.getAllEmployeesFromTo(null, null, null);
    }

    @Test
    public void getAllEmployeesForPositionFromToWithValidParameters() {
        try{

            Employee employee = new Employee("Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","34856124124144141",
                    "561828000014958","BKAUATW",185000L, "Manager",
                    "/stefan.jpg",false, "", "",1);


            ObservableList<Employee> employees = FXCollections.observableArrayList();
            ObservableList<Employee> retrievedEmployees;

            employees.add(employee);
            Mockito.when(employeeDAO.findAllFromToForPosition("BDATE", "Manager","1980-01-01", "1984-01-01")).thenReturn(employees);
            employeeService.setEmployeeDAO(employeeDAO);
            retrievedEmployees = employeeService.getAllEmployeesForPositionFromTo("BDATE", "Manager", "1980-01-01", "1984-01-01");

            Assert.assertTrue(retrievedEmployees.contains(employee));

        }catch (ServiceException | DAOException | ColumnNotFoundException e) {

        }
    }

    @Test(expected = ServiceException.class)
    public void getAllEmployeesForPositionFromToWithNullShouldThrowException() throws ServiceException {
        employeeService.getAllEmployeesForPositionFromTo("BDATE","Manager", null, null);
    }

    @Test
    public void getAllEmployeesForPositionWithValidParameters() {
        try{

            Employee employee = new Employee("Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","34856124124144141",
                    "561828000014958","BKAUATW",185000L, "Manager",
                    "/stefan.jpg",false, "", "",1);


            ObservableList<Employee> employees = FXCollections.observableArrayList();
            ObservableList<Employee> retrievedEmployees;

            employees.add(employee);
            Mockito.when(employeeDAO.findAllForPosition("Manager")).thenReturn(employees);
            employeeService.setEmployeeDAO(employeeDAO);
            retrievedEmployees = employeeService.getAllEmployeesForPosition("Manager");

            Assert.assertTrue(retrievedEmployees.contains(employee));

        }catch (ServiceException | DAOException | NoEmployeesFoundException e) {

        }
    }

    @Test(expected = ServiceException.class)
    public void getAllEmployeesForPositionWithNullShouldThrowException() throws ServiceException {
        employeeService.getAllEmployeesForPosition(null);
    }


}
