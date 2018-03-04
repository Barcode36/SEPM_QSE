package main.java.ac.at.tuwien.sepm.QSE15.test.invoiceTest;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.dao.invoiceDAO.JDBCInvoiceDAO;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Luka on 5/11/17.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class JDBCInvoiceDAOTest extends AbstractInvoiceDAOTest{

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCInvoiceDAOTest.class);
    private Connection conn;
    private JDBCSingletonConnection jdbcSingletonConnection;


    private JDBCInvoiceDAO invoiceDAO;

    private AnnotationConfigApplicationContext context;

    @Before
    public void setUp() throws SQLException {

        context = new AnnotationConfigApplicationContext(this.getClass());

        jdbcSingletonConnection = context.getBean(JDBCSingletonConnection.class);

        try {

            invoiceDAO = context.getBean(JDBCInvoiceDAO.class);

            setInvoiceDAO(invoiceDAO);
            conn = jdbcSingletonConnection.getConnection();

        } catch (DAOException e) {
            LOGGER.error("Connection failed.");
        }

        assert conn!=null;
        conn.setAutoCommit(false);

    }

    @After
    public void tearDown() throws SQLException,ClassNotFoundException {
        conn.rollback();
    }
}
