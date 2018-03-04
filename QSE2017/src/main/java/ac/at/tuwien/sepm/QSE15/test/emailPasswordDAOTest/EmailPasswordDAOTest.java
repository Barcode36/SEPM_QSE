package main.java.ac.at.tuwien.sepm.QSE15.test.emailPasswordDAOTest;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.dao.email.EmailPasswordsDAO;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by ervincosic on 29/05/2017.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class EmailPasswordDAOTest extends AbstractEmailPasswordDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailPasswordsDAO.class);

    private AnnotationConfigApplicationContext context;

    private JDBCSingletonConnection jdbcSingletonConnection;

    private Connection connection;



    @Before
    public void setUp()throws DAOException{
        context = new AnnotationConfigApplicationContext(this.getClass());

        jdbcSingletonConnection = context.getBean(JDBCSingletonConnection.class);

        connection = jdbcSingletonConnection.getConnection();

        super.emailPasswordsDAO = context.getBean(EmailPasswordsDAO.class);
    }

    @After
    public void tearDown()throws SQLException{
        connection.rollback();
    }
}
