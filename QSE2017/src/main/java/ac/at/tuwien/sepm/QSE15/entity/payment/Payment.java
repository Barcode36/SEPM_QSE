package main.java.ac.at.tuwien.sepm.QSE15.entity.payment;

import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;

import java.sql.Date;

/**
 * Created by Bajram Saiti on 02.05.17.
 */
public class Payment {

    private Integer payId;
    private Integer reservation;
    private Long paidAmount;
    private String paymentMethod;
    private Date date;


    public Payment(){

    }

    public Payment(Integer payId, Integer reservation, Long paidAmount, String paymentMethod, Date date){
        this.payId = payId;
        this.reservation = reservation;
        this.paidAmount = paidAmount;

        this.paymentMethod = paymentMethod;
        this.date = date;
    }

    public Integer getPayId() {
        return payId;
    }

    public void setPayId(Integer payId) {
        this.payId = payId;
    }

    public Integer getReservation() {
        return reservation;
    }

    public void setReservation(Integer reservation) {
        this.reservation = reservation;
    }

    public Long getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Long paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
