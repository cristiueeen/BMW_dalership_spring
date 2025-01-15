package com.example.bmw_dalership_spring.controller;

import com.example.bmw_dalership_spring.configuration.CurrentUser;
import com.example.bmw_dalership_spring.model.*;
import com.example.bmw_dalership_spring.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import javafx.collections.ObservableList;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class AddVehicleController extends BaseController {

    private final VehicleService vehicleService;
    private final OptionService optionService;
    private final MarketplaceService marketplaceService;
    private final MakeService makeService;
    private final ModelService modelService;



    @Autowired
    public AddVehicleController(VehicleService vehicleService, OptionService optionService, MarketplaceService marketplaceService, MakeService makeService, ModelService modelService ) {
        this.makeService = makeService;
        this.modelService = modelService;
        this.vehicleService = vehicleService;
        this.optionService = optionService;
        this.marketplaceService = marketplaceService;
    }
    @FXML
    private Label recommendedPriceLabel;
    @FXML
    private TextField vinField, powerField, torqueField, mileageField, yearField, priceTextField, locationTextField;
    @FXML
    private ChoiceBox<String> engineField, fuelField;
    @FXML
    private ChoiceBox<Model> modelField;
    @FXML
    private ChoiceBox<Make> makeField;
    @FXML
    private ColorPicker colorField;
    @FXML
    private CheckComboBox<String> optionsField;
    @FXML
    private ComboBox<String> vehicleConditionField;
    @FXML
    private Button addNewVehicle, addExistingVehicleButton, selectImagesButton, clearFormButton;
    @FXML
    private ListView<Vehicle> mainListViewAddListing;
    @FXML
    private TextArea vehicleDescriptionField;



    private byte[] image;
    private ObservableList<Vehicle> vehicleObservableList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        vehicleObservableList = FXCollections.observableArrayList(getUnlistedVehicles());

        mainListViewAddListing.setItems(vehicleObservableList);
        mainListViewAddListing.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Vehicle vehicle, boolean empty) {
                super.updateItem(vehicle, empty);
                if (empty || vehicle == null) {
                    setText(null);
                } else {
                    String listItem = String.format("%d %s %s - %d - %.2f €",
                            vehicle.getId(), vehicle.getMake(), vehicle.getModel(), vehicle.getYear(), vehicle.getPrice());
                    setText(listItem);
                }
            }
        });


        mainListViewAddListing.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateForm(newValue);
                updateRecommendedPrice();
            }
        });
        mainListViewAddListing.setOnMouseClicked(event -> {
            if (mainListViewAddListing.getSelectionModel().getSelectedIndex() == -1) return;

            if (event.getClickCount() == 2) {
                mainListViewAddListing.getSelectionModel().clearSelection();
                handleClearForm();
            }
        });
        addNewVehicle.disableProperty().bind(mainListViewAddListing.getSelectionModel().selectedItemProperty().isNotNull());
        addExistingVehicleButton.disableProperty().bind(mainListViewAddListing.getSelectionModel().selectedItemProperty().isNull());
        addFormFieldListeners();
        String[] bmwEngineCodes = {

                "N42", "N43", "N46", "N46T", "N13", "N20", "N26", "B38", "B48", "N18", "N12",

                "M20", "M50", "M52", "M54", "N52", "N53", "N54", "N55", "B58", "S55",

                "M60", "M62", "N62", "N63", "S62", "S63", "B63", "B64",

                "S85",

                "M70", "M73", "N73", "N74", "S70", "B66",

                "M41", "M47", "M47TU", "M47TU2", "N47", "N47TU", "B47",

                "M51", "M57", "M57TU", "M57TU2", "N57", "N57TU", "B57",

                "M67"
        };


        makeField.getItems().addAll(makeService.getAllMakes());
        modelField.setDisable(true);

        makeField.setOnAction(event -> {
            Make selectedMake = makeField.getValue();
            if (selectedMake == null) {
                modelField.getItems().clear();
                modelField.setDisable(true);
            } else {
                modelField.setDisable(false);
                List<Model> modelsForMake = modelService.getModelsByMake(selectedMake);
                modelField.getItems().setAll(modelsForMake);
            }
        });
        engineField.getItems().addAll(bmwEngineCodes);
        fuelField.getItems().addAll("Gasoline", "Diesel", "Electric", "Hybrid");
        optionsField.getItems().addAll(optionService.getAllOptionsString());
        vehicleConditionField.getItems().addAll("New", "Used", "Certified Pre-Owned");
    }

    private List<Vehicle> getUnlistedVehicles(){
        List<Vehicle> unlistedVehicles = new ArrayList<>();
        List<Vehicle> allVehicles = vehicleService.getVehiclesByUser(CurrentUser.getInstance().getUser());
        List<Vehicle> listedVehicles = new ArrayList<>();
        for (Marketplace listing :  marketplaceService.getListingsByUser(CurrentUser.getInstance().getUser())) {
            listedVehicles.add(listing.getVehicle());
        }
        for(Vehicle vehicle : allVehicles){
            if (!listedVehicles.contains(vehicle)) {
                unlistedVehicles.add(vehicle);
            }
        }
        return unlistedVehicles;
    }

    private void populateForm(Vehicle vehicle) {
        makeField.setValue(makeService.getMakeByName(vehicle.getMake()));
        modelField.setValue( modelService.getModelByName(vehicle.getModel()));
        vinField.setText(vehicle.getVin());
        engineField.setValue(vehicle.getEngine());
        fuelField.setValue(vehicle.getFuel());
        powerField.setText(String.valueOf(vehicle.getPower()));
        torqueField.setText(String.valueOf(vehicle.getTorque()));
        mileageField.setText(String.valueOf(vehicle.getMileage()));
        yearField.setText(String.valueOf(vehicle.getYear()));
        colorField.setValue(javafx.scene.paint.Color.web(vehicle.getColor()));
        optionsField.getCheckModel().clearChecks();
        Vehicle lazyVehicle = vehicleService.getVehicleWithOptions(vehicle.getId());
        for (Option option : lazyVehicle.getOptions()) {
            optionsField.getCheckModel().check(option.getName());
        }
    }

    @FXML
    private void handleAddNewVehicle() {
        String make = makeField.getSelectionModel().getSelectedItem().toString();
        String model = modelField.getSelectionModel().getSelectedItem().toString();
        String vin = vinField.getText();
        String engine = engineField.getSelectionModel().getSelectedItem();
        String fuel = fuelField.getSelectionModel().getSelectedItem();
        int power = Integer.parseInt(powerField.getText()) ;
        int torque = Integer.parseInt(torqueField.getText());
        int mileage = Integer.parseInt(mileageField.getText());
        String color = colorField.getValue().toString();
        int year = Integer.parseInt(yearField.getText());
        List<String> options = optionsField.getCheckModel().getCheckedItems();
        Set<Option> optionList = optionService.getOptionList(options);


        Vehicle vehicle = new Vehicle(vin, make, model, mileage, CurrentUser.getInstance().getUser(), image, color, engine, fuel, power, torque, year, optionList);
        vehicleService.saveVehicle(vehicle);

        saveMarketplaceListing(vehicle);

        updateMarketplaceList();


        handleClearForm();
    }
    @FXML
    private void handleSelectImages() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Vehicle Image");


        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );


        File selectedFile = fileChooser.showOpenDialog(addNewVehicle.getScene().getWindow());

        if (selectedFile != null) {

            Path mediaFolderPath = Paths.get("src", "main", "resources", "media");

            try {

                if (!Files.exists(mediaFolderPath)) {
                    Files.createDirectories(mediaFolderPath);
                }


                Path destinationPath = mediaFolderPath.resolve(selectedFile.getName());


                Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

                image = Files.readAllBytes(destinationPath);

                System.out.println("Image successfully saved and passed to the database.");

            } catch (IOException e) {
                System.err.println("Error saving the image: " + e.getMessage());
            }
        } else {
            System.out.println("No image was selected.");
        }
    }

    @FXML
    private void handleAddExistingVehicle() {
        Vehicle selectedVehicleStr = mainListViewAddListing.getSelectionModel().getSelectedItem();

        if (selectedVehicleStr != null) {
            Vehicle selectedVehicle = vehicleService.getVehicleWithOptions(selectedVehicleStr.getId());
            saveMarketplaceListing(selectedVehicle);
        } else {
            showAlert("Error", "No vehicle selected. Please select a vehicle from the list.");
        }
    }

    private void saveMarketplaceListing(Vehicle vehicle) {
        if (vehicle != null) {
            String description = vehicleDescriptionField.getText();
            String priceString = priceTextField.getText();
            String location = locationTextField.getText();
            String condition = vehicleConditionField.getValue();

            if (description.isEmpty() || priceString.isEmpty() || location.isEmpty() || condition == null) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceString);
            } catch (NumberFormatException e) {
                showAlert("Invalid Price", "Please enter a valid price.");
                return;
            }
            vehicle.setPrice(price);
            vehicleService.saveVehicle(vehicle);

            Marketplace newEntry = new Marketplace(
                    condition,
                    location,
                    vehicle,
                    CurrentUser.getInstance().getUser(),
                    description
            );

            marketplaceService.saveMarketplace(newEntry);

            handleClearForm();

            showAlert("Success", "The vehicle has been added to the marketplace.");
        } else {
            showAlert("Error", "The selected vehicle is not valid.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    @FXML
    private void handleSelectImage() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage());

        if (selectedFiles != null) {

            for (File file : selectedFiles) {
                System.out.println("Selected image: " + file.getAbsolutePath());
            }
        }
    }


    @FXML
    private void handleClearForm() {
        makeField.getSelectionModel().clearSelection();
        modelField.getSelectionModel().clearSelection();
        vinField.clear();
        powerField.clear();
        torqueField.clear();
        mileageField.clear();
        yearField.clear();
        priceTextField.clear();
        locationTextField.clear();
        vehicleDescriptionField.clear();
        engineField.setValue(null);
        fuelField.setValue(null);
        colorField.setValue(null);
        optionsField.getCheckModel().clearChecks();
        vehicleConditionField.setValue(null);
    }

    private void updateMarketplaceList() {
    }
    @FXML
    private void handleBackButton(ActionEvent actionEvent) {


        switchToScene("/fxml/MainTabPane.fxml", actionEvent);
    }
    private void addFormFieldListeners() {

        modelField.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> checkAndUpdateRecommendedPrice());
        vinField.textProperty().addListener((observable, oldValue, newValue) -> checkAndUpdateRecommendedPrice());
        powerField.textProperty().addListener((observable, oldValue, newValue) -> checkAndUpdateRecommendedPrice());
        torqueField.textProperty().addListener((observable, oldValue, newValue) -> checkAndUpdateRecommendedPrice());
        mileageField.textProperty().addListener((observable, oldValue, newValue) -> checkAndUpdateRecommendedPrice());
        yearField.textProperty().addListener((observable, oldValue, newValue) -> checkAndUpdateRecommendedPrice());

        engineField.valueProperty().addListener((observable, oldValue, newValue) -> checkAndUpdateRecommendedPrice());
        fuelField.valueProperty().addListener((observable, oldValue, newValue) -> checkAndUpdateRecommendedPrice());
        colorField.valueProperty().addListener((observable, oldValue, newValue) -> checkAndUpdateRecommendedPrice());
        optionsField.getCheckModel().getCheckedItems().addListener((ListChangeListener<? super String>) change -> {
            while (change.next()) {
                checkAndUpdateRecommendedPrice();
            }
        });

    }

    private void checkAndUpdateRecommendedPrice() {
        if (areAllFieldsEntered()) {
            updateRecommendedPrice();
        }
    }

    private boolean areAllFieldsEntered() {
        return !modelField.getSelectionModel().isEmpty() &&
                !vinField.getText().isEmpty() &&
                !powerField.getText().isEmpty() &&
                !torqueField.getText().isEmpty() &&
                !mileageField.getText().isEmpty() &&
                !yearField.getText().isEmpty() &&

                engineField.getValue() != null &&
                fuelField.getValue() != null &&
                colorField.getValue() != null &&
                !optionsField.getCheckModel().getCheckedItems().isEmpty();

    }
    private void updateRecommendedPrice() {
        String make = makeField.getSelectionModel().getSelectedItem().toString();
        String model = modelField.getSelectionModel().getSelectedItem().toString();
        String vin = vinField.getText();
        String engine = engineField.getSelectionModel().getSelectedItem();
        String fuel = fuelField.getSelectionModel().getSelectedItem();
        int power = Integer.parseInt(powerField.getText()) ;
        int torque = Integer.parseInt(torqueField.getText());
        int mileage = Integer.parseInt(mileageField.getText());
        String color = colorField.getValue().toString();
        int year = Integer.parseInt(yearField.getText());
        List<String> options = optionsField.getCheckModel().getCheckedItems();
        Set<Option> optionList = optionService.getOptionList(options);


        Vehicle vehicle = new Vehicle(vin, make, model, mileage, CurrentUser.getInstance().getUser(), image, color, engine, fuel, power, torque, year, optionList);

        Marketplace marketplace = new Marketplace(
                "New",
                "Berlin",
                vehicle,
                CurrentUser.getInstance().getUser(),
                "loremIpsum"
        );


        double recommendedPrice = marketplaceService.getPriceEstimation(marketplace);
        System.out.println(recommendedPrice);
        recommendedPriceLabel.setText(Double.toString(recommendedPrice));
    }
}
