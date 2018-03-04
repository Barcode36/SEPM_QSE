package main.java.ac.at.tuwien.sepm.QSE15.dao.employeeDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.ColumnNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.EmployeeNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.NoEmployeesFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.*;

/**
 * Created by Stefan Puhalo on 25.04.17.
 * Edited by Ervin Cosic
 */

@Repository
public class JDBCEmployeeDAO implements EmployeeDAO {

    @Autowired
    private JDBCSingletonConnection jdbcSingletonConnection;

    private Connection connection;

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCEmployeeDAO.class);

    @PostConstruct
    private void initEmployee() {
        try {
            connection = jdbcSingletonConnection.getConnection();
        } catch (DAOException e) {
            LOGGER.error("Initialisation of Employee failed");
        }
    }

    @Override
    public Employee create(Employee employee) throws DAOException {

        if(employee == null){
            LOGGER.error("The given employee to create(Employee) is a null pointer.");
            throw new DAOException("The given employee to create(Employee) is a null pointer.");
        }

        try {
            PreparedStatement createEmployeeStatement = connection.prepareStatement("INSERT INTO Employee VALUES " +
                    "(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",Statement.RETURN_GENERATED_KEYS );

            createEmployeeStatement.setString(1, employee.getName());
            createEmployeeStatement.setString(2, employee.getSurname());
            createEmployeeStatement.setString(3, employee.getAddress());
            createEmployeeStatement.setString(4, employee.getZip());
            createEmployeeStatement.setString(5, employee.getPlace());
            createEmployeeStatement.setString(6, employee.getCountry());
            createEmployeeStatement.setString(7, employee.getPhone());
            createEmployeeStatement.setString(8, employee.getEmail());
            createEmployeeStatement.setDate(9, employee.getBdate());
            createEmployeeStatement.setString(10, employee.getSex());
            createEmployeeStatement.setString(11, employee.getSvnr());
            createEmployeeStatement.setString(12, employee.getIban());
            createEmployeeStatement.setString(13, employee.getBic());
            createEmployeeStatement.setLong(14, employee.getSalary());
            createEmployeeStatement.setString(15, employee.getRole());
            createEmployeeStatement.setString(16, employee.getPicture());
            createEmployeeStatement.setBoolean(17, false);
            createEmployeeStatement.setString(18, employee.getUsername());
            createEmployeeStatement.setString(19, employee.getPassword());
            createEmployeeStatement.setInt(20, employee.getRights());

            createEmployeeStatement.executeUpdate();

            ResultSet rs = createEmployeeStatement.getGeneratedKeys();
            rs.next();
            int pid = rs.getInt(1);
            rs.close();

            employee.setPid(pid);
            employee.setDeleted(false);
            connection.commit();

        } catch (SQLException e) {

            LOGGER.error(e.getMessage());
            throw new DAOException("Create employee failed.");
        }

        LOGGER.info("Employee persisted.");
        return employee;

    }

    @Override
    public ObservableList<Employee> findAll() throws DAOException, NoEmployeesFoundException {

        ObservableList<Employee> employees = FXCollections.observableArrayList();

        try {

            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM employee WHERE is_deleted = FALSE;");
            /**
             * Check if result set is null
             */
            if(rs == null){
                LOGGER.info("No employees found.");
                throw new NoEmployeesFoundException("There are no employees in the database.");
            }

            while(rs.next()) {
                employees.add(parseToEmployee(rs));
            }

        } catch (SQLException e) {
            LOGGER.error("Find all employees failed.");
            throw new DAOException(e.getMessage());
        }

        return employees;
    }

    @Override
    public ObservableList<String> findAllPositions() throws DAOException {
        ObservableList<String> positions = FXCollections.observableArrayList();

        try {

            ResultSet rs = connection.createStatement().executeQuery("SELECT DISTINCT ROLLE FROM Employee;");

            while(rs.next()) {
                positions.add(rs.getString(1));
            }
        }catch (SQLException e) {
            LOGGER.error("Find all positions failed");
            throw new DAOException(e.getMessage());

        }

        return positions;
    }

    @Override
    public Employee find(Integer pid) throws DAOException, EmployeeNotFoundException {

        if(pid < 0) {
            throw new DAOException("The PID can't be a negative integer.");
        }

        Employee employee = null;

        try {

            PreparedStatement findStmt = connection.prepareStatement("SELECT * FROM employee WHERE is_Deleted = FALSE and pid = ?;");

            findStmt.setInt(1, pid);

            ResultSet rs = findStmt.executeQuery();

            /**
             * Check if an employee was found
             */
            if(rs == null){
                throw new EmployeeNotFoundException("No Employee found with given PID.");
            }

            while(rs.next()) {
                employee = parseToEmployee(rs);
            }

            rs.close();

        } catch (SQLException e) {
            LOGGER.error("Find employee failed");
            throw new DAOException("Find employee failed");
        }

        return employee;
    }

    @Override
    public ObservableList<Employee> findAllFromTo(String column, Object from, Object to) throws DAOException, ColumnNotFoundException {


        if(column == null || from == null || to == null) {
            LOGGER.error("Find all employees from to failed because some of parameters is null.");
            throw new DAOException("Find all employees from to failed because some of parameters is null.");
        }

        ObservableList<Employee> employees = FXCollections.observableArrayList();

        column = column.toUpperCase();

        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet metaDataRS = metaData.getColumns(null,null,"EMPLOYEE",column);
            if(metaDataRS.next()) {
                ResultSet rs = connection.createStatement().executeQuery(
                        "SELECT * FROM Employee WHERE " + column + " BETWEEN '" + from + "' AND '" + to + "'");
                while(rs.next()) {
                    employees.add(parseToEmployee(rs));
                }
            } else {
                LOGGER.error("Wrong column name given");
                throw new ColumnNotFoundException();
            }

        }catch (SQLException e) {
            LOGGER.error("Generic query could not be executed");
            throw new DAOException(e.getMessage());
        }

        return employees;

    }

    @Override
    public ObservableList<Employee> findAllFromToForPosition(String column, String position, Object from, Object to) throws DAOException, ColumnNotFoundException {

        if(column == null || position == null || from == null || to == null) {
            LOGGER.error("Find all customers from to for position failed because some of parameters is null.");
            throw new DAOException("Find all customers from to for position failed because some of parameters is null.");
        }

        ObservableList<Employee> employees = FXCollections.observableArrayList();

        column = column.toUpperCase();

        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet metaDataRS = metaData.getColumns(null,null,"EMPLOYEE",column);
            if(metaDataRS.next()) {
                ResultSet rs = connection.createStatement().executeQuery(
                        "SELECT * FROM Employee WHERE Rolle = '" + position + "'  AND " + column + " BETWEEN '" + from + "' AND '" + to + "'");
                while(rs.next()) {
                    employees.add(parseToEmployee(rs));
                }
            } else {
                LOGGER.error("Wrong column name given");
                throw new ColumnNotFoundException();
            }

        }catch (SQLException e) {
            LOGGER.error("Generic query could not be executed");
            throw new DAOException(e.getMessage());

        }

        return employees;

    }

    @Override
    public ObservableList<Employee> findAllForPosition(String position) throws DAOException, NoEmployeesFoundException {

        if(position == null) {
            LOGGER.error("Find all employees for position to failed because type is null.");
            throw new DAOException("Find all employees for position failed because type is null.");
        }

        ObservableList<Employee> employees = FXCollections.observableArrayList();

        try {

            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM employee WHERE Rolle = '" + position + "';");
            /**
             * Check if result set is null
             */
            if(rs == null){
                LOGGER.info("No employees found.");
                throw  new NoEmployeesFoundException("There are no employees with such a position in the database.");
            }

            while(rs.next()) {
                employees.add(parseToEmployee(rs));
            }

        } catch (SQLException e) {
            LOGGER.error("Find all employees failed.");
            throw new DAOException(e.getMessage());

        }

        return employees;
    }


    @Override
    public void update(Employee employee) throws DAOException {

        if(employee == null) {
            LOGGER.error("The given employee to update(Employee) is a null pointer.");
            throw new DAOException("The given employee to update(Employee) is a null pointer.");
        }

        if(employee.getRights() != null) {


            try {
                PreparedStatement updateStatement = connection.prepareStatement("UPDATE employee SET" +
                        " name = ?, surname = ?, address = ?, zip = ?, place = ?, country = ?, phone = ?," +
                        "email = ?, bdate = ?, sex = ?, svnr = ?, iban = ?, bic = ?, salary = ?, rolle = ?," +
                        "picture = ?, is_Deleted = ?, username = ?, user_password = ?, rights = ?" +
                        "WHERE pid = ?");

                updateStatement.setString(1, employee.getName());
                updateStatement.setString(2, employee.getSurname());
                updateStatement.setString(3, employee.getAddress());
                updateStatement.setString(4, employee.getZip());
                updateStatement.setString(5, employee.getPlace());
                updateStatement.setString(6, employee.getCountry());
                updateStatement.setString(7, employee.getPhone());
                updateStatement.setString(8, employee.getEmail());
                updateStatement.setDate(9, employee.getBdate());
                updateStatement.setString(10, employee.getSex());
                updateStatement.setString(11, employee.getSvnr());
                updateStatement.setString(12, employee.getIban());
                updateStatement.setString(13, employee.getBic());
                updateStatement.setLong(14, employee.getSalary());
                updateStatement.setString(15, employee.getRole());
                updateStatement.setString(16, employee.getPicture());
                updateStatement.setBoolean(17, employee.getDeleted());
                updateStatement.setString(18, employee.getUsername());
                updateStatement.setString(19 , employee.getPassword());
                updateStatement.setInt(20, employee.getRights());
                updateStatement.setInt(21, employee.getPid());

                if(updateStatement.executeUpdate() == 0){
                    throw new DAOException("No such employee in database.");
                }

                connection.commit();

            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
                throw  new DAOException("Can't update employee.");
            }

        } else {
            try {
                PreparedStatement updateStatement = connection.prepareStatement("UPDATE employee SET" +
                        " name = ?, surname = ?, address = ?, zip = ?, place = ?, country = ?, phone = ?," +
                        "email = ?, bdate = ?, sex = ?, svnr = ?, iban = ?, bic = ?, salary = ?, rolle = ?," +
                        "picture = ? WHERE pid = ?");

                updateStatement.setString(1, employee.getName());
                updateStatement.setString(2, employee.getSurname());
                updateStatement.setString(3, employee.getAddress());
                updateStatement.setString(4, employee.getZip());
                updateStatement.setString(5, employee.getPlace());
                updateStatement.setString(6, employee.getCountry());
                updateStatement.setString(7, employee.getPhone());
                updateStatement.setString(8, employee.getEmail());
                updateStatement.setDate(9, employee.getBdate());
                updateStatement.setString(10, employee.getSex());
                updateStatement.setString(11, employee.getSvnr());
                updateStatement.setString(12, employee.getIban());
                updateStatement.setString(13, employee.getBic());
                updateStatement.setLong(14, employee.getSalary());
                updateStatement.setString(15, employee.getRole());
                updateStatement.setString(16, employee.getPicture());
                updateStatement.setInt(17, employee.getPid());

                if(updateStatement.executeUpdate() == 0){
                    throw new DAOException("No such employee in database.");
                }

                connection.commit();

            } catch (SQLException e) {
                LOGGER.error("Can't update employee.");
                throw  new DAOException("Can't update employee.");
            }

        }

    }

    @Override
    public void delete(Employee employee) throws DAOException {

        if(employee == null) {
            throw new DAOException("The given employee is a null Pointer.");
        }else if (employee.getPid() < 0){
            throw  new DAOException("The given employee has a negative PID.");
        }else {

            try {
                PreparedStatement deleteStmt = connection.prepareStatement("UPDATE employee SET is_Deleted = TRUE WHERE pid = ?");

                deleteStmt.setInt(1, employee.getPid());

                if (deleteStmt.executeUpdate() == 0) {
                    throw new DAOException("No such employee.");
                }
                connection.commit();
                employee.setDeleted(true);

            } catch (SQLException e) {
                LOGGER.error("Delete employee failed.");
                throw new DAOException("Can't delete the employee.");
            }
        }

    }

    public Employee findWithUsernameAndPassword(Employee employee) throws DAOException, EmployeeNotFoundException{

        Employee found;

        if(employee == null){
            throw  new DAOException("The given employee is a null pointer.");
        }

        try {
            PreparedStatement findEmployeeStmt = connection.prepareCall("SELECT * FROM employee WHERE username = ? AND user_password = ?");

            findEmployeeStmt.setString(1, employee.getUsername());
            findEmployeeStmt.setString(2, employee.getPassword());

            ResultSet resultSet = findEmployeeStmt.executeQuery();

            /**
             * Check if an employee ith given username and password exists
             */
            if(resultSet.next()) {
                found = parseToEmployee(resultSet);
            }else {
                throw  new EmployeeNotFoundException("Employee with given username and password does not exist.");
            }

            resultSet.close();


        } catch (SQLException e) {
            LOGGER.error("Search failed with message: " + e.getMessage());
            throw  new DAOException("SQL Exception");
        }

        return found;
    }

    /**
     * This method takes a ResultSet pointing on a row
     * and returns an employee object
     * @param rs - ResultSet already pointing to a row
     * @return - Employee object
     * @throws SQLException
     */
    private Employee parseToEmployee(ResultSet rs) throws SQLException {

        int pid = rs.getInt("PID");

        String name = rs.getString("name");

        String surname = rs.getString("surname");

        String address = rs.getString("address");

        String zip = rs.getString("zip");

        String place = rs.getString("place");

        String country = rs.getString("country");

        String phone = rs.getString("phone");

        String email = rs.getString("email");

        Date bdate = rs.getDate("bdate");

        String sex = rs.getString("sex");

        String svnr = rs.getString("svnr");

        String iban = rs.getString("iban");

        String bic = rs.getString("bic");

        Long salary = rs.getLong("salary");

        String role = rs.getString("rolle");

        String picture = rs.getString("picture");

        String username = rs.getString("username");

        String password = rs.getString("user_password");

        Integer rights = rs.getInt("rights");

        return new Employee(pid, name, surname, address, zip, place, country, phone, email, bdate, sex, svnr
                , iban, bic, salary, role, picture, false, username, password, rights);
    }

}
