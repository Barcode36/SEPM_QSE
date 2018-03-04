package main.java.ac.at.tuwien.sepm.QSE15.service.invoiceService;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.invoiceDAO.InvoiceDAO;
import main.java.ac.at.tuwien.sepm.QSE15.dao.invoiceDAO.JDBCInvoiceDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.invoice.Invoice;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Luka on 5/8/17.
 */

@Service
public class InvoiceServiceIMPL implements InvoiceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceServiceIMPL.class);
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    @Autowired
    private JDBCInvoiceDAO JDBCInvoiceDAO;

    @Override
    public Invoice generateInvoice(Reservation reservation) throws ServiceException {

        try {

            return JDBCInvoiceDAO.generateInvoice(reservation);
        } catch (Exception e) {

            LOGGER.error("Error while generating pdf in InvoiceServiceIMPL.");
            throw new ServiceException();
        }
    }

    @Override
    public Invoice exportInvoice(Reservation reservation) throws ServiceException {

        Long allServicePrice = 0L;
        Invoice invoice;
        try {
            invoice = JDBCInvoiceDAO.generateInvoice(reservation);
        } catch (DAOException e) {

            LOGGER.error("Error while exporting pdf in InvoiceServiceIMPL.");
            throw new ServiceException();
        }

        DirectoryChooser chooser = new DirectoryChooser();
        File selectedDirectory = chooser.showDialog(null);

        LOGGER.info("Choosing directory...");

        File file;
        file = new File(selectedDirectory.getAbsolutePath() + "/Reservation-" + reservation.getRid() + ".pdf");

        try {

            LocalDateTime now = LocalDateTime.now();
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            OutputStream out = new FileOutputStream(file);
            Font timesRomanBig = new Font(Font.FontFamily.TIMES_ROMAN, 30, Font.BOLD, new BaseColor(0, 0, 128));
            Font courier = new Font(Font.FontFamily.COURIER, 13, Font.NORMAL, new BaseColor(0, 0, 0));
            Font helvetica = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, new BaseColor(112, 128, 144));
            Font undefined = new Font(Font.FontFamily.UNDEFINED, 12, Font.NORMAL, new BaseColor(0, 0, 0));
            Font timesRomanSmall = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD, new BaseColor(0, 0, 128));
            Paragraph title = new Paragraph("INVOICE ", timesRomanBig);
            Paragraph currentDate = new Paragraph(dtf.format(now));
            Paragraph reservationNumber = new Paragraph("reservation number: " + reservation.getRid(), courier);
            Paragraph servicePrice = new Paragraph("", courier);
            Paragraph roomInfo = new Paragraph("", courier);
            Paragraph totalPrice = new Paragraph("", timesRomanSmall);
            Paragraph farewell = new Paragraph("", undefined);
            Paragraph guestInfo = new Paragraph("", helvetica);
            PdfPTable table = new PdfPTable(4);
            PdfPCell cell;
           // Image img = Image.getInstance("QSE2017/src/res/images/logoHMS.png");
            Image img = Image.getInstance("QSE2017/src/res/images/logo.jpg");


            document.open();
            title.setAlignment(Element.ALIGN_CENTER);
            currentDate.setAlignment(Element.ALIGN_RIGHT);
            img.scaleAbsolute(100, 50);
            reservationNumber.setAlignment(Element.ALIGN_RIGHT);
            guestInfo.setAlignment(Element.ALIGN_LEFT);
            title.add(img);

            if (!invoice.getServices().isEmpty()) {

                cell = new PdfPCell(new Phrase("                                                    Additional services"));
                cell.setColspan(10);
                cell.setVerticalAlignment(5);
                table.addCell(cell);
                table.addCell("type");
                table.addCell("description");
                table.addCell("date");
                table.addCell("price");

                LOGGER.info("Table created.");

                for (int i = 0; i < invoice.getServices().size(); i++) { //adding services

                    allServicePrice += invoice.getServices().get(i).getPrice();
                    table.addCell(invoice.getServices().get(i).getType());
                    table.addCell(invoice.getServices().get(i).getDescription());
                    table.addCell(invoice.getServiceReservations().get(i).getDate() + "");
                    table.addCell((invoice.getServices().get(i).getPrice() / 100) + "€");

                    LOGGER.info("Adding services.");
                }
            }

            servicePrice.add("Service price sum:  " + (allServicePrice / 100) + "€");
            servicePrice.setAlignment(Element.ALIGN_CENTER);
            guestInfo.add(invoice.getName() + " " + invoice.getSurname() +
                    "\nDocument nr: " + invoice.getIdentification() + "\n" +
                    (invoice.getEmail() == null || invoice.getEmail().equals("") || invoice.getEmail().equals(" ") ? "" : (invoice.getEmail() + "\n")) +
                    (invoice.getPhone() == null || invoice.getPhone().equals("") || invoice.getPhone().equals(" ")? "" :  (invoice.getPhone() + "\n"))  +
                    (invoice.getAddress() == null || invoice.getAddress().equals("") || invoice.getAddress().equals(" ") ? "" : (invoice.getAddress() + "\n")) +
                    (invoice.getZip() == null || invoice.getZip().equals("") || invoice.getZip().equals(" ")? "" : (invoice.getZip() + "\n")) +
                    (invoice.getPlace() == null || invoice.getPlace().equals("")|| invoice.getPlace().equals(" ") ? "" : (invoice.getPlace() + "\n")) +
                    (invoice.getCountry() == null || invoice.getCountry().equals("")|| invoice.getCountry().equals(" ") ? "" : (invoice.getCountry() + "\n\n\n\n")));
            roomInfo.add("\nRoom type: " + invoice.getRoomType() + "\nDate from " +
                    invoice.getFromDate() + " to " + invoice.getUntilDate() + "\nPrice of the room per night: " +
                    (invoice.getRoomPrice() / 100) + "€" + "\nNumber of nights: " + invoice.getNumberOfDays() + "\nRoom price sum: " +
                    ((invoice.getRoomPrice() * invoice.getNumberOfDays()) / 100) + "€");
            totalPrice.add("\n\nTotal price: " + ((allServicePrice + invoice.getRoomPrice() * invoice.getNumberOfDays()) / 100) + "€");
            totalPrice.setAlignment(Element.ALIGN_RIGHT);
            farewell.add("\n\nWith compliments, \n" + invoice.getHotelName());
            farewell.setAlignment(Element.ALIGN_RIGHT);
            document.add(title);
            document.add(currentDate);
            document.add(reservationNumber);
            document.add(guestInfo);
            document.add(table);
            if (!invoice.getServices().isEmpty()) document.add(servicePrice);
            document.add(roomInfo);
            document.add(totalPrice);
            document.add(farewell);
            document.close();
            out.close();

            Utility.showAlert("Pdf document created successfully.", Alert.AlertType.INFORMATION);
            LOGGER.info("Pdf document created successfully.");

        } catch (Exception e){

            Utility.showAlert("Error while creating a new .pdf document.", Alert.AlertType.ERROR);
            LOGGER.error("Error while creating a new .pdf document.");
            throw new ServiceException();
        }

        return invoice;
    }
}
