package main.java.ac.at.tuwien.sepm.QSE15.test.Service_TEST;

import main.java.ac.at.tuwien.sepm.QSE15.dao.serviceDAO.JDBCServiceDAO;
import main.java.ac.at.tuwien.sepm.QSE15.service.serviceService.ServiceService;
import main.java.ac.at.tuwien.sepm.QSE15.service.serviceService.ServiceServiceIMPL;
import org.junit.Before;

import static org.mockito.Mockito.mock;

/**
 * Created by ivana on 21.6.2017.
 */
public class ServiceServiceTest extends AbstractServiceServiceTest {

    ServiceService serviceService;

    @Before
    public void setUp(){

        AbstractServiceServiceTest.serviceService = new ServiceServiceIMPL();

        super.serviceDAO = mock(JDBCServiceDAO.class);


        serviceService = new ServiceServiceIMPL();
    }
}
