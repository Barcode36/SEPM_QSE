package main.java.ac.at.tuwien.sepm.QSE15.service.roomService;

import javafx.collections.ObservableList;
import main.java.ac.at.tuwien.sepm.QSE15.dao.DAOException;
import main.java.ac.at.tuwien.sepm.QSE15.dao.roomDAO.JDBCCategoryDAO;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Category;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomService.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ivana on 29.5.2017.
 */
@Service
public class CategoryServiceIMPL implements CategoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceIMPL.class);

    @Autowired
    private JDBCCategoryDAO categoryDAO;

    @Override
    public Category create(Category category) throws ServiceException {

        if (category == null){
            LOGGER.error("Cannot create Category! Input value is null!");
            throw new ServiceException("Input parameter is null");
        }

        try{
            return categoryDAO.create(category);

        } catch (DAOException e){
            LOGGER.error("Unable to create Category.");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void update(Category category) throws ServiceException {

        if (category == null){
            LOGGER.error("Cannot update Category! Input value is null!");
            throw new ServiceException("Input parameter is null");
        }

        try{
            categoryDAO.update(category);

        } catch (DAOException e){
            LOGGER.error("Unable to update Category.");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Category get(String name) throws ServiceException {

        if (name == null || name.equals("")){
            LOGGER.error("Cannot get Category! Input value is null!");
            throw new ServiceException("Input parameter is null");
        }

        try{
            return categoryDAO.get(name);

        } catch (DAOException e){
            LOGGER.error("Unable to get Category.");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Category> getAll() throws ServiceException {

        try{
            return categoryDAO.getAll();

        } catch (DAOException e){
            LOGGER.error("Unable to get all Categories.");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void delete(Category category) throws ServiceException {

        if (category == null){
            LOGGER.error("Cannot delete Category! Input value is null!");
            throw new ServiceException("Input parameter is null");
        }

        try{
            categoryDAO.delete(category);

        } catch (DAOException e){
            LOGGER.error("Unable to delete Category.");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public ObservableList<String> getAllRoomCategories() {
        return categoryDAO.getAllRoomCategories();
    }

    @Override
    public void updatePriceOfRoomCategory(String name, String operation, Double amount) {
        categoryDAO.updatePriceOfRoomCategory(name, operation, amount);
    }

    @Override
    public Category getCategoryFromName(String name) throws ServiceException {
        try {
            return categoryDAO.getCategoryFromName(name);
        } catch (DAOException e) {
            LOGGER.error("Get category from name failed.");
            throw new ServiceException("Get category from name failed.");
        }
    }


}
