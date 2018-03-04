package main.java.ac.at.tuwien.sepm.QSE15.service.roomService;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.dao.roomDAO.JDBCRoomDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.roomDAO.RoomDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Category;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Lock;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Room;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.security.sasl.SaslException;
import java.sql.Date;
import java.util.List;

/**
 * Created by Bajram Saiti on 02.05.17.
 */
@Service
public class RoomServiceIMPL implements RoomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomServiceIMPL.class);

    @Autowired
    private JDBCRoomDAO jdbcRoomDAO;

    public Room create(Room room) throws ServiceException {

        if (room == null){
            LOGGER.error("Cannot create Room! Input value is null!");
            throw new ServiceException("Input parameter is null");
        }

        try{
            return jdbcRoomDAO.create(room);

        } catch (DAOException e){
            LOGGER.error("Unable to create Room.");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Room> search(Room r, Category c) throws ServiceException {

        try {
            return jdbcRoomDAO.search(r,c);

        } catch (DAOException e) {
            LOGGER.error("Unable to search Room.");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Room> searchFree(Room r1, Category c, Date d1, Date d2) throws ServiceException {

        if (d1 == null || d2 == null){
            LOGGER.error("Cannot search free Rooms! One of the date values or both is null!");
            throw new ServiceException("Date parameter is null");
        }

        try {
            return jdbcRoomDAO.searchFreeRooms(r1, c, d1, d2);

        } catch (DAOException e) {
            LOGGER.error("Unable to search FREE Rooms.");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void update(Room room) throws ServiceException {

        if (room == null){
            LOGGER.error("Cannot update Room! Input value is null!");
            throw new ServiceException("Input parameter is null");
        }

        try {
            jdbcRoomDAO.update(room);

        } catch (DAOException e) {
            LOGGER.error("Unable to update Room.");
            throw new ServiceException(e.getMessage());
        }

    }

    @Override
    public void lockRoom(Lock lock) throws ServiceException {

        if (lock == null){
            LOGGER.error("Cannot lock Room! Input value is null!");
            throw new ServiceException("Input parameter is null");
        }

        try {
            jdbcRoomDAO.lock(lock);

        } catch (DAOException e) {
            LOGGER.error("Unable to lock the room");
            throw new ServiceException(e.getMessage());
        }

    }

    @Override
    public Room get(Integer rnr) throws ServiceException {

        if (rnr == null || rnr<0){
            LOGGER.error("Cannot get Room! Input value is null!");
            throw new ServiceException("Input value is null");
        }

        try {
            return jdbcRoomDAO.get(rnr);

        } catch (DAOException e) {
            LOGGER.error("Unable to get room");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Lock> getLocks(Integer rnr) throws ServiceException {
        if (rnr == null){
            LOGGER.error("Cannot get Room! Input value is null!");
            throw new ServiceException("Input value is null");
        }

        try {
            return jdbcRoomDAO.getLocks(rnr);

        } catch (DAOException e) {
            LOGGER.error("Unable to get Locks");
            throw new ServiceException(e.getMessage());
        }
    }
}
