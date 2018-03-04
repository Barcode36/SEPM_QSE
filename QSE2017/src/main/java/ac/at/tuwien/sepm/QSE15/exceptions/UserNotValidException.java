package main.java.ac.at.tuwien.sepm.QSE15.exceptions;

/**
 * Created by ervincosic on 06/05/2017.
 */

/**
 * This exception should be thrown in case the user does not exist
 */
public class UserNotValidException extends  Exception {

    public UserNotValidException() {
        super();
    }

    public UserNotValidException(String message) {
        super(message);
    }
}
