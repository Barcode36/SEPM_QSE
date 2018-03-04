package main.java.ac.at.tuwien.sepm.QSE15.test;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.dao.customerDAO.CustomerDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.customerDAO.JDBCCustomerDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.employeeDAO.JDBCEmployeeDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.serviceDAO.JDBCServiceDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.serviceDAO.ServiceDAO;
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
 * Created by Nemanja Vukoje on 06/05/2017.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class JDBCServiceDAOTest extends AbrstractServiceDaoTest{

    private static final Logger logger = LoggerFactory.getLogger(JDBCServiceDAOTest.class);

    private AnnotationConfigApplicationContext context;

    private Connection connection;

    @Autowired
    private JDBCServiceDAO serviceDAO;

    private JDBCSingletonConnection jdbcSingletonConnection;


    @Before
    public void setUp() throws SQLException {

        context = new AnnotationConfigApplicationContext(this.getClass());
        jdbcSingletonConnection = context.getBean(JDBCSingletonConnection.class);

        serviceDAO = context.getBean(JDBCServiceDAO.class);
        setServiceDAO(serviceDAO);
        try {
            connection = jdbcSingletonConnection.getConnection();
        } catch (DAOException e) {
            logger.error("Error in SETUP!");
        }
        assert connection!=null;
        connection.setAutoCommit(false);

    }

    @After
    public void tearDown() throws SQLException,ClassNotFoundException {
        connection.rollback();
    }
}
