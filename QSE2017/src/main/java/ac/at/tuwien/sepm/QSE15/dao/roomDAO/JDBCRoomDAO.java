package main.java.ac.at.tuwien.sepm.QSE15.dao.roomDAO;

import javafx.scene.control.Alert;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Category;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Lock;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Room;
import main.java.ac.at.tuwien.sepm.QSE15.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bajram Saiti on 01.05.17.
 */
@Repository
public class JDBCRoomDAO implements RoomDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCRoomDAO.class);
    private Connection connection;

    @Autowired
    private JDBCSingletonConnection jdbcSingletonConnection;

    @PostConstruct
    public void init () {

        try {
            connection = jdbcSingletonConnection.getConnection();
        } catch (DAOException e) {
            LOGGER.error("Unable to get connection.");
        }
    }


    //Luka Veljkovic
    @Override
    public Room create(Room room) throws DAOException {

        String createQuerry = "INSERT INTO ROOM VALUES(?, ?, ?)";
        LOGGER.info("Creating a new room.");

        if (room == null){

            LOGGER.error("Error while creating a new room because the room is null.");
            throw new DAOException();
        }

        else {

            try {
                PreparedStatement ps = connection.prepareStatement(createQuerry);
                ps.setInt(1, room.getRnr());
                ps.setString(2, room.getCategory());
                ps.setString(3, room.getExtras());
                ps.executeUpdate();
                connection.commit();


            } catch (SQLException e) {

                LOGGER.error("Room with that number already exists, please choose another one.");
                Utility.showAlert("Room with that number already exists, please choose another one.", Alert.AlertType.ERROR);
                throw new DAOException();
            }

            LOGGER.info("A room is created successfully.");
        }

        return room;
    }

    @Override
    public List<Room> search(Room r, Category c) throws DAOException {
        
        connection = JDBCSingletonConnection.reconnect();
        ArrayList<Room> list = new ArrayList<>();
        String s = "SELECT r.RNR, r.Room_category, c.price, r.Extras FROM ROOM r JOIN Category c on r.room_category = c.name WHERE r.Room_category = c.name";

        s += r.getRnr()!=null?" and r.rnr = " + r.getRnr() : "";

        s+= c.getBeds()!=null?" and c.beds = " + c.getBeds() : "";

        s+= r.getCategory()!=null? " and r.room_category = '" + r.getCategory() + "'" : "";

        s+= c.getName()!=null? " and r.room_category = '" + r.getCategory() + "'" : "";

        s+= c.getPrice()!=null? " and c.price <= " + c.getPrice(): "";

        s+= r.getExtras()!=null? " and r.extras  LIKE '%" + r.getExtras() +"%'" : "";

        s+= ";";


        try {
            ResultSet rs = connection.createStatement().executeQuery(s);
            while (rs.next()){
                list.add(parseToRoom(rs));
            }

            return list;

        } catch (SQLException e) {
            LOGGER.error("Error while searching room.");
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public List<Room> searchFreeRooms(Room r1, Category c, Date d1, Date d2) throws DAOException {

        connection = JDBCSingletonConnection.reconnect();
        List<Room> roomList = new ArrayList<Room>();
        String statement = "(SELECT ro.rnr, ro.room_category, c.price, ro.extras \n" +
                "FROM Reservation r JOIN Room_reservation rr ON r.rid = rr.rid JOIN Room ro ON ro.rnr = rr.roomID JOIN Category c ON ro.room_category = c.name\n" +
                "WHERE ro.rnr NOT IN (SELECT ro.rnr FROM Reservation r JOIN Room_reservation rr ON r.rid = rr.rid JOIN Room ro ON ro.rnr = rr.roomID\n" +
                "                     WHERE  NOT ((r.from_date < '" + d1 + "' AND r.until_date <= '"+ d1 + "' ) OR (r.from_date > '" + d2 + "' AND r.until_date >= '" + d2 + "' )))\n" +
                "      AND ro.rnr NOT IN (SELECT l.rnr\n" +
                "                         FROM Locked l\n" +
                "                         WHERE  NOT ((l.locked_from <= '" + d1 + "' AND l.locked_until <= '" + d1 + "') OR (l.locked_from >=  '" + d2 + "' AND l.locked_until >= '" + d2 + "'))) ";

        statement += r1.getRnr()!= null ?" AND ro.rnr = " + r1.getRnr() : "";

        statement += r1.getCategory()!= null ? " AND ro.room_category = '" + r1.getCategory() + "'" : "";

        statement += c.getName()!= null ? " AND ro.room_category = '" + c.getName() + "'" : "";

        statement += c.getPrice()!= null ? " AND c.price <= " + c.getPrice(): "";

        statement += " ) UNION \n" +
                "(SELECT ro.rnr, ro.room_category, c.price, ro.extras \n" +
                "FROM Room ro JOIN Category c ON ro.room_category = c.name \n" +
                "WHERE ro.rnr NOT IN (SELECT rr.roomID \n" +
                "FROM Reservation r JOIN Room_Reservation rr ON r.rid = rr.rid) AND \n" +
                "ro.rnr NOT IN (SELECT l.rnr \n" +
                "FROM Locked l \n" +
                "WHERE  NOT ((l.locked_from <= '" + d1 + "' AND l.locked_until <= '" + d1 + "') OR (l.locked_from >=  '" + d2 + "' AND l.locked_until >= '" + d2 + "')))";

        statement += r1.getRnr()!= null ?" AND ro.rnr = " + r1.getRnr() : "";

        statement += r1.getCategory()!= null ? " AND ro.room_category = '" + r1.getCategory() + "'" : "";

        statement += c.getName()!= null ? " AND ro.room_category = '" + c.getName() + "'" : "";

        statement += c.getPrice()!= null ? " AND c.price <= " + c.getPrice(): "";

        statement += ") ORDER BY rnr;";

        try {
            ResultSet rs = connection.createStatement().executeQuery(statement);
            while (rs.next()){
                roomList.add(new Room(rs.getInt(1), rs.getString(2), rs.getLong(3), rs.getString(4)));
            }

            return roomList;

        } catch (SQLException e) {
            LOGGER.error("Error while searching free rooms.");
            throw new DAOException(e.getMessage());
        }
    }



    @Override
    public void update(Room room) throws DAOException {

        if (room==null ||  get(room.getRnr())==null){
            throw new DAOException("Invalid Room");
        }
        try {

            PreparedStatement pstm = connection.prepareStatement("UPDATE Room SET room_category = ? , extras = ? WHERE rnr = ?;");
            pstm.setString(1, room.getCategory());
            pstm.setString(2,  room.getExtras());
            pstm.setInt(3, room.getRnr());
            pstm.executeUpdate();
            connection.commit();

            pstm.close();
            LOGGER.info("Room updated in database");

        } catch (SQLException e) {
            LOGGER.error("Unable to update Room");
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public void lock(Lock lock) throws DAOException {

        try {
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO Locked(RNR, Reason, Locked_from, Locked_until) VALUES (?, ?, ?, ?);");
            pstm.setInt(1, lock.getRnr());
            pstm.setString(2, lock.getReason());
            pstm.setDate(3, lock.getLocked_from());
            pstm.setDate(4, lock.getLocked_until());
            pstm.executeUpdate();
            connection.commit();

            pstm.close();
            LOGGER.info("Room successfully locked in database");

        } catch (SQLException e) {
            LOGGER.error("Unable to lock the room");
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public Room get(Integer rnr) throws DAOException {
        if (rnr==null || rnr<0){
            throw new DAOException("Invalid Rnr.");
        }
        connection = JDBCSingletonConnection.reconnect();
        Room room = null;
        String s1 = "SELECT  r.Room_category, c.price, r.Extras from room r join category c on r.room_category = c.name where r.rnr = " + rnr + ";";

        try {
            ResultSet rs = connection.createStatement().executeQuery(s1);
            if (rs.next()){
                room = new Room();
                room.setRnr(rnr);
                room.setCategory(rs.getString(1));
                room.setPrice(rs.getLong(2));
                room.setExtras(rs.getString(3));
            }

            connection.commit();
            return room;

        } catch (SQLException e) {
            LOGGER.error("Unable to get Room.");
            throw new DAOException(e.getMessage());
        }

    }

    @Override
    public List<Lock> getLocks(Integer rnr) throws DAOException {
        //LID INTEGER IDENTITY(4000,1) PRIMARY KEY RNR INTEGER, Reason VARCHAR(255),Locked_From DATE,Locked_Until DATE

        String stmt = "SELECT lid, rnr, reason, locked_from, locked_until from Locked where rnr = " + rnr + ";";
        List<Lock> locksList = new ArrayList<Lock>();
        try {
            ResultSet rs = connection.createStatement().executeQuery(stmt);

            while (rs.next()){
                locksList.add(new Lock(rs.getInt(1),rs.getInt(2),rs.getString(3), rs.getDate(4),rs.getDate(5)));
            }

            return locksList;

        } catch (SQLException e) {
            LOGGER.error("Error while geting Locks.");
            throw new DAOException(e.getMessage());
        }
    }

    public Room parseToRoom(ResultSet rs) throws DAOException{
        try {
            return new Room(rs.getInt("RNR"), rs.getString("room_category"), rs.getLong("Price"),rs.getString("Extras"));
        } catch (SQLException e) {
            LOGGER.error("Result set Error");
            throw new DAOException(e.getMessage());
        }
    }
}
