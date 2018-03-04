package main.java.ac.at.tuwien.sepm.QSE15.test.authenticationTest;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.employeeDAO.EmployeeDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.employeeDAO.JDBCEmployeeDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.EmployeeNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.NoEmployeesFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.UserNotValidException;
import main.java.ac.at.tuwien.sepm.QSE15.service.authenticationService.AuthenticationService;
import main.java.ac.at.tuwien.sepm.QSE15.service.authenticationService.MyAuthenticationService;
import main.java.ac.at.tuwien.sepm.QSE15.test.stubs.EmployeeDaoStub;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.cglib.core.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.when;

/**
 * Created by ervincosic on 08/05/2017.
 */
public class AbstractAuthenticationServiceTest {


    JDBCEmployeeDAO employeeDAO;

    protected static AuthenticationService authenticationService;

    protected static final Logger LOGGER = LoggerFactory.getLogger(MyAuthenticationService.class);

    @Test
    public void testIfEncryptionIsPseudoRandom(){

        String firstEncryption = authenticationService.encryptPassword("admin");
        String secondEncryption = authenticationService.encryptPassword("admin");

        LOGGER.info(firstEncryption);
        LOGGER.info(secondEncryption);

        Assert.assertTrue(firstEncryption.equals(secondEncryption));

    }
    @Test
    public void testIfEncryptionIsDifferentForDifferentStrings(){

        String firstEncryption = authenticationService.encryptPassword("ErvinCcsic*123"); // minimal difference
        String secondEncryption = authenticationService.encryptPassword("ErvinCosic*123");

        Assert.assertTrue(!firstEncryption.equals(secondEncryption));
    }

    /**
     * Here is the object employeeDao mocked and added to the authentication service in the line
     * <Code> when(employeeDAO.findWithUsernameAndPassword(employee)).thenReturn(employee);</Code>
     * the <Code>findWithUsernameAndPasssword(Employee employee)</Code> method is mocked, hence
     * its determined in the runtime what the method should do with a specific input.
     *
     * @throws UserNotValidException
     *
     * @throws NoEmployeesFoundException
     */
    @Test
    public void testValidUserLogin() throws UserNotValidException, NoEmployeesFoundException {
        Employee employee = new Employee();

        employee.setUsername("Ervin");
        employee.setPassword("Ervin");

        try {
            /**
             * This is mocking
             */
            when(employeeDAO.findWithUsernameAndPassword(employee)).thenReturn(employee);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage());
        } catch (EmployeeNotFoundException e) {
            LOGGER.error(e.getMessage());
        }

        authenticationService.setEmployeeDAO(employeeDAO);

        employee = authenticationService.validateUsernameAndPassword(employee);

        Assert.assertTrue(employee != null);

    }

    /**
     * In this testcase the employeeDAO object ist stubbed in the line
     * <Code>  employeeDAO = new EmployeeDaoStub();</Code> in instantiated
     * the <Code>EmployeeDaoStub</Code> class which has predetermined responses to given calls.
     * @throws Exception
     */
    @Test(expected = UserNotValidException.class)
    public void testInvalidUserLogin()throws Exception{
        Employee employee = new Employee();

        employee.setUsername("ervin");
        employee.setPassword("ervin123");

        /**
         * This is stubbing
         */
        employeeDAO = new EmployeeDaoStub();

        authenticationService.setEmployeeDAO(employeeDAO);

        Employee validEmployee = authenticationService.validateUsernameAndPassword(employee);

    }
    @Test
    public void testUserWithNullUsernameOrPassword(){
        int exceptionCount = 0;

        Employee testEmployee = new Employee();

        testEmployee.setUsername(null);
        testEmployee.setPassword(null);

        try {
            authenticationService.validateUsernameAndPassword(testEmployee);
        } catch (UserNotValidException e) {
            exceptionCount ++;
            LOGGER.info("Exception thrown when both username and password are null pointers.");
        }

        testEmployee.setPassword("SomePaswword");

        try {
            authenticationService.validateUsernameAndPassword(testEmployee);
        } catch (UserNotValidException e) {
            exceptionCount++;
            LOGGER.info("Exception is thrown if username is a null pointer.");
        }

        testEmployee.setPassword(null);
        testEmployee.setUsername("someusername");

        try {
            authenticationService.validateUsernameAndPassword(testEmployee);
        } catch (UserNotValidException e) {
            exceptionCount++;
            LOGGER.info("Exception is thrown if the password is a null pointer.");
        }

        Assert.assertTrue( exceptionCount == 3);

    }



}
