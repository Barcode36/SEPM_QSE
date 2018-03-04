package main.java.ac.at.tuwien.sepm.QSE15.exceptions;

/**
 * Created by Stefan Puhalo on 5/10/2017.
 */
public class NoCustomerFoundException extends Exception {

    public NoCustomerFoundException(String message) {
        super(message);
    }

    public NoCustomerFoundException() {
        super();
    }
}
