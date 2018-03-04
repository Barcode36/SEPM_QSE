package main.java.ac.at.tuwien.sepm.QSE15.service.authenticationService;

import main.java.ac.at.tuwien.sepm.QSE15.dao.employeeDAO.EmployeeDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.employeeDAO.JDBCEmployeeDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.NoEmployeesFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.UserNotValidException;

/**
 * Created by ervincosic on 06/05/2017.
 */
public interface AuthenticationService {
    /**
     *
     * This method is used for the user validation on login
     *
     * @param employee - Employee with the set Username and Password on login,
     *                 the Password is encrypted with the encryptPassword(String password) method
     *
     * @return Employee object - in case of successful login
     *
     * @throws UserNotValidException - This exception is thrown:
     *                                  (1) in case the given username or password in the employee are null pointers
     *                                  (2) in case the given username and password don't exist hence user not valid
     *
     */
    Employee validateUsernameAndPassword(Employee employee) throws UserNotValidException;

    /**
     * This method is used for encrypting the password for saving in the database and comparing
     * with passwords in database;
     *
     * @param password - password that should be encrypted
     *
     * @return - Encrypted password
     *
     * @throws NullPointerException  - if the given string is a null pointer
     */
     String encryptPassword(String password) throws NullPointerException;


     void setEmployeeDAO (JDBCEmployeeDAO employeeDAO);
}
