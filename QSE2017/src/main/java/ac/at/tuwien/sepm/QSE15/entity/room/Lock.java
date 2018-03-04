package main.java.ac.at.tuwien.sepm.QSE15.entity.room;

import java.sql.Date;

/**
 * Created by Ivana on 08/05/2017.
 */
public class Lock {

    private Integer lid, rnr;
    private String reason;
    private Date locked_from,locked_until;

    public Lock(){}

    public Lock(Integer lid, Integer rnr, String reason, Date locked_from, Date locked_until) {
        this.lid = lid;
        this.rnr = rnr;
        this.reason = reason;
        this.locked_from = locked_from;
        this.locked_until = locked_until;
    }

    public Integer getLid() {
        return lid;
    }

    public void setLid(Integer lid) {
        this.lid = lid;
    }

    public Integer getRnr() {
        return rnr;
    }

    public void setRnr(Integer rnr) {
        this.rnr = rnr;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getLocked_from() {
        return locked_from;
    }

    public void setLocked_from(Date locked_from) {
        this.locked_from = locked_from;
    }

    public Date getLocked_until() {
        return locked_until;
    }

    public void setLocked_until(Date locked_until) {
        this.locked_until = locked_until;
    }
}
