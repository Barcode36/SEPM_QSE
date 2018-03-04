package main.java.ac.at.tuwien.sepm.QSE15.dao.roomDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.connectionDAO.JDBCSingletonConnection;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivana on 29.5.2017.
 */
@Repository
public class JDBCCategoryDAO implements CategoryDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCCategoryDAO.class);

    @Autowired
    private JDBCSingletonConnection singleton;

    Connection conn;

    @PostConstruct
    public void initCategory() {

        try {
            conn = singleton.getConnection();
            LOGGER.info("Getting connection successfully");
        } catch (DAOException e) {
            LOGGER.error("Unable to get Connection");
        }
    }

    @Override
    public Category create(Category c) throws DAOException {

        try {
            PreparedStatement pst = conn.prepareStatement("INSERT INTO Category VALUES (?, ?, ?);");

            pst.setString(1, c.getName());
            pst.setLong(2, c.getPrice());
            pst.setInt(3, c.getBeds());

            pst.execute();
            conn.commit();
            LOGGER.info("Category created with success");

            pst.close();

        } catch (SQLException e) {
            LOGGER.error("Unable to create Category");
            throw new DAOException(e.getMessage());
        }

        return c;
    }

    @Override
    public void update(Category c) throws DAOException {

        try {
            PreparedStatement pst = conn.prepareStatement("UPDATE Category SET price = ? WHERE name = ?;");

            pst.setLong(1, c.getPrice());
            pst.setString(2, c.getName());

            pst.executeUpdate();
            conn.commit();
            LOGGER.info("Category updated with success");

            pst.close();

        } catch (SQLException e) {
            LOGGER.error("Unable to update Category.");
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public Category get(String name) throws DAOException {

        Category category = new Category();

        try {

            String stm = "SELECT price, beds FROM Category WHERE name = '" + name + "';";

            ResultSet rs = conn.createStatement().executeQuery(stm);
            rs.next();

            category.setName(name);
            category.setPrice(rs.getLong(1));
            category.setBeds(rs.getInt(2));

            LOGGER.info("Get Category successful");
            rs.close();

            return category;

        } catch (SQLException e) {
            LOGGER.error("Unable to get Category");
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public List<Category> getAll() throws DAOException {

        List<Category> list = new ArrayList<>();

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT name, price, beds FROM Category;");

            while (rs.next()){

                list.add(new Category(rs.getString(1), rs.getLong(2), rs.getInt(3)));
            }

            LOGGER.info("Get all Categories successful");
            rs.close();
            stm.close();

            return list;

        } catch (SQLException e) {
            LOGGER.error("Unable to get all Categories");
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public void delete(Category c) throws DAOException {

        try {
            PreparedStatement pst = conn.prepareStatement("DELETE FROM Category WHERE name = ?;");

            pst.setString(1,c.getName());
            pst.execute();
            conn.commit();

            pst.close();

            LOGGER.info("Category deleted with success");

        } catch (SQLException e) {
            LOGGER.error("Unable to delete Category");
            throw new DAOException(e.getMessage());
        }
    }


    /**
     * Created by Stefan Puhalo on 12.06.2017
     * @return all room categories
     */
    @Override
    public ObservableList<String> getAllRoomCategories() {

        ObservableList<String> categories = FXCollections.observableArrayList();

        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT DISTINCT Room_Category FROM ROOM;");
            while(rs.next()) {
                categories.add(rs.getString(1));
            }
        } catch (SQLException e) {
            LOGGER.error("Retrieving categories failed");
        }

        return categories;

    }

    /**
     * Created by Stefan Puhalo on 18.06.2017
     * This method changes price for given room category
     */
    @Override
    public void updatePriceOfRoomCategory(String name, String operation, Double amount) {

        try {
            PreparedStatement pstmt = conn.prepareStatement("UPDATE CATEGORY SET PRICE = PRICE " + operation
                    + " " + amount + " WHERE NAME = ?");
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            LOGGER.error("Update price of room category " + name + "failed.");
        }
    }


    /**
     * Get category object from name of category
     */
    @Override
    public Category getCategoryFromName(String name) throws DAOException {

        Category category = null;

        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Category WHERE Name = ?");
            pstmt.setString(1,name);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                category = new Category(rs.getString(1), rs.getLong(2), rs.getInt(3));
            }

        } catch (SQLException e) {
            LOGGER.error("Get category from name failed.");
            throw new DAOException("Get category from name failed.");
        }

        return category;
    }

}
