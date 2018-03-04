package main.java.ac.at.tuwien.sepm.QSE15.dao.creditCardDAO;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.entity.creditCard.CreditCard;
import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.entity.creditCard.CreditCard;

import java.util.List;

/**
 * Created by Nemanja Vukoje on 09/05/2017.
 */
public interface CreditCardDAO {
    /**
     * Creates a new Credit Card
     *
     * @param creditCard Credit Card that needs to be created
     * @return created CreditCard
     * @throws DAOException - (1) if the given CreditCard is a null pointer
     *                        (2) if an SQL Exception occurred
     */
    CreditCard createCreditCard(CreditCard creditCard) throws DAOException;

    /**
     *  Deletes a CreditCArd from a table
     *
     * @param creditCard CreditCard that needs to be deleted
     * @return deleted Card
     * @throws DAOException - (1) if the given CreditCard is a null pointer
     *                        (2) if an SQL Exception occurred
     */
    CreditCard deleteCreditCard(CreditCard creditCard) throws DAOException;


    /**
     * Searches for a specific Credit Card
     *
     * @param creditCard Parameters for searched saved in a CreditCard
     * @return List of CreditCards
     * @throws DAOException - (1) if the given CreditCard is a null pointer
     *                        (2) if an SQL Exception occurred
     */
    List<CreditCard> searchCreditCard(CreditCard creditCard) throws DAOException;

    /**
     * List all CreditCards in a table
     *
     * @return List with all CreditCards
     * @throws DAOException - (1) if an SQL Exception occurred
     */
    ObservableList<CreditCard> getAllCreditCards() throws DAOException;

    CreditCard findCreditCard(String cnr) throws DAOException;

    void update(CreditCard creditCard) throws DAOException;
}


