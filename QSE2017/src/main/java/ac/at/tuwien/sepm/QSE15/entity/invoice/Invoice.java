package main.java.ac.at.tuwien.sepm.QSE15.entity.invoice;

import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.ServiceReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;

import java.sql.Date;
import java.util.List;

/**
 * Created by Luka on 5/8/17.
 */
public class Invoice {

    private Integer reservationId;
    private Long roomPrice;
    private String name, surname, hotelName, address, zip, place, country, phone, email, identification, roomType;
    private Date fromDate, untilDate;
    private List<Service> services;
    private List<ServiceReservation> serviceReservations;

    public Invoice (){}

    public Invoice(Integer reservationId, Long roomPrice, String name, String surname,
                   Date fromDate, Date untilDate, List<Service> services, List<ServiceReservation> serviceReservations,
                   String address, String zip, String place, String country, String phone, String email,
                   String identification, String hotelName, String roomType){

        this.reservationId = reservationId;
        this.roomPrice = roomPrice;
        this.name = name;
        this.surname = surname;
        this.fromDate = fromDate;
        this.untilDate = untilDate;
        this.services = services;
        this.serviceReservations = serviceReservations;
        this.address = address;
        this.zip = zip;
        this.place = place;
        this.country = country;
        this.phone = phone;
        this.email = email;
        this.identification = identification;
        this.hotelName = hotelName;
        this.roomType = roomType;
    }

    private static int daysBetween(Date one, Date two) {

        long difference = 0;

        if (one != null && two != null) {

            difference = (one.getTime() - two.getTime()) / 86400000;
        }

        return (int) -difference;
    }

    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public Long getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(Long roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(Date untilDate) {
        this.untilDate = untilDate;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<ServiceReservation> getServiceReservations() {
        return serviceReservations;
    }

    public void setServiceReservations(List<ServiceReservation> serviceReservations) {
        this.serviceReservations = serviceReservations;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Integer getNumberOfDays() {
        return daysBetween(fromDate, untilDate);
    }

}