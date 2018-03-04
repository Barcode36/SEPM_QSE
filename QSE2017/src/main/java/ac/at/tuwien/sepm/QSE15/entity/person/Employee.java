package main.java.ac.at.tuwien.sepm.QSE15.entity.person;

import java.sql.Date;

/**
 * Created by Bajram Saiti on 25.04.17.
 */
public class Employee extends Person {


    private String svnr;

    /**
     * The access rights of a user
     */
    private Integer rights;

    private Long salary;

    private Boolean isDeleted;

    private String picture;

    private String iban;

    private String role;

    private String bic;
    /**
     * Login data of a user
     */
    private String username;

    private String password;

    /**
     * This consturctor will be used for generating a non admin user the username and password are empty strings
     */
    public Employee(String name, String surname, String address, String zip,
                    String place, String country, String phone, String email, Date birthDay,
                    String sex, String svnr, String iban, String bic, Long salary,
                    String role, String picture, Boolean isDeleted, String username, String password, Integer rights) {
        super(name, surname, address, zip, place, country, phone, email, birthDay, sex);
        this.svnr = svnr;
        this.rights = rights; // 1 For rights is a non admin user
        this.salary = salary;
        this.isDeleted = isDeleted;
        this.picture = picture;
        this.iban = iban;
        this.role = role;
        this.bic = bic;
        this.username = username;
        this.password = password;
    }

    public Employee(Integer pid, String name, String surname, String address, String zip,
                    String place, String country, String phone, String email, Date birthDay,
                    String sex, String svnr, String iban, String bic, Long salary,
                    String role, String picture, Boolean isDeleted, String username, String password, Integer rights){
        super(pid, name, surname, address, zip, place, country, phone, email, birthDay, sex);
        this.svnr = svnr;
        this.iban = iban;
        this.bic = bic;
        this.role = role;
        this.salary = salary;
        this.picture = picture;
        this.isDeleted = isDeleted;
        this.username = username;
        this.password = password;
        this.rights = rights;
    }

    public Employee(Integer pid, String name, String surname, String address, String zip,
                    String place, String country, String phone, String email, Date  bdate,
                    String sex, String svnr, String iban, String bic, Long salary,
                    String role, String picture, Boolean isDeleted) {
        super(pid,name,surname,address,zip,place,country,phone,email,bdate,sex);
        this.svnr = svnr;
        this.iban = iban;
        this.bic = bic;
        this.role = role;
        this.salary = salary;
        this.picture = picture;
        this.isDeleted = isDeleted;
    }

    public Employee(String name, String surname, String address, String zip,
                    String place, String country, String phone, String email, Date  bdate,
                    String sex, String svnr, String iban, String bic, Long salary,
                    String role, String picture, Boolean isDeleted) {
        super(name,surname,address,zip,place,country,phone,email,bdate,sex);
        this.svnr = svnr;
        this.iban = iban;
        this.bic = bic;
        this.role = role;
        this.salary = salary;
        this.picture = picture;
        this.isDeleted = isDeleted;
    }

    public Employee() {

    }

    public Integer getRights() {
        return rights;
    }

    public void setRights(Integer rights) {
        this.rights = rights;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSvnr() {
        return svnr;
    }

    public void setSvnr(String svnr) {
        this.svnr = svnr;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }



    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }

        if((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }

        Employee that = (Employee) obj;

        if(this.getPid() == null || that.getPid() == null){
            return false;
        }

        return that.getPid().equals(this.getPid());
    }

}
