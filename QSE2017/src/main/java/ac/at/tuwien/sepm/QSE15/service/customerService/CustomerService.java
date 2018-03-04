package main.java.ac.at.tuwien.sepm.QSE15.service.customerService;

import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Customer;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;

import java.sql.Date;
import java.util.HashMap;

/**
 * Created by Stefan Puhalo on 02.05.17.
 */
public interface CustomerService {

    /**
     * Inserts customer object into database
     * @param customer
     * @return inserted customer with set generated id
     * @throws ServiceException when DAOException is caught
     */
    Customer createCustomer(Customer customer) throws ServiceException;

    /**
     * Update customer object in database
     * @param customer
     * @throws ServiceException
     */
    void updateCustomer(Customer customer) throws ServiceException;

    /**
     * Delete customer from database
     * @param customer
     * @throws ServiceException if customer is null; when SQLException is caught
     */
    void deleteCustomer(Customer customer) throws ServiceException;

    /**
     * Update all customers from given list
     * @param customers list of customers to be updated
     * @throws ServiceException when DAOException is caught
     */
    void updateAllCustomers(ObservableList<Customer> customers) throws ServiceException;

    /**
     * Get all customers
     * @return list of all customers in database
     * @throws ServiceException if customer is null; when SQLException is caught
     */
    ObservableList<Customer> getAllCustomers() throws ServiceException;

    /**
     * Search for customer with given pid
     * @param pid
     * @return Customer object if found
     * @throws ServiceException if pid is 0; when SQLException is caught; if customer with this id is not found
     */
    Customer getCustomer(Integer pid) throws ServiceException;

    /**
     * Generic search method to filter the table
     * @param column name of customer table column
     * @param from for Name, Surname, Date of Birth, Country
     * @param to for Name, Surname, Date of Birth, Country
     * @return filtered list of customers
     * @throws ServiceException when SQLException is caught; when column name is not found
     */
    ObservableList<Customer> getAllCustomersFromTo(String column, Object from, Object to) throws ServiceException;


    /**
     * Generic search method to filter the table inclusive their type {customer, guest}
     * @param column name of customer table column
     * @param type customer or guest
     * @param from for Name, Surname, Date of Birth, Country
     * @param to for Name, Surname, Date of Birth, Country
     * @return filtered list of customers
     * @throws ServiceException when SQLException or ColumnNotFoundException are caught
     */
    ObservableList<Customer> getAllCustomersForTypeFromTo(String column, String type, Object from, Object to) throws ServiceException;

    /**
     * Search method to filters customers on their type {customer, guest}
     * @param type customer or guest
     * @return filtered list of customers
     * @throws ServiceException when SQLException is caught; if there is no customer for certain type
     */
    ObservableList<Customer> getAllCustomersForType(String type) throws ServiceException;


    /**
     * Check if there is a customer whit given name, surname and date of birth
     * @param name of searched customer
     * @param surname of searched customer
     * @param dateOfBirth of searched customer
     * @return customer object if found
     * @throws ServiceException when SQLException or CustomerNotFoundException is caught
     */
    Customer checkCustomer(String name, String surname, Date dateOfBirth) throws ServiceException;

    /**
     * Search all reservations made by customers
     * @return Map
     *             key: full name of customer
     *             value: id of his/her reservation
     * @throws ServiceException when SQLException is caught
     */
    HashMap<String, Integer> getReservationsOfAllCustomers() throws ServiceException;



}