package main.java.ac.at.tuwien.sepm.QSE15.test.Service_TEST;

import main.java.ac.at.tuwien.sepm.QSE15.dao.serviceDAO.JDBCServiceDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.serviceService.ServiceService;
import main.java.ac.at.tuwien.sepm.QSE15.service.serviceService.ServiceServiceIMPL;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ivana on 21.6.2017.
 */
public class AbstractServiceServiceTest {

    JDBCServiceDAO serviceDAO;

    protected static ServiceService serviceService;

    protected static final Logger LOGGER = LoggerFactory.getLogger(ServiceServiceIMPL.class);

    @Test(expected = ServiceException.class)
    public void createServiceWithNullAsInputParameterShouldThrowException() throws ServiceException{

        serviceService.create(null);
    }

    @Test(expected = ServiceException.class)
    public void updateServiceWithNullAsInputParameterShouldThrowException() throws ServiceException{

        serviceService.update(null);

    }

    @Test(expected = ServiceException.class)
    public void deleteServiceWithNullAsInputParameterShouldThrowException() throws ServiceException{

        serviceService.delete(null);
    }

    @Test(expected = ServiceException.class)
    public void getServiceWithNullAsInputParameterShouldThrowException() throws ServiceException{

        serviceService.get(null);
    }

}
