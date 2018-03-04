package main.java.ac.at.tuwien.sepm.QSE15.test.stubs;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.customerDAO.JDBCCustomerDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Stefan Puhalo on 6/21/2017.
 */
public class CustomerDaoStub extends JDBCCustomerDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDaoStub.class);


    @Override
    public void update(Customer customer) throws DAOException {
        LOGGER.info("Update method in EmployeeDaoStub invoked.");
        customer.setName("John");
    }

    @Override
    public void delete(Customer customer)throws DAOException {
        customer.setName(null);
    }




}
