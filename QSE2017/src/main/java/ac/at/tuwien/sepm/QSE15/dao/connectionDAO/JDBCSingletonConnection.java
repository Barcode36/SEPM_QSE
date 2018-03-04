package main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import org.h2.tools.RunScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Bajram Saiti on 28.04.17.
 */


@Component
@Scope("singleton")
public class JDBCSingletonConnection {

    private static Connection con = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCSingletonConnection.class);

    public void initDatabase() throws DAOException {

        try {
            LOGGER.info("Trying to Connect to database.");
            Class.forName("org.h2.Driver");
            con = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/hsm", "sa", "");
            InputStream in = JDBCSingletonConnection.class.getResourceAsStream("/res/layouts/database/create.sql");
            if (in!=null){
                RunScript.execute(con,new InputStreamReader(in));
                LOGGER.info("Creating Table success");
            }
            else {
                LOGGER.error("Input error");
            }
            con.setAutoCommit(false);

        }
        catch (ClassNotFoundException e){
            LOGGER.error("Unable to connect");
            throw new DAOException(e.getMessage());

        }
        catch (SQLException e){

            throw new DAOException(e.getMessage());
        }
    }

    public Connection getConnection() throws DAOException {
        if (con == null){
            initDatabase();
        }
        return con;
    }

    public void closeConnection() throws DAOException{

        if (con!=null){
            try {

                con.close();
            } catch (SQLException e) {
                throw new DAOException(e.getMessage());
            }
        }
    }

    public static Connection reconnect() throws DAOException {

        try {
            if (con!=null && !con.isValid(3)){
                con = null;
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }

        //con = JDBCSingletonConnection.getConnection();
        return con;
    }

}