package main.java.ac.at.tuwien.sepm.QSE15.entity.newsletter;

import main.java.ac.at.tuwien.sepm.QSE15.entity.person.Employee;

/**
 * Created by ervincosic on 26/06/2017.
 */
public class Newsletter {

    private String subject;
    private String message;

    private Employee employee;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public boolean isNewsletterValid(){
        if(subject == null ){
            return false;
        }else if(subject.length() == 0){
            return false;
        }else if(message == null){
            return false;
        }else if(message.length() == 0){
            return false;
        }else if (employee == null){
            return false;
        }else{
            return true;
        }
    }

}