package main.java.ac.at.tuwien.sepm.QSE15.dao.statisticsDAO;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;

import java.sql.Date;
import java.util.HashMap;

/**
 * Created by Stefan Puhalo on 6/15/2017.
 */
public interface StatisticsDAO {

    /**
     * Calculates statistics for all types of services in date range from-to
     * @param from first Date of range
     * @param to last Date of range
     * @return Map:
     *              key: service types
     *              value: number of reservations of certain type in date range from-to
     * @throws DAOException - (1) if from or to are null
     *                        (2) if from is after to
     */
    HashMap<String,Integer> statisticsForAllServicesFromDate(Date from, Date to) throws DAOException;

    /**
     * Calculates statistics for given type of services in date range from-to per month
     * @param serviceType type of chosen service
     * @param from first Date of range
     * @param to last Date of range
     * @return Map:
     *              key: month of reservation date
     *              value: number of reservations for given service types in date range from-to
     * @throws DAOException - (1) if from, to or serviceType are null
     *                        (2) if from is after to
     */
    HashMap<String, Integer> statisticsForOneServiceFromDate(String serviceType, Date from, Date to) throws DAOException;

    /**
     * Calculates statistics for all rooms in date range from-to
     * @param from first Date of range
     * @param to last Date of range
     * @return Map:
     *              key: room category
     *              values: number of reservations of certain category in date range from-to
     * @throws DAOException - (1) if from or to are null
     *                        (2) if from is after to
     */
    HashMap<String, Integer> statisticsForAllRoomsFromDate(Date from, Date to) throws DAOException;

    /**
     * Calculates statistics for given room category
     * @param category of room
     * @param from first Date of range
     * @param to last Date of range
     * @return Map:
     *              key: date of start of reservation
     *              value: number of reservations
     * @throws DAOException - (1) if from, to or category are null
     *                        (2) if from is after to
     */
    HashMap<Date, Integer> statisticsForOneRoomFromDate(String category, Date from, Date to) throws DAOException;



}
