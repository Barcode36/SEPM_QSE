package main.java.ac.at.tuwien.sepm.QSE15.service.invoiceService;

import main.java.ac.at.tuwien.sepm.QSE15.entity.invoice.Invoice;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;

/**
 * Created by Luka on 5/8/17.
 */
public interface InvoiceService {

    /**
     * @param reservation that we need to generate invoice for
     * @return generated invoice
     * @throws ServiceException if reservation ID not found
     * @author Luka Veljkovic
     */
    Invoice generateInvoice(Reservation reservation) throws ServiceException;

    /**
     * @param reservation that we need to export invoice for
     * @return invoice that we need to export
     * @throws ServiceException if reservation ID not found
     * @author Luka Veljkovic
     */
    Invoice exportInvoice(Reservation reservation) throws ServiceException;
}
