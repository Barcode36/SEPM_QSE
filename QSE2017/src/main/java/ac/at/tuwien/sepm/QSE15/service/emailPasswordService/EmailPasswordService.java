package main.java.ac.at.tuwien.sepm.QSE15.service.emailPasswordService;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.email.EmailPasswordsDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by ervincosic on 29/05/2017.
 */
@Service
public class EmailPasswordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailPasswordService.class);

    @Autowired
    EmailPasswordsDAO emailPasswordsDAO;

    public void saveEmailPasswordForUser(Employee employee, Employee admin, String adminPassword)throws ServiceException{

        if(employee == null){
            throw  new ServiceException("The given employee is a null pointer.");
        }else if(employee.getPassword() == null || employee.getPassword().length() == 0) {
            throw new ServiceException("The employee has no password.");
        }else if(employee.getUsername() == null || employee.getUsername().length() == 0){
            throw new ServiceException("The employe has no username.");
        }

        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(employee.getPassword()); // uses the employees hashed password

        String decryptedEmailPassword = getDecryptedEmailPassword(admin, adminPassword);

        String encryptedEmailPassword = encryptor.encrypt(decryptedEmailPassword);

        try {

            emailPasswordsDAO.savePassword(employee, encryptedEmailPassword);

        } catch (DAOException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException();
        }

    }

    /**
     * This method is used to retrieve the password for the email
     * @param employee - Employee that you need the password for
     *                 NOTE in this object the employee password needs to be plaintext
     * @return - Password as plain text
     * @throws ServiceException - in case an error while reading from the database occurred
     */
    public String getEmailPassword(Employee employee)throws ServiceException{

        if(employee == null){
            throw new ServiceException("The given employee is a null pointer.");
        }

        if(employee.getPassword() == null || employee.getPassword().length() == 0) {
            throw new ServiceException("The employee has no password.");
        }else if(employee.getUsername() == null || employee.getUsername().length() == 0){
            throw new ServiceException("The employee has no username.");
        }

        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(employee.getPassword());

        String encryptedEmailPassword;

        try {
            encryptedEmailPassword = emailPasswordsDAO.getPassword(employee);

        } catch (DAOException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException();
        }
        return encryptor.decrypt(encryptedEmailPassword);
    }

    /**
     * This method returns the decrypted email password
     * @param admin - admin user
     * @param adminPassword - admin password
     * @return - decrypted password
     * @throws ServiceException - if an error occurred while retrieving the password
     */
    private String getDecryptedEmailPassword(Employee admin, String adminPassword)throws ServiceException{

        BasicTextEncryptor encryptor = new BasicTextEncryptor();

        encryptor.setPassword(adminPassword);

        String encryptedEmailPassword;

        try {

            encryptedEmailPassword = emailPasswordsDAO.getPassword(admin);

        } catch (DAOException e) {
            LOGGER.error(e.getMessage());
            throw  new ServiceException();
        }

        return encryptor.decrypt(encryptedEmailPassword);
    }

    public void saveEmailPasswordForAdmin(Employee admin, String password)throws ServiceException{

        if(admin == null || admin.getUsername() == null || admin.getUsername().length() == 0){
            throw new ServiceException();
        }else if(password == null || password.length() == 0){
            throw new ServiceException();
        }

        BasicTextEncryptor encryptor = new BasicTextEncryptor();

        encryptor.setPassword(password);

        try {
            emailPasswordsDAO.savePassword(admin, encryptor.encrypt(password));
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Setter only to be used for testing
     * @param emailPasswordsDAOStub - a stub of the EmailPasswordDAO
     */
    public void setEmailPasswordsDAO(EmailPasswordsDAO emailPasswordsDAOStub) {
        this.emailPasswordsDAO = emailPasswordsDAOStub;
    }
}
