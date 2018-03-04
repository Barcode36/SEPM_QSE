package main.java.ac.at.tuwien.sepm.QSE15.test;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.dao.employeeDAO.EmployeeDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.employeeDAO.JDBCEmployeeDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.NoEmployeesFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.test.stubs.EmployeeDaoStub;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Stefan Puhalo on 5/4/2017.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class JDBCEmployeeDAOTest extends AbstractEmployeeDAOTest{

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCEmployeeDAOTest.class);

    private AnnotationConfigApplicationContext context;

    private Connection connection;

    private JDBCEmployeeDAO employeeDAO;

    private JDBCSingletonConnection jdbcSingletonConnection;

    @Before
    public void setUp() throws SQLException {

        context = new AnnotationConfigApplicationContext(this.getClass());
        jdbcSingletonConnection = context.getBean(JDBCSingletonConnection.class);

        employeeDAO = context.getBean(JDBCEmployeeDAO.class);
        setEmployeeDAO(employeeDAO);
        try {
            connection = jdbcSingletonConnection.getConnection();
        } catch (DAOException e) {
            LOGGER.error("Error in SETUP!");
        }
        assert connection!=null;
        connection.setAutoCommit(false);
    }

    @After
    public void tearDown() throws SQLException,ClassNotFoundException {
        connection.rollback();
    }


}
