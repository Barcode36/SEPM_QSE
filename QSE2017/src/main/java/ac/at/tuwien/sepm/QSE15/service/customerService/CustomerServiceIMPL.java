package main.java.ac.at.tuwien.sepm.QSE15.service.customerService;

import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.creditCardDAO.JDBCCreditCardDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.customerDAO.JDBCCustomerDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Customer;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.ColumnNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.CustomerNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.NoCustomerFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.HashMap;

/**
 * Created by  Stefan Puhalo on 19.05.17.
 */
@Service
public class CustomerServiceIMPL implements CustomerService {

    @Autowired
    private JDBCCustomerDAO customerDAO;

    @Autowired
    private JDBCCreditCardDAO creditCardDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceIMPL.class);

    @Override
    public Customer createCustomer(Customer customer) throws ServiceException {

        if(customer == null) {
            LOGGER.error("Create customer failed because customer is null.");
            throw new ServiceException("Customer is null.");
        }
        try {
            return customerDAO.create(customer);
        } catch (DAOException e) {
            LOGGER.error("Create customer failed.");
            throw new ServiceException("Create customer failed.");
        }
    }

    @Override
    public void updateCustomer(Customer customer) throws ServiceException {

        if(customer == null) {
            LOGGER.error("Update customer failed because customer is null.");
            throw new ServiceException("Customer is null.");
        }

        try {
            customerDAO.update(customer);
        } catch (DAOException e) {
            LOGGER.error("Update customer failed.");
            throw new ServiceException("Update customer failed.");
        }

    }

    @Override
    public void deleteCustomer(Customer customer) throws ServiceException {

        if(customer == null) {
            LOGGER.error("Delete customer failed because customer is null.");
            throw new ServiceException("Customer is null.");
        }

        try {
            customerDAO.delete(customer);
        }catch (DAOException e) {
            LOGGER.error("Delete customer failed.");
            throw new ServiceException("Delete customer failed.");
        }
    }

    @Override
    public void updateAllCustomers(ObservableList<Customer> customers) throws ServiceException {

        if(customers == null) {
            LOGGER.error("Update all customers failed because list of customers is null.");
            throw new ServiceException("List of customers is null.");
        }

        for(Customer customer : customers) {
            try {
                customerDAO.update(customer);
            } catch (DAOException e) {
                LOGGER.error("Update all customers failed.");
                throw new ServiceException("Update all customers failed.");
            }
        }
    }

    @Override
    public ObservableList<Customer> getAllCustomers() throws ServiceException {

        ObservableList<Customer> customers;

        try {
            customers =  customerDAO.findAll();
            for(Customer customer : customers) {
                if(customer.getCreditCard().getCnr() != null) {
                    customer.setCreditCard(creditCardDAO.findCreditCard(customer.getCreditCard().getCnr()));
                }
            }
        } catch (DAOException e) {
            LOGGER.error("Get all customers failed.");
            throw new ServiceException("Get all customers failed.");
        } catch (NoCustomerFoundException e) {
            LOGGER.error("No customer found.");
            throw new ServiceException("No customer found.");
        }
        return customers;
    }

    @Override
    public Customer getCustomer(Integer pid) throws ServiceException {

        if(pid == 0) {
            LOGGER.error("Get customer failed because pid is 0.");
            throw new ServiceException("Pid is 0.");
        }

        try {
            return customerDAO.find(pid);
        } catch (DAOException e) {
            LOGGER.error("Get customer failed.");
            throw new ServiceException("Get customer failed.");
        } catch (CustomerNotFoundException e) {
            LOGGER.error("Customer with id = " + pid + " not found.");
            throw new ServiceException("Customer with id = " + pid + " not found.");
        }

    }

    @Override
    public Customer checkCustomer(String name, String surname, Date dateOfBirth) throws ServiceException {

        if(name == null || surname == null || dateOfBirth == null) {
            LOGGER.error("Check customer failed because some of parameters is null.");
            throw new ServiceException("Check customer failed because some of parameters is null.");
        }

        try {
            return customerDAO.checkCustomer(name, surname, dateOfBirth);

        } catch (CustomerNotFoundException e) {
            LOGGER.error("Customer not found");
            throw new ServiceException("Customer not found.");
        }
    }

    @Override
    public ObservableList<Customer> getAllCustomersFromTo(String column, Object from, Object to) throws ServiceException {

        if(column == null || from == null || to == null) {
            LOGGER.error("Get all customers from to failed because some of parameters is null.");
            throw new ServiceException("Get all customers from to failed because some of parameters is null.");
        }

        try {
            return customerDAO.findAllFromTo(column, from, to);
        } catch (ColumnNotFoundException e) {
            LOGGER.error("Wrong column name given.");
            throw new ServiceException("Wrong column name given.");
        } catch (DAOException e) {
            LOGGER.error("Filtering table in getAllCustomersFromTo failed.");
            throw new ServiceException("Filtering table in getAllCustomersFromTo failed.");
        }
    }

    @Override
    public ObservableList<Customer> getAllCustomersForTypeFromTo(String column, String type, Object from, Object to) throws ServiceException {

        if(column == null || type == null || from == null || to == null) {
            LOGGER.error("Get all customers for type from to failed because some of parameters is null.");
            throw new ServiceException("Check customer failed because some of parameters is null.");
        }

        if(type != "Customer" && type != "Guest") {
            LOGGER.error("Find all customers for type to failed because type is invalid.");
            throw new ServiceException("Find all customers for type failed because type is invalid.");
        }

        try {
            return customerDAO.findAllFromToForType(column, type, from, to);
        } catch (ColumnNotFoundException e) {
            LOGGER.error("Column " + column + "not found.");
            throw new ServiceException("Column " + column + "not found.");
        } catch (DAOException e) {
            LOGGER.error("Filtering table in getAllCustomersForTypeFromTo failed.");
            throw new ServiceException("Filtering table in getAllCustomersForTypeFromTo failed.");
        }
    }

    @Override
    public ObservableList<Customer> getAllCustomersForType(String type) throws ServiceException {

        if(type != "Customer" && type != "Guest") {
            LOGGER.error("Find all customers for type to failed because type is invalid.");
            throw new ServiceException("Find all customers for type failed because type is invalid.");
        }

        if(type == null) {
            LOGGER.error("Get all customers for type failed because type is null.");
            throw new ServiceException("Get all customers for type failed because type is null.");
        }

        try {
            return customerDAO.findAllForType(type);
        } catch (NoCustomerFoundException e) {
            LOGGER.error("No customer for given type found.");
            throw new ServiceException("No customer for given type found.");
        } catch (DAOException e) {
            LOGGER.error("Filtering table in getAllCustomersForType failed.");
            throw new ServiceException("Filtering table in getAllCustomersForType failed.");
        }

    }

    @Override
    public HashMap<String, Integer> getReservationsOfAllCustomers() throws ServiceException {
        try {
            return customerDAO.reservationsOfAllCustomers();
        } catch (DAOException e) {
            LOGGER.error("GetReservationsOfALlCustomers failed.");
            throw new ServiceException("GetReservationsOfALlCustomers failed.");
        }
    }

    public void setCustomerDAO (JDBCCustomerDAO customerDAO){
        this.customerDAO =  customerDAO;
    }

}