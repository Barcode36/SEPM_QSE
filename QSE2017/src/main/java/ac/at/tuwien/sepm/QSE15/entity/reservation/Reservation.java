package main.java.ac.at.tuwien.sepm.QSE15.entity.reservation;

import java.sql.Date;

/**
 * Created by Bajram Saiti on 28.04.17.
 */
public abstract class Reservation {

    private Integer rid, costumerId;
    private Date from,until;
    private Long total, needsTObePaid;
    private Boolean isPaid, isCanceled;

    public Reservation(){

    }

    public Reservation(Integer rid, Integer costumerId, Date from, Date until, Long total, Boolean isPaid, Boolean isCanceled){

        this.rid = rid;
        this.costumerId = costumerId;
        this.from = from;
        this.until = until;
        this.total = total;
        this.isPaid = isPaid;
        this.isCanceled = isCanceled;
        this.needsTObePaid = total;

    }



    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getCostumerId() {
        return costumerId;
    }

    public void setCostumerId(Integer costumerId) {
        this.costumerId = costumerId;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getUntil() {
        return until;
    }

    public void setUntil(Date until) {
        this.until = until;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    public Boolean getCanceled() {
        return isCanceled;
    }

    public void setCanceled(Boolean canceled) {
        isCanceled = canceled;
    }

    public Long getNeedsTObePaid() {
        return needsTObePaid;
    }

    public void setNeedsTObePaid(Long needsTObePaid) {
        this.needsTObePaid = needsTObePaid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reservation that = (Reservation) o;

        if (rid != null ? !rid.equals(that.rid) : that.rid != null) return false;
        if (costumerId != null ? !costumerId.equals(that.costumerId) : that.costumerId != null) return false;
        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (until != null ? !until.equals(that.until) : that.until != null) return false;
        if (total != null ? !total.equals(that.total) : that.total != null) return false;
        if (isPaid != null ? !isPaid.equals(that.isPaid) : that.isPaid != null) return false;
        return isCanceled != null ? isCanceled.equals(that.isCanceled) : that.isCanceled == null;
    }

    @Override
    public int hashCode() {
        int result = rid != null ? rid.hashCode() : 0;
        result = 31 * result + (costumerId != null ? costumerId.hashCode() : 0);
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (until != null ? until.hashCode() : 0);
        result = 31 * result + (total != null ? total.hashCode() : 0);
        result = 31 * result + (isPaid != null ? isPaid.hashCode() : 0);
        result = 31 * result + (isCanceled != null ? isCanceled.hashCode() : 0);
        return result;
    }

}
