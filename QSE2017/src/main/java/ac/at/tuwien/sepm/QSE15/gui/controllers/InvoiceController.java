package main.java.ac.at.tuwien.sepm.QSE15.gui.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import main.java.ac.at.tuwien.sepm.QSE15.entity.invoice.Invoice;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.RoomReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.invoiceService.InvoiceServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import java.util.List;

/**
 * Created by Luka Veljkovic on 5/16/17.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class InvoiceController {
    public TextField reservationIdTextField;
    public TableView<Service> servicesTableView;
    public TableColumn <Service, String> serviceTableColumn, descritpionTableColumn;
    public TableColumn <Service, Long>priceTableColumn;
    public Text firstNameText, lastNameText, addressText, zipText, documentNumberText, eMailText, telephoneText,
    countryText, cityText, roomTypeText, pricePerNightText, fromText, toText, servicePriceText, totalPriceText;
    public MenuItem exportMenuItem;
    Integer ridForExport;

    private InvoiceServiceIMPL invoiceService;

    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceController.class);

    private AnnotationConfigApplicationContext context;

    public void initialize(){

        serviceTableColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        descritpionTableColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        priceTableColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
        ridForExport = 0;
    }

    public void setTable(List<Service> services){

        servicesTableView.setItems(FXCollections.observableArrayList(services));
    }

    public void exportMenuItemClicked() {

        Reservation reservation = new RoomReservation();

        context = new AnnotationConfigApplicationContext(this.getClass());
        invoiceService = context.getBean(InvoiceServiceIMPL.class);

        reservation.setRid(ridForExport);

        try {
            invoiceService.exportInvoice(reservation);
            LOGGER.info("Export clicked successful.");
        } catch (ServiceException e) {
            LOGGER.error("Export clicked fail.");
        }
    }

    public void loadButtonClicked() {

        if (reservationIdTextField.getText().matches("[0-9]+")){

            context = new AnnotationConfigApplicationContext(this.getClass());
            invoiceService = context.getBean(InvoiceServiceIMPL.class);

            Invoice invoice;
            Reservation roomReservation = new RoomReservation();
            List<Service> services;
            Long servicePrice = 0L;

            roomReservation.setRid(Integer.parseInt(reservationIdTextField.getText()));
            ridForExport = Integer.parseInt(reservationIdTextField.getText());

            try {
                invoice = invoiceService.generateInvoice(roomReservation);
                //enable button exportPdf
                LOGGER.info("Invoice is generated.");

                services = invoice.getServices();

                for (Service s : services){

                    s.setPrice(s.getPrice() / 100);
                }

                setTable(services);

                if (services != null) {

                    for (Service service : services) {

                        servicePrice += service.getPrice();
                        LOGGER.info("Calculating total service price.");
                    }
                }

                firstNameText.setText("First name: " + invoice.getName());
                lastNameText.setText("Last name: " + invoice.getSurname());
                addressText.setText("Address: " + invoice.getAddress());
                zipText.setText("ZIP: " + invoice.getZip());
                documentNumberText.setText("Document number: " + invoice.getIdentification());
                eMailText.setText("E-mail: " + invoice.getEmail());
                telephoneText.setText("Telephone: " + invoice.getPhone());
                countryText.setText("Country: " + invoice.getCountry());
                cityText.setText("City: " + invoice.getPlace());
                roomTypeText.setText("Room type: " + invoice.getRoomType());
                pricePerNightText.setText("Price per night: " + (invoice.getRoomPrice() / 100) + "€");
                fromText.setText("From: " + invoice.getFromDate());
                toText.setText("To: " + invoice.getUntilDate());
                servicePriceText.setText("Service price: " + servicePrice + "€");
                totalPriceText.setText("Total price: " + ((invoice.getRoomPrice() * invoice.getNumberOfDays() / 100) + servicePrice) + "€");
                exportMenuItem.setDisable(false);

            } catch (ServiceException e) {

                exportMenuItem.setDisable(true);
                servicesTableView.getItems().clear();
                firstNameText.setText("First name: ____________________________");
                lastNameText.setText("Last name: ____________________________");
                addressText.setText("Address: ______________________________");
                zipText.setText("ZIP: ___________________________________");
                documentNumberText.setText("Document number: ___________________");
                eMailText.setText("E-mail: ________________________________");
                telephoneText.setText("Telephone: ____________________________");
                countryText.setText("Country: ______________________________");
                cityText.setText("City: __________________________________");
                roomTypeText.setText("Room type: ____________________________________________________");
                pricePerNightText.setText("Price per night: ________________________________________________");
                fromText.setText("From: __________________________________________________________");
                toText.setText("To: _____________________________________________________________");
                servicePriceText.setText("Service price: ________________");
                totalPriceText.setText("Total price: __________________");

                Utility.showAlert("Reservation not found.", Alert.AlertType.ERROR);
                LOGGER.error("Error while generating invoice.");
            }

        } else{

            exportMenuItem.setDisable(true);
            Utility.showAlert("Entered value is not an integer number.", Alert.AlertType.ERROR);
            LOGGER.error("Entered value is not an integer number.");
        }
    }

    public void reservationIdTextFieldKeyPressed() {

        exportMenuItem.setDisable(true);
    }
}
