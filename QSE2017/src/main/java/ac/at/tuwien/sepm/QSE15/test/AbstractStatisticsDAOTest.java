package main.java.ac.at.tuwien.sepm.QSE15.test;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.roomReservationDAO.RoomReservationDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.serviceReservationDAO.ServiceReservationDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.statisticsDAO.StatisticsDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.RoomReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.ServiceReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.util.HashMap;

/**
 * Created by Stefan Puhalo on 6/19/2017.
 */
public class AbstractStatisticsDAOTest {

    protected StatisticsDAO statisticsDAO;

    protected RoomReservationDAO roomReservationDAO;

    protected ServiceReservationDAO serviceReservationDAO;


    protected void setStatisticsDAO(StatisticsDAO statisticsDAO) {
        this.statisticsDAO = statisticsDAO;
    }

    protected void setRoomReservationDAO(RoomReservationDAO roomReservationDAO) { this.roomReservationDAO = roomReservationDAO; }

    protected void setServiceReservationDAO(ServiceReservationDAO serviceReservationDAO) { this.serviceReservationDAO = serviceReservationDAO; }


    @Test
    public void testStatisticsCalculationsForAllRoomCategories() {
        HashMap<String, Integer>  allRoomsStats = null;

        RoomReservation roomReservation1 = new RoomReservation(null, 3000, Date.valueOf("2123-10-10"), Date.valueOf("2123-10-20"),
                null, false, false, 101, 5000L, true);
        RoomReservation roomReservation2 = new RoomReservation(null, 3000, Date.valueOf("2123-10-01"), Date.valueOf("2123-10-10"),
                null, false, false, 202, 5000L, true);
        RoomReservation roomReservation3 = new RoomReservation(null, 3000, Date.valueOf("2123-10-20"), Date.valueOf("2123-10-30"),
                null, false, false, 102, 5000L, true);
        RoomReservation roomReservation4 = new RoomReservation(null, 3000, Date.valueOf("2123-10-05"), Date.valueOf("2123-10-15"),
                null, false, false, 102, 5000L, true);

        try {
            roomReservationDAO.create(roomReservation1);
            roomReservationDAO.create(roomReservation2);
            roomReservationDAO.create(roomReservation3);
            roomReservationDAO.create(roomReservation4);

            allRoomsStats = statisticsDAO.statisticsForAllRoomsFromDate(Date.valueOf("2123-10-01"), Date.valueOf("2123-11-01"));

        } catch (DAOException e) {
        }

        Assert.assertTrue(allRoomsStats.get("Suite") == 22);

    }

    @Test(expected = DAOException.class)
    public void testStatisticsCalculationsForAllRoomCategoriesWithInvalidDatesShouldThrowException() throws DAOException {
        statisticsDAO.statisticsForAllRoomsFromDate(Date.valueOf("2123-10-01"), Date.valueOf("2122-11-01"));
    }

    @Test(expected = DAOException.class)
    public void testStatisticsCalculationsForAllRoomCategoriesWithNullShouldThrowException() throws DAOException {
        statisticsDAO.statisticsForAllRoomsFromDate(null, null);
    }


    @Test
    public void testStatisticsCalculationForOneRoomCategory() {
        HashMap<Date, Integer>  oneRoomsStats = null;

        RoomReservation roomReservation1 = new RoomReservation(null, 3000, Date.valueOf("2133-10-10"), Date.valueOf("2133-10-20"),
                null, false, false, 101, 5000L, true);
        RoomReservation roomReservation2 = new RoomReservation(null, 3000, Date.valueOf("2133-10-01"), Date.valueOf("2133-10-10"),
                null, false, false, 202, 5000L, true);

        try {
            roomReservationDAO.create(roomReservation1);
            roomReservationDAO.create(roomReservation2);


            oneRoomsStats = statisticsDAO.statisticsForOneRoomFromDate("Single Room", Date.valueOf("2133-10-01"), Date.valueOf("2133-11-01"));

        } catch (DAOException e) {
        }

        Assert.assertTrue(oneRoomsStats.get(Date.valueOf("2133-10-10")) == 11);
    }

    @Test(expected = DAOException.class)
    public void testStatisticsCalculationsForOneRoomCategoryWithInvalidDatesShouldThrowException() throws DAOException {
        statisticsDAO.statisticsForOneRoomFromDate("Single Room", Date.valueOf("2153-10-01"), Date.valueOf("2133-11-01"));
    }

