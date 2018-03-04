package main.java.ac.at.tuwien.sepm.QSE15.entity.creditCard;

/**
 * Created by Bajram Saiti on 28.04.17.
 */
public class CreditCard {

    private Integer expMonth, expYear;
    private String cnr, holder, cardType, cvv;

    public CreditCard(String cnr) {
        this.cnr = cnr;
    }

    public CreditCard(){

    }


    public CreditCard(String cnr, String holder, String cardType, Integer expMonth, Integer expYear, String cvv){
        this.cnr = cnr;
        this.holder = holder;
        this.cardType = cardType;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.cvv = cvv;
    }

    public Integer getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(Integer expMonth) {
        this.expMonth = expMonth;
    }

    public Integer getExpYear() {
        return expYear;
    }

    public void setExpYear(Integer expYear) {
        this.expYear = expYear;
    }

    public String getCnr() {
        return cnr;
    }

    public void setCnr(String cnr) {
        this.cnr = cnr;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
