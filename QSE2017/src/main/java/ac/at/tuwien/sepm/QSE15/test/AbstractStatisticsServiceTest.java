package main.java.ac.at.tuwien.sepm.QSE15.test;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.statisticsDAO.JDBCStatisticsDAO;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.statistics.StatisticServiceIMPL;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Date;
import java.util.HashMap;

/**
 * Created by Stefan Puhalo on 6/21/2017.
 */
public class AbstractStatisticsServiceTest {

    protected StatisticServiceIMPL statisticService;

    protected JDBCStatisticsDAO statisticsDAO;

    protected void setStatisticsService(StatisticServiceIMPL statisticService) { this.statisticService = statisticService; }

    protected void setStatisticsDAO(JDBCStatisticsDAO statisticsDAO) { this.statisticsDAO = statisticsDAO; }


    @Test
    public void statisticsForAllRoomsWithValidParameters() {

        HashMap<String, Integer> map = new HashMap<>();
        map.put("Single Room", 20);

        try {

            Mockito.when(statisticsDAO.statisticsForAllRoomsFromDate(Date.valueOf("2015-01-01"), Date.valueOf("2016-01-01"))).thenReturn(map);
            statisticService.setStatisticsDAO(statisticsDAO);
            HashMap<String, Integer> retrievedMap = statisticService.getStatisticsForAllRoomsFromDate(Date.valueOf("2015-01-01"), Date.valueOf("2016-01-01"));



            Assert.assertTrue(retrievedMap.get("Single Room") == 20);


        } catch (DAOException | ServiceException e) {
        }

    }

    @Test(expected = ServiceException.class)
    public void statisticsForAllRoomsWithInvalidDates() throws ServiceException {
        statisticService.getStatisticsForAllRoomsFromDate(Date.valueOf("2017-01-01"), Date.valueOf("2016-01-01"));
    }

    @Test(expected = ServiceException.class)
    public void statisticsForAllRoomsWithNull() throws ServiceException {
        statisticService.getStatisticsForAllRoomsFromDate(null, Date.valueOf("2016-01-01"));
    }


    @Test
    public void statisticsForOneRoomTypeWithValidParameters() {

        HashMap<Date, Integer> map = new HashMap<>();
        map.put(Date.valueOf("2016-01-01"), 20);

        try {

            Mockito.when(statisticsDAO.statisticsForOneRoomFromDate("Single Room", Date.valueOf("2015-01-01"), Date.valueOf("2016-01-01"))).thenReturn(map);
            statisticService.setStatisticsDAO(statisticsDAO);
            HashMap<Date, Integer> retrievedMap = statisticService.getStatisticsForOneRoomFromDate("Single Room", Date.valueOf("2015-01-01"), Date.valueOf("2016-01-01"));
            Assert.assertTrue(retrievedMap.get(Date.valueOf("2016-01-01")) == 20);

        } catch (DAOException |ServiceException e) {
        }

    }

    @Test(expected = ServiceException.class)
    public void statisticsForOneRoomTypeWithInvalidDates() throws ServiceException {
        statisticService.getStatisticsForOneRoomFromDate("Single Room", Date.valueOf("2018-01-01"), Date.valueOf("2016-01-01"));
    }


    @Test(expected = ServiceException.class)
    public void statisticsForOneRoomTypeWithNull() throws ServiceException {
        statisticService.getStatisticsForOneRoomFromDate(null, Date.valueOf("2018-01-01"), Date.valueOf("2016-01-01"));
    }


    @Test
    public void statisticsForAllServiceTypesWithValidParameters() {

        HashMap<String, Integer> map = new HashMap<>();
        map.put("Transport", 20);

        try {

            Mockito.when(statisticsDAO.statisticsForAllServicesFromDate(Date.valueOf("2015-01-01"), Date.valueOf("2016-01-01"))).thenReturn(map);
            statisticService.setStatisticsDAO(statisticsDAO);
            HashMap<String, Integer> retrievedMap = statisticService.getStatisticsForAllServicesFromDate(Date.valueOf("2015-01-01"), Date.valueOf("2016-01-01"));


            Assert.assertTrue(retrievedMap.get("Transport") == 20);


        } catch (DAOException |ServiceException e) {
        }
    }

    @Test(expected = ServiceException.class)
    public void statisticsForAllServiceTypesWithInvalidDates() throws ServiceException {
        statisticService.getStatisticsForAllServicesFromDate(Date.valueOf("2017-01-01"), Date.valueOf("2016-01-01"));
    }

    @Test(expected = ServiceException.class)
    public void statisticsForAllServiceTypesWithNull() throws ServiceException {
        statisticService.getStatisticsForAllServicesFromDate(null, Date.valueOf("2016-01-01"));
    }


    @Test
    public void statisticsForOneServiceTypeWithValidParameters() {

        HashMap<String, Integer> map = new HashMap<>();
        map.put("June", 20);

        try {

            Mockito.when(statisticsDAO.statisticsForOneServiceFromDate("Transport", Date.valueOf("2015-01-01"), Date.valueOf("2016-01-01"))).thenReturn(map);
            statisticService.setStatisticsDAO(statisticsDAO);
            HashMap<String, Integer> retrievedMap = statisticService.getStatisticsForOneServiceFromDate("Transport", Date.valueOf("2015-01-01"), Date.valueOf("2016-01-01"));


            Assert.assertTrue(retrievedMap.get("June") == 20);


        } catch (DAOException |ServiceException e) {
        }

    }

    @Test(expected = ServiceException.class)
    public void statisticsForOneServiceTypeWithInvalidDates() throws ServiceException {
        statisticService.getStatisticsForOneServiceFromDate("Transport", Date.valueOf("2017-01-01"), Date.valueOf("2016-01-01"));
    }

    @Test(expected = ServiceException.class)
    public void statisticsForOneServiceTypeWithNull() throws ServiceException {
        statisticService.getStatisticsForOneServiceFromDate(null, Date.valueOf("2017-01-01"), Date.valueOf("2016-01-01"));
    }


}
