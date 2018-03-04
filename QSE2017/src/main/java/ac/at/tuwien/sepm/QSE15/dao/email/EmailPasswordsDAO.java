package main.java.ac.at.tuwien.sepm.QSE15.dao.email;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by ervincosic on 29/05/2017.
 */
@Repository
public class EmailPasswordsDAO {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EmailPasswordsDAO.class);

    @Autowired
    JDBCSingletonConnection jdbcSingletonConnection;

    Connection connection;

    @PostConstruct
    public void init(){
        try {
            connection = jdbcSingletonConnection.getConnection();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public void savePassword(Employee employee, String password)throws DAOException{

        if(employee == null || password == null){
            throw  new DAOException("The given values are null pointers.");
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO email_passwords VALUES (?, ?);");

            LOGGER.info("Password statement created");

            preparedStatement.setString(1, employee.getUsername());

            preparedStatement.setString(2, password);

            int rows = preparedStatement.executeUpdate();

            LOGGER.info("Rows created: " + rows);


        } catch (SQLException e) {
            LOGGER.error("Error in savePassword: " + e.getMessage());
           throw  new DAOException("Cannot save password for user.");
        }
    }

    public void updatePassword(Employee employee, String password) throws DAOException{

        if(employee == null || password == null){
            throw  new DAOException("The given values are null pointers.");
        }

        try{

            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE email_passwords SET password = ? WHERE username =?");

            preparedStatement.setString(1, password);
            preparedStatement.setString(2, employee.getUsername());

            int rows = preparedStatement.executeUpdate();

            LOGGER.info("Rows created: " + rows);


        }catch (SQLException e){
            LOGGER.error("Error in updatePassword: " + e.getMessage());
            throw new DAOException();
        }

    }

    public String getPassword(Employee employee)throws DAOException{
        String password = "";

        if(employee == null){
            LOGGER.error("Employee is a null pointer");
            throw new DAOException();
        }

        try {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM email_passwords WHERE username = ?");

            preparedStatement.setString(1, employee.getUsername());

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                password = resultSet.getString("password");
            }

        }catch (SQLException e){
            LOGGER.error("Error in getPassword: " + e.getMessage());
            throw new DAOException();
        }


        return password;
    }


}
