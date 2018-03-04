package main.java.ac.at.tuwien.sepm.QSE15.dao.customerDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.creditCardDAO.CreditCardDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.creditCardDAO.JDBCCreditCardDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.creditCard.CreditCard;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Customer;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.ColumnNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.CustomerNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.NoCustomerFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.HashMap;

/**
 * Created by Stefan Puhalo on 28.04.17.
 */
@Repository
public class JDBCCustomerDAO implements CustomerDAO {

    private Connection connection;

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCCustomerDAO.class);

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


    @Override
    public Customer create(Customer customer) throws DAOException {

        PreparedStatement createCustomerStmt;

        if(customer == null) {
            LOGGER.error("Create null customer is not possible.");
            throw new DAOException();
        }

        /**
         * If customer is just a Guest - not a main person of reservation
         */

        if(customer.getCreditCard() == null) {

            try {

                createCustomerStmt = connection.prepareStatement("INSERT INTO Customer (name,surname," +
                                "address,zip,place,country,phone,email,bdate,sex,identification,note,newsletter,rid)" +
                                " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                createCustomerStmt.setString(1, customer.getName());
                createCustomerStmt.setString(2, customer.getSurname());
                createCustomerStmt.setString(3, customer.getAddress());
                createCustomerStmt.setString(4, customer.getZip());
                createCustomerStmt.setString(5, customer.getPlace());
                createCustomerStmt.setString(6, customer.getCountry());
                createCustomerStmt.setString(7, customer.getPhone());
                createCustomerStmt.setString(8, customer.getEmail());
                createCustomerStmt.setDate(9, customer.getBdate());
                createCustomerStmt.setString(10, customer.getSex());
                createCustomerStmt.setString(11, customer.getIdentification());
                createCustomerStmt.setString(12, customer.getNote());
                createCustomerStmt.setBoolean(13, customer.getNewsletter());
                createCustomerStmt.setInt(14, customer.getRid());

            } catch (SQLException e) {
                LOGGER.error("Insert into table Customer failed.");
                throw new DAOException(e.getMessage());

            }
        }else {

            /**
             * If customer is the main person of reservation
             */

            try {

                createCustomerStmt = connection.prepareStatement("INSERT INTO Customer VALUES (null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                createCustomerStmt.setString(1, customer.getName());
                createCustomerStmt.setString(2, customer.getSurname());
                createCustomerStmt.setString(3, customer.getAddress());
                createCustomerStmt.setString(4, customer.getZip());
                createCustomerStmt.setString(5, customer.getPlace());
                createCustomerStmt.setString(6, customer.getCountry());
                createCustomerStmt.setString(7, customer.getPhone());
                createCustomerStmt.setString(8, customer.getEmail());
                createCustomerStmt.setDate(9, customer.getBdate());
                createCustomerStmt.setString(10, customer.getSex());
                createCustomerStmt.setString(11, customer.getIdentification());
                createCustomerStmt.setString(12, customer.getCreditCard().getCnr());
                createCustomerStmt.setString(13, customer.getNote());
                createCustomerStmt.setBoolean(14, customer.getNewsletter());
                createCustomerStmt.setInt(15, customer.getRid());



            }catch (SQLException e) {
                LOGGER.error("Insert into table Customer failed.");
                throw new DAOException(e.getMessage());
            }
        }

        try {

            createCustomerStmt.executeUpdate();

            ResultSet rs = createCustomerStmt.getGeneratedKeys();
            rs.next();
            int pid = rs.getInt(1);
            rs.close();
            customer.setPid(pid);

            connection.commit();
        } catch (SQLException e) {
            LOGGER.error("Insert into table Customer failed.");
            throw new DAOException(e.getMessage());
        }

        return customer;

    }

    @Override
    public ObservableList<Customer> findAll() throws DAOException, NoCustomerFoundException {

        ObservableList<Customer> customers = FXCollections.observableArrayList();

        try {

            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Customer;");

            if(rs == null) {
                LOGGER.error("No customers found.");
                throw new NoCustomerFoundException("No customers found.");
            }

            while(rs.next()) {
                customers.add(parseToCustomer(rs));

            }

        } catch (SQLException e) {
            LOGGER.error("Database unreachable.");
            throw new DAOException(e.getMessage());
        }

        return customers;
    }

    @Override
    public Customer find(Integer pid) throws DAOException, CustomerNotFoundException {

        Customer customer;

        if(pid == 0) {
            LOGGER.error("Find customer with pid = 0 is not possible.");
            throw new DAOException("Find customer with pid = 0 is not possible.");
        }

        try {

            PreparedStatement findStmt = connection.prepareStatement("SELECT * FROM Customer WHERE PID=?");

            findStmt.setInt(1,pid);

            ResultSet rs = findStmt.executeQuery();

            if(rs.next()){
                customer = parseToCustomer(rs);

            }else {
                throw new CustomerNotFoundException("No Customer with given PID found.");
            }

        } catch (SQLException e) {
            LOGGER.error("Find customer failed.");
            throw new DAOException(e.getMessage());
        }

        return customer;
    }

    @Override
    public void update(Customer customer) throws DAOException {

        if(customer == null) {
            LOGGER.error("The given customer to update(Customer) is a null pointer.");
            throw new DAOException("The given customer to update(Customer) is a null pointer.");
        }

        PreparedStatement updateStmt;

        try {

            updateStmt = connection.prepareStatement("UPDATE Customer SET name=?, surname=?, address=?," +
                    "zip=?, place=?, country=?, phone=?, email=?, bdate=?, sex=?, identification=?, credit_card=?, note=?," +
                    "newsletter=?, rid=? WHERE pid=?");

            updateStmt.setString(1, customer.getName());
            updateStmt.setString(2, customer.getSurname());
            updateStmt.setString(3, customer.getAddress());
            updateStmt.setString(4, customer.getZip());
            updateStmt.setString(5, customer.getPlace());
            updateStmt.setString(6, customer.getCountry());
            updateStmt.setString(7, customer.getPhone());
            updateStmt.setString(8, customer.getEmail());
            updateStmt.setDate(9, customer.getBdate());
            updateStmt.setString(10, customer.getSex());
            updateStmt.setString(11, customer.getIdentification());
            updateStmt.setString(12, customer.getCreditCard().getCnr());
            updateStmt.setString(13, customer.getNote());
            updateStmt.setBoolean(14, customer.getNewsletter());
            updateStmt.setInt(15, customer.getRid());
            updateStmt.setInt(16, customer.getPid());

            updateStmt.executeUpdate();
            connection.commit();

        }catch (SQLException e) {
            LOGGER.error("Update customer failed.");
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public void delete(Customer customer) throws DAOException {

        if(customer == null) {
            throw new DAOException("The given customer is a null Pointer.");
        }else if (customer.getPid() < 0){
            throw  new DAOException("The given customer has a negative PID.");
        }else {

            try {
                PreparedStatement deleteStmt = connection.prepareStatement("DELETE FROM Customer WHERE pid = ?");

                deleteStmt.setInt(1, customer.getPid());

                if (deleteStmt.executeUpdate() == 0) {
                    LOGGER.error("Delete customer failed because the customer is not found.");
                    throw new DAOException("No such customer.");

                }

                connection.commit();

            } catch (SQLException e) {
                LOGGER.error("Delete customer failed.");
                throw new DAOException("Can't delete the customer.");
            }
        }

    }

    @Override
    public ObservableList<Customer> findAllFromTo(String column, Object from, Object to) throws DAOException, ColumnNotFoundException {

        if(column == null || from == null || to == null) {
            LOGGER.error("Find all customers from to failed because some of parameters is null.");
            throw new DAOException("Find all customers from to failed because some of parameters is null.");
        }


        ObservableList<Customer> customers = FXCollections.observableArrayList();

        column = column.toUpperCase();

        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet metaDataRS = metaData.getColumns(null,null,"CUSTOMER", column);
            if(metaDataRS.next()) {
                ResultSet rs = connection.createStatement().executeQuery(
                        "SELECT * FROM Customer WHERE " + column + " BETWEEN '" + from + "' AND '" + to + "'");
                while(rs.next()) {
                    customers.add(parseToCustomer(rs));
                }
            } else {
                LOGGER.error("Wrong column name given.");
                throw new ColumnNotFoundException();
            }

        }catch (SQLException e) {
            LOGGER.error("Generic query could not be executed.");
            throw new DAOException(e.getMessage());
        }

        return customers;

    }

    @Override
    public ObservableList<Customer> findAllFromToForType(String column, String type, Object from, Object to) throws DAOException, ColumnNotFoundException {

        if(column == null || type == null || from == null || to == null) {
            LOGGER.error("Find all customers from to for type failed because some of parameters is null.");
            throw new DAOException("Find all customers from to for type failed because some of parameters is null.");
        }

        if(type != "Customer" && type != "Guest") {
            LOGGER.error("Find all customers for type to failed because type is invalid.");
            throw new DAOException("Find all customers for type failed because type is invalid.");
        }


        ObservableList<Customer> customers = FXCollections.observableArrayList();

        column = column.toUpperCase();

        String isOrIsNot;

        if(type.equals("Customer")) {
            isOrIsNot = "=";
        }else {
            isOrIsNot = "!=";
        }
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet metaDataRS = metaData.getColumns(null,null,"CUSTOMER",column);
            if(metaDataRS.next()) {
                ResultSet rs = connection.createStatement().executeQuery(
                        "SELECT * FROM Customer WHERE RID" + isOrIsNot + "0" + " AND " + column + " BETWEEN '" + from + "' AND '" + to + "'");
                while(rs.next()) {
                    customers.add(parseToCustomer(rs));
                }
            } else {
                LOGGER.error("Wrong column name given.");
                throw new ColumnNotFoundException();
            }

        }catch (SQLException e) {
            LOGGER.error("Generic query could not be executed.");
            throw new DAOException(e.getMessage());
        }

        return customers;

    }

    @Override
    public ObservableList<Customer> findAllForType(String type) throws DAOException, NoCustomerFoundException {

        if(type == null) {
            LOGGER.error("Find all customers for type to failed because type is null.");
            throw new DAOException("Find all customers for type failed because type is null.");
        }

        if(type != "Customer" && type != "Guest") {
            LOGGER.error("Find all customers for type to failed because type is invalid.");
            throw new DAOException("Find all customers for type failed because type is invalid.");
        }

        ObservableList<Customer> customers = FXCollections.observableArrayList();

        String isOrIsNot;

        if(type.equals("Customer")) {
            isOrIsNot = "=";
        }else {
            isOrIsNot = "!=";
        }

        try {

            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Customer WHERE RID" + isOrIsNot + "0");
            /**
             * Check if result set is null
             */
            if(rs == null){
                LOGGER.info("No customer found.");
                throw  new NoCustomerFoundException("There are no customers with such type in the database.");
            }

            while(rs.next()) {
                customers.add(parseToCustomer(rs));
            }

        } catch (SQLException e) {
            LOGGER.error("Find all customers of type failed.");
            throw new DAOException(e.getMessage());
        }

        return customers;
    }

    @Override
    public Customer checkCustomer(String name, String surname, Date dateOfBirth) throws CustomerNotFoundException {
        Customer customer = null;

        try {
            String statement = "SELECT * FROM Customer WHERE NAME = '" + name + "' AND SURNAME = '"+ surname +"' AND BDATE = '" + dateOfBirth + "';";
            PreparedStatement findStmt = connection.prepareStatement(statement);
            ResultSet rs = findStmt.executeQuery();

            if(rs.next()){
                customer = parseToCustomer(rs);
                return customer;
            }

        } catch (SQLException e) {
            LOGGER.error("Find customer failed");
        }
        return customer;
    }

    @Override
    public HashMap<String, Integer> reservationsOfAllCustomers() throws DAOException {

        HashMap<String, Integer> reservationIDs = new HashMap<>();

        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT Reservation.rid, Customer.name, " +
                    "Customer.surname FROM Reservation, Customer WHERE customer.pid = Reservation.customerid;");

            while(rs.next()) {

                Integer rid = rs.getInt(1);
                String fullname = rs.getString(2) + " " + rs.getString(3);
                reservationIDs.put(fullname,rid);
            }
        } catch (SQLException e) {
            LOGGER.error("Retrieving reservations of all customers failed.");
            throw new DAOException(e.getMessage());
        }

        return reservationIDs;

    }


    private Customer parseToCustomer(ResultSet rs) throws SQLException {

        Integer pid = rs.getInt(1);
        String name = rs.getString(2);
        String surname = rs.getString(3);
        String address = rs.getString(4);
        String zip = rs.getString(5);
        String place = rs.getString(6);
        String country = rs.getString(7);
        String phone = rs.getString(8);
        String email = rs.getString(9);
        Date bdate = rs.getDate(10);
        String sex = rs.getString(11);
        String identification = rs.getString(12);
        CreditCard creditCard = new CreditCard(rs.getString(13));
        String note = rs.getString(14);
        Boolean newsletter = rs.getBoolean(15);
        Integer rid = rs.getInt(16);

        Customer customer = new Customer(pid,name,surname,address,zip,place,country,phone,email,bdate,sex,
                identification,creditCard,note,newsletter,rid);

        return customer;


    }
}
