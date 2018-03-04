package main.java.ac.at.tuwien.sepm.QSE15.dao.invoiceDAO;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.entity.invoice.Invoice;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;

/**
 * Created by Luka on 5/8/17.
 */
public interface InvoiceDAO {

    /**
     *
     * @param reservation that we need to generate invoice for
     * @return generated invoice
     * @throws DAOException if reservation ID is not found
     * @author Luka Veljkovic
     */
    Invoice generateInvoice (Reservation reservation) throws DAOException;

}