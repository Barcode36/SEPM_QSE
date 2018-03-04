package main.java.ac.at.tuwien.sepm.QSE15.entity.room;

/**
 * Created by Bajram Saiti on 28.04.17.
 */
public class Room {

    private Integer rnr;
    private String category, extras;
    private Long price;

    public Room(){

    }

    public Room(Integer rnr, String category, Long price, String extras){
        this.rnr = rnr;
        this.category = category;
        this.price = price;
        this.extras = extras;
    }

    public Integer getRnr() {
        return rnr;
    }

    public void setRnr(Integer rnr) {
        this.rnr = rnr;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;

        if (rnr != null ? !rnr.equals(room.rnr) : room.rnr != null) return false;
        if (category != null ? !category.equals(room.category) : room.category != null) return false;
        if (extras != null ? !extras.equals(room.extras) : room.extras != null) return false;
        return price != null ? price.equals(room.price) : room.price == null;
    }

    @Override
    public int hashCode() {
        int result = rnr != null ? rnr.hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (extras != null ? extras.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }
}
