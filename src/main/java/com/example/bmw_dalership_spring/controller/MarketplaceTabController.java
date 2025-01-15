package com.example.bmw_dalership_spring.controller;
import com.example.bmw_dalership_spring.configuration.CurrentUser;
import com.example.bmw_dalership_spring.model.Make;
import com.example.bmw_dalership_spring.model.Marketplace;
import com.example.bmw_dalership_spring.model.Model;
import com.example.bmw_dalership_spring.model.Vehicle;
import com.example.bmw_dalership_spring.repository.MarketplaceRepository;
import com.example.bmw_dalership_spring.repository.OptionRepository;
import com.example.bmw_dalership_spring.repository.UserRepository;
import com.example.bmw_dalership_spring.repository.VehicleRepository;
import com.example.bmw_dalership_spring.service.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ListChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.ByteArrayInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class MarketplaceTabController extends BaseController {

    private final VehicleService vehicleService;
    private final MarketplaceService marketplaceService;
    private final UserService userService;
    private final OptionService optionService;
    private final MakeService makeService;
    private final ModelService modelService;

    @Autowired
    public MarketplaceTabController(MarketplaceService marketplaceService, UserService userService, VehicleService vehicleService, OptionService optionService, MakeService makeService, ModelService modelService  )
    {
        this.marketplaceService = marketplaceService;
        this.vehicleService = vehicleService;
        this.userService = userService;
        this.optionService = optionService;
        this.makeService = makeService;
        this.modelService = modelService;
    }
    @FXML
    private CheckComboBox<Model> modelCheckComboBox;
    @FXML
    public Button filterResultsButton;
    @FXML
    public AnchorPane optionsAnchorPane;
    @FXML
    private ImageView imageView;

    @FXML
    private ImageView logoImageView;

    @FXML
    private ListView<String> mainListView;

    @FXML
    private Button sellVehicleButton;

    @FXML
    private TextField yearFromField;

    @FXML
    private TextField yearToField;

    @FXML
    private TextField mileageFromField;

    @FXML
    private TextField mileageToField;

    @FXML
    private TextField priceFromField;

    @FXML
    private TextField priceToField;

    @FXML
    private TextField powerFromField;

    @FXML
    private TextField powerToField;

    @FXML
    private CheckComboBox<Make> makeCheckComboBox;

    @FXML
    private CheckComboBox<String> fuelCheckComboBox;

    @FXML
    private Label priceLabel;

    @FXML
    private Label makeLabel;

    @FXML
    private Label modelLabel;

    @FXML
    private Label mileageLabel;

    @FXML
    private Label fuelLabel;

    @FXML
    private Label engineLabel;

    @FXML
    private Label powerLabel;

    @FXML
    private Label torqueLabel;

    @FXML
    private Label colorLabel;

    @FXML
    private Label conditionLabel;

    @FXML
    private Label vinLabel;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private VBox optionsVbox;

    @FXML
    private ScrollPane optionsScrollPane;

    @FXML
    private GridPane descriptionGridPane;

    @FXML
    private Label firstNameLabel;

    @FXML
    private Label lastNameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private GridPane userDetailsGridPane;

    private List<Marketplace> marketplaceList = new ArrayList<Marketplace>();

    @FXML
    public void initialize() {
        loadMarketplaceData();
        populateListView();
        setListViewListener();
        makeCheckComboBox.getItems().addAll(makeService.getAllMakes());
        modelCheckComboBox.setDisable(true);

        makeCheckComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<Make>) change -> {

            List<Make> selectedMakes = new ArrayList<>(makeCheckComboBox.getCheckModel().getCheckedItems());


            modelCheckComboBox.getItems().clear();

            if (selectedMakes.isEmpty()) {

                modelCheckComboBox.setDisable(true);
            } else {

                modelCheckComboBox.setDisable(false);


                for (Make mk : selectedMakes) {
                    List<Model> modelsForMake = modelService.getModelsByMake(mk);
                    modelCheckComboBox.getItems().addAll(modelsForMake);
                }
            }
        });
        fuelCheckComboBox.getItems().addAll("Gasoline", "Diesel", "Electric");
        mainListView.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);

                    setStyle("-fx-font-family: Helvetica; -fx-font-weight: bold; -fx-font-size: 18px;");
                }
            }
        });
    }

    
    private void loadMarketplaceData() {
        marketplaceList.clear();
        marketplaceList.addAll(marketplaceService.getAllMarketplaces());
    }
    
    private void populateListView() {
        mainListView.getItems().clear();
        for (Marketplace marketplace : marketplaceList) {
            Vehicle vehicle = marketplace.getVehicle();
            String listItem = String.format("%s %s - %d - %.2f €",
                    vehicle.getMake(), vehicle.getModel(), vehicle.getYear(), vehicle.getPrice());
            mainListView.getItems().add(listItem);
        }
    }

    
    private void setListViewListener() {
        mainListView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            int selectedIndex = newValue.intValue();
            if (selectedIndex >= 0 && selectedIndex < marketplaceList.size()) {
                displayVehicleDetails(marketplaceList.get(selectedIndex));
            }
        });
    }

    
    private void displayVehicleDetails(Marketplace marketplace) {
        Vehicle vehicle = vehicleService.getVehicleWithOptions(marketplace.getVehicle().getId());



        makeLabel.setText(vehicle.getMake());
        modelLabel.setText(vehicle.getModel());
        mileageLabel.setText(vehicle.getMileage() + " km");
        engineLabel.setText(vehicle.getEngine());
        torqueLabel.setText(vehicle.getTorque() + " Nm");
        vinLabel.setText(vehicle.getVin());
        fuelLabel.setText(vehicle.getFuel());
        powerLabel.setText(vehicle.getPower() + " HP");
        colorLabel.setText(vehicle.getColor());
        conditionLabel.setText(marketplace.getCondition());
        priceLabel.setText(String.format("%.2f", vehicle.getPrice()));


        if (vehicle.getImage() != null) {
            Image image = new Image(new ByteArrayInputStream(vehicle.getImage()));
            imageView.setImage(image);
        } else {
            imageView.setImage(null);
        }


        if (marketplace.getOwner() != null) {
            firstNameLabel.setText(marketplace.getOwner().getFirstName());
            lastNameLabel.setText(marketplace.getOwner().getLastName());
            emailLabel.setText(marketplace.getOwner().getEmail());
            locationLabel.setText(marketplace.getLocation());
        } else {
            firstNameLabel.setText("N/A");
            lastNameLabel.setText("N/A");
            emailLabel.setText("N/A");
            locationLabel.setText("N/A");
        }


        descriptionTextArea.setText(marketplace.getDescription());
        optionsVbox.getChildren().clear();
        vehicle.getOptions().forEach(option -> {
            Label optionLabel = new Label(option.getName());
            optionsVbox.getChildren().add(optionLabel);
        });
    }

    public void handleSellVehicle(ActionEvent actionEvent) {

        switchToScene("/fxml/AddVehicleToMarketplace.fxml", actionEvent);
    }

    @FXML
    public void handleFilterResults(ActionEvent actionEvent) {

        List<String> make = makeCheckComboBox.getCheckModel().getCheckedItems().isEmpty()
                ? null
                : makeCheckComboBox.getCheckModel().getCheckedItems().stream().map(Make::getMake).toList();
        List<String> model = modelCheckComboBox.getCheckModel().getCheckedItems().isEmpty()
                ? null
                : modelCheckComboBox.getCheckModel().getCheckedItems().stream().map(Model::getModel).toList();
        List<String> fuel = fuelCheckComboBox.getCheckModel().getCheckedItems().isEmpty()
                ? null
                : fuelCheckComboBox.getCheckModel().getCheckedItems();

        int fromYear = yearFromField.getText().isEmpty() ? Integer.MIN_VALUE : Integer.parseInt(yearFromField.getText());
        int toYear = yearToField.getText().isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(yearToField.getText());
        int fromPower = powerFromField.getText().isEmpty() ? Integer.MIN_VALUE : Integer.parseInt(powerFromField.getText());
        int toPower = powerToField.getText().isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(powerToField.getText());
        int fromPrice = priceFromField.getText().isEmpty() ? Integer.MIN_VALUE : Integer.parseInt(priceFromField.getText());
        int toPrice = priceToField.getText().isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(priceToField.getText());
        int fromMileage = mileageFromField.getText().isEmpty() ? Integer.MIN_VALUE : Integer.parseInt(mileageFromField.getText());
        int toMileage = mileageToField.getText().isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(mileageToField.getText());


        List<Marketplace> filteredMarketplaces = marketplaceService.getSortedMarketplaces(
                make, model, fuel, fromYear, toYear, fromPower, toPower, fromPrice, toPrice, fromMileage, toMileage);
        System.out.println("found fileterd vehicles");
        for(Marketplace ad : filteredMarketplaces) {
            System.out.println(ad.getId());
        }


        mainListView.getItems().clear();
        for (Marketplace marketplace : filteredMarketplaces) {
            Vehicle vehicle = marketplace.getVehicle();
            String listItem = String.format("%s %s - %d - %.2f €",
                    vehicle.getMake(), vehicle.getModel(), vehicle.getYear(), vehicle.getPrice());
            mainListView.getItems().add(listItem);
        }


        marketplaceList = filteredMarketplaces;
    }

    @FXML
    public void handleClearFilters(ActionEvent actionEvent) {
        makeCheckComboBox.getCheckModel().clearChecks();
        fuelCheckComboBox.getCheckModel().clearChecks();
        modelCheckComboBox.getCheckModel().clearChecks();
        yearFromField.clear();
        yearToField.clear();
        powerFromField.clear();
        powerToField.clear();
        priceFromField.clear();
        priceToField.clear();
        mileageFromField.clear();
        mileageToField.clear();
        marketplaceList = marketplaceService.getAllMarketplaces();
        populateListView();
    }

}
