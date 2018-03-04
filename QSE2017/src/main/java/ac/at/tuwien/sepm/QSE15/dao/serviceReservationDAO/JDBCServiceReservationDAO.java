package main.java.ac.at.tuwien.sepm.QSE15.dao.serviceReservationDAO;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.ReservationAbstract;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.dao.roomReservationDAO.JDBCRoomReservationDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.serviceDAO.JDBCServiceDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.serviceDAO.ServiceDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.ServiceReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Room;
import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;
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
public class JDBCServiceReservationDAO extends ReservationAbstract implements ServiceReservationDAO {

    @Autowired
    private JDBCSingletonConnection singleton;

    @Autowired
    private JDBCRoomReservationDAO roomReservationDAO;

    @Autowired
    private JDBCServiceDAO serviceDAO;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(JDBCServiceReservationDAO.class);
    private Connection connection;

    @PostConstruct
    public void initServiceReservation() {

        try {
            connection = singleton.getConnection();
            LOGGER.info("Getting connection successfully");
        } catch (DAOException e) {
            LOGGER.error("Unable to get Connection");

        }
    }

    @Override
    public ServiceReservation create(ServiceReservation reservation) throws DAOException {
        connection = JDBCSingletonConnection.reconnect();

        String s = "INSERT INTO Service_reservation(SRID, RID, on_date) VALUES (?,?,?)";
        String updateTotal = "UPDATE Reservation SET total =";
        Service service = serviceDAO.get(reservation.getSrid());
        Long total = 0L;
        try {

            if (reservation.getRid()==null){

                reservation.setTotal(0L);
              //  long difference = ChronoUnit.DAYS.between(reservation.getFrom().toLocalDate(), reservation.getUntil().toLocalDate());
              //  reservation.setTotal(service.getPrice());
                reservation.setRid(createReservation(reservation).getRid());
            }


            else {
                String getTotal =  "SELECT total from Reservation WHERE rid = " + reservation.getRid() + ";";
                ResultSet rs = connection.createStatement().executeQuery(getTotal);

                while (rs.next()){
                    total = rs.getLong(1);
                }

            }

            total+=service.getPrice();

            if (total!=-1){
                reservation.setTotal(total);
                updateTotal += total + " where rid = " + reservation.getRid() + ";";

                PreparedStatement ps2 = connection.prepareStatement(s,Statement.RETURN_GENERATED_KEYS);
                ps2.setInt(1,reservation.getSrid().getSrid());
                ps2.setInt(2,reservation.getRid());
                ps2.setDate(3,reservation.getDate());
                ps2.executeUpdate();
                ps2.close();
                PreparedStatement ps3 = connection.prepareStatement(updateTotal);
                ps3.executeUpdate();
                connection.commit();
                LOGGER.info("ServiceReservation '" + reservation.getSrid() + "' is created successfully.");
                return reservation;
            }

            else {
                throw  new DAOException("Unable to get total.");
            }

        } catch (SQLException e) {
            LOGGER.error("Service ServiceReservationDAO - unable to create");
            throw new DAOException(e.getMessage());
        }

    }


    @Override
    public void update(ServiceReservation reservation) throws DAOException {

    }

    @Override
    public List<ServiceReservation> search(ServiceReservation r) throws DAOException {

        ArrayList<ServiceReservation> list = new ArrayList<>();

        String statement = "SELECT r.rid, r.customerID, r.from_date, r.until_date, r.total, r.is_paid, r.is_Canceled, sr.srid, sr.on_date\n" +
                "FROM Reservation r JOIN Service_reservation sr ON r.rid = sr.rid\n" +
                "WHERE r.rid = sr.rid ";

        statement += r.getRid() != null ? " AND r.rid = " + r.getRid() : "";

        statement += r.getSrid() != null ? " AND sr.srid = " + r.getSrid().getSrid() : "";

        statement += r.getDate() != null ? " AND sr.on_date = '" + r.getDate() + "'" : "";

        statement += r.getCostumerId() != null ? " AND r.customerID = " + r.getCostumerId() : "";

        statement += r.getCanceled() != null ? " AND r.is_Canceled = " + r.getCanceled() : "";

        statement += r.getTotal() != null ? " AND r.total <= " + r.getTotal() : "";

        statement += r.getPaid() != null ? " AND r.is_paid = " + r.getPaid() : "";

        statement += ((r.getFrom() != null) && (r.getUntil() != null)) ? " AND r.from_date <= '" + r.getUntil() + "' AND r.until_date >= '" + r.getFrom() + "'" : "";

        statement += " ;";

        try {
            ResultSet rs = connection.createStatement().executeQuery(statement);

            while (rs.next()){
                list.add(new ServiceReservation(rs.getInt(1), rs.getInt(2), rs.getDate(3), rs.getDate(4), rs.getLong(5), rs.getBoolean(6), rs.getBoolean(7), new Service(rs.getInt(8), null, null, null), rs.getDate(9)));
            }

            LOGGER.info("Successfully searched Service Reservations.");
            return list;

        } catch (SQLException e) {
            LOGGER.error("Unable to search Service Reservations.");
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public ServiceReservation get(ServiceReservation res) throws DAOException {

        ServiceReservation reservation = null;
        connection = JDBCSingletonConnection.reconnect();
        Service s = serviceDAO.get(res.getSrid());

        String s1 = "SELECT r.CustomerID, r.from_date, r.until_date, r.Total, r.is_paid, r.is_canceled, s.on_date from Service_reservation s JOIN Reservation r on s.rid = r.rid where s.srid = " + res.getSrid().getSrid() + " and s.rid = " + res.getRid() + ";";

        try {
            ResultSet rs = connection.createStatement().executeQuery(s1);
            if(rs.next()){
                reservation = new ServiceReservation();
                reservation.setDate(rs.getDate(1));
                reservation.setSrid(s);
            }



            return reservation;

        } catch (SQLException e) {

            LOGGER.error("Unable to get reservation.");
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public void cancel(Reservation reservation) throws DAOException{

        try {
            cancelReservation(reservation);
        //    get((ServiceReservation) reservation);
        }catch (Exception e){

            throw new DAOException(e.getMessage());
        }

    }
}
