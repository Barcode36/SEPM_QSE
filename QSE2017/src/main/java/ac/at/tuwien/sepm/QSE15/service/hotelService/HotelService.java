package main.java.ac.at.tuwien.sepm.QSE15.service.hotelService;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.hotelDAO.JDBCHotelDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.hotel.Hotel;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.HotelNotValidException;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;

/**
 * Created by Bajram Saiti on 02.05.17.
 */
public interface HotelService {

    Hotel create(Hotel hotel) throws ServiceException, HotelNotValidException;

    void update(Hotel hotel) throws ServiceException, HotelNotValidException;

    void delete(Hotel hotel) throws  ServiceException;

    /**
     * Use this method only for testing
     * @param hotelDAO - stubbed or mocked hotelDAO
     */
    void setHotelDAO(JDBCHotelDAO hotelDAO);

    /**
     * This method checks if there is already a hotel set in the database
     * @return true if there is a hotel
     *         false if it there is no hotel saved
     * @throws ServiceException if there is a problem with the database
     */
    boolean isHotelSet() throws ServiceException;

    Hotel get()throws ServiceException;
}
