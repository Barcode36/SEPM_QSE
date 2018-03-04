package main.java.ac.at.tuwien.sepm.QSE15.entity.hotel;

import java.sql.Date;

/**
 * Created by Bajram Saiti on 28.04.17.
 */
public class Hotel {

    private Integer hnr;
    private String name, address, iban, bic;

    private String email;

    private String password;

    private Date date;

    private String port;

    private String host;

    public Hotel(){

    }

    public Hotel(Integer hnr, String name, String address, String iban, String bic){
        this.hnr = hnr;
        this.name = name;
        this.address = address;
        this.iban = iban;
        this.bic = bic;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getHnr() {
        return hnr;
    }

    public void setHnr(Integer hnr) {
        this.hnr = hnr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
