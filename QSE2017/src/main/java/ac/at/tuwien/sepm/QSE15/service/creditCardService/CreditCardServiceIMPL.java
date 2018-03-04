package main.java.ac.at.tuwien.sepm.QSE15.service.creditCardService;
import main.java.ac.at.tuwien.sepm.QSE15.service.creditCardService.CreditCardService;
import org.springframework.stereotype.Service;


import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.creditCardDAO.CreditCardDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.creditCard.CreditCard;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.serviceService.ServiceServiceIMPL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Nemanja Vukoje on 15/05/2017.
 */
@Service
public class CreditCardServiceIMPL implements CreditCardService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceServiceIMPL.class);

    @Autowired
    private CreditCardDAO creditCardDAO;

    @Override
    public CreditCard createCreditCard(CreditCard creditCard) throws ServiceException {
        try {
            return creditCardDAO.createCreditCard(creditCard);
        } catch (DAOException e) {
            LOGGER.error("Error in CreditCard Service create");
            throw new ServiceException("Error in CreditCard Service");
        }
    }

    @Override
    public CreditCard deleteCreditCard(CreditCard creditCard) throws ServiceException {
        try {
            return creditCardDAO.deleteCreditCard(creditCard);
        } catch (DAOException e) {
            LOGGER.error("Error in CreditCard Service delete");
            throw new ServiceException("Error in CreditCard Service");
        }
    }

    @Override
    public List<CreditCard> searchCreditCard(CreditCard creditCard) throws ServiceException {
        try {
            return creditCardDAO.searchCreditCard(creditCard);
        } catch (DAOException e) {
            LOGGER.error("Error in CreditCard Service search");
            throw new ServiceException("Error in CreditCard Service");
        }
    }

    @Override
    public ObservableList<CreditCard> getAllCreditCards() throws ServiceException {
        try {
            return creditCardDAO.getAllCreditCards();
        } catch (DAOException e) {
            LOGGER.error("Error in CreditCard Service getAllCards");
            throw new ServiceException("Error in CreditCard Service");
        }

    }

    @Override
    public CreditCard getCreditCard(String cnr) throws ServiceException {
        try {
            return creditCardDAO.findCreditCard(cnr);
        } catch (DAOException e) {
        }
        return null;
    }

    @Override
    public void updateCreditCard(CreditCard creditCard) throws ServiceException {
        try {
            creditCardDAO.update(creditCard);
        } catch (DAOException e) {
        }
    }
}
