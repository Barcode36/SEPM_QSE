package main.java.ac.at.tuwien.sepm.QSE15.test.RoomReservation_TEST;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.roomReservationDAO.RoomReservationDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.RoomReservation;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.util.List;

/**
 * Created by Ivana on 19/05/2017.
 */
public abstract class AbstractRoomReservationDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRoomReservationDAOTest.class);

    protected RoomReservationDAO roomReservationDAO;

    protected void setRoomReservationDAO(RoomReservationDAO roomReservationDAO){
        this.roomReservationDAO = roomReservationDAO;
    }

    @Test
    public void createRoomReservationWithValidParametersShouldPersist() throws DAOException{

        RoomReservation roomReservation = new RoomReservation(null, 3000, Date.valueOf("2020-10-10"), Date.valueOf("2020-10-20"),
                null, false, false, 101, 5000L, true);

            RoomReservation r = roomReservationDAO.create(roomReservation);

            r = roomReservationDAO.get(r.getRid());

            Assert.assertEquals(roomReservation.getRid(), r.getRid());
            Assert.assertEquals(roomReservation.getCostumerId(), r.getCostumerId());
            Assert.assertEquals(roomReservation.getFrom(), r.getFrom());
            Assert.assertEquals(roomReservation.getUntil(), r.getUntil());
            Assert.assertEquals(roomReservation.getTotal(), r.getTotal());
            Assert.assertEquals(roomReservation.getPaid(), r.getPaid());
            Assert.assertEquals(roomReservation.getCanceled(), r.getCanceled());
            Assert.assertEquals(roomReservation.getRoomId(), r.getRoomId());
            Assert.assertEquals(roomReservation.getRoomPrice(), r.getRoomPrice());
            Assert.assertEquals(roomReservation.getBreakfast(), r.getBreakfast());

            List<RoomReservation> list = roomReservationDAO.search(new RoomReservation());
            Assert.assertTrue(list.contains(r));

    }

    @Test(expected = DAOException.class)
    public void createRoomReservationWithInvalidPeriodShouldThrowException() throws DAOException{

        roomReservationDAO.create( new RoomReservation(null, 3000, Date.valueOf("2050-10-10"), Date.valueOf("2050-10-18"),
                    null, false, false, 101, 5000L, true));
        roomReservationDAO.create( new RoomReservation(null, 3000, Date.valueOf("2050-10-09"), Date.valueOf("2050-10-12"),
                    null, false, false, 101, 5000L, true));

    }

/*
    @Test
    public void updateRoomReservationWithValidParametersShouldPersist() throws DAOException{

    }

    @Test(expected = DAOException.class)
    public void updateRoomReservationWithInvalidParametersShouldThrowException() throws DAOException{

    }
*/

    @Test
    public void searchRoomReservationsWithValidParametersShouldPersist() throws DAOException{

        RoomReservation roomReservation = new RoomReservation(null,2008,Date.valueOf("2040-06-18"),Date.valueOf("2040-06-22"),null,false,false,303,7000L,true);

            List<RoomReservation> list1 = roomReservationDAO.search(roomReservation);
            Assert.assertFalse(list1.contains(roomReservation));

            roomReservationDAO.create(roomReservation);
            List<RoomReservation> list2 = roomReservationDAO.search(roomReservation);
            Assert.assertTrue(list2.contains(roomReservation));
    }

    @Test
    public void getRoomReservationWithValidParametersShouldPersist() throws DAOException{
            RoomReservation r1 = roomReservationDAO.get(2005);
            List<RoomReservation> list = roomReservationDAO.search(new RoomReservation(2005,null,null,null,null,null,null,null,null,null));

            Assert.assertTrue(list.contains(r1));
    }

    @Test(expected = DAOException.class)
    public void getRoomReservationWithWithInvalidParametersShouldThrowException() throws DAOException{

        RoomReservation r1 = roomReservationDAO.get(null);
    }

}