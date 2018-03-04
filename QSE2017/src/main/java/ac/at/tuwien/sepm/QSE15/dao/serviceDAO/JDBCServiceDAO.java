package main.java.ac.at.tuwien.sepm.QSE15.dao.serviceDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.dao.customerDAO.JDBCCustomerDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.RoomReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bajram Saiti on 02.05.17.
 * Editet by Nemanja Vukoje on 06.05.17.
 */
@Repository
public class JDBCServiceDAO implements ServiceDAO {

    private static final Logger logger = LoggerFactory.getLogger(JDBCServiceDAO.class);

    private Connection connection;
    private String createQuerry;
    private String getAllServicesQuerry;
    private String deleteServiceQuerry;

    @Autowired
    private JDBCSingletonConnection jdbcSingletonConnection;

    @PostConstruct
    public void init () {

        try {
            connection = jdbcSingletonConnection.getConnection();
        } catch (DAOException e) {
            logger.error("Unable to get connection.");
        }
    }



    @Override
    public Service create(Service service) throws DAOException {
        logger.info("Creating service");
        createQuerry = "INSERT INTO SERVICE(SERVICE_TYPE,DESCRIPTION,PRICE) VALUES(?,?,?)";
        if (service==null){
            logger.error("Wrong service input!");
            throw new DAOException("Wrong input"); // not sure about this, if not ture, please delete
        }
        try {

            PreparedStatement ps = connection.prepareStatement(createQuerry);
            ps.setString(1,service.getType());
            ps.setString(2,service.getDescription());
            ps.setLong(3,service.getPrice()) ;
            ps.executeUpdate( );
            connection.commit();

        } catch (SQLException e) {
            logger.error("Error in create Service!");
            throw new DAOException(e.getMessage());
        }
        logger.info("Create Successful!");
        return service;
    }


    @Override
    public List<Service> getAllServicesFromReservation(Reservation reservation) throws DAOException {
        logger.info("Getting all Services");
        
        if (reservation==null){
            logger.error("Wrong reservation input!");
            throw new DAOException("Wrong input"); // not sure about this, if not ture, please delete
        }

        getAllServicesQuerry = "select s.srid, s.SERVICE_TYPE , s.DESCRIPTION , s.PRICE from SERVICE s join SERVICE_RESERVATION sr on s.srid = sr.srid  where sr.rid = " + reservation.getRid() + ";";
        if (reservation.getRid()==-1){
            getAllServicesQuerry ="select srid, SERVICE_TYPE , DESCRIPTION , PRICE from SERVICE;";
        }

        List<Service> list = new ArrayList<>();

        try {

            ResultSet rs = connection.createStatement().executeQuery(getAllServicesQuerry);
            connection.commit();
            while (rs.next()){
                Service service = new Service();
                service.setSrid(rs.getInt(1));
                service.setType(rs.getString(2));
                service.setDescription(rs.getString(3));
                service.setPrice(rs.getLong(4));

                list.add(service);

            }
        } catch (SQLException e) {
            logger.error("Error in getAll Service!");
            throw new DAOException(e.getMessage());
        }
        logger.info("All services successfully gathered!");
        return list;
    }

    @Override
    public Service delete(Service service) throws DAOException {
        logger.info("Deleting Service");
        deleteServiceQuerry= "UPDATE SERVICE SET isDeleted=TRUE WHERE srid=? ";
        try {
            PreparedStatement ps = connection.prepareStatement(deleteServiceQuerry);
            ps.setInt(1,service.getSrid());
            ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.error("Error in delete Service!");
            throw  new DAOException(e.getMessage());
        }
        logger.info("Delete successful!");
        return service;
    }

    @Override
    public Service get(Service service) throws DAOException {

        connection = JDBCSingletonConnection.reconnect();
        Service s = null;
        String s1 = "SELECT service_type, description, price from service where srid = " + service.getSrid() + ";";
        try {
            ResultSet rs = connection.createStatement().executeQuery(s1);
            if (rs.next()){
                service.setType(rs.getString(1));
                service.setDescription(rs.getString(2));
                service.setPrice(rs.getLong(3));
                s = service;
            }

            return s;

        } catch (SQLException e) {
            logger.error("Unable to get Service (Get Method).");
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public void update(Service service) throws DAOException {

        try {

          //  Service ss = get(service);

            PreparedStatement pstm = connection.prepareStatement("UPDATE Service SET description = ? , price = ? WHERE srid = ?;");
            pstm.setString(1, service.getDescription());
            pstm.setLong(2,  service.getPrice());
            pstm.setInt(3, service.getSrid());
            pstm.executeUpdate();
            connection.commit();

            pstm.close();
            logger.info("Service updated in database");

        } catch (SQLException e) {
            logger.error("Unable to update Service");
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public List<Service> search(Service s) throws DAOException {

        String stmt ="select s.srid, s.SERVICE_TYPE , s.DESCRIPTION , s.PRICE from SERVICE s, Service ser WHERE s.srid = ser.srid";
        stmt += s.getSrid()!=null?" and s.srid = " + s.getSrid() : "";
        stmt += s.getDescription()!=null?" and s.description LIKE '%" + s.getDescription() + "%'" : "";
        stmt += s.getType()!=null?" and s.service_type = '" + s.getType()  + "'": "";
        stmt += s.getPrice()!=null?" and s.price <= " + s.getPrice() : "";
        stmt += ";";
        List<Service> list = new ArrayList<>();


        try {

            ResultSet rs = connection.createStatement().executeQuery(stmt);
            connection.commit();

            while (rs.next()){

                Service service = new Service();
                service.setSrid(rs.getInt(1));
                service.setType(rs.getString(2));
                service.setDescription(rs.getString(3));
                service.setPrice(rs.getLong(4));

                list.add(service);

         }
        } catch (SQLException e) {
            logger.error("Error in search Service!");
            throw new DAOException(e.getMessage());
        }
        logger.info("Search services successfully gathered!");
        return list;

    }


    /**
     * Created by Stefan Puhalo on 12.06.2017
     * @return
     */
    @Override
    public ObservableList<String> getAllServiceTypes() {

        ObservableList<String> types = FXCollections.observableArrayList();

        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT DISTINCT Service_Type FROM SERVICE ");
            while(rs.next()) {
                types.add(rs.getString(1));
            }
        } catch (SQLException e) {
            logger.error("Retrieving service types failed");
        }

        return types;

    }
}
