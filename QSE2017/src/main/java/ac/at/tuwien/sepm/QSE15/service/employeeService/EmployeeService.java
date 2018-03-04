package main.java.ac.at.tuwien.sepm.QSE15.service.employeeService;

import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;

/**
 * Created by Stefan Puhalo on 02.05.17.
 */
public interface EmployeeService {

    /**
     * This method persists an employee to the database
     * @param employee - employee with all set data to persist
     * @return employee with his PID
     * @throws ServiceException when DAOException is caught
     */
    Employee create(Employee employee) throws ServiceException;

    /**
     * Update all employees from given list
     * @param employees list of employees, which have to be updated
     * @throws ServiceException when DAOException is caught
     */
    void updateAllEmployees(ObservableList<Employee> employees) throws ServiceException;

    /**
     * This method takes a given employee and updates it in the database
     * @param employee - employee that you want to update
     * @throws ServiceException when DAOException is caught
     */
    void updateEmployee(Employee employee) throws ServiceException;

    /**
     * This method is used for deleting employees
     * @param employee - employee with set PID that you want to delete
     * @throws ServiceException when DAOException is caught
     */
    void delete(Employee employee) throws ServiceException;

    /**
     * This method returns all employees in a ObservableList
     * @return ObservableList of employees
     * @throws ServiceException when DAOException is caught
     */
    ObservableList<Employee> getAllEmployees() throws ServiceException;

    /**
     * This method finds a employee with given Id
     * @param pid - pid of the employee you want to find
     * @return - found employee
     * @throws ServiceException when DAOException is caught
     */
    Employee getEmployee(Integer pid) throws ServiceException;

    /**
     * Generic search method to filter the table
     * @param column name of column in table Employee
     * @param from for Name, Surname, Date of Birth, Salary
     * @return filtered list of employees
     * @throws ServiceException when DAOException is caught
     */
    ObservableList<Employee> getAllEmployeesFromTo(String column, Object from, Object to) throws ServiceException;

    /**
     * Generic search method to filter the table inclusive their type {customer, guest}
     * @param column name of column in Employee table
     * @param position of Employee in hotel
     * @param from for Name, Surname, Date of Birth, Salary
     * @param to for Name, Surname, Date of Birth, Salary
     * @return filtered list of employees
     * @throws ServiceException when DAOException is caught
     */
    ObservableList<Employee> getAllEmployeesForPositionFromTo(String column, String position, Object from, Object to) throws ServiceException;

    /**
     * Filters table with given position
     * @param position of Employees in hotel
     * @return filtered list of employees with given position
     * @throws ServiceException when SQLException is caught
     */
    ObservableList<Employee> getAllEmployeesForPosition(String position) throws ServiceException;

    /**
     * Find all positions in hotel of employees
     * @return list of all positions
     * @throws ServiceException
     */
    ObservableList<String> getAllPositions() throws ServiceException;


}
