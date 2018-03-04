package main.java.ac.at.tuwien.sepm.QSE15.dao.roomReservationDAO;


import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.ReservationAbstract;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.dao.roomDAO.JDBCRoomDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.roomDAO.RoomDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.RoomReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bajram Saiti on 28.04.17.
 */
@Repository
public class JDBCRoomReservationDAO extends ReservationAbstract implements RoomReservationDAO {

    @Autowired
    private JDBCSingletonConnection singleton;

    @Autowired
            private JDBCRoomDAO roomDAO;
    Connection conn;
    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCRoomReservationDAO.class);

    @PostConstruct
    public void initRoomReservation() {

        try {
            conn = singleton.getConnection();
            LOGGER.info("Getting connection successfully");
        } catch (DAOException e) {
            LOGGER.error("Unable to get Connection");

        }
    }

    @Override
    public RoomReservation create(RoomReservation reservation) throws DAOException {

        conn = JDBCSingletonConnection.reconnect();

        String s2 = "INSERT INTO Room_reservation(RID, RoomID, Room_Price, Breakfast) VALUES (?,?,?,?);";

        if (checkIfTimeIsValid(reservation)) {

            PreparedStatement ps2;

            try {

                long difference = ChronoUnit.DAYS.between(reservation.getFrom().toLocalDate(), reservation.getUntil().toLocalDate());
                Room room = roomDAO.get(reservation.getRoomId());
                reservation.setTotal((room.getPrice() * difference) + (reservation.getBreakfast() ? (difference * 500) : 0));

                Reservation r = createReservation(reservation);
                reservation.setRid(r.getRid());

                ps2 = conn.prepareStatement(s2, Statement.RETURN_GENERATED_KEYS);
                ps2.setInt(1, reservation.getRid());
                ps2.setInt(2, reservation.getRoomId());
                ps2.setLong(3, room.getPrice());
                ps2.setBoolean(4, reservation.getBreakfast());

                ps2.executeUpdate();
                ps2.close();

                conn.commit();
                LOGGER.info("Reservation '" + reservation.getRid() + "' is created successfully.");
                return reservation;

            } catch (SQLException e) {
                LOGGER.error("Unable to create reservation.");
                throw new DAOException(e.getMessage());
            }
        } else {
            LOGGER.error("This room is already booked in that period");
            throw new DAOException();
        }
    }

    @Override
    public void update(RoomReservation reservation) throws DAOException {

        // TODO: check if its allowed to update now.

        conn = JDBCSingletonConnection.reconnect();

        String statement1 = "UPDATE Reservation SET";
        int l1 = statement1.length();
        String statement2 = "UPDATE Room_Reservation SET";
        int l2 = statement2.length();

        Long newTotal;
        long difference = ChronoUnit.DAYS.between(reservation.getFrom().toLocalDate(), reservation.getUntil().toLocalDate());
        Room room = roomDAO.get(reservation.getRoomId());
        newTotal = room.getPrice() * difference;

        statement1 += (reservation.getFrom() != null && reservation.getUntil() != null) ? " from_date = '" + reservation.getFrom() + "' , until_date = '" + reservation.getUntil() + "' , total = " + newTotal + " ," : "";
        statement1 += reservation.getPaid() != null ? " is_Paid = " + reservation.getPaid() : "";
        statement1 = statement1.charAt(statement1.length() - 1) == ',' ?  statement1.substring(0,statement1.length()-2) : statement1;
        statement1 += " WHERE RID = " + reservation.getRid() + ";";

        statement2 += reservation.getRoomId() != null ? " roomID = " + reservation.getRoomId() + " ," : "";
        statement2 += reservation.getBreakfast() != null ? " breakfast = " + reservation.getBreakfast() : "";
        statement2 = statement2.charAt(statement2.length() - 1) == ',' ?  statement2.substring(0,statement2.length()-2) : statement2;
        statement2 += " WHERE RID = " + reservation.getRid() + ";";

        try {
            if (statement1.length() > l1) {
                conn.prepareStatement(statement1).executeUpdate();
                conn.commit();
            }

            if (statement2.length() > l2){
                conn.prepareStatement(statement2).executeUpdate();
                conn.commit();
            }

        } catch (SQLException e) {
            LOGGER.error("Unable to update reservation.");
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public List<RoomReservation> search(RoomReservation r) throws DAOException {

        conn = JDBCSingletonConnection.reconnect();
        ArrayList<RoomReservation> list = new ArrayList<>();

        String statement = "SELECT r.rid, r.customerID, r.from_date, r.until_date, r.total, r.is_paid, r.is_Canceled, rr.roomID, rr.room_price, rr.breakfast\n" +
                "FROM Reservation r JOIN Room_reservation rr ON r.rid = rr.rid\n" +
                "WHERE r.rid = rr.rid ";

        statement += r.getRid() != null ? " AND r.rid = " + r.getRid() : "";

        statement += r.getCostumerId() != null ? " AND r.customerID = " + r.getCostumerId() : "";

        statement += r.getPaid() != null ? " AND r.is_paid = " + r.getPaid() : "";

        statement += r.getCanceled() != null ? " AND r.is_canceled = " + r.getCanceled() : "";

        statement += r.getRoomId() != null ? " AND rr.roomID = " + r.getRoomId() : "";

        statement += r.getTotal() != null ? " AND r.total <= " + r.getTotal() : "";

        statement += r.getBreakfast() != null ? " AND rr.breakfast = " + r.getBreakfast() : "";

        statement += ((r.getFrom() != null) && (r.getUntil() != null)) ? " AND r.from_date <= '" + r.getUntil() + "' AND r.until_date >= '" + r.getFrom() + "'" : "";

        statement += " ;";


        try {

            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(statement);

            while (rs.next()){
                list.add(new RoomReservation(rs.getInt(1), rs.getInt(2), rs.getDate(3), rs.getDate(4), rs.getLong(5), rs.getBoolean(6), rs.getBoolean(7), rs.getInt(8), rs.getLong(9), rs.getBoolean(10)));
            }

            LOGGER.info("Successfully searched reservations.");
            return list;

        } catch (SQLException e) {
            LOGGER.error("Unable to search reservations.");
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public RoomReservation get(Integer rid) throws DAOException {

        if (rid == null){
            throw new DAOException();
        }

        conn = JDBCSingletonConnection.reconnect();
       // RoomReservation reservation = (RoomReservation) getReservatin(rid,true);
        RoomReservation reservation = null;
        conn = JDBCSingletonConnection.reconnect();


        String s1 = "SELECT r.CustomerID, r.from_date, r.until_date, r.Total, r.is_paid, r.is_canceled, ro.roomID, ro.Room_Price, ro.breakfast from room_reservation ro JOIN Reservation r on r.rid = ro.rid where ro.rid = " + rid + ";";

        try {
            ResultSet rs = conn.createStatement().executeQuery(s1);
            if ( rs.next()){
                reservation = new RoomReservation();
                reservation.setRid(rid);
                reservation.setCostumerId(rs.getInt(1));
                reservation.setFrom(rs.getDate(2));
                reservation.setUntil(rs.getDate(3));
                reservation.setTotal(rs.getLong(4));
                reservation.setPaid(rs.getBoolean(5));
                reservation.setCanceled(rs.getBoolean(6));
                reservation.setRoomId(rs.getInt(7));
                reservation.setRoomPrice(rs.getLong(8));
                reservation.setBreakfast(rs.getBoolean(9));
            }

            return reservation;

        } catch (SQLException e) {
            LOGGER.error("Unable to get reservation.");
            throw new DAOException(e.getMessage());
        }

    }

    @Override
    public void cancel(Reservation reservation) throws DAOException {
        conn = JDBCSingletonConnection.reconnect();
        cancelReservation(reservation);
    }

    @Override
    public List<RoomReservation> searchReservationsOnSpecifiedDate(RoomReservation reservation) throws DAOException {

        List<RoomReservation> list = new ArrayList<>();

        try {

            Statement stm = conn.createStatement();

            ResultSet rs = stm.executeQuery("SELECT r.rid, r.customerID, r.from_date, r.until_date, r.total, r.is_paid, r.is_canceled, rr.roomID, rr.room_price, rr.breakfast\n" +
                    "FROM Reservation r JOIN Room_reservation rr ON r.rid = rr.rid\n" +
                    "WHERE r.from_date <= '" + reservation.getFrom() + "' AND r.until_date >= '" + reservation.getUntil() + "' ;");

            while (rs.next()) {
                list.add(new RoomReservation(rs.getInt(1), rs.getInt(2), rs.getDate(3), rs.getDate(4), rs.getLong(5), rs.getBoolean(6), rs.getBoolean(7), rs.getInt(8), rs.getLong(9), rs.getBoolean(10)));
            }

            LOGGER.info("Successfully searched reservations on specified date.");
            return list;

        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    public boolean checkIfTimeIsValid(RoomReservation reservation)throws DAOException{

        try {
            PreparedStatement pstm = conn.prepareStatement("SELECT rr.rid\n" +
                    "FROM Reservation r JOIN Room_reservation rr ON r.rid = rr.rid\n" +
                    "WHERE NOT ((r.from_date < '" + reservation.getFrom() + "' AND r.until_date < '" + reservation.getFrom() + "' ) OR (r.from_date > '" + reservation.getUntil() + "' AND r.until_date > '" + reservation.getUntil() + "' )) AND rr.roomID = " + reservation.getRoomId() + ";");

            ResultSet rs = pstm.executeQuery();
            if (!rs.next()){
                return true;
            }
            return false;

        } catch (SQLException e) {
            LOGGER.error("Checking if time valid failed.");
            throw new DAOException(e.getMessage());
        }
    }
}
