package main.java.ac.at.tuwien.sepm.QSE15.entity.person;

import java.sql.Date;

/**
 * Created by Bajram Saiti on 25.04.17.
 */
public abstract class Person {



    private Integer pid;
    private String name, surname, address, zip, place, country, phone, email, sex;
    private Date bdate;

    public Person(){

    }

    public Person(String name, String surname, String address, String zip, String place, String country, String phone, String email, Date  bdate, String sex){

        this.name = name;
        this.surname = surname;
        this.address = address;
        this.zip = zip;
        this.place = place;
        this.country = country;
        this.phone = phone;
        this.email = email;
        this.bdate = bdate;
        this.sex = sex;

    }

    public Person(Integer pid, String name, String surname, String address, String zip, String place, String country, String phone, String email, Date bdate, String sex){

        this.pid = pid;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.zip = zip;
        this.place = place;
        this.country = country;
        this.phone = phone;
        this.email = email;
        this.bdate = bdate;
        this.sex = sex;

    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date  getBdate() {
        return bdate;
    }

    public void setBdate(Date  bdate) {
        this.bdate = bdate;
    }
}
