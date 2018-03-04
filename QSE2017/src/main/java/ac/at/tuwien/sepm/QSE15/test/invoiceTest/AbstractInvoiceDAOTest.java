package main.java.ac.at.tuwien.sepm.QSE15.test.invoiceTest;

import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.invoiceDAO.InvoiceDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.invoice.Invoice;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.RoomReservation;
import org.junit.Test;
import java.util.LinkedList;
import static org.junit.Assert.assertEquals;

/**
 * Created by Luka on 5/11/17.
 */
abstract class AbstractInvoiceDAOTest {

    protected InvoiceDAO invoiceDAO;

    public void setInvoiceDAO(InvoiceDAO invoiceDAO){ this.invoiceDAO = invoiceDAO;}

    @Test(expected = DAOException.class)
    public void generateWithInvalidParametersShouldThrowException() throws DAOException {

            invoiceDAO.generateInvoice(null);
    }

    @Test(expected = DAOException.class)
    public void generateWithReservationIdThatDoesNotExistShouldThrowException() throws DAOException {

        RoomReservation reservation = new RoomReservation();
        reservation.setRid(100012);
        invoiceDAO.generateInvoice(reservation);
    }

    @Test
    public void generateWithValidParametersShouldWork()throws DAOException {

        RoomReservation reservation = new RoomReservation();
        Invoice returnedInvoice = new Invoice();

        reservation.setRid(2001); //has services


        returnedInvoice = invoiceDAO.generateInvoice(reservation);

        assertEquals(returnedInvoice.getName(), "Meredith");
        assertEquals(returnedInvoice.getRoomType(), "Suite");
        assertEquals(returnedInvoice.getServices().get(0).getDescription(), "from the airport");

        reservation.setRid(2000); //doesn't have services

        returnedInvoice = invoiceDAO.generateInvoice(reservation);

        assertEquals(returnedInvoice.getName(), "Brenna");
        assertEquals(returnedInvoice.getRoomType(), "Single Room");
        assertEquals(returnedInvoice.getServices(), new LinkedList<>());
    }
}
