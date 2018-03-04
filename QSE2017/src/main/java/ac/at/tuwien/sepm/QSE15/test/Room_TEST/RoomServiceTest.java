package main.java.ac.at.tuwien.sepm.QSE15.test.Room_TEST;

import main.java.ac.at.tuwien.sepm.QSE15.dao.roomDAO.JDBCRoomDAO;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomService.RoomService;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomService.RoomServiceIMPL;
import org.junit.Before;

import static org.mockito.Mockito.mock;

/**
 * Created by ivana on 21.6.2017.
 */
public class RoomServiceTest extends AbstractRoomServiceTest {

    RoomService roomService;

    @Before
    public void setUp(){

        AbstractRoomServiceTest.roomService = new RoomServiceIMPL();

        super.roomDAO = mock(JDBCRoomDAO.class);


        roomService = new RoomServiceIMPL();
    }

}
