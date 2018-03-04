package main.java.ac.at.tuwien.sepm.QSE15.test.hotelDaoTest;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.dao.hotelDAO.JDBCHotelDAO;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by ervincosic on 14/05/2017.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class JDBCHotelDaoTest extends AbstractHotelTest{

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCHotelDAO.class);

    private JDBCSingletonConnection jdbcSingletonConnection;

    private Connection connection;

    private AnnotationConfigApplicationContext context;

    @Before
    public void setUp(){

        context = new AnnotationConfigApplicationContext(this.getClass());

        jdbcSingletonConnection = context.getBean(JDBCSingletonConnection.class);

        try {
            connection = jdbcSingletonConnection.getConnection();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage());
        }

        super.jdbcHotelDAO = context.getBean(JDBCHotelDAO.class);

    }

    @After
    public void tearDown() throws SQLException{
            connection.rollback();
    }

}
