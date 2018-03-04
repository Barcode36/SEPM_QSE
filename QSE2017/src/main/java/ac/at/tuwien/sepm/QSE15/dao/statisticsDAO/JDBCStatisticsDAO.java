package main.java.ac.at.tuwien.sepm.QSE15.dao.statisticsDAO;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.HashMap;

/**
 * Created by Stefan Puhalo on 6/15/2017.
 */
@Repository
public class JDBCStatisticsDAO implements StatisticsDAO {

    @Autowired
    private JDBCSingletonConnection jdbcSingletonConnection;
    Connection connection;
    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCStatisticsDAO.class);

    @PostConstruct
    public void init () {

        try {
            connection = jdbcSingletonConnection.getConnection();
        } catch (DAOException e) {
            LOGGER.error("Unable to get connection.");

        }
    }


    @Override
    public HashMap<String, Integer> statisticsForAllRoomsFromDate(Date from, Date to) throws DAOException{

        if(from == null || to == null) {
            LOGGER.error("One or both dates are null");
            throw new DAOException();
        }

        if(from.after(to)) {
            LOGGER.error("From date set after to date");
            throw new DAOException("From date set after to date");
        }

        HashMap<String, Integer> statisticsForAllRoomsFromDate = new HashMap<>();

        try {
            PreparedStatement amountStmt = connection.prepareStatement("SELECT Room_category, SUM(DATEDIFF(day,from_date,until_date) + 1) AS Days " +
                    "FROM Reservation rs, Room_Reservation rr, Room r " +
                    "WHERE rs.rid = rr.rid AND rr.roomid = r.rnr AND rs.from_Date BETWEEN ? AND ? GROUP BY Room_category;");

            amountStmt.setDate(1, from);
            amountStmt.setDate(2, to);

            ResultSet rs = amountStmt.executeQuery();

            while(rs.next()) {
                statisticsForAllRoomsFromDate.put(rs.getString(1), rs.getInt(2));
            }

        } catch (SQLException e) {
            LOGGER.error("Statistics for all rooms failed.");
            throw new DAOException(e.getMessage());
        }


        return statisticsForAllRoomsFromDate;

    }

    @Override
    public HashMap<Date, Integer> statisticsForOneRoomFromDate(String category, Date from, Date to) throws DAOException {


        HashMap<Date,Integer> amountsForOneRoom = new HashMap<>();

        if(category == null || from == null || to == null) {
            LOGGER.error("Category or dates are null");
            throw new DAOException("Category or dates are null");
        }

        if(from.after(to)) {
            LOGGER.error("From date set after to date");
            throw new DAOException("From date set after to date");
        }

        try {
            PreparedStatement forOneStmt = connection.prepareStatement("SELECT from_date, SUM(DATEDIFF(day,from_date,until_date) + 1) " +
                    "AS Days FROM Room r, Reservation rs, Room_Reservation rr " +
                    "WHERE rs.rid = rr.rid AND rr.roomid = r.rnr AND room_category = ? " +
                    "AND rs.from_Date between ? and ? GROUP BY room_category, from_date;");

            forOneStmt.setString(1, category);
            forOneStmt.setDate(2, from);
            forOneStmt.setDate(3, to);

            ResultSet rs = forOneStmt.executeQuery();

            while(rs.next()) {
                amountsForOneRoom.put(rs.getDate(1),rs.getInt(2));
            }

            rs.close();

        }catch (SQLException e) {
            LOGGER.error("Statistics for one room failed.");
            throw new DAOException(e.getMessage());
        }

        return amountsForOneRoom;

    }

    @Override
    public HashMap<String,Integer> statisticsForAllServicesFromDate(Date from, Date to) throws DAOException {

        if(from == null || to == null) {
            LOGGER.error("Date is null");
            throw new DAOException("Date is null");
        }

        if(from.after(to)) {
            LOGGER.error("From date set after to date");
            throw new DAOException("From date set after to date");
        }

        HashMap<String, Integer> statisticsForAllServicesFromDate = new HashMap<>();

        try {
            PreparedStatement amountStmt = connection.prepareStatement("SELECT service_type, COUNT(service_type) AS Amount " +
                    "FROM service_reservation, service WHERE service_reservation.srid = service.srid AND " +
                    "service_reservation.on_date BETWEEN ? AND ? group by service_type;");

            amountStmt.setDate(1, from);
            amountStmt.setDate(2, to);

            ResultSet rs = amountStmt.executeQuery();

            while(rs.next()) {
                statisticsForAllServicesFromDate.put(rs.getString(1), rs.getInt(2));
            }

        } catch (SQLException e) {
            LOGGER.error("Statistics for all services failed.");
            throw new DAOException(e.getMessage());
        }

        return statisticsForAllServicesFromDate;

    }

    @Override
    public HashMap<String, Integer> statisticsForOneServiceFromDate(String serviceType, Date from, Date to) throws DAOException {


        HashMap<String,Integer> amountsForOneService = new HashMap<>();

        if(serviceType == null || from == null || to == null) {
            throw new DAOException();
        }

        if(from.after(to)) {
            LOGGER.error("From date set after to date");
            throw new DAOException("From date set after to date");
        }


        try {
            PreparedStatement forOneStmt = connection.prepareStatement("SELECT month(on_date) AS MONTH, " +
                    "COUNT(service_type) AS Amount " +
                    "FROM service_reservation, service " +
                    "WHERE service_reservation.srid = service.srid AND service_type = ? " +
                    "AND service_reservation.on_date  BETWEEN ? AND ? GROUP BY month(on_date)");

            forOneStmt.setString(1,serviceType);
            forOneStmt.setDate(2, from);
            forOneStmt.setDate(3, to);

            ResultSet rs = forOneStmt.executeQuery();

            while(rs.next()) {
                int monthNumber = rs.getInt(1);
                amountsForOneService.put(getMonthFromNumber(monthNumber),rs.getInt(2));
            }

            rs.close();

        }catch (SQLException e) {
            LOGGER.error("Statistics for one service failed.");
            throw new DAOException(e.getMessage());
        }

        return amountsForOneService;

    }

    private String getMonthFromNumber(int month) {

        String monthName;

        switch(month) {

            case 1 :
                monthName = "January";
                break;

            case 2 :
                monthName = "February";
                break;

            case 3 :
                monthName = "March";
                break;

            case 4 :
                monthName = "April";
                break;

            case 5 :
                monthName = "May";
                break;

            case 6 :
                monthName = "June";
                break;

            case 7 :
                monthName = "July";
                break;

            case 8 :
                monthName = "August";
                break;

            case 9 :
                monthName = "September";
                break;

            case 10 :
                monthName = "October";
                break;

            case 11 :
                monthName = "November";
                break;

            case 12 :
                monthName = "December";
                break;

            default :
                monthName = "Not a valid day";

        }
        return monthName;
    }
}
