package main.java.ac.at.tuwien.sepm.QSE15.test;

import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.employeeDAO.EmployeeDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.ColumnNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.EmployeeNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.NoEmployeesFoundException;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.sql.SQLException;

/**
 * Created by Stefan Puhalo on 5/4/2017.
 */
public abstract class AbstractEmployeeDAOTest {

    protected EmployeeDAO employeeDAO;

    protected void setEmployeeDAO(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Test(expected = DAOException.class)
    public void createWithNullShouldThrowException() throws DAOException{

        employeeDAO.create(null);

    }

    @Test
    public void createWithValidParametersShouldPersist() throws NoEmployeesFoundException {

        try {

            Employee employee = new Employee("Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","34856124124144141",
                    "561828000014958","BKAUATW",185000L, "Manager",
                    "/stefan.jpg",false, "", "",1);

            ObservableList<Employee> employees;

            employee = employeeDAO.create(employee);

            employees = employeeDAO.findAll();

            Assert.assertTrue(employees.contains(employee));

        }catch (DAOException e) {

        }

    }

    @Test(expected = DAOException.class)
    public void findWithInvalidParameterShouldThrowException() throws DAOException{
        try {
            employeeDAO.find(-1);

        }catch (EmployeeNotFoundException e) {

        }
    }

    @Test
    public void findWithValidParameters() throws EmployeeNotFoundException{

        try {
            Employee employee = employeeDAO.create(new Employee("Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","3414146354653264856",
                    "561828000014958","BKAUATW",185000L, "Manager",
                    "/stefan.jpg",false,"","", 1));


            Employee foundEmployee = employeeDAO.find(employee.getPid());

            Assert.assertEquals(employee.getPid(), foundEmployee.getPid());

        }catch (DAOException e) {

        }

    }

    @Test(expected = DAOException.class)
    public void updateWithInvalidParametersShouldThrowException() throws DAOException {

        employeeDAO.update(null);

    }

    @Test
    public void updateWithValidParameters() throws EmployeeNotFoundException{

        try {
            Employee employee = employeeDAO.create(new Employee("Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","348858375911556",
                    "561828000014958","BKAUATW",185000L, "Manager",
                    "/stefan.jpg",false, "","",1));

            employee.setSurname("Puhalovic");
            employeeDAO.update(employee);
            Employee updatedEmployee = employeeDAO.find(employee.getPid());

            Assert.assertTrue(updatedEmployee.getSurname().equals("Puhalovic"));

        }catch (DAOException e) {

        }

    }


    @Test(expected = DAOException.class)
    public void deleteWithInvalidParametersShouldThrowException()throws DAOException {
        employeeDAO.delete(null);
    }

    @Test
    public void deleteWithValidParameters()throws EmployeeNotFoundException {

        try {
            Employee employee = employeeDAO.create(new Employee("Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","348858375911556",
                    "561828000014958","BKAUATW",185000L, "Manager",
                    "/stefan.jpg",false, "","",1));
            Assert.assertFalse(employee.getDeleted());
            employeeDAO.delete(employee);
            Employee foundEmployee = null;

            foundEmployee = employeeDAO.find(employee.getPid());

            Assert.assertEquals(foundEmployee,null);

        }catch (DAOException e) {

        }

    }

    @Test
    public void findAllWithSalaryBetweenFromAndTo() throws DAOException, ColumnNotFoundException {

        Employee employee = employeeDAO.create(new Employee("Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","348858375911556",
                    "561828000014958","BKAUATW",185000L, "Manager",
                    "/stefan.jpg",false, "","",1));

        ObservableList<Employee> employees = employeeDAO.findAllFromTo("SALARY", 180000, 186000);
        Assert.assertTrue(employees.contains(employee));

    }

    @Test(expected = ColumnNotFoundException.class)
    public void findAllFromToWithInvalidColumnShouldThrowException() throws DAOException, ColumnNotFoundException {
        employeeDAO.findAllFromTo("Stefan", 180000, 186000);
    }

    @Test(expected = DAOException.class)
    public void findAllFromToWithNullShouldThrowException() throws DAOException, ColumnNotFoundException {
        employeeDAO.findAllFromTo(null, null, null);
    }

    @Test
    public void findAllWithDateBetweenFromAndTo() {

        try {
            Employee employee = employeeDAO.create(new Employee("Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","348858375911556",
                    "561828000014958","BKAUATW",185000L, "Manager",
                    "/stefan.jpg",false, "","",1));
            ObservableList<Employee> employees = employeeDAO.findAllFromTo("BDATE",  Date.valueOf("1982-01-01"),
                    Date.valueOf("1985-01-01"));
            Assert.assertTrue(employees.contains(employee));
        }catch (ColumnNotFoundException e) {

        }catch (DAOException e) {

        }

    }

    @Test
    public void findAllWithSalaryForPositionBetweenFromAndTo() throws DAOException, ColumnNotFoundException {

        Employee employee = employeeDAO.create(new Employee("Stefan","Puhalo","Address1", "1090",
                "Vienna","AT","066038381959",
                "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","348858375911556",
                "561828000014958","BKAUATW",185000L, "Manager",
                "/stefan.jpg",false, "","",1));

        ObservableList<Employee> employees = employeeDAO.findAllFromToForPosition("SALARY", "Manager",180000, 186000);
        Assert.assertTrue(employees.contains(employee));

    }

    @Test(expected = ColumnNotFoundException.class)
    public void findAllFromToForPositionWithInvalidColumnShouldThrowException() throws DAOException, ColumnNotFoundException {
        employeeDAO.findAllFromToForPosition("Stefan", "Manager",180000, 186000);
    }

    @Test(expected = DAOException.class)
    public void findAllFromToForPositionWithNullShouldThrowException() throws DAOException, ColumnNotFoundException {
        employeeDAO.findAllFromToForPosition(null, null, null, null);
    }

    @Test
    public void findAllForPosition() throws DAOException, NoEmployeesFoundException {

        Employee employee = employeeDAO.create(new Employee("Stefan","Puhalo","Address1", "1090",
                "Vienna","AT","066038381959",
                "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","348858375911556",
                "561828000014958","BKAUATW",185000L, "Manager",
                "/stefan.jpg",false, "","",1));

        ObservableList<Employee> employees = employeeDAO.findAllForPosition("Manager");
        Assert.assertTrue(employees.contains(employee));

    }

    @Test(expected = DAOException.class)
    public void findAllForPositionWithNullShouldThrowException() throws DAOException, NoEmployeesFoundException {
        employeeDAO.findAllForPosition(null);
    }

    @Test
    public void findWithUsernameAndPasswordWithValidParameters() throws DAOException, EmployeeNotFoundException{


        Employee employee = employeeDAO.create(new Employee("Stefan","Puhalo","Address1", "1090",
                "Vienna","AT","066038381959",
                "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","3414146354653264856",
                "561828000014958","BKAUATW",185000L, "Manager",
                "/stefan.jpg",false,"stefan","stefan", 1));


        Employee foundEmployee = employeeDAO.findWithUsernameAndPassword(employee);

        Assert.assertTrue(foundEmployee != null);

    }

    @Test(expected = DAOException.class)
    public void findWithUserNameAndPasswordWithNullShouldThrowException() throws DAOException, EmployeeNotFoundException {
        employeeDAO.findWithUsernameAndPassword(null);
    }

    @Test(expected = EmployeeNotFoundException.class)
    public void findWithUsernameAndPasswordWithNotInsertedEmployeeShouldThrowException() throws DAOException, EmployeeNotFoundException{


        Employee employee = new Employee("Stefan","Puhalo","Address1", "1090",
                "Vienna","AT","066038381959",
                "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","3414146354653264856",
                "561828000014958","BKAUATW",185000L, "Manager",
                "/stefan.jpg",false,"stefan","stefan", 1);


        Employee foundEmployee = employeeDAO.findWithUsernameAndPassword(employee);

        Assert.assertTrue(foundEmployee != null);

    }





}
