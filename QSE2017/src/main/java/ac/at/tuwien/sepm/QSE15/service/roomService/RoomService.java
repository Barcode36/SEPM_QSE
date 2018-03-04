package main.java.ac.at.tuwien.sepm.QSE15.service.roomService;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Category;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Lock;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Room;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;

import java.sql.Date;
import java.util.List;

/**
 * Created by Bajram Saiti on 02.05.17.
 */
public interface RoomService {

    Room create(Room room) throws ServiceException;

    List<Room> search(Room r, Category c) throws ServiceException;

    List<Room> searchFree(Room r1, Category c, Date d1, Date d2) throws ServiceException;

    void update(Room room) throws ServiceException;

    void lockRoom(Lock lock) throws ServiceException;

    Room get(Integer rnr) throws ServiceException;

    List<Lock> getLocks(Integer rnr) throws ServiceException;
}
