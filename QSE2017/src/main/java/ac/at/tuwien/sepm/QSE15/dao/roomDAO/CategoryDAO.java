package main.java.ac.at.tuwien.sepm.QSE15.dao.roomDAO;

import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Category;

import java.util.List;

/**
 * Created by ivana on 29.5.2017.
 */
public interface CategoryDAO {

     /**
      * Description: With this method we will create a new Category in Database.
      * @param c This is a object we want to create.
      * @return If we created it in database wi will return back the created object.
      * @throws DAOException
      */
     Category create(Category c) throws DAOException;

     /**
      * Description: This method is used to Edit some already existing Category.
      * @param c In this object are saved new date we want do update.
      * @throws DAOException
      */
     void update(Category c) throws DAOException;

     /**
      * Description: This method will give us back the Category only by giving it its name:
      * @param name Name of Category we want to get.
      * @return Returns found Category from database.
      * @throws DAOException
      */
     Category get(String name) throws DAOException;

     /**
      * Description: With this method we will get all existing Categories from our Database.
      * @return Returns a list of All Category objects from Database.
      * @throws DAOException
      */
     List<Category> getAll() throws DAOException;

     /**
      * Description: This method deletes some existing category.
      * @param c This is the Category we want to delete.
      * @throws DAOException
      */
     void delete(Category c) throws DAOException;

     ObservableList<String> getAllRoomCategories();

     void updatePriceOfRoomCategory(String name, String operation, Double amount);

     Category getCategoryFromName(String name) throws DAOException;

}
