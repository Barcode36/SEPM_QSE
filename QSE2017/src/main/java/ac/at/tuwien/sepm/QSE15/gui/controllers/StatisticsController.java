package main.java.ac.at.tuwien.sepm.QSE15.gui.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Category;
import main.java.ac.at.tuwien.sepm.QSE15.entity.room.Room;
import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomService.CategoryServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomService.RoomServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.service.serviceService.ServiceServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.service.statistics.StatisticServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.rmi.CORBA.Util;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashMap;

/**
 * Created by Stefan Puhalo on 6/13/2017.
 */

@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class StatisticsController {

    @FXML
    BarChart<String, Integer> roomReservationBarChart;

    @FXML
    BarChart<String, Integer> serviceReservationBarChart;

    @FXML
    PieChart roomReservationPieChart;

    @FXML
    PieChart serviceReservationPieChart;

    @FXML
    ChoiceBox roomCategoryChoiceBox;

    @FXML
    ChoiceBox serviceTypeChoiceBox;

    @FXML
    DatePicker fromDatePicker;

    @FXML
    DatePicker toDatePicker;

    @FXML
    Label caption;

    @FXML
    private RadioButton percentageRadioButton;

    @FXML
    private RadioButton amountRadioButton;

    @FXML
    private RadioButton mostRadioButton;

    @FXML
    private RadioButton leastRadioButton;

    @FXML
    private ToggleGroup mostOrLeastToggleGroup;

    @FXML
    private ToggleGroup percentageOrAmountToggleGroup;

    @FXML
    private ChoiceBox percentageChoiceBox;

    @FXML
    private TextField amountTextField;



    private AnnotationConfigApplicationContext context;

    StatisticServiceIMPL statisticService;
    CategoryServiceIMPL categoryService;
    ServiceServiceIMPL serviceService;

    HashMap<String, Integer> statisticsForAllRooms;
    HashMap<String, Integer> statisticsForAllServices;

    private static final Logger logger = LoggerFactory.getLogger(AddNewCustomerController.class);

    Date from, to;

    public void initialize() {

        context = new AnnotationConfigApplicationContext(this.getClass());
        statisticService = context.getBean(StatisticServiceIMPL.class);
        categoryService = context.getBean(CategoryServiceIMPL.class);
        serviceService = context.getBean(ServiceServiceIMPL.class);

        logger.info("Entering Statistics stage.");

        ObservableList<String> serviceTypes = serviceService.getAllServiceTypes();
        serviceTypeChoiceBox.getItems().addAll(serviceTypes);

        ObservableList<String> roomCategories = categoryService.getAllRoomCategories();
        roomCategoryChoiceBox.getItems().addAll(roomCategories);

        percentageChoiceBox.getItems().add("5%");
        percentageChoiceBox.getItems().add("10%");
        percentageChoiceBox.getItems().add("15%");
        percentageChoiceBox.getItems().add("20%");
        percentageChoiceBox.getItems().add("25%");
        percentageChoiceBox.getItems().add("30%");
        percentageChoiceBox.getItems().add("50%");

        toDatePicker.setValue(LocalDate.now().plusMonths(6));
        fromDatePicker.setValue(LocalDate.now().minusYears(1));

        from = Date.valueOf(fromDatePicker.getValue());
        to = Date.valueOf(toDatePicker.getValue());

        setDataOfPieCharts();

    }

    public void clickOnShowStatistics() {
        if(roomCategoryChoiceBox.getValue() == null && serviceTypeChoiceBox.getValue() == null) {
            Utility.showError("Please select a category of rooms and a type of services.");
            return;
        }

        if(from == null || to == null) {
            from = Date.valueOf(fromDatePicker.getValue());
            to = Date.valueOf(toDatePicker.getValue());
            setDataOfPieCharts();
        }

        if(Date.valueOf(fromDatePicker.getValue()).after(Date.valueOf(toDatePicker.getValue()))) {
            Utility.showError("Please select From date before To date.");
            return;
        }

        if(!from.equals(Date.valueOf(fromDatePicker.getValue())) || !to.equals(Date.valueOf(toDatePicker.getValue()))) {
            setDataOfPieCharts();
        }

        if(roomCategoryChoiceBox.getValue() != null) {

            roomReservationBarChart.getData().clear();
            String category = roomCategoryChoiceBox.getValue().toString();
            HashMap<Date, Integer> statisticsForSelectedCategory = new HashMap<>();

            try {
                statisticsForSelectedCategory = statisticService.getStatisticsForOneRoomFromDate(category, Date.valueOf(fromDatePicker.getValue()), Date.valueOf(toDatePicker.getValue()));
            } catch (ServiceException e) {
                logger.error("Loading statistics for one room failed.");
            }

            XYChart.Series series = new XYChart.Series();

            for(Date date : statisticsForSelectedCategory.keySet()) {
                series.getData().add(new XYChart.Data<>(date.toString(),statisticsForSelectedCategory.get(date)));
            }
            roomReservationBarChart.getData().add(series);
            roomReservationBarChart.setTitle("Statistics for room category: " +
                    roomCategoryChoiceBox.getSelectionModel().getSelectedItem().toString());
            roomReservationBarChart.getYAxis().setLabel("Reserved days");
            roomReservationBarChart.getXAxis().setLabel("Beginning day of reservation");
        }

        if(serviceTypeChoiceBox.getValue() != null) {

            serviceReservationBarChart.getData().clear();
            String type = serviceTypeChoiceBox.getValue().toString();
            HashMap<String, Integer> statisticsForSelectedType = new HashMap<>();

            try {
                statisticsForSelectedType = statisticService.getStatisticsForOneServiceFromDate(type, Date.valueOf(fromDatePicker.getValue()), Date.valueOf(toDatePicker.getValue()));
            } catch (ServiceException e) {
                logger.error("Loading statistics for one service failed.");
            }

            XYChart.Series series = new XYChart.Series();

            for(String month : statisticsForSelectedType.keySet()) {
                series.getData().add(new XYChart.Data<>(month, statisticsForSelectedType.get(month)));
            }
            serviceReservationBarChart.getData().add(series);
            serviceReservationBarChart.setTitle("Statistics for service type: " +
                    serviceTypeChoiceBox.getSelectionModel().getSelectedItem().toString());
            serviceReservationBarChart.getXAxis().setLabel("Month of reservation");
            serviceReservationBarChart.getYAxis().setLabel("Number of reservations");
        }

    }

    public void clearCharts() {
        roomReservationBarChart.getData().clear();
        serviceReservationBarChart.getData().clear();
        roomReservationPieChart.getData().clear();
        serviceReservationPieChart.getData().clear();
        serviceTypeChoiceBox.getSelectionModel().clearSelection();
        roomCategoryChoiceBox.getSelectionModel().clearSelection();
        caption.setVisible(false);
        from = null;
        to = null;
    }

    public void updatePrice() throws ServiceException {

        int max, min;
        double amount;
        String categoryName = null;
        double oldPrice, newPrice;

        DecimalFormat newFormat = new DecimalFormat("#.##");


        if (mostOrLeastToggleGroup.getSelectedToggle().toString().contains("The most often reserved")) {
            max = statisticService.getMaxValueFromMap(statisticsForAllRooms);
            for (String s : statisticsForAllRooms.keySet()) {
                if (statisticsForAllRooms.get(s) == max) {
                    categoryName = s;
                }
            }

            Category category = categoryService.getCategoryFromName(categoryName);
            oldPrice = category.getPrice().doubleValue() / 100.0;

            if (percentageOrAmountToggleGroup.getSelectedToggle().toString().contains("For Amount")) {
                try {
                    amount = Double.parseDouble(amountTextField.getText()) * 100.0;
                }catch (NumberFormatException e) {
                    Utility.showError("Please set the amount as a number.");
                    return;
                }
                categoryService.updatePriceOfRoomCategory(categoryName, "+", amount);
                newPrice = oldPrice + (amount/100);
                Utility.showAlert("Price for room category " + categoryName + " changed from " +
                        oldPrice + " to " + newFormat.format(newPrice), Alert.AlertType.CONFIRMATION);

            } else {
                String percentage = percentageChoiceBox.getSelectionModel().getSelectedItem().toString();
                Double percentageFactor = 1.0 + ((Double.parseDouble(percentage.substring(0, percentage.length()-1)))/100.0);
                categoryService.updatePriceOfRoomCategory(categoryName, "*", percentageFactor);
                newPrice = oldPrice * percentageFactor;
                Utility.showAlert("Price for room category " + categoryName + " changed from " +
                        oldPrice + " to " + newFormat.format(newPrice), Alert.AlertType.CONFIRMATION);
            }

        }else {
            min = statisticService.getMinValueFromMap(statisticsForAllRooms);
            for(String s : statisticsForAllRooms.keySet()) {
                if(statisticsForAllRooms.get(s) == min) {
                    categoryName = s;
                }
            }

            Category category = categoryService.getCategoryFromName(categoryName);
            oldPrice = category.getPrice() / 100.0;

            if (percentageOrAmountToggleGroup.getSelectedToggle().toString().contains("For Amount")) {
                try {
                    amount = Double.parseDouble(amountTextField.getText());
                    if(amount > oldPrice) {
                        Utility.showError("Please set the amount as a number lesser then " + oldPrice + ".");
                        return;
                    }
                }catch (NumberFormatException e) {
                    Utility.showError("Please set the amount as a number.");
                    return;
                }
                amount = amount * 100.0;
                categoryService.updatePriceOfRoomCategory(categoryName, "-", amount);
                newPrice = oldPrice - (amount / 100.0);
                Utility.showAlert("Price for room category " + categoryName + " changed from " +
                        oldPrice + "€ to " + newFormat.format(newPrice) + "€", Alert.AlertType.CONFIRMATION);

            } else {
                String percentage = percentageChoiceBox.getSelectionModel().getSelectedItem().toString();
                Double percentageFactor = 1.0 - ((Double.parseDouble(percentage.substring(0, percentage.length()-1)))/100.0);
                categoryService.updatePriceOfRoomCategory(categoryName, "*", percentageFactor);
                newPrice = oldPrice * percentageFactor;
                Utility.showAlert("Price for room category " + categoryName + " changed from " +
                        oldPrice + "€ to " + newFormat.format(newPrice) + "€", Alert.AlertType.CONFIRMATION);
            }
        }

    }

    private void setDataOfPieCharts() {

        statisticsForAllRooms = new HashMap<>();
        statisticsForAllServices = new HashMap<>();

        caption.setVisible(true);

        try {
            statisticsForAllRooms = statisticService.getStatisticsForAllRoomsFromDate(
                    Date.valueOf(fromDatePicker.getValue()), Date.valueOf(toDatePicker.getValue()));
        } catch (ServiceException e) {
            logger.error("Loading statistics for all rooms failed.");
        }

        ObservableList<PieChart.Data> roomPieChartData = FXCollections.observableArrayList();

        for(String category : statisticsForAllRooms.keySet()) {
            roomPieChartData.add(new PieChart.Data(category + " (" + statisticsForAllRooms.get(category) + ")", statisticsForAllRooms.get(category)));
        }

        roomReservationPieChart.setData(roomPieChartData);
        roomReservationPieChart.setLegendSide(Side.LEFT);
        roomReservationPieChart.setLabelsVisible(false);
        roomReservationPieChart.setTitle("Statistics for all room categories");
        //showPercentageOnClick(roomReservationPieChart);


        try {
            statisticsForAllServices = statisticService.getStatisticsForAllServicesFromDate(Date.valueOf(fromDatePicker.getValue()), Date.valueOf(toDatePicker.getValue()));
        } catch (ServiceException e) {
            logger.error("Loading statistics for all services failed.");
        }

        ObservableList<PieChart.Data> servicePieChartData = FXCollections.observableArrayList();

        for(String type : statisticsForAllServices.keySet()) {
            servicePieChartData.add(new PieChart.Data(type + " (" + statisticsForAllServices.get(type) + ")", statisticsForAllServices.get(type)));
        }

        serviceReservationPieChart.setData(servicePieChartData);
        serviceReservationPieChart.setLegendSide(Side.LEFT);
        serviceReservationPieChart.setLabelsVisible(false);
        serviceReservationPieChart.setTitle("Statistics or all Services");
        //showPercentageOnClick(serviceReservationPieChart);

    }

    /**
     private void showPercentageOnClick(PieChart pieChart) {
     caption.setTextFill(Color.BLACK);
     caption.setStyle("-fx-font: 24 arial;");

     for(PieChart.Data data : pieChart.getData()) {
     data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED,
     new EventHandler<MouseEvent>() {
    @Override
    public void handle(MouseEvent e) {
    caption.setLayoutX(e.getSceneX());
    caption.setLayoutY(e.getSceneY() - 40.0);
    caption.setText(String.valueOf(data.getPieValue()));
    caption.setVisible(true);
    }
    });
     }
     }
     */

}