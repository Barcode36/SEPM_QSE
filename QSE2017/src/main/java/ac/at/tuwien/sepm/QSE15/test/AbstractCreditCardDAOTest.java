package main.java.ac.at.tuwien.sepm.QSE15.test;

import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.creditCardDAO.CreditCardDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.serviceDAO.ServiceDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.creditCard.CreditCard;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.RoomReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Nemanja Vukoje on 11/05/2017.
 */
abstract class AbstractCreditCardDAOTest {


    private CreditCardDAO creditCardDAO;

    public void setCreditCardDAO(CreditCardDAO creditCardDAO) {
        this.creditCardDAO = creditCardDAO;
    }

    @Test(expected = DAOException.class)
    public void createWithInvalidParametersShouldThrowException() throws DAOException {

            creditCardDAO.createCreditCard(null);

    }
    @Test
    public void creteWithValiedParametersShouldWOrkl(){
        try {
            ObservableList<CreditCard> list = creditCardDAO.getAllCreditCards();
            CreditCard creditCard = new CreditCard("123", "Nemanja","Visa",12,2021,"444");
            creditCardDAO.createCreditCard(creditCard);
            ObservableList<CreditCard> list1 = creditCardDAO.getAllCreditCards();
            assertEquals(list.size()+1, list1.size());

        } catch (DAOException e) {
        }
    }
    @Test(expected = DAOException.class)
    public void removeTestWithInvalidParameter() throws DAOException {
        creditCardDAO.deleteCreditCard(null);
    }
    @Test
    public void removeTestWithValidParameter() throws Exception {
        ObservableList<CreditCard> list = creditCardDAO.getAllCreditCards();
        CreditCard creditCard = new CreditCard("444", "Nemanja","Visa",12,2021,"444");
        creditCardDAO.createCreditCard(creditCard);
        creditCardDAO.deleteCreditCard(creditCard);
        ObservableList<CreditCard> list1 = creditCardDAO.getAllCreditCards();
        assertEquals(list.size(), list1.size());

    }
    @Test(expected = DAOException.class)
    public void searchWithInvalidParametersShouldThrowException() throws DAOException {

        creditCardDAO.searchCreditCard(null);

    }

    @Test
    public void searchWithValidParamterShouldWork() throws DAOException{
        CreditCard creditCard = new CreditCard("12345678", "NEmanja","VISA",1,2001,"ASD!");

        creditCardDAO.createCreditCard(creditCard);
        List<CreditCard> list = creditCardDAO.searchCreditCard(creditCard);
        Assert.assertTrue(list.contains(creditCard));
    }
}
