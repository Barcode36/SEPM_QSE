package main.java.ac.at.tuwien.sepm.QSE15.entity.person;

import main.java.ac.at.tuwien.sepm.QSE15.entity.creditCard.CreditCard;

import java.sql.Date;

/**
 * Created by Bajram Saiti on 28.04.17.
 */
public class Customer extends Person {

    private String identification, note;
    private CreditCard creditCard;
    private Integer rid;
    private Boolean newsletter;


    public Customer(Integer pid, String name, String surname, String address, String zip, String place, String country,
                         String phone, String email, Date bdate, String sex, String identification, CreditCard creditCard, String note, Boolean newsletter){

        super(pid,name,surname,address,zip,place,country,phone,email,bdate,sex);
        this.identification = identification;
        this.creditCard = creditCard;
        this.note = note;
        this.newsletter = newsletter;

    }


    public Customer(Integer pid, String name, String surname, String address, String zip, String place, String country,
                    String phone, String email, Date bdate, String sex, String identification, String note, Boolean newsletter, Integer rid){

        super(pid,name,surname,address,zip,place,country,phone,email,bdate,sex);
        this.identification = identification;
        this.note = note;
        this.newsletter = newsletter;
        this.rid = rid;
    }


    public Customer(Integer pid, String name, String surname, String address, String zip, String place, String country,
                    String phone, String email, Date bdate, String sex, String identification, CreditCard creditCard, String note, Boolean newsletter, Integer rid){

        super(pid,name,surname,address,zip,place,country,phone,email,bdate,sex);
        this.identification = identification;
        this.creditCard = creditCard;
        this.note = note;
        this.newsletter = newsletter;
        this.rid = rid;

    }

    public Customer(){

    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Boolean getNewsletter() {
        return newsletter;
    }

    public void setNewsletter(Boolean newsletter) {
        this.newsletter = newsletter;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }
        Customer that = (Customer) obj;
        if(this.getPid()==null || that.getPid()==null){
            return false;
        }
        return that.getPid().equals(this.getPid());
    }

}
