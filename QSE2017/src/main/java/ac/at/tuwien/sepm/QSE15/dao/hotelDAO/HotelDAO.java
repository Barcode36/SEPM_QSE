package main.java.ac.at.tuwien.sepm.QSE15.dao.hotelDAO;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.entity.hotel.Hotel;

/**
 * Created by Bajram Saiti on 01.05.17.
 * Developed by Ervin Cosic
 */
public interface HotelDAO {
    /**
     * Use this method to save hotel to the database
     * NOTE: only the first hotel is read with the get method
     * @param hotel - hotel with set parameters to save
     * @return hotel object with set HNR
     * @throws DAOException (1) if the given hotel is null pointer
     *                      (2) the hotel cannot be saved
     */
    Hotel create(Hotel hotel) throws DAOException;

    /**
     * Use this method to update a hotel
     * @param hotel - hotel to update NOTE: the HNR musst be set
     * @throws DAOException - (1) if the given hotel is null pointer
     *                        (2) the hotel cannot be updated
     */
    void update(Hotel hotel) throws DAOException;

    void delete(Hotel hotel) throws  DAOException;

    /**
     * Returns the first hotel in the database
     * @return Hotel object
     * @throws DAOException - if an SQL error occures
     */
    Hotel get()throws DAOException;
}
