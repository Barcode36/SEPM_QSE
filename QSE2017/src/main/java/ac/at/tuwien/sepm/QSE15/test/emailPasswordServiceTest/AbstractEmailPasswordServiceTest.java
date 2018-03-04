package main.java.ac.at.tuwien.sepm.QSE15.test.emailPasswordServiceTest;

import main.java.ac.at.tuwien.sepm.QSE15.dao.email.EmailPasswordsDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.emailPasswordService.EmailPasswordService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ervincosic on 30/05/2017.
 */
public abstract class AbstractEmailPasswordServiceTest {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractEmailPasswordServiceTest.class);

    protected EmailPasswordService emailPasswordService;

    private Employee generateTestEmployee(){
        Employee employee = new Employee();

        employee.setUsername("cosa");
        employee.setPassword("password");

        return employee;
    }

    @Test
    public void testGetEmailPasswordForUserWithValidParameters()throws ServiceException{
        Employee employee = generateTestEmployee();

        String emailPassword = emailPasswordService.getEmailPassword(employee);

        Assert.assertTrue(emailPassword.equals("Your e-mail password."));

    }

    @Test(expected = ServiceException.class)
    public void testGetEmailPasswordForNullUserShouldthrowServiceException()throws ServiceException{
        emailPasswordService.getEmailPassword(null);
    }

    @Test(expected = ServiceException.class)
    public void testGetEmailPasswordForUserWithNoUsername()throws ServiceException{
        Employee employee = generateTestEmployee();
        employee.setUsername("");
        emailPasswordService.getEmailPassword(employee);
    }

    @Test(expected = ServiceException.class)
    public void testGetEmailPasswordWithNullUsername() throws ServiceException{
        Employee employee = generateTestEmployee();
        employee.setUsername(null);
        emailPasswordService.getEmailPassword(employee);
    }

    @Test(expected = ServiceException.class)
    public void testGetEmailPasswordWithNoUserPassword() throws ServiceException{
        Employee employee = generateTestEmployee();
        employee.setPassword(null);
        emailPasswordService.getEmailPassword(employee);
    }

    @Test(expected = ServiceException.class)
    public void testGetEmailPasswordWithNoPassword() throws ServiceException{
        Employee employee = generateTestEmployee();
        employee.setPassword("");
        emailPasswordService.getEmailPassword(employee);
    }

    @Test
    public void testSaveEmailPasswordForUserWithValidParameters() throws ServiceException{
        Employee employee = generateTestEmployee();

        employee.setUsername("ervin");

        emailPasswordService.saveEmailPasswordForUser(employee, generateTestEmployee(), "password");

        String decryptedPassword = emailPasswordService.getEmailPassword(employee);

        Assert.assertTrue(decryptedPassword.equals("Your e-mail password."));

    }

    @Test(expected = ServiceException.class)
    public void testSaveEmailPasswordForUserWithInvalidParameters()throws ServiceException{

        emailPasswordService.saveEmailPasswordForUser(null, null, null);

    }

}
