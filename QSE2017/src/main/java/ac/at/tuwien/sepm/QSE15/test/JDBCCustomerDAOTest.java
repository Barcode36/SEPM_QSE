package main.java.ac.at.tuwien.sepm.QSE15.test;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.dao.creditCardDAO.JDBCCreditCardDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.customerDAO.CustomerDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.customerDAO.JDBCCustomerDAO;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Stefan Puhalo on 5/3/2017.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class JDBCCustomerDAOTest extends AbstractCustomerDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCCustomerDAOTest.class);

    JDBCSingletonConnection jdbcSingletonConnection;

    private Connection connection;

    @Autowired
    private JDBCCustomerDAO customerDAO;

    @Autowired
    private JDBCCreditCardDAO creditCardDAO;

    private AnnotationConfigApplicationContext context;

    @Before
    public void setUp() throws SQLException {

        context = new AnnotationConfigApplicationContext(this.getClass());
        jdbcSingletonConnection = context.getBean(JDBCSingletonConnection.class);

        try {
            customerDAO = context.getBean(JDBCCustomerDAO.class);
            creditCardDAO = context.getBean(JDBCCreditCardDAO.class);

            setCustomerDAO(customerDAO);
            setCreditCardDAO(creditCardDAO);

            connection = jdbcSingletonConnection.getConnection();

        } catch (DAOException e) {
            LOGGER.error("Connection failed");
        }

        assert connection != null;

        connection.setAutoCommit(false);

    }

    @After
    public void tearDown() throws SQLException,ClassNotFoundException {
        connection.rollback();
    }

}
