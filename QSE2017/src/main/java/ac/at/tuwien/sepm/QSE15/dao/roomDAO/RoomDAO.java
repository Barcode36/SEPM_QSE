package main.java.ac.at.tuwien.sepm.QSE15.dao.roomDAO;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Category;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Lock;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Room;

import java.sql.Date;
import java.util.List;

/**
 * Created by Bajram Saiti on 01.05.17.
 */
public interface RoomDAO {

    /**
     * Description Method creates new Room
     * Precondition: Room object shouldn't be null
     * Postcondition: New Room will be added in Database
     * @param room
     * @return Returns created room
     * @throws DAOException
     */
    Room create(Room room) throws DAOException;

    /**
     * Description Searching rooms according to the parameters we gave.
     * Precondition: Arguments shouldn't be null.
     * Postcondition: List with such rooms will be returned.
     * @param r
     * @param c
     * @return
     * @throws DAOException
     */
    List<Room> search(Room r, Category c) throws DAOException;

    /**\
     * Description Searching Free rooms between two dates.
     * Precondition: Arguments shouldn't be null.
     * Postcondition: List with such rooms will be returned.
     * @param r1
     * @param c
     * @param d1
     * @param d2
     * @return
     * @throws DAOException
     */
    List<Room> searchFreeRooms(Room r1, Category c, Date d1, Date d2) throws DAOException;

    /**
     * Description Editing some Room parameters.
     * Precondition: Argument shouldn't be null.
     * Postcondition: Room in database will have new data.
     * @param room
     * @throws DAOException
     */
    void update(Room room) throws DAOException;

    /**
     * Description Locking room for some reason.
     * Precondition: Argument Lock shouldn't be null.
     * Postcondition: Room will be locked.
     * @param lock
     * @throws DAOException
     */
    void lock(Lock lock) throws DAOException;

    /**
     * Description: This method will give us Room object by giving only the room nr (room id).
     * Precondition: Rnr should be positive number.
     * Postcondition: Room with that number will be returned.
     * @param rnr
     * @return
     * @throws DAOException
     */
    Room get(Integer rnr) throws DAOException;

    List<Lock> getLocks(Integer rnr) throws DAOException;
}
