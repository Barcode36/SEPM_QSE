package main.java.ac.at.tuwien.sepm.QSE15.test.ServiceReservation_TEST;

import main.java.ac.at.tuwien.sepm.QSE15.dao.serviceReservationDAO.JDBCServiceReservationDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.ServiceReservation;
import main.java.ac.at.tuwien.sepm.QSE15.service.serviceReservationService.ServiceReservationService;
import main.java.ac.at.tuwien.sepm.QSE15.service.serviceReservationService.ServiceReservationServiceIMPL;
import org.junit.Before;

import static org.mockito.Mockito.mock;

/**
 * Created by ivana on 21.6.2017.
 */
public class ServiceReservationServiceTest extends AbstractServiceReservationServiceTest {

    ServiceReservationService serviceReservationService;

    @Before
    public void setUp(){

        AbstractServiceReservationServiceTest.serviceReservationService = new ServiceReservationServiceIMPL();

        super.serviceReservationDAO = mock(JDBCServiceReservationDAO.class);

        serviceReservationService = new ServiceReservationServiceIMPL();
    }
}
