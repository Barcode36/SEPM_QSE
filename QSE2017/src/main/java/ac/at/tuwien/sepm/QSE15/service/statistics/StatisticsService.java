package main.java.ac.at.tuwien.sepm.QSE15.service.statistics;

import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;

import java.sql.Date;
import java.util.HashMap;

/**
 * Created by Stefan Puhalo on 17.06.17.
 */
public interface StatisticsService {


    /**
     * Calculates statistics for all rooms in date range from-to
     * @param from first Date of range
     * @param to last Date of range
     * @return Map:
     *              key: room category
     *              values: number of reservations of certain category in date range from-to
     * @throws ServiceException when DAOException is caught
     */
    HashMap<String, Integer> getStatisticsForAllRoomsFromDate(Date from, Date to) throws ServiceException;

    /**
     * Calculates statistics for given room category
     * @param category of room
     * @param from first Date of range
     * @param to last Date of range
     * @return Map:
     *              key: date of start of reservation
     *              value: number of reservations
     * @throws ServiceException when DAOException is caught
     */
    HashMap<Date, Integer> getStatisticsForOneRoomFromDate(String category, Date from, Date to) throws ServiceException;

    /**
     * Calculates statistics for all types of services in date range from-to
     * @param from first Date of range
     * @param to last Date of range
     * @return Map:
     *              key: service types
     *              value: number of reservations of certain type in date range from-to
     * @throws ServiceException when DAOException is caught
     */
    HashMap<String,Integer> getStatisticsForAllServicesFromDate(Date from, Date to) throws ServiceException;

    /**
     * Calculates statistics for given type of services in date range from-to per month
     * @param serviceType type of chosen service
     * @param from first Date of range
     * @param to last Date of range
     * @return Map:
     *              key: month of reservation date
     *              value: number of reservations for given service types in date range from-to
     * @throws ServiceException when DAOException is caught
     */
    HashMap<String, Integer> getStatisticsForOneServiceFromDate(String serviceType, Date from, Date to) throws ServiceException;

    /**
     * Searches for max value of given map
     * @param map
     * @return max value
     * @throws ServiceException if map is null
     */
    Integer getMaxValueFromMap(HashMap<String, Integer> map) throws ServiceException;


    /**
     * Searches for min value of given map
     * @param map
     * @return min value
     * @throws ServiceException if map is null
     */
    Integer getMinValueFromMap(HashMap<String, Integer> map) throws ServiceException;

}
