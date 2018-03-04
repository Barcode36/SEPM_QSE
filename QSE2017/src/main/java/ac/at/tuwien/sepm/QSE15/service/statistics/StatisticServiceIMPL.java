package main.java.ac.at.tuwien.sepm.QSE15.service.statistics;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.statisticsDAO.JDBCStatisticsDAO;
import main.java.ac.at.tuwien.sepm.QSE15.gui.controllers.CustomerManagerController;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.HashMap;


/**
 * Created by Stefan Puhalo on 02.05.17.
 */

@Service
public class StatisticServiceIMPL implements StatisticsService {

    @Autowired
    private JDBCStatisticsDAO statisticsDAO;

    private static final Logger logger = LoggerFactory.getLogger(CustomerManagerController.class);

    @Override
    public HashMap<String, Integer> getStatisticsForAllRoomsFromDate(Date from, Date to) throws ServiceException {

        if(from == null || to == null) {
            logger.error("Statistics calculation failed as some of parameters are null.");
            throw new ServiceException("Statistics calculation failed as some of parameters are null.");
        }

        if(from.after(to)) {
            logger.error("From date set after to date");
            throw new ServiceException("From date set after to date");
        }

        try {
            return statisticsDAO.statisticsForAllRoomsFromDate(from, to);
        } catch (DAOException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    @Override
    public HashMap<Date, Integer> getStatisticsForOneRoomFromDate(String category, Date from, Date to) throws ServiceException {

        if(category == null || from == null || to == null) {
            logger.error("Statistics calculation failed as some of parameters are null.");
            throw new ServiceException("Statistics calculation failed as some of parameters are null.");
        }


        if(from.after(to)) {
            logger.error("From date set after to date");
            throw new ServiceException("From date set after to date");
        }

        try {
            return statisticsDAO.statisticsForOneRoomFromDate(category, from, to);
        } catch (DAOException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    @Override
    public HashMap<String, Integer> getStatisticsForAllServicesFromDate(Date from, Date to) throws ServiceException {

        if(from == null || to == null) {
            logger.error("Statistics calculation failed as some of parameters are null.");
            throw new ServiceException("Statistics calculation failed as some of parameters are null.");
        }


        if(from.after(to)) {
            logger.error("From date set after to date");
            throw new ServiceException("From date set after to date");
        }

        try {
            return statisticsDAO.statisticsForAllServicesFromDate(from, to);
        } catch (DAOException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    @Override
    public HashMap<String, Integer> getStatisticsForOneServiceFromDate(String serviceType, Date from, Date to) throws ServiceException {

        if(serviceType == null || from == null || to == null) {
            logger.error("Statistics calculation failed as some of parameters are null.");
            throw new ServiceException("Statistics calculation failed as some of parameters are null.");
        }


        if(from.after(to)) {
            logger.error("From date set after to date");
            throw new ServiceException("From date set after to date");
        }

        try {
            return statisticsDAO.statisticsForOneServiceFromDate(serviceType, from, to);
        } catch (DAOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Integer getMaxValueFromMap(HashMap<String, Integer> map) throws ServiceException {

        if(map == null) {
            logger.error("Get max Value failed as the given map is null.");
            throw new ServiceException("Get max Value failed as the given map is null.");
        }


        int max = 0;

        for(String s : map.keySet()) {
            if(map.get(s) > max) {
                max = map.get(s);
            }
        }

        return max;
    }


    @Override
    public Integer getMinValueFromMap(HashMap<String, Integer> map) throws ServiceException {

        if(map == null) {
            logger.error("Get min Value failed as the given map is null.");
            throw new ServiceException("Get min Value failed as the given map is null.");
        }

        int min = Integer.MAX_VALUE;

        for(String s : map.keySet()) {
            if(map.get(s) < min) {
                min = map.get(s);
            }
        }

        return min;
    }

    public void setStatisticsDAO(JDBCStatisticsDAO statisticsDAO) { this.statisticsDAO = statisticsDAO; }
}
