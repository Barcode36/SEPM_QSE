package main.java.ac.at.tuwien.sepm.QSE15.dao;

import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;

import java.sql.*;

import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.RoomReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.ServiceReservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

/**
 * Created by Bajram Saiti on 11.05.17.
 */
@Repository
public class ReservationAbstract {

    @Autowired
    private JDBCSingletonConnection singleton;

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationAbstract.class);
    private Connection connection;


    @PostConstruct
    public void initReservationAbst() {

        try {
            connection = singleton.getConnection();
            LOGGER.info("Getting connection successfully");
        } catch (DAOException e) {
            LOGGER.error("Unable to get Connection",e);

        }
    }

    public void cancelReservation(Reservation reservation) throws DAOException {

        String s = "UPDATE Reservation SET is_canceled = true WHERE RID = " + reservation.getRid() + " ;";

        try {
            PreparedStatement cancelStmt = connection.prepareStatement(s);
            cancelStmt.executeUpdate();
            connection.commit();
            LOGGER.info("ReservationAbst with id:" + reservation.getRid() + " is canceled successfully.");
        } catch (SQLException e) {
            LOGGER.error("Unable to cancel ReservationAbst.");
            throw new DAOException(e.getMessage());
        }
    }

    public Reservation createReservation(Reservation reservation) throws DAOException {

        connection = JDBCSingletonConnection.reconnect();
        String s1 = "INSERT INTO Reservation(CustomerID, from_date, until_date, Total, is_paid) VALUES (?,?,?,?,?);";

        PreparedStatement ps;
        try {
            ps = connection.prepareStatement(s1, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,reservation.getCostumerId());
            ps.setDate(2,reservation.getFrom());
            ps.setDate(3,reservation.getUntil());
            ps.setLong(4,reservation.getTotal());
            ps.setBoolean(5,reservation.getPaid());
            ps.executeUpdate();
            ResultSet rs1 = ps.getGeneratedKeys();
            rs1.next();
            reservation.setRid(rs1.getInt(1));
            reservation.setCanceled(false);
            ps.close();
            connection.commit();
            LOGGER.info("Reservation '" + reservation.getRid() + "' is created successfully.");
            return reservation;

        } catch (SQLException e) {
            LOGGER.error("Unable to create reservation.");
            throw new DAOException(e.getMessage());
        }
    }

    public Reservation getReservatin(Integer rid,boolean room) throws DAOException{
        connection = JDBCSingletonConnection.reconnect();
        String s1 = "SELECT CustomerID, from_date, until_date, Total, is_paid, is_canceled from Reservation where rid = " + rid + " ;";

        try {
            ResultSet rs = connection.createStatement().executeQuery(s1);
            Reservation reservation;
            if (room){
                reservation = new RoomReservation();
            }
            else {
                reservation = new ServiceReservation();
            }
            if (rs.next()){
                reservation.setRid(rid);
                reservation.setCostumerId(rs.getInt(1));
                reservation.setFrom(rs.getDate(2));
                reservation.setUntil(rs.getDate(3));
                reservation.setTotal(rs.getLong(4));
                reservation.setPaid(rs.getBoolean(5));
                reservation.setCanceled(rs.getBoolean(6));
            }

            connection.commit();
            return reservation;

        } catch (SQLException e) {
            LOGGER.error("Unable to get reservation.");
            throw new DAOException(e.getMessage());
        }
    }
}
