package main.java.ac.at.tuwien.sepm.QSE15.test.RoomReservation_TEST;

import main.java.ac.at.tuwien.sepm.QSE15.dao.roomReservationDAO.JDBCRoomReservationDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.RoomReservation;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomReservationService.RoomReservationIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomReservationService.RoomReservationService;
import org.junit.Before;

import static org.mockito.Mockito.mock;

/**
 * Created by ivana on 21.6.2017.
 */
public class RoomReservationServiceTest extends AbstractRoomReservationServiceTest {

    RoomReservationService roomReservationService;

    @Before
    public void setUp(){

        AbstractRoomReservationServiceTest.roomReservationService = new RoomReservationIMPL();

        super.roomReservationDAO = mock(JDBCRoomReservationDAO.class);

        roomReservationService = new RoomReservationIMPL();
    }
}
