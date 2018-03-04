package main.java.ac.at.tuwien.sepm.QSE15.test.ServiceReservation_TEST;

import main.java.ac.at.tuwien.sepm.QSE15.dao.serviceReservationDAO.JDBCServiceReservationDAO;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.serviceReservationService.ServiceReservationService;
import main.java.ac.at.tuwien.sepm.QSE15.service.serviceReservationService.ServiceReservationServiceIMPL;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ivana on 21.6.2017.
 */
public abstract class AbstractServiceReservationServiceTest {

    JDBCServiceReservationDAO serviceReservationDAO;

    protected static ServiceReservationService serviceReservationService;

    protected static final Logger LOGGER = LoggerFactory.getLogger(ServiceReservationServiceIMPL.class);

    @Test(expected = ServiceException.class)
    public void createServiceReservationWithNullAsParameterShouldThrowException() throws ServiceException{

        serviceReservationService.create(null);
    }

    @Test(expected = ServiceException.class)
    public void updateServiceReservationWithNullAsParameterShouldThrowException() throws ServiceException{

        serviceReservationService.update(null);
    }

    @Test(expected = ServiceException.class)
    public void getServiceReservationWithNullAsParameterShouldThrowException() throws ServiceException{

        serviceReservationService.get(null);
    }

    @Test(expected = ServiceException.class)
    public void cancelServiceReservationWithNullAsParameterShouldThrowException() throws ServiceException{

        serviceReservationService.cancel(null);
    }

    @Test(expected = ServiceException.class)
    public void sendEmailServiceReservationWithNullAsParameterShouldThrowException() throws ServiceException {

        serviceReservationService.sendEmail(null);
    }
}



