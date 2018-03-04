package main.java.ac.at.tuwien.sepm.QSE15.test.emailPasswordServiceTest;

import main.java.ac.at.tuwien.sepm.QSE15.service.emailPasswordService.EmailPasswordService;
import main.java.ac.at.tuwien.sepm.QSE15.test.emailPasswordDAOTest.AbstractEmailPasswordDAOTest;
import main.java.ac.at.tuwien.sepm.QSE15.test.stubs.EmailPasswordDAOStub;
import org.junit.Before;

/**
 * Created by ervincosic on 30/05/2017.
 */
public class EmailPasswordServiceTest  extends AbstractEmailPasswordServiceTest {

    @Before
    public void setUp(){
        super.emailPasswordService = new EmailPasswordService();
        emailPasswordService.setEmailPasswordsDAO(new EmailPasswordDAOStub());
    }
}