    @Test(expected = DAOException.class)
    public void testStatisticsCalculationsForOneRoomCategoryWithNullShouldThrowException() throws DAOException {
        statisticsDAO.statisticsForOneRoomFromDate(null, Date.valueOf("2153-10-01"), null);
    }

    @Test
    public void testStatisticsCalculationsForAllServiceTypes() {

        ServiceReservation serviceReservation1 = new ServiceReservation();
        Service service1 = new Service();
        service1.setSrid(1);
        serviceReservation1.setSrid(service1);
        serviceReservation1.setRid(2005);
        serviceReservation1.setDate(Date.valueOf("2123-10-02"));

        ServiceReservation serviceReservation2 = new ServiceReservation();
        Service service2 = new Service();
        service2.setSrid(2);
        serviceReservation2.setSrid(service2);
        serviceReservation2.setRid(2007);
        serviceReservation2.setDate(Date.valueOf("2123-10-10"));

        ServiceReservation serviceReservation3 = new ServiceReservation();
        Service service3 = new Service();
        service3.setSrid(4);
        serviceReservation3.setSrid(service3);
        serviceReservation3.setRid(2003);
        serviceReservation3.setDate(Date.valueOf("2123-10-16"));

        try {
            serviceReservationDAO.create(serviceReservation1);
            serviceReservationDAO.create(serviceReservation2);
            serviceReservationDAO.create(serviceReservation3);

            HashMap<String, Integer> allServices = statisticsDAO.statisticsForAllServicesFromDate(Date.valueOf("2123-10-01"), Date.valueOf("2123-10-30"));

            Assert.assertTrue(allServices.get("Wellness") == 1 && allServices.get("Transport") == 2);

        } catch (DAOException e) {
        }

    }

    @Test(expected = DAOException.class)
    public void testStatisticsCalculationsForAllServiceTypesWithInvalidDatesShouldThrowException() throws DAOException {
        statisticsDAO.statisticsForAllServicesFromDate(Date.valueOf("2123-10-01"), Date.valueOf("2122-11-01"));
    }

    @Test(expected = DAOException.class)
    public void testStatisticsCalculationsForAllServiceTypesWithNullShouldThrowException() throws DAOException {
        statisticsDAO.statisticsForAllRoomsFromDate(null, null);
    }

    @Test
    public void testStatisticsCalculationsForOneServiceType() {
        ServiceReservation serviceReservation1 = new ServiceReservation();
        Service service1 = new Service();
        service1.setSrid(1);
        serviceReservation1.setSrid(service1);
        serviceReservation1.setRid(2005);
        serviceReservation1.setDate(Date.valueOf("2123-05-02"));

        ServiceReservation serviceReservation2 = new ServiceReservation();
        Service service2 = new Service();
        service2.setSrid(2);
        serviceReservation2.setSrid(service2);
        serviceReservation2.setRid(2007);
        serviceReservation2.setDate(Date.valueOf("2123-06-10"));

        ServiceReservation serviceReservation3 = new ServiceReservation();
        Service service3 = new Service();
        service3.setSrid(2);
        serviceReservation3.setSrid(service3);
        serviceReservation3.setRid(2003);
        serviceReservation3.setDate(Date.valueOf("2123-06-16"));

        try {
            serviceReservationDAO.create(serviceReservation1);
            serviceReservationDAO.create(serviceReservation2);
            serviceReservationDAO.create(serviceReservation3);

            HashMap<String, Integer> forOneService = statisticsDAO.statisticsForOneServiceFromDate("Transport", Date.valueOf("2123-01-01"), Date.valueOf("2123-10-30"));

            Assert.assertTrue(forOneService.get("May") == 1 && forOneService.get("June") == 2);

        } catch (DAOException e) {
        }

    }

    @Test(expected = DAOException.class)
    public void testStatisticsCalculationsForOneServiceTypeWithInvalidDatesShouldThrowException() throws DAOException {
        statisticsDAO.statisticsForOneServiceFromDate("Transport", Date.valueOf("2123-10-01"), Date.valueOf("2122-11-01"));
    }

    @Test(expected = DAOException.class)
    public void testStatisticsCalculationsForOneServiceTypeWithNullShouldThrowException() throws DAOException {
        statisticsDAO.statisticsForOneServiceFromDate(null,null, null);
    }

}
