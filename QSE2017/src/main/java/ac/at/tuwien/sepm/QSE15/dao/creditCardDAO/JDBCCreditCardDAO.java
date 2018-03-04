package main.java.ac.at.tuwien.sepm.QSE15.dao.creditCardDAO;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;

import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.entity.creditCard.CreditCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nemanja Vukoje on 09/05/2017.
 */
@Repository
public class JDBCCreditCardDAO implements CreditCardDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCCreditCardDAO.class);


    private Connection connection;
    @Autowired
    private JDBCSingletonConnection jdbcSingletonConnection;

    private AnnotationConfigApplicationContext context;



    private String createCreditCard;
    private String deleteCreditCard;
    private String searchCreditCard;
    private String getAllCreditCards;




            @PostConstruct
            public void init () {
                try {
                    connection = jdbcSingletonConnection.getConnection();



                } catch (DAOException e) {
                    LOGGER.error("Unable to get connection.");
                }
            }


            @Override
            public CreditCard createCreditCard (CreditCard creditCard) throws DAOException {
                if (creditCard == null) {
                    throw new DAOException("Invalid parameter");
                }
                LOGGER.info("Creating CreditCard");
                createCreditCard = "INSERT INTO Creditcard(CNR, Holder, card_type, Exp_month, Exp_year, CVV) VALUES (?,?,?,?,?,?);";
                try {
                    PreparedStatement ps = connection.prepareStatement(createCreditCard);


                        ps.setString(1, creditCard.getCnr());
                        ps.setString(2, creditCard.getHolder());
                        ps.setString(3, creditCard.getCardType());
                        ps.setInt(4, creditCard.getExpMonth());
                        ps.setInt(5, creditCard.getExpYear());
                        ps.setString(6, creditCard.getCvv());

                        ps.executeUpdate();
                        connection.commit();


                    } catch (SQLException e) {
                        LOGGER.error("Error in create CreditCard");
                        throw new DAOException(e.getMessage());
                    }
                    LOGGER.info("CreditCard successfully added");
                    return creditCard;
            }

                @Override
                public CreditCard deleteCreditCard (CreditCard creditCard) throws DAOException {
                    LOGGER.info("deleting CreditCard!");
                    deleteCreditCard = "DELETE Creditcard where CNR=?";
                    if (creditCard == null) {
                        LOGGER.error("CreditCard is null!");
                        throw new DAOException("Invalid parameter!");

                    }
                    try {
                            PreparedStatement ps = connection.prepareStatement(deleteCreditCard);
                            ps.setString(1, creditCard.getCnr());
                            ps.executeUpdate();
                            connection.commit();
                        } catch (SQLException e) {
                            LOGGER.error("Error in delete CreditCard");
                            throw new DAOException(e.getMessage());
                        }

                        LOGGER.info("Credit card successfully deleted!");
                        return creditCard;
                    }

    /**
     * Created by Stefan Puhalo 6.6.2017
     * Method to get credit card from CNR from Object Customer
     * @param cnr
     * @return
     * @throws DAOException
     */
    @Override
    public CreditCard findCreditCard(String cnr) throws DAOException {

        if(cnr == null) {
            throw new DAOException("CNR is null");
        }
        CreditCard creditCard = null;

        try {

            PreparedStatement findStmt = connection.prepareStatement("SELECT * FROM CREDITCARD WHERE CNR = ?");

            findStmt.setString(1, cnr);

            ResultSet rs = findStmt.executeQuery();

            /**
             * Check if an employee was found
             */
            if(rs == null){
                throw new DAOException("No Credit Card found with given CNR.");
            }

            while(rs.next()) {
                creditCard = new CreditCard(cnr, rs.getString(2),rs.getString(3), rs.getInt(4),
                        rs.getInt(5),rs.getString(6));
            }

            rs.close();

        } catch (SQLException e) {
            LOGGER.error("Find credit card failed");
            throw  new DAOException("Find credit card failed");
        }

        return creditCard;


    }

    /**
     * Created by Stefan Puhalo
     * @param creditCard
     * @throws DAOException
     */
    @Override
    public void update(CreditCard creditCard) throws DAOException {


        try {
            PreparedStatement updateStmt = connection.prepareStatement("UPDATE CREDITCARD SET holder=?, card_type=?," +
                    "exp_month=?, exp_year=?, cvv=? WHERE cnr = ?");


            updateStmt.setString(1, creditCard.getHolder());
            updateStmt.setString(2, creditCard.getCardType());
            updateStmt.setInt(3, creditCard.getExpMonth());
            updateStmt.setInt(4, creditCard.getExpYear());
            updateStmt.setString(5, creditCard.getCvv());
            updateStmt.setString(6, creditCard.getCnr());

            updateStmt.executeUpdate();
            connection.commit();
            LOGGER.info("Credit card successfully updated");
        }catch (SQLException e) {
            LOGGER.error("Credit card update failed");
        }

    }


                    @Override
                    public List<CreditCard> searchCreditCard (CreditCard creditCard) throws DAOException {
                        LOGGER.info("Searching for CreditCard");
                        List<CreditCard> listAll = new ArrayList<>();
                        searchCreditCard = "SELECT * FROM CREDITCARD WHERE HOLDER=?";
                        if (creditCard == null) {
                            throw new DAOException("Invalid input!");
                        }
                        try {
                            PreparedStatement ps = connection.prepareStatement(searchCreditCard);
                            ps.setString(1, creditCard.getHolder());
                            ResultSet rs = ps.executeQuery();
                            while (rs.next()) {
                                CreditCard creditCard1 = new CreditCard();
                                creditCard1.setCnr(rs.getString("CNR"));
                                creditCard1.setHolder(rs.getString("Holder"));
                                creditCard1.setExpMonth(rs.getInt("Exp_Month"));
                                creditCard1.setCardType(rs.getString("Card_type"));
                                creditCard1.setExpYear(rs.getInt("Exp_Year"));
                                listAll.add(creditCard1);

                            }
                        } catch (SQLException e) {
                            throw new DAOException(e.getMessage());
                        }
                        return listAll;
                    }

                    @Override
                    public ObservableList<CreditCard> getAllCreditCards () throws DAOException {
                        LOGGER.info("Getting all CreditCards");
                        ObservableList<CreditCard> list = FXCollections.observableArrayList();
                        getAllCreditCards = "SELECT * FROM CREDITCARD";
                        try {
                            PreparedStatement ps = connection.prepareStatement(getAllCreditCards);
                            ResultSet rs = ps.executeQuery();
                            while (rs.next()) {
                                CreditCard creditCard = new CreditCard();
                                creditCard.setCnr(rs.getString("CNR"));
                                creditCard.setHolder(rs.getString("Holder"));
                                creditCard.setCardType(rs.getString("Card_type"));
                                creditCard.setExpMonth(rs.getInt("Exp_Month"));
                                creditCard.setExpYear(rs.getInt("Exp_Year"));
                                list.add(creditCard);
                            }
                        } catch (SQLException e) {
                            throw new DAOException(e.getMessage());
                        }
                        return list;
                    }
                }


