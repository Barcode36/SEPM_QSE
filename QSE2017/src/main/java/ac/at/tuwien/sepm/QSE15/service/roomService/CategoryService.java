package main.java.ac.at.tuwien.sepm.QSE15.service.roomService;

import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Category;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;

import java.util.List;

/**
 * Created by ivana on 29.5.2017.
 */
public interface CategoryService {

    Category create(Category c) throws ServiceException;

    void update(Category c) throws ServiceException;

    Category get(String name) throws ServiceException;

    List<Category> getAll() throws ServiceException;

    void delete(Category c) throws ServiceException;

    ObservableList<String> getAllRoomCategories();

    void updatePriceOfRoomCategory(String name, String operation, Double amount);

    Category getCategoryFromName(String name) throws ServiceException;
}
