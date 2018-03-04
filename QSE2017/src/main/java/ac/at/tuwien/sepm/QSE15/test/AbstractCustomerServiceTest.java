package main.java.ac.at.tuwien.sepm.QSE15.test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.customerDAO.JDBCCustomerDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Customer;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.ColumnNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.CustomerNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.NoCustomerFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.customerService.CustomerServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.test.stubs.CustomerDaoStub;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Date;

/**
 * Created by Stefan Puhalo on 6/21/2017.
 */
public class AbstractCustomerServiceTest {

    protected JDBCCustomerDAO customerDAO;

    protected CustomerServiceIMPL customerService;

    protected CustomerDaoStub customerDaoStub;

    protected void setCustomerDAO(JDBCCustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    protected void setCustomerService(CustomerServiceIMPL customerService) { this.customerService = customerService; }

    @Test
    public void createCustomerWithValidParametersShouldPersist() {
        try {

            Customer customer  = new Customer(null,"Stefan","Puhalo","Address1", "1090",
                "Vienna","AT","066038381959",
                "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","231385",
                "Non Smoking room", true,0);

            Mockito.when(customerDAO.create(customer)).thenReturn(customer);
            Customer createdCustomer = customerService.createCustomer(customer);
            Assert.assertTrue(createdCustomer.getName().equals("Stefan"));

        }catch(ServiceException | DAOException e) {

        }

    }

    @Test(expected = ServiceException.class)
    public void createCustomerWithNullShouldThrowException() throws ServiceException {
        customerService.createCustomer(null);
    }

    @Test
    public void updateAllCustomersWithValidParametersShouldPersist() {

        try {

            Customer customer  = new Customer(null,"Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","231385",
                    "Non Smoking room", true,0);

            Customer customer2  = new Customer(null,"Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","231385",
                    "Non Smoking room", true,0);

            ObservableList<Customer> customers = FXCollections.observableArrayList();
            customers.addAll(customer,customer2);

            customerDaoStub = new CustomerDaoStub();
            customerService.setCustomerDAO(customerDaoStub);
            customerService.updateAllCustomers(customers);


            Assert.assertTrue(customer2.getName().equals("John"));

        }catch (ServiceException e) {

        }

    }

    @Test(expected = ServiceException.class)
    public void updateAllCustomerWithNullShouldThrowException() throws ServiceException {
        customerService.updateAllCustomers(null);
    }


    @Test
    public void updateWithValidParametersShouldPersist() {
        try {

            Customer customer  = new Customer(null,"Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","231385",
                    "Non Smoking room", true,0);


            customerDaoStub = new CustomerDaoStub();
            customerService.setCustomerDAO(customerDaoStub);
            customerService.updateCustomer(customer);

            Assert.assertTrue(customer.getName() == "John");


        }catch (ServiceException e) {

        }

    }

    @Test(expected = ServiceException.class)
    public void updateCustomerWithNullShouldThrowException() throws ServiceException {
        customerService.updateCustomer(null);
    }

    @Test
    public void deleteWithValidParametersShouldPersist() {
        try {

            Customer customer = new Customer(null,"Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","231385",
                    "Non Smoking room", true,0);

            customerDaoStub = new CustomerDaoStub();
            customerService.setCustomerDAO(customerDaoStub);
            customerService.deleteCustomer(customer);

            Assert.assertTrue(customer.getName() == null);


        }catch (ServiceException e) {

        }

    }

    @Test(expected = ServiceException.class)
    public void deleteCustomerWithNullShouldThrowException() throws ServiceException {
        customerService.deleteCustomer(null);
    }

    /**
    @Test
    public void getAllCustomersWithValidParameters() {

        try{

            Customer customer  = new Customer(null,"Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","231385",
                    "Non Smoking room", true,0);


            ObservableList<Customer> customers = FXCollections.observableArrayList();
            ObservableList<Customer> retrievedCustomers;

            customers.add(customer);
            Mockito.when(customerDAO.findAll()).thenReturn(customers);
            customerService.setCustomerDAO(customerDAO);

            retrievedCustomers = customerService.getAllCustomers();

            Assert.assertTrue(retrievedCustomers.contains(customer));

        }catch (ServiceException | DAOException | NoCustomerFoundException e) {

        }
    }
     */

    @Test
    public void getCustomerWithValidID() {

        try{

            Customer customer  = new Customer(null,"Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","231385",
                    "Non Smoking room", true,0);
            customer.setPid(102);
            Mockito.when(customerDAO.find(102)).thenReturn(customer);
            customerService.setCustomerDAO(customerDAO);
            Customer retrievedCustomer1;

            retrievedCustomer1 = customerService.getCustomer(102);

            Assert.assertTrue(retrievedCustomer1.getName().equals("Stefan"));

        }catch (ServiceException | DAOException | CustomerNotFoundException e) {

        }

    }

    @Test(expected = ServiceException.class)
    public void getCustomerWithZeroShouldThrowException() throws ServiceException {
        customerService.getCustomer(0);
    }

    @Test
    public void getAllCustomersFromToWithValidParameters() {
        try{

            Customer customer  = new Customer(null,"Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","231385",
                    "Non Smoking room", true,0);


            ObservableList<Customer> customers = FXCollections.observableArrayList();
            ObservableList<Customer> retrievedCustomers;

            customers.add(customer);
            Mockito.when(customerDAO.findAllFromTo("BDATE","1980-01-01", "1984-01-01")).thenReturn(customers);
            customerService.setCustomerDAO(customerDAO);
            retrievedCustomers = customerService.getAllCustomersFromTo("BDATE","1980-01-01", "1984-01-01");

            Assert.assertTrue(retrievedCustomers.contains(customer));

        }catch (ServiceException | DAOException | ColumnNotFoundException e) {

        }

    }

    @Test(expected = ServiceException.class)
    public void getAllCustomersFromToWithNullShouldThrowException() throws ServiceException {
        customerService.getAllCustomersFromTo(null, null, null);
    }

    @Test
    public void getAllCustomersForPositionFromToWithValidParameters() {
        try{

            Customer customer  = new Customer(null,"Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","231385",
                    "Non Smoking room", true,0);


            ObservableList<Customer> customers = FXCollections.observableArrayList();
            ObservableList<Customer> retrievedCustomers;

            customers.add(customer);
            Mockito.when(customerDAO.findAllFromToForType("BDATE", "Customer","1980-01-01", "1984-01-01")).thenReturn(customers);
            customerService.setCustomerDAO(customerDAO);
            retrievedCustomers = customerService.getAllCustomersForTypeFromTo("BDATE", "Customer", "1980-01-01", "1984-01-01");

            Assert.assertTrue(retrievedCustomers.contains(customer));

        }catch (ServiceException | DAOException | ColumnNotFoundException e) {

        }
    }

    @Test(expected = ServiceException.class)
    public void getAllCustomersForPositionFromToWithNullShouldThrowException() throws ServiceException {
        customerService.getAllCustomersForTypeFromTo("BDATE", null, null, null);
    }

    @Test
    public void getAllCustomersForTypeWithValidParameters() {
        try{

            Customer customer  = new Customer(null,"Stefan","Puhalo","Address1", "1090",
                    "Vienna","AT","066038381959",
                    "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","231385",
                    "Non Smoking room", true,0);


            ObservableList<Customer> customers = FXCollections.observableArrayList();
            ObservableList<Customer> retrievedCustomers;

            customers.add(customer);
            Mockito.when(customerDAO.findAllForType("Customer")).thenReturn(customers);
            customerService.setCustomerDAO(customerDAO);
            retrievedCustomers = customerService.getAllCustomersForType("Customer");

            Assert.assertTrue(retrievedCustomers.contains(customer));

        }catch (ServiceException | DAOException | NoCustomerFoundException e) {

        }
    }

    @Test(expected = ServiceException.class)
    public void getAllCustomersForTypeWithNullShouldThrowException() throws ServiceException {
        customerService.getAllCustomersForType(null);
    }

}
