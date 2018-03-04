package main.java.ac.at.tuwien.sepm.QSE15.test.emailPasswordDAOTest;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.email.EmailPasswordsDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ervincosic on 29/05/2017.
 */
public class AbstractEmailPasswordDAOTest {

    protected EmailPasswordsDAO emailPasswordsDAO;

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractEmailPasswordDAOTest.class);

    @Test
    public void testSaveValidParameters() throws DAOException {
        Employee employee = new Employee();

        employee.setUsername("ErvinCosic");

        emailPasswordsDAO.savePassword(employee, "password");
    }

    @Test(expected = DAOException.class)
    public void testSaveEmployeeNullShouldThrowDAOException() throws DAOException{
        Employee employee = null;

        emailPasswordsDAO.savePassword(employee, "password");
    }

    @Test(expected = DAOException.class)
    public void testSaveEmployeeNullPassword()throws DAOException{

        Employee employee = new Employee();

        employee.setUsername("ErvinCosic");

        emailPasswordsDAO.savePassword(employee, null);

    }
    @Test
    public void testGetPasswordValidUser()throws DAOException{
        Employee employee = new Employee();

        employee.setUsername("ErvinCosic");

        emailPasswordsDAO.savePassword(employee, "password");

        String password = emailPasswordsDAO.getPassword(employee);

        Assert.assertTrue(password.equals("password"));
    }

    @Test(expected = DAOException.class)
    public void testGetPasswordNullUserShouldThrowDAOException()throws DAOException{
        emailPasswordsDAO.getPassword(null);
    }

    @Test
    public void testUpdatePasswordValidParameters()throws DAOException{
        Employee employee = new Employee();

        employee.setUsername("ErvinCosic");

        emailPasswordsDAO.savePassword(employee, "password");

        emailPasswordsDAO.updatePassword(employee, "otherPassword");

        String password = emailPasswordsDAO.getPassword(employee);

        Assert.assertFalse(password.equals("password"));

    }

    @Test(expected = DAOException.class)
    public void testUpdatePasswordNullUser()throws DAOException{
        emailPasswordsDAO.updatePassword(null, "password");
    }

    @Test(expected = DAOException.class)
    public void testUpdatePasswordNullPassword()throws DAOException{
        Employee employee = new Employee();

        employee.setUsername("ErvinCosic");

        emailPasswordsDAO.updatePassword(employee, null);
    }
}
