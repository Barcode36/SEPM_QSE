package main.java.ac.at.tuwien.sepm.QSE15.test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.creditCardDAO.CreditCardDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.customerDAO.CustomerDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.creditCard.CreditCard;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Customer;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.ColumnNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.CustomerNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.NoCustomerFoundException;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;

/**
 * Created by Stefan Puhalo on 5/3/2017.
 */
public abstract class AbstractCustomerDAOTest {


    protected CustomerDAO customerDAO;

    protected CreditCardDAO creditCardDAO;

    protected void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    protected void setCreditCardDAO(CreditCardDAO creditCardDAO) { this.creditCardDAO = creditCardDAO; }


    @Test(expected = DAOException.class)
    public void createWithInvalidParametersShouldThrowException() throws DAOException {
        customerDAO.create(null);
    }

    @Test
    public void createCustomerWithValidParametersShouldPersist() {

        try {
            CreditCard creditCard = new CreditCard ("14449","Stefan Puhalo","VISA",4,2020,"2414");
            creditCardDAO.createCreditCard(creditCard);
            Customer customer  = new Customer(null,"Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","231385", creditCard,
                    "Non Smoking room", true,0);
            ObservableList<Customer> customers = customerDAO.findAll();
            Assert.assertFalse(customers.contains(customer));
            customer = customerDAO.create(customer);
            customers = customerDAO.findAll();
            Assert.assertTrue(customers.contains(customer));

        } catch (DAOException | NoCustomerFoundException e) {

        }

    }

