package main.java.ac.at.tuwien.sepm.QSE15.service.employeeService;

import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.employeeDAO.JDBCEmployeeDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.ColumnNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.EmployeeNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.NoEmployeesFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Stefan Puhalo on 02.05.17.
 */

@Service
public class EmployeeServiceIMPL implements EmployeeService {


    @Autowired
    private JDBCEmployeeDAO employeeDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceIMPL.class);

    @Override
    public Employee create(Employee employee) throws ServiceException {

        if(employee == null) {
            LOGGER.error("Employee is null.");
            throw new ServiceException("Employee is null.");
        }

        try {
            return employeeDAO.create(employee);
        } catch (DAOException e) {
            LOGGER.error("Create employee failed.");
            throw new ServiceException("Create employee failed.");
        }

    }

    @Override
    public void updateAllEmployees(ObservableList<Employee> employees) throws ServiceException {

        if(employees == null) {
            LOGGER.error("Employees list is null.");
            throw new ServiceException("Employees list is null.");
        }

        for(Employee employee : employees) {
            try {
                employeeDAO.update(employee);
            } catch (DAOException e) {
                LOGGER.error("Update all employees failed.");
                throw new ServiceException("Update all employees failed.");
            }
        }
    }

    @Override
    public void updateEmployee(Employee employee) throws ServiceException {

        if(employee == null) {
            LOGGER.error("Employee is null.");
            throw new ServiceException("Employee is null.");
        }

        try {
            employeeDAO.update(employee);
        } catch (DAOException e) {
            LOGGER.error("Update employee failed.");
            throw new ServiceException("Update employee failed.");        }
    }

    @Override
    public void delete(Employee employee) throws ServiceException {

        if(employee == null) {
            LOGGER.error("Employee is null.");
            throw new ServiceException("Employee is null.");
        }

        try {
            employeeDAO.delete(employee);
        } catch (DAOException e) {
            LOGGER.error("Delete employee failed.");
            throw new ServiceException("Delete employee failed.");
        }

    }

    @Override
    public ObservableList<Employee> getAllEmployees() throws ServiceException {
        try {
            return employeeDAO.findAll();
        } catch (DAOException e) {
            LOGGER.error("Get all employees failed.");
            throw new ServiceException("Get all employees failed.");
        } catch (NoEmployeesFoundException e) {
            LOGGER.error("No Employee found.");
            throw new ServiceException("No Employee found.");
        }

    }

    @Override
    public Employee getEmployee(Integer pid) throws ServiceException {

        if(pid == 0) {
            LOGGER.error("Pid is 0.");
            throw new ServiceException("Pid is 0.");
        }

        try {
            return employeeDAO.find(pid);
        } catch (DAOException e) {
            LOGGER.error("Find employee with id = " + pid + " failed.");
            throw new ServiceException("Find employee with id = " + pid + " failed.");
        } catch (EmployeeNotFoundException e) {
            LOGGER.error("Employee with id = " + pid + " not found.");
            throw new ServiceException("Employee with id = " + pid + " not found.");
        }

    }

    @Override
    public ObservableList<Employee> getAllEmployeesFromTo(String column, Object from, Object to) throws ServiceException {

        if(column == null || from == null || to == null) {
            LOGGER.error("Column is null or dates are null.");
            throw new ServiceException("Employee is null.");
        }


        try {
            return employeeDAO.findAllFromTo(column, from, to);
        } catch (DAOException e) {
            LOGGER.error("Get all employees from-to failed.");
            throw new ServiceException("Get all employees from-to failed.");
        } catch (ColumnNotFoundException e) {
            LOGGER.error("Column + " + column + " not found.");
            throw new ServiceException("Column + " + column + " not found.");
        }
    }

    @Override
    public ObservableList<Employee> getAllEmployeesForPositionFromTo(String column, String position, Object from, Object to) throws ServiceException {

        if(column == null || from == null || to == null || position == null) {
            LOGGER.error("Column is null or dates are null.");
            throw new ServiceException("Column is null or dates are null.");
        }

        try {
            return employeeDAO.findAllFromToForPosition(column, position, from, to);
        } catch (DAOException e) {
            LOGGER.error("Get all employees for position from-to failed.");
            throw new ServiceException("Get all employees for position from-to failed.");
        } catch (ColumnNotFoundException e) {
            LOGGER.error("Column + " + column + " not found.");
            throw new ServiceException("Column + " + column + " not found.");
        }
    }

    @Override
    public ObservableList<Employee> getAllEmployeesForPosition(String position) throws ServiceException {

        if(position == null) {
            LOGGER.error("Position is null.");
                throw new ServiceException("Position is null.");
        }

        try {
            return employeeDAO.findAllForPosition(position);
        } catch (DAOException e) {
            LOGGER.error("Get all employees for position failed.");
            throw new ServiceException("Get all employees for position failed.");
        } catch (NoEmployeesFoundException e) {
            LOGGER.error("No employees found.");
            throw new ServiceException("No employees found.");
        }

    }

    @Override
    public ObservableList<String> getAllPositions() throws ServiceException {
        try {
            return employeeDAO.findAllPositions();
        } catch (DAOException e) {
            LOGGER.error("Get all positions of employees failed.");
            throw new ServiceException("Get all positions of employees failed.");
        }
    }

    public void setEmployeeDAO (JDBCEmployeeDAO employeeDAO){
        this.employeeDAO =  employeeDAO;
    }

}
