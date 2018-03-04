package main.java.ac.at.tuwien.sepm.QSE15.test.ServiceReservation_TEST;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.serviceReservationDAO.ServiceReservationDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.RoomReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.ServiceReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;
import org.junit.Assert;
import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Date;
import java.util.List;
/**
 * Created by Ivana on 19/05/2017.
 */
public abstract class AbstractServiceReservationDAOTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractServiceReservationDAOTest.class);
    protected ServiceReservationDAO serviceReservationDAO;
    protected void setServiceReservationDAO(ServiceReservationDAO serviceReservationDAO){
        this.serviceReservationDAO = serviceReservationDAO;
    }
    @Test
    public void createServiceReservationWithValidParametersShouldPersist() throws DAOException {
        ServiceReservation serviceReservation = new ServiceReservation(null,100,null,null,null,false,false,null,null);
        Service s = new Service();
        s.setSrid(1);
        serviceReservation.setSrid(s);
        try {
            ServiceReservation r = serviceReservationDAO.create(serviceReservation);
            r = serviceReservationDAO.get(r);
            Assert.assertEquals(serviceReservation.getRid(), r.getRid());
            Assert.assertEquals(serviceReservation.getCostumerId(), r.getCostumerId());
            Assert.assertEquals(serviceReservation.getFrom(), r.getFrom());
            Assert.assertEquals(serviceReservation.getUntil(), r.getUntil());
            Assert.assertEquals(serviceReservation.getTotal(), r.getTotal());
            Assert.assertEquals(serviceReservation.getPaid(), r.getPaid());
            Assert.assertEquals(serviceReservation.getCanceled(), r.getCanceled());
            Assert.assertEquals(serviceReservation.getSrid().getSrid(), r.getSrid().getSrid());
            Assert.assertEquals(serviceReservation.getDate(), r.getDate());
            List<ServiceReservation> list = serviceReservationDAO.search(new ServiceReservation());
            Assert.assertTrue(list.contains(r));
        } catch (DAOException e) {
            LOGGER.error("Create Service Reservation did not succed", e);
        }
    }
}