package main.java.ac.at.tuwien.sepm.QSE15.test.authenticationTest;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.dao.employeeDAO.EmployeeDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.employeeDAO.JDBCEmployeeDAO;
import main.java.ac.at.tuwien.sepm.QSE15.service.authenticationService.AuthenticationService;
import main.java.ac.at.tuwien.sepm.QSE15.service.authenticationService.MyAuthenticationService;
import org.junit.After;
import org.junit.Before;

import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.Mockito.*;

/**
 * Created by ervincosic on 06/05/2017.
 */
public class MyAuthenticationServiceTest extends AbstractAuthenticationServiceTest{


    AuthenticationService authenticationService;



    @Before
    public void setUp(){

        AbstractAuthenticationServiceTest.authenticationService = new MyAuthenticationService();

        super.employeeDAO = mock(JDBCEmployeeDAO.class);


        authenticationService = new MyAuthenticationService();
    }


}
