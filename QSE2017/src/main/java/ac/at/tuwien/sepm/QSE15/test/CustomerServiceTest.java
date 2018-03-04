package main.java.ac.at.tuwien.sepm.QSE15.test;

import main.java.ac.at.tuwien.sepm.QSE15.dao.customerDAO.JDBCCustomerDAO;
import main.java.ac.at.tuwien.sepm.QSE15.service.customerService.CustomerServiceIMPL;
import org.junit.Before;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.sql.SQLException;

import static org.mockito.Mockito.mock;

/**
 * Created by Stefan Puhalo on 6/21/2017.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class CustomerServiceTest extends AbstractCustomerServiceTest {

    private AnnotationConfigApplicationContext context;

    private JDBCCustomerDAO customerDAO;

    private CustomerServiceIMPL customerService;


    @Before
    public void setUp() throws SQLException {

        context = new AnnotationConfigApplicationContext(this.getClass());

        customerService = context.getBean(CustomerServiceIMPL.class);

        customerDAO = mock(JDBCCustomerDAO.class);
        setCustomerDAO(customerDAO);
        setCustomerService(customerService);

    }




}
