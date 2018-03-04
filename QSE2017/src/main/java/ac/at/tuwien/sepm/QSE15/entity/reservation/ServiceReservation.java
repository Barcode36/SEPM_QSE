package main.java.ac.at.tuwien.sepm.QSE15.entity.reservation;

import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;

import java.sql.Date;

/**
 * Created by Bajram Saiti on 28.04.17.
 */
public class ServiceReservation extends Reservation {

    private Service srid;
    private Date date;

    public Integer getServiceID() {
        return serviceID;
    }

    public void setServiceID(Integer serviceID) {
        this.serviceID = serviceID;
    }

    private Integer serviceID;

    public ServiceReservation(){

    }

    public ServiceReservation(Integer rid, Integer costumerId, Date from, Date until, Long total, Boolean isPaid, Boolean isCancled, Service srid, Date date){
        super(rid,costumerId,from,until,total,isPaid,isCancled);
        this.srid = srid;
        this.date = date;
        //this.serviceID = srid.getSrid();
    }

    public Service getSrid() {
        return srid;
    }

    public void setSrid(Service srid) {
        this.srid = srid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ServiceReservation that = (ServiceReservation) o;

        if (srid != null ? !srid.equals(that.srid) : that.srid != null) return false;
        return date != null ? date.equals(that.date) : that.date == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (srid != null ? srid.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
