package main.java.ac.at.tuwien.sepm.QSE15.test.RoomReservation_TEST;

import main.java.ac.at.tuwien.sepm.QSE15.dao.roomReservationDAO.JDBCRoomReservationDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.RoomReservation;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomReservationService.RoomReservationIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomReservationService.RoomReservationService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ivana on 21.6.2017.
 */
public class AbstractRoomReservationServiceTest {

    JDBCRoomReservationDAO roomReservationDAO;

    protected static RoomReservationService roomReservationService;

    protected static final Logger LOGGER = LoggerFactory.getLogger(RoomReservationIMPL.class);

    @Test(expected = ServiceException.class)
    public void createRoomReservationWithNullAsParameterShouldThrowException() throws ServiceException{

        roomReservationService.create(null);
    }

    @Test(expected = ServiceException.class)
    public void updateRoomReservationWithNullAsParameterShouldThrowException() throws ServiceException{

        roomReservationService.update(null);
    }

    @Test(expected = ServiceException.class)
    public void getRoomReservationWithNullAsParameterShouldThrowException() throws ServiceException{

        roomReservationService.get(null);
    }

    @Test(expected = ServiceException.class)
    public void cancelRoomReservationWithNullAsParameterShouldThrowException() throws ServiceException{

        roomReservationService.cancel(null);
    }

    @Test(expected = ServiceException.class)
    public void searchReservationOnSpecifiedDateWithNullDatesAsParameterShouldThrowException() throws ServiceException{

        roomReservationService.searchReservationsOnSpecifiedDate(new RoomReservation(null,null,null,null,null,null,null,null,null,null));
    }
}


