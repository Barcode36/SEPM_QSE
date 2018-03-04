package main.java.ac.at.tuwien.sepm.QSE15.exceptions;

/**
 * Created by Stefan Puhalo on 5/9/2017.
 */
public class ColumnNotFoundException extends Exception {
    public ColumnNotFoundException(String message) {
        super(message);
    }

    public ColumnNotFoundException() {
        super();
    }
}