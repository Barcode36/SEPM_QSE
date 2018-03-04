package main.java.ac.at.tuwien.sepm.QSE15.exceptions;

/**
 * Created by Stefan Puhalo on 5/10/2017.
 */
public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException() {
        super();
    }
}
