package main.java.ac.at.tuwien.sepm.QSE15.test.hotelDaoTest;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.hotelDAO.JDBCHotelDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.hotel.Hotel;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by ervincosic on 14/05/2017.
 */
public abstract class AbstractHotelTest {

    protected JDBCHotelDAO jdbcHotelDAO;

    @Test
    public void testCreationOfValidHotel() throws DAOException {
        Hotel hotel = getWhiteHouse();

        hotel.setName("White House");
        hotel.setIban("US013 1234 1245 1234 1234");
        hotel.setBic("BKEWASF");
        hotel.setAddress("1600 Pennsylvania ave");
        hotel.setEmail("oval_office@whitehouse.gov");
        hotel.setPassword("trumpRocks");

        hotel = jdbcHotelDAO.create(hotel);

        Assert.assertTrue(hotel.getHnr() > -1);
    }

    @Test(expected = DAOException.class)
    public void testCreateNullHotel() throws DAOException{
        jdbcHotelDAO.create(null);
    }

    @Test
    public void testGetHotel() throws DAOException {
        jdbcHotelDAO.create(getWhiteHouse());

        Hotel hotel = jdbcHotelDAO.get();

        Assert.assertTrue(hotel != null);
    }
    @Test
    public void testUpdateHotel() throws DAOException{
        Hotel hotel = jdbcHotelDAO.create(getWhiteHouse());

        hotel.setName("Reichstag");

        jdbcHotelDAO.update(hotel);

        hotel = jdbcHotelDAO.get();

        Assert.assertTrue(hotel.getName().equals("Reichstag"));


    }

    private Hotel getWhiteHouse(){
        Hotel whiteHouse = new Hotel();

        whiteHouse.setName("White House");
        whiteHouse.setIban("US013 1234 1245 1234 1234");
        whiteHouse.setBic("BKEWASF");
        whiteHouse.setAddress("1600 Pennsylvania ave");
        whiteHouse.setEmail("oval_office@whitehouse.gov");
        whiteHouse.setPassword("trumpRocks");

        return whiteHouse;
    }

}
