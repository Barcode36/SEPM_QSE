package main.java.ac.at.tuwien.sepm.QSE15.service.creditCardService;

import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.entity.creditCard.CreditCard;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;

import java.util.List;

/**
 * Created by Nemanja Vukoje on 15/05/2017.
 */
public interface CreditCardService {
    /**
     * Creates a new Credit Card
     *
     * @param creditCard Credit Card that needs to be created
     * @return created CreditCard
     * @throws ServiceException
     */
    CreditCard createCreditCard(CreditCard creditCard) throws ServiceException;

    /**
     *  Deletes a CreditCArd from a table
     *
     * @param creditCard CreditCard that needs to be deleted
     * @return deleted Card
     * @throws ServiceException
     */
    CreditCard deleteCreditCard(CreditCard creditCard) throws ServiceException;


    /**
     * Searches for a specific Credit Card
     *
     * @param creditCard Parameters for searched saved in a CreditCard
     * @return List of CreditCards
     * @throws ServiceException
     */
    List<CreditCard> searchCreditCard(CreditCard creditCard) throws ServiceException;

    /**
     * List all CreditCards in a table
     *
     * @return List with all CreditCards
     * @throws ServiceException - (1) if an SQL Exception occurred
     */
    ObservableList<CreditCard> getAllCreditCards() throws ServiceException;

    CreditCard getCreditCard(String cnr) throws ServiceException;

    void updateCreditCard(CreditCard creditCard) throws ServiceException;


}

