package main.java.ac.at.tuwien.sepm.QSE15.exceptions;

/**
 * Created by ervincosic on 07/05/2017.
 */
public class NoEmployeesFoundException extends Exception {
    public NoEmployeesFoundException() {
        super();
    }

    public NoEmployeesFoundException(String message) {
        super(message);
    }
}
