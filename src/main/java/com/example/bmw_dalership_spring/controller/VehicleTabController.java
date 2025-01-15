package com.example.bmw_dalership_spring.controller;

import com.example.bmw_dalership_spring.configuration.CurrentUser;
import com.example.bmw_dalership_spring.model.Make;
import com.example.bmw_dalership_spring.model.Model;
import com.example.bmw_dalership_spring.model.Option;
import com.example.bmw_dalership_spring.model.Vehicle;
import com.example.bmw_dalership_spring.service.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.controlsfx.control.CheckComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;

@Controller
public class VehicleTabController extends BaseController {

    private final MakeService makeService;
    private ObservableList<Vehicle> vehicleObservableList;

    @FXML
    private Button addVehicleButton;
    @FXML
    private Button removeVehicleButton;
    @FXML
    private TableView vehicleTable;
    @FXML
    private Button viewOptionsButton;
    @FXML
    private TableColumn<Vehicle, Long> vehicleIdColumn;
    @FXML
    private TableColumn<Vehicle, String> makeColumn;
    @FXML
    private TableColumn<Vehicle, String> modelColumn;
    @FXML
    private TableColumn<Vehicle, String> vinColumn;
    @FXML
    private TableColumn<Vehicle, String> engineColumn;
    @FXML
    private TableColumn<Vehicle, String> fuelColumn;
    @FXML
    private TableColumn<Vehicle, Integer> powerColumn;
    @FXML
    private TableColumn<Vehicle, Integer> torqueColumn;
    @FXML
    private TableColumn<Vehicle, String> colorColumn;
    @FXML
    private TableColumn<Vehicle, Integer> mileageColumn;
    @FXML
    private TableColumn<Vehicle, Integer> yearColumn;
    @FXML
    private ImageView vehicleImage;
    @FXML
    private GridPane gridPane;
    @FXML
    private ChoiceBox<Make> makeField;
    @FXML
    private ChoiceBox<Model> modelField;
    @FXML
    private TextField vinField;
    @FXML
    private ChoiceBox<String> engineField;
    @FXML
    private ChoiceBox<String> fuelField;
    @FXML
    private TextField powerField;
    @FXML
    private TextField torqueField;
    @FXML
    private ColorPicker colorField;
    @FXML
    private TextField mileageField;
    @FXML
    private TextField yearField;
    @FXML
    private CheckComboBox<String> optionsField;
    @FXML
    private Button selectImagesButton;


    private final VehicleService vehicleService;
    private final OptionService optionService;
    private final ModelService modelService;


    @Autowired
    public VehicleTabController(VehicleService vehicleService, OptionService optionService, MakeService makeService, ModelService modelService) {
        this.vehicleService = vehicleService;
        this.optionService = optionService;
        this.makeService = makeService;
        this.modelService = modelService;
    }

    private byte[] image;
    @FXML
    private void handleViewOptions() {
        Vehicle selectedVehicle =(Vehicle) vehicleTable.getSelectionModel().getSelectedItem();

        if (selectedVehicle != null) {
            Vehicle vehicleWithOptions = vehicleService.getVehicleWithOptions(selectedVehicle.getId());
            Set<Option> options = vehicleWithOptions.getOptions();

            StringBuilder optionsText = new StringBuilder("Options:\n");
            for (Option option : options) {
                optionsText.append("- ").append(option.getName()).append("\n");
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Vehicle Options");
            alert.setHeaderText("Options for " + vehicleWithOptions.getMake() + " " + vehicleWithOptions.getModel());
            alert.setContentText(optionsText.toString());
            alert.showAndWait();
        } else {
            System.out.println("No vehicle selected.");
        }
    }
    @FXML
    private void handleAddVehicle() {
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
        System.out.println("Options i got from the input" + make);
        Set<Option> optionList = optionService.getOptionList(options);
        Vehicle vehicle = new Vehicle(vin, make, model, mileage, CurrentUser.getInstance().getUser(), image, color, engine, fuel, power, torque, year, optionList);
        vehicleService.saveVehicle(vehicle);
        vehicleObservableList.add(vehicle);
        System.out.println("Add Vehicle clicked");
    }

    @FXML
    private void handleRemoveVehicle() {

        Vehicle selectedVehicle = (Vehicle) vehicleTable.getSelectionModel().getSelectedItem();

        if (selectedVehicle != null) {
            vehicleService.removeVehicle(selectedVehicle);
            vehicleObservableList.remove(selectedVehicle);
            System.out.println("Removed vehicle with VIN: " + selectedVehicle.getVin());
        } else {
            System.out.println("No vehicle selected.");
        }
    }

    @FXML
    private void handleSort() {

        System.out.println("Sorting vehicles");

    }

    @FXML
    private void handleSelectImages() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Vehicle Image");


        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );


        File selectedFile = fileChooser.showOpenDialog(gridPane.getScene().getWindow());

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
    private void initialize() {
        vehicleObservableList = FXCollections.observableArrayList(vehicleService.getVehiclesByUser(CurrentUser.getInstance().getUser()));
        vehicleTable.setItems(vehicleObservableList);
        viewOptionsButton.disableProperty().bind(vehicleTable.getSelectionModel().selectedItemProperty().isNull());

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
        fuelField.getItems().addAll("Gasoline", "Diesel", "Electric");


        optionsField.getItems().addAll(optionService.getAllOptionsString());

        vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        makeColumn.setCellValueFactory(new PropertyValueFactory<>("make"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        vinColumn.setCellValueFactory(new PropertyValueFactory<>("vin"));
        vinColumn.setCellFactory(column -> new TableCell<Vehicle, String>() {
            private final Text text = new Text();

            {

                text.wrappingWidthProperty().bind(column.widthProperty().subtract(5));
                text.setStyle("-fx-padding: 0 5px 0 5px;");
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    text.setText(item);
                    setGraphic(text);
                }
            }
        });
        engineColumn.setCellValueFactory(new PropertyValueFactory<>("engine"));
        fuelColumn.setCellValueFactory(new PropertyValueFactory<>("fuel"));
        powerColumn.setCellValueFactory(new PropertyValueFactory<>("power"));
        torqueColumn.setCellValueFactory(new PropertyValueFactory<>("torque"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        mileageColumn.setCellValueFactory(new PropertyValueFactory<>("mileage"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));



        vehicleTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayVehicleImage((Vehicle) newValue);
            }
            viewOptionsButton.setVisible(newValue != null);
        });
    }

    private void displayVehicleImage(Vehicle vehicle) {
        if (vehicle.getImage() != null && vehicle.getImage().length > 0) {

            ByteArrayInputStream bis = new ByteArrayInputStream(vehicle.getImage());
            Image image = new Image(bis);
            vehicleImage.setImage(image);
        } else {
            vehicleImage.setImage(null);
        }
    }
}
