package main.java.ac.at.tuwien.sepm.QSE15.test;

import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.dao.employeeDAO.JDBCEmployeeDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import main.java.ac.at.tuwien.sepm.QSE15.service.employeeService.EmployeeService;
import main.java.ac.at.tuwien.sepm.QSE15.service.employeeService.EmployeeServiceIMPL;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;

/**
 * Created by Stefan Puhalo on 6/21/2017.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class EmployeeServiceTest extends AbstractEmployeeServiceTest{

    private AnnotationConfigApplicationContext context;

    private JDBCEmployeeDAO employeeDAO;

    private EmployeeServiceIMPL employeeService;


        @Before
        public void setUp() throws SQLException {

            context = new AnnotationConfigApplicationContext(this.getClass());

            employeeService = context.getBean(EmployeeServiceIMPL.class);

            employeeDAO = mock(JDBCEmployeeDAO.class);
            setEmployeeDAO(employeeDAO);
            setEmployeeService(employeeService);

    }

}
