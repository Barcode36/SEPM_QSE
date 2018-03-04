package main.java.ac.at.tuwien.sepm.QSE15.exceptions;

/**
 * Created by ervincosic on 07/05/2017.
 */
public class EmployeeNotFoundException extends Exception {
    public EmployeeNotFoundException(String message) {
        super(message);
    }

    public EmployeeNotFoundException() {
        super();
    }
}
