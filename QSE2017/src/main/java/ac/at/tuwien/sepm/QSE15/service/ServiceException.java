package main.java.ac.at.tuwien.sepm.QSE15.service;

/**
 * Created by Bajram Saiti on 25.04.17.
 */
public class ServiceException extends Exception{

    public ServiceException(){
        super();
    }

    public ServiceException(String message){
        super(message);
    }
}
