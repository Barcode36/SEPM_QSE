package main.java.ac.at.tuwien.sepm.QSE15.test.stubs;

import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.employeeDAO.EmployeeDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.employeeDAO.JDBCEmployeeDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.ColumnNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.EmployeeNotFoundException;
import main.java.ac.at.tuwien.sepm.QSE15.exceptions.NoEmployeesFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Date;

/**
 * Created by ervincosic on 08/05/2017.
 * extended by Stefan Puhalo on 21/06/2017
 */
public class EmployeeDaoStub extends JDBCEmployeeDAO{

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDaoStub.class);


    @Override
    public Employee findWithUsernameAndPassword(Employee employee) throws DAOException, EmployeeNotFoundException {

        Employee toReturn = new Employee(123,"Stefan","Puhalo","Address1", "1090",
                "Vienna","AT","066038381959",
                "stefanpuhalo@gmail.com", Date.valueOf("1983-01-01"),"man","219491491249",
                "561828000014958","BKAUATW",185000L, "Manager",
                "/stefan.jpg",false, "stefan","stefan123",1);
        if(employee.getUsername().equals(toReturn.getUsername()) && employee.getPassword().equals(toReturn.getPassword())){
            return toReturn;
        }else{
            throw new EmployeeNotFoundException("No such user in the database.");
        }

    }

    @Override
    public void update(Employee employee) throws DAOException {
        LOGGER.info("Update method in EmployeeDaoStub invoked.");
        employee.setName("John");
    }

    @Override
    public void delete(Employee employee) throws DAOException {
        LOGGER.info("Delete method in EmployeeDaoStub invoked.");
        employee.setDeleted(true);
    }
}
