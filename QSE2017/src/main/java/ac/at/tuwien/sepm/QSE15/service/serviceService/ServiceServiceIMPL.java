package main.java.ac.at.tuwien.sepm.QSE15.service.serviceService;

import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.serviceDAO.ServiceDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Bajram Saiti on 02.05.17.
 */
@org.springframework.stereotype.Service
public class ServiceServiceIMPL implements ServiceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceServiceIMPL.class);

    @Autowired
    private ServiceDAO serviceDAO;

    @Override
    public Service create(Service service) throws ServiceException {

        if (service == null){
            LOGGER.error("Cannot create Service! Input value is null!");
            throw new ServiceException("Input parameter is null");
        }

        try {
            return serviceDAO.create(service);

        } catch (DAOException e) {
            LOGGER.error("Error in ServiceDAO Create");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Service> getAllServicesFromReservation(Reservation reservation) throws ServiceException {

        if (reservation == null){
            LOGGER.error("Cannot get Services! Input value is null!");
            throw new ServiceException("Input parameter is null");
        }

        try {
            return serviceDAO.getAllServicesFromReservation(reservation);

        } catch (DAOException e) {
            LOGGER.error("Error in ServiceDAO getAllServiceReservation");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Service delete(Service service) throws ServiceException {

        if (service == null){
            LOGGER.error("Cannot delete Service! Input value is null!");
            throw new ServiceException("Input parameter is null");
        }

        try {
            return serviceDAO.delete(service);

        } catch (DAOException e) {
            LOGGER.error("Error in ServiceDAO Delete");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Service get(Service service) throws ServiceException {

        if (service == null || service.getSrid() == null){
            LOGGER.error("Cannot get Service! Input value in null");
            throw new ServiceException("Input parameter is null");
        }

        try {
            return serviceDAO.get(service);

        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public ObservableList<String> getAllServiceTypes() {
        return serviceDAO.getAllServiceTypes();
    }

    public void update(Service service) throws ServiceException {
        if (service==null || service.getSrid()==null){
            LOGGER.error("Cannot get Service! Input value in null");
            throw new ServiceException("Input parameter is null");
        }
        try {
            serviceDAO.update(service);

        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Service> search(Service service) throws ServiceException {
        if (service==null){
            LOGGER.error("Cannot Search Service! Input value in null");
            throw new ServiceException("Input parameter is null");
        }
        try {
            return serviceDAO.search(service);

        } catch (DAOException e) {
            LOGGER.error("Error in ServiceDAO getAllServiceReservation");
            throw new ServiceException(e.getMessage());
        }
    }


}