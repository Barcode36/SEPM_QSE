package main.java.ac.at.tuwien.sepm.QSE15.gui.controllers.Service_Controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.RoomReservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.serviceService.ServiceServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.utility.Utility;
import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ivana on 22.5.2017.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class ServiceMainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceMainController.class);

    public TableView<Service> servicesTableView;
    public TableColumn<Service,String> sridColumn,typColumn,descriptionColumn,priceColumn;

    public ImageView serviceImage;
    public TextField typCreateTF;
    public TextArea descriptionCreateTA;
    public TextField priceCreateTF;
    public ImageView imageIcon;
    public ImageView editServiceIcon;
    public ImageView CreateIcon;
    public TextField sridEditTF;
    public TextArea descriptionEditTA;
    public TextField priceEditTF;
    public Slider priceSlider;
    public Button searchBTN;
    public Button clearBTN;
    public TextField typSearchTF;
    public ImageView typIcon;
    public ImageView sridIcon;
    public ImageView priceIcon;
    public ComboBox sridCBox;
    public Label priceLB;
    public TextField descriptionSearchTF;
    public ImageView descriptionIcon;
    private Stage primaryStage;



    private List<Service> serviceList;
    private Service selectedService;
    private List<String> listTypes;

    private AnnotationConfigApplicationContext context;
    private ServiceServiceIMPL serviceServiceIMPL;

    public void initialize() {
        selectedService = new Service();
        context = new AnnotationConfigApplicationContext(this.getClass());
        serviceServiceIMPL = context.getBean(ServiceServiceIMPL.class);
        primaryStage = new Stage();

        getAllServices();
        setIcons();
        setUpTable();
        setUpSlider(serviceList);

        servicesTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Service>() {
            public void changed(ObservableValue<? extends Service> observable, Service oldValue, Service newValue) {
                changeSelectionModel(oldValue,newValue);

            }
        });

        priceSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                priceLB.setText(String.format("%.2f", new_val));
            }
        });
    }

    public void setUpAutoFillTextFieldAndComboBox(){
        listTypes = new ArrayList<>();
        List<String> sridList = new ArrayList<>();
        sridList.add("Any");
        getAllServices();
        if (serviceList!=null) {
            for (Service r : serviceList) {
                if (!listTypes.contains(r.getType())) {
                    listTypes.add(r.getType());
                }
                if (!sridList.contains(r.getSrid()+"")){
                    sridList.add(r.getSrid()+"");
                }
            }
            TextFields.bindAutoCompletion(typSearchTF,listTypes);

            sridCBox.setItems(FXCollections.observableArrayList(sridList));
        }

    }

    public void setUpSlider(List<Service> services) {
        Long max = 0L;
        Long min = Long.MAX_VALUE;

        for (Service r : services) {
            Long price = r.getPrice();

            if (price > max) {
                max = price;
            }

            if (price < min) {
                min = price;
            }

        }
        String s = min + "";
        Double dmin = Double.parseDouble(s);
        s = max + "";
        Double dmax = Double.parseDouble(s);
        priceSlider.setMin(dmin);
        priceSlider.setMax(dmax);
        priceSlider.setShowTickLabels(true);
        priceSlider.setShowTickMarks(true);
        priceSlider.setMajorTickUnit(20);
        priceSlider.setMinorTickCount(10);
        priceSlider.setValue(max);
        priceLB.setText(String.format("%.2f", priceSlider.getValue()));
    }

    private void changeSelectionModel(Service oldValue, Service newValue) {

            if (newValue!=null){

                selectedService = newValue;
                sridEditTF.setText(selectedService.getSrid()+"");
                descriptionEditTA.setText(selectedService.getDescription());
                priceEditTF.setText(selectedService.getPrice()+"");
                String path = "/res/images/";
                String typ = newValue.getType();
                path+= typ.equals("Transport")? "transport" : (typ.equals("Wellness")? "massageservice" : (typ.equals("Cleaning service")? "washingservice" : (typ.equals("Restaurant")? "restaurantImage" : "noImage" )));
                path+= ".jpg";
                serviceImage.setImage(new Image(path));
                }


    }

    private void getAllServices() {
        Reservation r = new RoomReservation();
        r.setRid(-1);
        try {
            this.serviceList = serviceServiceIMPL.getAllServicesFromReservation(r);
            this.serviceList = priceToDouble(serviceList);
        } catch (ServiceException e) {
            LOGGER.error("Unable to load all services.");
        }
    }

    public void setUpTable(){

        sridColumn.setCellValueFactory(new PropertyValueFactory<Service, String>("srid"));
        typColumn.setCellValueFactory(new PropertyValueFactory<Service, String>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Service, String>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Service, String>("price"));


        servicesTableView.setItems(FXCollections.observableArrayList(serviceList));
        servicesTableView.getSelectionModel().selectFirst();
        changeSelectionModel(null,servicesTableView.getSelectionModel().getSelectedItem());
        setUpAutoFillTextFieldAndComboBox();
       // setRowFactory(servicesTableView);

    }

    private void setIcons() {
        try {

            imageIcon.setImage(new Image("/res/icons/image.png"));
            editServiceIcon.setImage(new Image("/res/icons/edit.png"));
            CreateIcon.setImage(new Image("/res/icons/add.png"));
            priceIcon.setImage(new Image("/res/icons/euro.png"));
            sridIcon.setImage(new Image("/res/icons/number.png"));
            descriptionIcon.setImage(new Image("/res/icons/description.png"));
            typIcon.setImage(new Image("/res/icons/type.png"));


        }catch (Exception e){
            LOGGER.error("Could't load the icons.");
        }

    }

    public List<Service> priceToDouble(List<Service> services){
        for (int i = 0; i<services.size(); i++){
            Long total = services.get(i).getPrice()/100;
            services.get(i).setPrice(total);
        }
        return services;
    }

    protected void setRowFactory(TableView<Service> tableView){
        tableView.setRowFactory(new Callback<TableView<Service>, TableRow<Service>>() {
            @Override
            public TableRow<Service> call(TableView<Service> tableView) {
                final TableRow<Service> row = new TableRow<Service>();
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem deleteMenuItem = new MenuItem("Delete Service");
                deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                            onDeleteClicked();
                    }
                });

                contextMenu.getItems().addAll(deleteMenuItem);
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu)null)
                                .otherwise(contextMenu)
                );
                return row ;
            }
        });


    }

    private void onDeleteClicked() {
        try {
            serviceServiceIMPL.delete(selectedService);
            Utility.showAlert("Service : " +  selectedService.getSrid() + " is deleted successfully.", Alert.AlertType.CONFIRMATION);
        } catch (ServiceException e) {
            Utility.showAlert(" Unable to delete Service : " +  selectedService.getSrid() + ".", Alert.AlertType.CONFIRMATION);

            LOGGER.error("Unable to delete this Service (srid: " + selectedService.getSrid() + ")");
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void onReservationMainClicked(ActionEvent actionEvent) {
    }

    public void onNewRoomClicked(ActionEvent actionEvent) {
    }

    public void onSearchClicked(ActionEvent actionEvent) {

        Service s = new Service();
        boolean allV = true;

        try {

            if (!sridCBox.getSelectionModel().isEmpty() && !sridCBox.getValue().equals("Any")){
                s.setSrid(Integer.parseInt(sridCBox.getValue()+""));
            }

            Double dPrice = priceSlider.getValue()*100;
            Long longPrice = dPrice.longValue();
            s.setPrice(longPrice);


            if(descriptionSearchTF.getText()!=null && !descriptionSearchTF.getText().equals("")){
                if (Utility.checkIfValidAlpha(descriptionSearchTF.getText())){
                    s.setDescription(descriptionSearchTF.getText());
                }
                else {
                    descriptionSearchTF.setText("");
                    allV = false;
                }

            }


            if (typSearchTF.getText()!=null && !typSearchTF.getText().equals("")){
                if (Utility.checkIfValidAlpha(typSearchTF.getText())){
                    s.setType(typSearchTF.getText());
                }
                else {
                    typSearchTF.setText("");
                    allV = false;
                }
            }


            if(allV) {
                serviceList = serviceServiceIMPL.search(s);
                serviceList = priceToDouble(serviceList);
                setUpTable();
            }

        } catch (ServiceException e) {
            LOGGER.error("Unable to search Service.");
        }

    }

    public void onClearClicked(ActionEvent actionEvent) {

        priceSlider.setValue(priceSlider.getMax());
        sridCBox.getSelectionModel().select(-1);
        descriptionSearchTF.setText("");
        typSearchTF.setText("");
        getAllServices();
        //serviceList = priceToDouble(serviceList);
        setUpTable();
    }


    public void onCreateClicked(ActionEvent actionEvent) {
        Service uService = selectedService;
        if (Utility.checkIfTextFieldValidNotEmpty(Arrays.asList(priceCreateTF, typCreateTF))){
            if (!descriptionCreateTA.getText().equals("")) {
                try {
                    if (Utility.checkIfValidNumbersOnly(priceCreateTF.getText()) && Utility.checkIfValidAlpha(typCreateTF.getText())) {
                        uService.setType(typCreateTF.getText());
                        uService.setPrice(Long.parseLong(priceCreateTF.getText()) * 100);
                        uService.setDescription(descriptionCreateTA.getText());
                        serviceServiceIMPL.create(uService);
                        getAllServices();
                        setUpSlider(serviceList);
                        setUpTable();
                        Utility.showAlert("New Service is created successfully", Alert.AlertType.INFORMATION);
                        descriptionCreateTA.setText("");
                        priceCreateTF.setText("");
                        typCreateTF.setText("");
                    }
                } catch (ServiceException e) {
                    LOGGER.error("Unable to Create Service");
                }
            }
            else {
                Utility.showAlert("Please fill description text area.", Alert.AlertType.WARNING);
            }
         }

    }

    public void onUpdateClicked(ActionEvent actionEvent) {
        Service uService = selectedService;

        try {
            if (Utility.checkIfValidNumbersOnly(priceEditTF.getText())){
                uService.setPrice(Long.parseLong(priceEditTF.getText())*100);
                uService.setDescription(descriptionEditTA.getText());
                serviceServiceIMPL.update(uService);
                getAllServices();
                setUpTable();
                Utility.showAlert("Service : " + uService.getSrid() + " is updated successfully", Alert.AlertType.INFORMATION);
            }

            else {
                changeSelectionModel(null,selectedService);
            }

        } catch (ServiceException e) {
            LOGGER.error("Unable to update Service : " + uService.getSrid());
        }
    }
}
