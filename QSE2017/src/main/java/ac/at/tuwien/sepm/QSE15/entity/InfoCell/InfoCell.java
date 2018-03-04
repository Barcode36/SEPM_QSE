package main.java.ac.at.tuwien.sepm.QSE15.entity.InfoCell;

import com.sun.org.apache.regexp.internal.RE;
import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Customer;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.service.paymentService.PaymentServiceIMPL;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by Nemanja Vukoje on 19/05/2017.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class InfoCell {

    private String dataForCell;
    private Reservation reservation;
    private PaymentServiceIMPL paymentServiceIMPL;
    private String status;

    public InfoCell() {

        dataForCell = " ";
        status = " ";
    }

    public String getDataForCell() {
        return dataForCell;
    }

    public void setDataForCell(String dataForCell) {
        this.dataForCell = dataForCell;
    }

    @Override
    public String toString() {
        return dataForCell;
    }

    public String getStatusFromInteger(Integer status){

        switch (status) {

            case 0: this.status = "Arriving [not paid]";
                break;

            case 10: this.status = "Checked-in [not paid]";
                break;

            case 100: this.status = "Arriving [paid]";
                break;

            case 110: this.status = "Checked-in [paid]";
                break;

            case 1110: this.status = "Check-out";
                break;

            case 10000: this.status = "Canceled";
                break;

            default: this.status = "Error";
                break;
        }

        return this.status;
    }

    public void setValues(Reservation reservation, Customer customer, Integer status){

        this.status = getStatusFromInteger(status);
        this.reservation = reservation;
        this.dataForCell = "ID: " + reservation.getRid() + "\n" + customer.getName() + " " + customer.getSurname()+"\n"
                + "Status: " + this.status;
    }
}