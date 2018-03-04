package main.java.ac.at.tuwien.sepm.QSE15.exceptions;

/**
 * Created by ervincosic on 14/05/2017.
 */
public class HotelNotValidException extends Exception {
    public HotelNotValidException() {
    }

    public HotelNotValidException(String message) {
        super(message);
    }
}
