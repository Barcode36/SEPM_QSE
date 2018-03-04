package main.java.ac.at.tuwien.sepm.QSE15.dao.employeeDAO;

import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.ColumnNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.EmployeeNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.NoEmployeesFoundException;

import java.util.List;
import java.util.Objects;

/**
 * Created by Stefan Puhalo on 04.05.17.
 * Edited by Ervin Cosic
 */
public interface EmployeeDAO {

    /**
     * This method persists an employee to the database
     * @param employee - employee with all set data to persist
     * @return employee with his PID
     * @throws DAOException - (1) in case a SQL Exception occurred
     *                        (2) in case the given employee is a null pointer
     */
    Employee create(Employee employee) throws DAOException;

    /**
     * This method returns all employees in a ObservableList
     * @return ObservableList of employees
     * @throws DAOException - If an error occurred
     * @throws NoEmployeesFoundException - If there are no employees in the database
     */
    ObservableList<Employee> findAll() throws DAOException, NoEmployeesFoundException;

    /**
     * This method finds a employee with given Id
     * @param id - pid of the employee you want to find
     * @return - found employee
     * @throws DAOException - (1) if the given employee is a null pointer
     *                        (2) if an SQL exception occures
     * @throws EmployeeNotFoundException - if there is no employee with the given PID
     */
    Employee find(Integer id) throws DAOException, EmployeeNotFoundException;

    /**
     * This method searches in the database for an employee with set username and password
     * @param employee - employee object with set username and password
     * @return - Set employee with al his data and rights
     * @throws DAOException - (1) if the given employee is a null pointer
     *                        (2) if an SQL Exception occurred
     * @throws EmployeeNotFoundException - If there is no employee with given username and password
     */
    Employee findWithUsernameAndPassword(Employee employee) throws DAOException, EmployeeNotFoundException;

    /**
     * This method takes a given employee and updates it in the database
     * @param employee - employee that you want to update
     * @throws DAOException - (1) if employee is null,
     *                        (2) if the PID of the employee is a negative integer
     *                        (3) if no row in the database is changes hence no such PID
     *                        (4) if an SQL Error occurred
     */
    void update(Employee employee) throws DAOException;

    /**
     * This method is used for deleting employees
     * @param employee - employee with set PID that you want to delete
     * @throws DAOException - (1) if the given employee is a null pointer
     *                        (2) if the given employee has a negative PID
     *                        (3) if there is no employee with the given PID in the database
     */
    void delete(Employee employee) throws DAOException;

    /**
     * Generic search method to filter the table
     * @param column name of column in table Employee
     * @param from for Name, Surname, Date of Birth, Salary
     * @return filtered list of employees
     * @throws DAOException when SQLException is caught
     * @throws ColumnNotFoundException if column not found
     */
    ObservableList<Employee> findAllFromTo(String column, Object from, Object to) throws DAOException, ColumnNotFoundException;

    /**
     * Generic search method to filter the table inclusive their type {customer, guest}
     * @param parameter name of column in Employee table
     * @param role of Employee in hotel
     * @param from for Name, Surname, Date of Birth, Salary
     * @param to for Name, Surname, Date of Birth, Salary
     * @return filtered list of employees
     * @throws DAOException when SQLException is caught
     * @throws ColumnNotFoundException when column name is not found
     */
    ObservableList<Employee> findAllFromToForPosition(String parameter, String role, Object from, Object to) throws DAOException, ColumnNotFoundException;

    /**
     * Filters table with given position
     * @param position of Employees in hotel
     * @return filtered list of employees with given position
     * @throws DAOException when SQLException is caught
     * @throws NoEmployeesFoundException if no employee with given position is found
     */
    ObservableList<Employee> findAllForPosition(String position) throws DAOException, NoEmployeesFoundException;

    /**
     * Find all positions in hotel of employees
     * @return list of all positions
     * @throws DAOException
     */
    ObservableList<String> findAllPositions() throws DAOException;
}
