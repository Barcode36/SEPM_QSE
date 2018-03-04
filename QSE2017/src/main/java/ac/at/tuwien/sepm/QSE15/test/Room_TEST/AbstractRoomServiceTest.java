package main.java.ac.at.tuwien.sepm.QSE15.test.Room_TEST;

import main.java.ac.at.tuwien.sepm.QSE15.dao.roomDAO.JDBCRoomDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Category;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Room;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomService.RoomService;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomService.RoomServiceIMPL;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ivana on 21.6.2017.
 */
public abstract class AbstractRoomServiceTest {

    JDBCRoomDAO roomDAO;

    protected static RoomService roomService;

    protected static final Logger LOGGER = LoggerFactory.getLogger(RoomServiceIMPL.class);

    @Test(expected = ServiceException.class)
    public void createWithNullAsParameterShouldThrowException() throws ServiceException{

        roomService.create(null);
    }

    @Test(expected = ServiceException.class)
    public void searchFreeWithNullDatesAsParametersShouldThrowException() throws ServiceException{

        roomService.searchFree(new Room(), new Category(), null, null);
    }

    @Test(expected = ServiceException.class)
    public void updateWithNullAsParameterShouldThrowException() throws ServiceException{

        roomService.update(null);
    }

    @Test(expected = ServiceException.class)
    public void lockRoomWithNullAsParameterShouldThrowException() throws ServiceException{

        roomService.lockRoom(null);
    }

    @Test(expected = ServiceException.class)
    public void getWithNullAsParameterShouldThrowException() throws ServiceException{

        roomService.get(null);
    }

    @Test(expected = ServiceException.class)
    public void getLocksWithNullAsParameterShouldThrowException() throws ServiceException{

        roomService.getLocks(null);
    }

}

