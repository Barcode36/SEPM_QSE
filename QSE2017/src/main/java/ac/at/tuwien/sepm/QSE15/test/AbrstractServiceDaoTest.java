package main.java.ac.at.tuwien.sepm.QSE15.test;
import jdk.nashorn.internal.objects.NativeUint8Array;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.serviceDAO.ServiceDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.RoomReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.ComponentScan;
import static org.junit.Assert.*;
import java.awt.*;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.List;
/**
 * Created by Nemanja Vukoje on 06/05/2017.
 */
abstract class AbrstractServiceDaoTest {
    protected ServiceDAO serviceDAO;
    public void setServiceDAO(ServiceDAO serviceDAO) {
        this.serviceDAO = serviceDAO;
    }
    // Nemanja tested
    @Test
    public void createWithInvalidParametersShouldThrowException() {
        try {
            serviceDAO.create(null);
        }catch (DAOException e) {
            Assert.assertTrue(true);
        }
    }
    /*@Test
    public void getAllServicesFromReservation() throws Exception {
        Reservation reservation = new RoomReservation();
        reservation.setRid(1);
        Service service = new Service(1,"","",300L);
        List<Service> list = serviceDAO.getAllServicesFromReservation(reservation);
        serviceDAO.create(service);
        List<Service> list1 = serviceDAO.getAllServicesFromReservation(reservation);
        assertEquals(list.size()+1, list1.size());
    }*/
    // Ivana tested
    @Test
    public void getWithValidParametersShouldPersist() throws DAOException{
        Service actual = new Service(null, "Transport","Ivana",1L);
        try {
            serviceDAO.create(actual);
            Service expected = serviceDAO.get(new Service(11,null,null,null));
            Assert.assertEquals(expected.getDescription(),actual.getDescription());
            Assert.assertEquals(expected.getPrice(),actual.getPrice());
            Assert.assertEquals(expected.getType(),actual.getType());
        } catch (DAOException e) {
            throw new DAOException(e.getMessage());
        }
    }
}