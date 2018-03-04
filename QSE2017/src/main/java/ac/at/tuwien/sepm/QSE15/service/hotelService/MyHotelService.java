package main.java.ac.at.tuwien.sepm.QSE15.service.hotelService;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.dao.hotelDAO.JDBCHotelDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.hotel.Hotel;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.HotelNotValidException;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.authenticationService.MyAuthenticationService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * Created by Bajram Saiti on 02.05.17.
 * Implemented by ervincosic
 */
@Service
public class MyHotelService implements HotelService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MyHotelService.class);

    @Autowired
    JDBCHotelDAO hotelDAO;


    @Override
    public Hotel create(Hotel hotel) throws ServiceException, HotelNotValidException{

        Hotel createdHotel;

        if(hotel == null){
            return null;
        }
        checkHotelValidity(hotel);

        try {
            createdHotel = hotelDAO.create(hotel);
        } catch (DAOException e) {
            LOGGER.error("Erorr on create hotel");
            LOGGER.error(e.getMessage());
            throw  new ServiceException("Unable to create hotel.");
        }

        return createdHotel;
    }

    @Override
    public void update(Hotel hotel) throws ServiceException, HotelNotValidException {

        checkHotelValidity(hotel);

        if(hotel.getHnr() < 0){
            throw new HotelNotValidException("The HNR is not valid.");
        }

        try {
            hotelDAO.update(hotel);
        } catch (DAOException e) {
            throw new ServiceException("Cannot update hotel.");
        }
    }

    @Override
    public void delete(Hotel hotel) throws ServiceException {
        //todo implement this
    }

    public boolean isHotelSet() throws ServiceException{
        Hotel hotel = null;

        try {
            hotel = hotelDAO.get();
        } catch (DAOException e) {
            throw  new ServiceException(e.getMessage());
        }

        return hotel != null;
    }

    @Override
    public Hotel get() throws ServiceException {

        try {
            return hotelDAO.get();
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Use this method only for testing
     * @param hotelDAO - stubbed or mocked hotelDAO
     */
    public void setHotelDAO(JDBCHotelDAO hotelDAO) {
        this.hotelDAO = hotelDAO;
    }

    /**
     * Check if a string is null or empty
     * @param string - to be checked
     * @return true if its a valid string
     */
    private boolean isStringSet(String string){
        if(string == null){
            return false;
        }else if (string.equals("")){
            return false;
        }
        return true;
    }

    /**
     * This method checks every single string in the hotel object and checks if it is set
     * @param hotel
     * @throws HotelNotValidException is thrown in case ona of the hotel variables is not set
     */
    private void checkHotelValidity(Hotel hotel) throws HotelNotValidException{
        if(!isStringSet(hotel.getName())){

            throw new HotelNotValidException("The hotel has no name.");

        }else if(!isStringSet(hotel.getEmail())){

            throw new HotelNotValidException("The hotel has no email.");

        }else if(!isStringSet(hotel.getAddress())){

            throw new HotelNotValidException("The hotel has no address.");

        }else if(!isStringSet(hotel.getBic())){

            throw new HotelNotValidException("The Bic is not set.");

        }else if(!isStringSet(hotel.getIban())){

            throw new HotelNotValidException("The iban is not set.");

        }else if(!isStringSet(hotel.getEmail())){

            throw new HotelNotValidException("The e-mail is not set.");

        }else if(!isStringSet(hotel.getPassword())){
            throw new HotelNotValidException("The password is not set.");
        }
    }
}
