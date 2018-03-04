package main.java.ac.at.tuwien.sepm.QSE15.service.serviceService;

import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;

import java.util.List;

/**
 * Created by Bajram Saiti on 02.05.17.
 */
public interface ServiceService {


    /**
     *
     * @param service Creats a new service
     * @return Created service
     * @throws ServiceException
     */
    Service create(Service service) throws ServiceException;

    /**
     *
     * @param reservation Reservation, that has services
     * @return List of all services
     * @throws ServiceException
     */
    List<Service> getAllServicesFromReservation(Reservation reservation) throws ServiceException;

    /**
     *
     * @param service Service that we want to delte
     * @return deleted service
     * @throws ServiceException
     */
    Service delete(Service service) throws ServiceException;

    Service get(Service service) throws ServiceException;

    /**
     * Created by Stefan Puhalo
     * @return
     */
    ObservableList<String> getAllServiceTypes();

    void update(Service service) throws ServiceException;

    List<Service> search(Service service) throws ServiceException;

}
