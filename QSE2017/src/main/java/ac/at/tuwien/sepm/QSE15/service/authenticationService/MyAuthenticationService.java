package main.java.ac.at.tuwien.sepm.QSE15.service.authenticationService;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.employeeDAO.EmployeeDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.employeeDAO.JDBCEmployeeDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.EmployeeNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.UserNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;

/**
 * Created by ervincosic on 06/05/2017.
 */
@Service
public class MyAuthenticationService implements AuthenticationService{

    private static final Logger LOGGER = LoggerFactory.getLogger(MyAuthenticationService.class);

    @Autowired
    private JDBCEmployeeDAO employeeDAO;

    public MyAuthenticationService() {
    }

    @Override
    public Employee validateUsernameAndPassword(Employee employee) throws UserNotValidException {
        if(employee.getUsername() == null || employee.getPassword() == null ){

            LOGGER.error("Username or password is a null pointer.");

            throw new UserNotValidException("Username or password is a null pointer");
        }

        Employee loggedInEmployee;

        try {
            loggedInEmployee = employeeDAO.findWithUsernameAndPassword(employee);
        } catch (DAOException e) {

            LOGGER.error(e.getMessage());
            throw  new UserNotValidException(e.getMessage());

        } catch (EmployeeNotFoundException e) {
            throw new UserNotValidException("No user with given username and password.");
        }

        return loggedInEmployee;
    }

    @Override
    public String encryptPassword(String password){

        String encryptedPassword;

        encryptedPassword = sha256((password + generateSaltForString(password)));

        return encryptedPassword;
    }

    /**
     * Method for getting a sha-2 signature
     * @param base - String that you want to get the signature
     * @return sha-2 signature of the given string
     */
    private String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /**
     * This method generates a salt for a given string
     * the salt is the same length as the input string
     *
     * @param string - input to be salted
     *
     * @return string salt
     */
    private String generateSaltForString(String string){

        String salt = "";

        String numberFromString = Long.toString(generateNumberFromString(string));

        //generate a even number of characters
        if(numberFromString.length() % 2 == 1){
            numberFromString = numberFromString.substring(0, numberFromString.length() - 1);
        }

        for(int i = 0; i < (numberFromString.length() - 1); i ++){

            char[] characters = {'a','a'};

            characters[0] = (char) numberFromString.charAt(i);
            characters[1] = (char) numberFromString.charAt(i+1);

            String ab = new String(characters);

            int number = Integer.parseInt(ab);

            salt += toASCII(number);

        }

        return salt;
    }

    /**
     * This method generates a number of a given string
     * by adding and multiplying the character ascii values
     * one by one
     *
     * @param string - input string that you need the number for
     *
     * @return number for given string
     */
    private long generateNumberFromString(String string)throws NullPointerException{

        if(string == null){
            throw new NullPointerException("The given string is a null pointer.");
        }

        long seed = 1;

        for (int i = 0; i < string.length(); i++){
            if(i % 2 == 0) {
                seed *= (int) string.charAt(i);
            }else{
                seed += (int) string.charAt(i);
            }

        }

        return  seed;
    }

    /**
     * This methos is used to convert a int value to ascii string
     * @param value
     * @return
     */
    private static String toASCII(int value) {
        int length = 4;
        StringBuilder builder = new StringBuilder(length);
        for (int i = length - 1; i >= 0; i--) {
            builder.append((char) ((value >> (8 * i)) & 0xFF));
        }
        return builder.toString();
    }

    public void setEmployeeDAO (JDBCEmployeeDAO employeeDAO){
        this.employeeDAO =  employeeDAO;
    }

}
