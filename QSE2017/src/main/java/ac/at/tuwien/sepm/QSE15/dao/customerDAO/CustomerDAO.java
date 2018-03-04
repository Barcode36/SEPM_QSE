package main.java.ac.at.tuwien.sepm.QSE15.dao.customerDAO;

import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Customer;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.ColumnNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.CustomerNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.NoCustomerFoundException;

import java.sql.Date;
import java.util.HashMap;

/**
 * Created by Stefan Puhalo on 28.04.17.
 */
public interface CustomerDAO {

    /**
     * Inserts customer object into database
     * @param customer
     * @return inserted customer with set generated id
     * @throws DAOException if customer is null; when SQLException is caught
     */
    Customer create(Customer customer) throws DAOException;

    /**
     * Updates details about customer in database
     * @param customer
     * @throws DAOException if customer is null; when SQLException is caught
     */
    void update(Customer customer) throws DAOException;

    /**
     * Delete customer from database
     * @param customer
     * @throws DAOException if customer is null; when SQLException is caught
     */
    void delete(Customer customer) throws DAOException;

    /**
     * Search all customers
     * @return list of all customers in database
     * @throws DAOException if customer is null; when SQLException is caught
     * @throws NoCustomerFoundException if no customer is found in database
     */
    ObservableList<Customer> findAll() throws DAOException, NoCustomerFoundException;

    /**
     * Search for customer with given pid
     * @param pid
     * @return Customer object if found
     * @throws DAOException if pid is 0; when SQLException is caught
     * @throws CustomerNotFoundException if customer with this id is not found
     */
    Customer find(Integer pid) throws DAOException, CustomerNotFoundException;

    /**
     * Generic search method to filter the table
     * @param column name of customer table column
     * @param from for Name, Surname, Date of Birth, Country
     * @param to for Name, Surname, Date of Birth, Country
     * @return filtered list of customers
     * @throws DAOException when SQLException is caught
     * @throws ColumnNotFoundException when column name is not found
     */
    ObservableList<Customer> findAllFromTo(String column, Object from, Object to) throws DAOException, ColumnNotFoundException;

    /**
     * Generic search method to filter the table inclusive their type {customer, guest}
     * @param column name of customer table column
     * @param type customer or guest
     * @param from for Name, Surname, Date of Birth, Country
     * @param to for Name, Surname, Date of Birth, Country
     * @return filtered list of customers
     * @throws DAOException when SQLException is caught
     * @throws ColumnNotFoundException when column name is not found
     */
    ObservableList<Customer> findAllFromToForType(String column, String type, Object from, Object to) throws DAOException, ColumnNotFoundException;

    /**
     * Search method to filters customers on their type {customer, guest}
     * @param type customer or guest
     * @return filtered list of customers
     * @throws DAOException when SQLException is caught
     * @throws NoCustomerFoundException if there is no customer for certain type
     */
    ObservableList<Customer> findAllForType(String type) throws DAOException, NoCustomerFoundException;

    /**
     * Check if there is a customer whit given name, surname and date of birth
     * @param name of searched customer
     * @param surname of searched customer
     * @param dateOfBirth of searched customer
     * @return customer object if found
     * @throws DAOException when SQLException is caught
     * @throws CustomerNotFoundException if no customer with given details is found
     */
    Customer checkCustomer(String name, String surname, Date dateOfBirth) throws DAOException, CustomerNotFoundException;

    /**
     * Search all reservations made by customers
     * @return Map
     *             key: full name of customer
     *             value: id of his/her reservation
     * @throws DAOException when SQLException is caught
     */
    HashMap<String, Integer> reservationsOfAllCustomers() throws DAOException;


}
