package main.java.ac.at.tuwien.sepm.QSE15.dao.hotelDAO;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.dao.employeeDAO.JDBCEmployeeDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.hotel.Hotel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sun.rmi.runtime.Log;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * Created by Bajram Saiti on 01.05.17.
 * Developed by Ervin Cosic
 */
@Repository
public class JDBCHotelDAO implements HotelDAO {

    @Autowired
    private JDBCSingletonConnection jdbcSingletonConnection;

    private Connection connection;

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCHotelDAO.class);

    private PreparedStatement createHotelStmnt;

    private PreparedStatement readHotelStmnt;

    private PreparedStatement getHotelIdStmnt;

    private PreparedStatement updateHotelStmnt;

    @PostConstruct
    public void init(){
        try {
            connection = jdbcSingletonConnection.getConnection();

            createHotelStmnt = connection.prepareStatement("INSERT INTO hotel (name, address, iban, bic, email, password, CREATION_DATE, HOST, PORT)" +
                    "values (?, ?, ?, ?, ?, ?, ?, ?, ?)");

            readHotelStmnt = connection.prepareStatement("SELECT * FROM hotel;");

            getHotelIdStmnt = connection.prepareStatement("SELECT MAX(HNR)AS id FROM hotel;");

            updateHotelStmnt = connection.prepareStatement("" +
                    "UPDATE hotel SET name = ?, address = ?, iban = ?, bic = ?, email = ?, password =?, host = ?, port = ? WHERE hnr = ?;");

        } catch (DAOException e) {
            LOGGER.error("Unable to establish connection.");
        } catch (SQLException e){
            LOGGER.error("Cannot prepare calls : " + e.getMessage());
        }


    }

    public Hotel get() throws DAOException{
        Hotel hotel = null;

        ResultSet resultSet;

        try {
            resultSet = readHotelStmnt.executeQuery();

            if(resultSet.next()){

                hotel = new Hotel();

                hotel.setName(resultSet.getString("name"));
                hotel.setHnr(resultSet.getInt("hnr"));
                hotel.setAddress(resultSet.getString("address"));
                hotel.setEmail(resultSet.getString("email"));
                hotel.setPassword(resultSet.getString("password"));
                hotel.setIban(resultSet.getString("iban"));
                hotel.setBic(resultSet.getString("bic"));
                hotel.setDate(resultSet.getDate("CREATION_DATE"));
                hotel.setHost(resultSet.getString("HOST"));
                hotel.setPort(resultSet.getString("PORT"));

            }

        }catch (SQLException e){
            LOGGER.info("Unable to load hotel.");
            throw new DAOException(e.getMessage());
        }

        return hotel;
    }

    @Override
    public void update(Hotel hotel) throws DAOException {

        if(hotel == null){
            throw  new DAOException("The given hotel is a null pointer.");
        }

        try {

            updateHotelStmnt.setString(1, hotel.getName());
            updateHotelStmnt.setString(2, hotel.getAddress());
            updateHotelStmnt.setString(3, hotel.getIban());
            updateHotelStmnt.setString(4, hotel.getBic());
            updateHotelStmnt.setString(5, hotel.getEmail());
            updateHotelStmnt.setString(6, hotel.getPassword());
            updateHotelStmnt.setString(7, hotel.getHost());
            updateHotelStmnt.setString(8, hotel.getPort());

            updateHotelStmnt.setInt(9, hotel.getHnr());

            updateHotelStmnt.executeUpdate();

            LOGGER.info("Hotel updated.");

        }catch (SQLException e ){
            LOGGER.error("Cannot update hotel.");
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public Hotel create(Hotel hotel) throws DAOException {

        if(hotel == null) {
            throw new DAOException("The given hotel is a null pointer.");
        }

        try {

            createHotelStmnt.setString(1, hotel.getName());
            createHotelStmnt.setString(2, hotel.getAddress());
            createHotelStmnt.setString(3, hotel.getIban());
            createHotelStmnt.setString(4, hotel.getBic());
            createHotelStmnt.setString(5, hotel.getEmail());
            createHotelStmnt.setString(6, hotel.getPassword());
            createHotelStmnt.setString(8, hotel.getHost());
            createHotelStmnt.setString(9, hotel.getPort());

            java.sql.Date sqlDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());

            createHotelStmnt.setString(7, sqlDate.toString());

            int rows = createHotelStmnt.executeUpdate();

            LOGGER.info("Created: " + rows );

            ResultSet resultSet = getHotelIdStmnt.executeQuery();

            int hotelId = -1;

            if(resultSet.next()){
                hotelId = resultSet.getInt("id");
            }

            hotel.setHnr(hotelId);

            LOGGER.info("Hotel id: " + hotelId);

        }catch (SQLException e){
            throw new DAOException("Unable to create hotel for some reason.");
        }

        LOGGER.info("Hotel created.");
        return hotel;
    }

    @Override
    public void delete(Hotel hotel) throws DAOException {

    }
}
