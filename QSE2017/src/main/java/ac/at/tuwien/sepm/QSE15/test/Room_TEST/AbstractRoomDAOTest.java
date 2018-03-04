package main.java.ac.at.tuwien.sepm.QSE15.test.Room_TEST;

import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.customerDAO.JDBCCustomerDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.roomDAO.RoomDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Category;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Room;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Ivana on 19/05/2017.
 */
public abstract class AbstractRoomDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCCustomerDAO.class);


    protected RoomDAO roomDAO;

    protected void setRoomDAO(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }

    @Test
    public void getRoomWithValidParametersShouldPersist() throws DAOException{
        //Integer rnr, String category, Long price, String extras
        Category category = new Category("Single Room", 5000l , 1);
        Room actual = new Room(666, category.getName(), category.getPrice(), "TV, Wifi");

        try {
            roomDAO.create(actual);
            /*Room r = new Room();
            r.setRnr(666);*/
            Room expected = roomDAO.get(666);

            Assert.assertEquals(expected.getExtras(),actual.getExtras());
            Assert.assertEquals(expected.getPrice(),actual.getPrice());
            Assert.assertEquals(expected.getCategory(),actual.getCategory());

        } catch (DAOException e) {
            LOGGER.error("Testing getRoom with valid parameters is not working.");
        }
    }

    @Test(expected = DAOException.class)
    public void getRoomWithInvalidParametersShouldThrowException() throws DAOException{

        roomDAO.get(-1);

    }

    @Test
    public void createRoomWithValidParametersShouldPersist() throws DAOException{
        Category category = new Category("Single Room", 5000l , 1);
        Room actual = new Room(888, category.getName(), category.getPrice(), "TV, Wifi");

        try {

            List<Room> rooms = roomDAO.search(new Room(),new Category());
            Assert.assertFalse(rooms.contains(actual));
            roomDAO.create(actual);
            rooms = roomDAO.search(new Room(),new Category());
            Assert.assertTrue(rooms.contains(actual));
        } catch (DAOException e) {
            LOGGER.error("Testing CreateRoom with invalid parameters is not working.");
        }

    }



    @Test
    public void updateRoomWithValidParametersShouldPersist() throws DAOException{
        Category category = new Category("Single Room", 5000l, 1);
        Room actual = new Room(999, category.getName(), category.getPrice(), "TV, Wifi");

        try {
            actual = roomDAO.create(actual);
            actual.setExtras("Mini Bar");
            actual.setCategory("Double or Twin Room");
            roomDAO.update(actual);
            Room updated = roomDAO.get(999);

            Assert.assertEquals("Mini Bar", updated.getExtras());
            Assert.assertEquals("Double or Twin Room", updated.getCategory());

        } catch (DAOException e) {
            LOGGER.error("Testing getRoom with invalid parameters is not working.");
        }

    }


     @Test(expected = DAOException.class)
    public void updateRoomWithInvalidParametersShouldThrowException() throws DAOException{
        Room actual = new Room(-5, "Not existing one", 50L, "TV, Wifi");
        roomDAO.update(actual);

    }

    /*     @Test
    public void searchRoomWithInvalidParametersShouldThrowException() throws DAOException{
        Category category = new Category("Not existing category", -5L, 1);
        Room actual = new Room(-5, "Not existing one", category.getPrice(), "TV, Wifi");

        roomDAO.search(actual,category);

    }
    */

     @Test(expected = DAOException.class)
    public void createRoomWithInvalidParametersShouldThrowException() throws DAOException{

        roomDAO.create(null);

    }

    @Test
    public void SearchRoomWithValidParametersShouldPersist() throws DAOException{
        Category category = new Category("Single Room", 5000l, 1);
        Room actual = new Room(555, category.getName(), category.getPrice(), "TV, Wifi");

        try {

            List<Room> rooms = roomDAO.search(new Room(), new Category());
            Assert.assertFalse(rooms.contains(actual));

            roomDAO.create(actual);
            rooms = roomDAO.search(new Room(), new Category());
            Assert.assertTrue(rooms.contains(actual));

        } catch (DAOException e) {
            LOGGER.error("Testing getRoom with invalid parameters is not working.");
        }

    }


    @Test
    public void searchFreeRoomWithValidParametersShouldPersist() throws DAOException{

        Category category = new Category("Single Room", 5000l, 1);
        Room actual = new Room(555, category.getName(), category.getPrice(), "TV, Wifi");
        Date d1 = Date.valueOf("2020-06-25");
        Date d2 = Date.valueOf("2020-06-30");
        try {
            List<Room> rooms = roomDAO.searchFreeRooms(new Room(), new Category(), d1,d2);
            Room r = roomDAO.get(101);
            Assert.assertTrue(rooms.contains(r));
            d1 = Date.valueOf("2017-06-25");
            d2 = Date.valueOf("2017-07-06");
            rooms = roomDAO.searchFreeRooms(new Room(), new Category(), d1,d2);
            Assert.assertFalse(rooms.contains(r));
        } catch (DAOException e) {
            LOGGER.error("Testing searchFreeRoom with valid parameter is not working");
        }
    }

    @Test(expected = DAOException.class)
    public void searchFreeRoomWithInvalidParametersShouldThrowException() throws DAOException{

        roomDAO.searchFreeRooms(new Room(),new Category(), null,null);

    }


}
