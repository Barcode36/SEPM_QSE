package main.java.ac.at.tuwien.sepm.QSE15.test.RoomReservation_TEST;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.dao.roomReservationDAO.JDBCRoomReservationDAO;
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
 * Created by Ivana on 19/05/2017.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class JDBCRoomReservationDAOTest extends AbstractRoomReservationDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCRoomReservationDAOTest.class);

    JDBCSingletonConnection jdbcSingletonConnection;

    private Connection connection;

    @Autowired
    private JDBCRoomReservationDAO roomReservationDAO;

    private AnnotationConfigApplicationContext context;

    @Before
    public void setUp() throws SQLException {

        context = new AnnotationConfigApplicationContext(this.getClass());
        jdbcSingletonConnection = context.getBean(JDBCSingletonConnection.class);

        try {
            roomReservationDAO = context.getBean(JDBCRoomReservationDAO.class);

            setRoomReservationDAO(roomReservationDAO);

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
