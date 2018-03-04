package main.java.ac.at.tuwien.sepm.QSE15.test;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.dao.roomReservationDAO.JDBCRoomReservationDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.serviceReservationDAO.JDBCServiceReservationDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.statisticsDAO.JDBCStatisticsDAO;
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
 * Created by Stefan Puhalo on 6/19/2017.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class JDBCStatisticsDAOTest extends AbstractStatisticsDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCStatisticsDAOTest.class);

    JDBCSingletonConnection jdbcSingletonConnection;

    private Connection connection;

    @Autowired
    private JDBCStatisticsDAO statisticsDAO;

    @Autowired
    private JDBCRoomReservationDAO roomReservationDAO;

    @Autowired
    private JDBCServiceReservationDAO serviceReservationDAO;

    private AnnotationConfigApplicationContext context;

    @Before
    public void setUp() throws SQLException {

        context = new AnnotationConfigApplicationContext(this.getClass());
        jdbcSingletonConnection = context.getBean(JDBCSingletonConnection.class);

        try {
            statisticsDAO = context.getBean(JDBCStatisticsDAO.class);
            roomReservationDAO = context.getBean(JDBCRoomReservationDAO.class);
            serviceReservationDAO = context.getBean(JDBCServiceReservationDAO.class);

            setStatisticsDAO(statisticsDAO);
            setRoomReservationDAO(roomReservationDAO);
            setServiceReservationDAO(serviceReservationDAO);

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
