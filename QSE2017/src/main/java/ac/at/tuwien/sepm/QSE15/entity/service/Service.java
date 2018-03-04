package main.java.ac.at.tuwien.sepm.QSE15.entity.service;

/**
 * Created by Bajram Saiti on 28.04.17.
 */
public class Service {

    private Integer srid;
    private String type, description;
    private Long price;

    public Service(){

    }

    public Service(Integer srid, String type, String description, Long price){
        this.srid = srid;
        this.type = type;
        this.description = description;
        this.price = price;
    }

    public Integer getSrid() {
        return srid;
    }

    public void setSrid(Integer srid) {
        this.srid = srid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
