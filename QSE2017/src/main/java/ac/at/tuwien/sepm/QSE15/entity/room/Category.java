package main.java.ac.at.tuwien.sepm.QSE15.entity.room;

/**
 * Created by Bajram Saiti on 28.04.17.
 */
public class Category {

    String name;
    Long price;
    Integer beds;

    public Category(){

    }

    public Category(String name, Long price, Integer beds){
        this.name = name;
        this.price = price;
        this.beds = beds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getBeds() {
        return beds;
    }

    public void setBeds(Integer beds) {
        this.beds = beds;
    }
}