    @Test
    public void updateWithInvalidParametersShouldThrowException() {
        try {
            customerDAO.update(null);
        }catch (DAOException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void updateWithValidParameters() {

        try {

            CreditCard creditCard = new CreditCard("654841","Stefan Puhalo","VISA",4,2020,"2414");
            creditCardDAO.createCreditCard(creditCard);

            Customer customer  = new Customer(null,"Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","231385", creditCard,
                    "Non Smoking room", true,0);

            customerDAO.create(customer);
            customer.setAddress("Favoritenstrasse 14");
            customerDAO.update(customer);
            Customer foundCustomer = customerDAO.find(customer.getPid());
            Assert.assertTrue(foundCustomer.getAddress().equals("Favoritenstrasse 14"));

        }catch (DAOException | CustomerNotFoundException e) {
        }

    }

    @Test
    public void findAllCustomersTest() {

        try {

            CreditCard creditCard = new CreditCard("654841","Stefan Puhalo","VISA",4,2020,"2414");
            creditCardDAO.createCreditCard(creditCard);

            Customer customer1  = new Customer(null,"Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","231385", creditCard,
                    "Non Smoking room", true,0);

            Customer customer2  = new Customer(null,"John","Newman","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","231385", creditCard,
                    "Non Smoking room", true,0);


            customerDAO.create(customer1);
            customerDAO.create(customer2);

            ObservableList<Customer> customers = customerDAO.findAll();
            Assert.assertTrue(customers.contains(customer1) && customers.contains(customer2));

        }catch (DAOException | NoCustomerFoundException e) {
        }

    }

    @Test
    public void findCustomerWithValidID() {

        try {

            CreditCard creditCard = new CreditCard("654841","Stefan Puhalo","VISA",4,2020,"2414");
            creditCardDAO.createCreditCard(creditCard);

            Customer customer1  = new Customer(null,"Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","231385", creditCard,
                    "Non Smoking room", true,0);

            customerDAO.create(customer1);

            Customer foundCustomer = customerDAO.find(customer1.getPid());
            Assert.assertTrue(foundCustomer.getPid() == customer1.getPid());

        }catch (DAOException | CustomerNotFoundException e) {
        }

    }

    @Test(expected = DAOException.class)
    public void findCustomerWithZeroShouldThrowException() throws DAOException, CustomerNotFoundException {
        customerDAO.find(0);
    }

    @Test(expected = CustomerNotFoundException.class)
    public void findCustomerWithNotInsertedCustomerShouldThrowException()throws DAOException, CustomerNotFoundException  {
        customerDAO.find(Integer.MAX_VALUE);
    }

    @Test
    public void deleteCustomerShouldRemoveItFromDatabase() throws DAOException, CustomerNotFoundException {

        Customer deletedCustomer = null;


        Customer customer1  = new Customer(null,"Stefan","Puhalo","Address1", "1090",
                "Vienna","AT","066038381959",
                "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","231385",
                "Non Smoking room", true,0);

        customerDAO.create(customer1);

        Customer foundCustomer = customerDAO.find(customer1.getPid());
        customerDAO.delete(customer1);
        try {
            deletedCustomer = customerDAO.find(customer1.getPid());
        }catch (CustomerNotFoundException e) {

        }
        Assert.assertTrue(foundCustomer != null && deletedCustomer == null);

    }

    @Test(expected = DAOException.class)
    public void deleteCustomerWithNullShouldThrowException() throws DAOException {
        customerDAO.delete(null);
    }

    @Test
    public void findAllFromToWithValidParameters() throws DAOException, ColumnNotFoundException {
        CreditCard creditCard = new CreditCard("654842","Stefan Puhalo","VISA",4,2020,"2414");
        creditCardDAO.createCreditCard(creditCard);

        Customer customer1  = new Customer(null,"Stefan","Puhalo","Address1", "1090",
                "Vienna","AT","066038381959",
                "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","231385", creditCard,
                "Non Smoking room", true,0);

        Customer guest1  = new Customer(null,"Stefan","Puhalo","Address1", "1090",
                "Vienna","AT","066038381959",
                "stefanpuhalo@gmail.com", Date.valueOf("1985-01-01"),"man","231385",
                "Non Smoking room", true,0);

        customerDAO.create(customer1);
        customerDAO.create(guest1);

        ObservableList<Customer> customers = customerDAO.findAllFromTo("BDATE", Date.valueOf("1982-01-01"), Date.valueOf("1985-12-01"));
        Assert.assertTrue(customers.contains(customer1) && customers.contains(guest1));

    }

    @Test(expected = DAOException.class)
    public void findAllFromToWithInvalidParameters() throws DAOException, ColumnNotFoundException {
        customerDAO.findAllFromTo("BDATE", null, null);
    }

    @Test(expected = ColumnNotFoundException.class)
    public void findAllFromToWithInvalidColumn() throws DAOException, ColumnNotFoundException {
        customerDAO.findAllFromTo("Stefan", Date.valueOf("1982-01-01"), Date.valueOf("1985-12-01"));
    }

    @Test
    public void findAllFromToForTypeWithValidParameters() throws DAOException, ColumnNotFoundException {

        Customer customer1  = new Customer(null,"Stefan","Puhalo","Address1", "1090",
                "Vienna","AT","066038381959",
                "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","231385",
                "Non Smoking room", true,0);

        Customer guest1  = new Customer(null,"Stefan","Puhalo","Address1", "1090",
                "Vienna","AT","066038381959",
                "stefanpuhalo@gmail.com", Date.valueOf("1985-01-01"),"man","231385",
                "Non Smoking room", true,45);

        customerDAO.create(customer1);
        customerDAO.create(guest1);

        ObservableList<Customer> customers = customerDAO.findAllFromToForType("BDATE","Customer", Date.valueOf("1982-01-01"), Date.valueOf("1985-12-01"));
        Assert.assertTrue(customers.contains(customer1) && !customers.contains(guest1));
    }

    @Test(expected = DAOException.class)
    public void findAllFromToForTypeWithInvalidParameters() throws DAOException, ColumnNotFoundException {
        customerDAO.findAllFromToForType("BDATE", null, null, null);
    }

    @Test(expected = ColumnNotFoundException.class)
    public void findAllFromToForTypeWithInvalidColumn() throws DAOException, ColumnNotFoundException {
        customerDAO.findAllFromToForType("Stefan", "Customer", Date.valueOf("1982-01-01"), Date.valueOf("1985-12-01"));
    }

    @Test
    public void findAllForTypeWithValidParameters() throws DAOException, NoCustomerFoundException {
        Customer customer1  = new Customer(null,"Stefan","Puhalo","Address1", "1090",
                "Vienna","AT","066038381959",
                "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","231385",
                "Non Smoking room", true,0);

        Customer guest1  = new Customer(null,"Stefan","Puhalo","Address1", "1090",
                "Vienna","AT","066038381959",
                "stefanpuhalo@gmail.com", Date.valueOf("1985-01-01"),"man","231385",
                "Non Smoking room", true,45);

        customerDAO.create(customer1);
        customerDAO.create(guest1);

        ObservableList<Customer> customers = customerDAO.findAllForType("Customer");
        Assert.assertTrue(customers.contains(customer1) && !customers.contains(guest1));
    }

    @Test(expected = DAOException.class)
    public void findAllForTypeWithNullShouldThrowException() throws DAOException, NoCustomerFoundException {
        customerDAO.findAllForType(null);
    }

    @Test(expected = DAOException.class)
    public void findAllForTypeWithInvalidTypeFoundShouldThrowException() throws DAOException, NoCustomerFoundException {
        customerDAO.findAllForType("Stefan");
    }

    @Test
    public void checkCustomerWithValidParameters() throws DAOException, CustomerNotFoundException {

        Customer customer1  = new Customer(null,"Stefan","Puhalo","Address1", "1090",
                "Vienna","AT","066038381959",
                "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","231385",
                "Non Smoking room", true,0);
        customerDAO.create(customer1);

        Customer foundCustomer = customerDAO.checkCustomer("Stefan", "Puhalo", Date.valueOf("1983-01-01"));
        Assert.assertTrue(foundCustomer != null);

    }

    @Test(expected = DAOException.class)
    public void checkCustomerWithNullShouldThrowException() throws DAOException, CustomerNotFoundException {
        customerDAO.checkCustomer(null,null,null);
    }

    @Test
    public void reservationsOfAllCustomersTest() throws DAOException {
        Assert.assertTrue(customerDAO.reservationsOfAllCustomers().containsKey("Brenna Morgan"));
    }

}