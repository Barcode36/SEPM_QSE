package main.java.ac.at.tuwien.sepm.QSE15.test.stubs;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.email.EmailPasswordsDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * Created by ervincosic on 30/05/2017.
 */
public class EmailPasswordDAOStub extends EmailPasswordsDAO {

    private String encryptedPassword;

    @Override
    public String getPassword(Employee employee) throws DAOException {
        if(employee.getUsername().equals("cosa")) {
            BasicTextEncryptor encryptor = new BasicTextEncryptor();
            encryptor.setPassword("password");

            return encryptor.encrypt("Your e-mail password.");
        }else{
            return encryptedPassword;
        }

    }

    @Override
    public void savePassword(Employee employee, String password) throws DAOException {
        encryptedPassword = password;
    }
}
