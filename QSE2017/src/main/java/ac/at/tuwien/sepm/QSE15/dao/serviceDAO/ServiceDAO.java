package main.java.ac.at.tuwien.sepm.QSE15.dao.serviceDAO;

import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.RoomReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;

import java.util.List;

/**
 * Created by Bajram Saiti on 02.05.17.
 *  Editet by Nemanja Vukoje on 06.05.17.

 */
public interface ServiceDAO {

    /**
     *
     * @param service Creates a new service
     * @return Created service
     * @throws DAOException - (1) in case a SQL Exception occurred
     *                        (2) in case the given service is a null pointer
     */
    Service create(Service service) throws DAOException;

    /**
     *
     * @param reservation ReservationAbst, that has services
     * @return List of all services
     * @throws DAOException - (1) in case a SQL Exception occurred
     *                        (2) in case the given service is a null pointer
     */
    List<Service> getAllServicesFromReservation(Reservation reservation) throws DAOException;

    /**
     *
     * @param service Service that we want to delte
     * @return deleted service
     * @throws DAOException - (1) in case a SQL Exception occurred
     *                        (2) in case the given service is a null pointer
     */
    Service delete(Service service) throws DAOException;

    /**
     * Description: This method will give us back the object Service by giving it only service id (srid)
     * @param service Service with srid (id) we want to get
     * @return Returns that Service with all his parameters.
     * @throws DAOException
     */
    Service get(Service service) throws DAOException;

    ObservableList<String> getAllServiceTypes();

    /**
     * Description: This method will edit some Service parameters.
     * @param service Service we want to edit
     * @throws DAOException
     */
    void update(Service service) throws DAOException;

    /**
     * Description: This method will be used to search Services using some filters.
     * @param service In this service we will save the filters inputs as price until, witch type , descriptions contains.
     * @return Returns the list of such Service objects.
     * @throws DAOException
     */
    List<Service> search(Service service) throws DAOException;
}
