package com.example.bmw_dalership_spring.controller;

import com.example.bmw_dalership_spring.configuration.CurrentUser;
import com.example.bmw_dalership_spring.model.*;
import com.example.bmw_dalership_spring.service.CarPartService;
import com.example.bmw_dalership_spring.service.ServiceAppointmentService;
import com.example.bmw_dalership_spring.service.VehicleService;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.controlsfx.control.CheckComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
public class ServiceAppointmentController extends BaseController {

    @FXML
    private ListView<Vehicle> vehicleListView;

    @FXML
    private CheckComboBox<String> partsCheckComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label repairTimeLabel;

    @FXML
    private Label repairCostLabel;

    @FXML
    private Button scheduleAppointmentButton;

    private final VehicleService vehicleService;
    private final CarPartService partService;
    private final ServiceAppointmentService serviceAppointmentService;

    private final ObservableList<LocalDate> bookedDates = FXCollections.observableArrayList();


    @Autowired
    public ServiceAppointmentController(VehicleService vehicleService, CarPartService partService, ServiceAppointmentService serviceAppointmentService) {
        this.vehicleService = vehicleService;
        this.partService = partService;
        this.serviceAppointmentService = serviceAppointmentService;
    }

    @FXML
    private void initialize() {

        List<Vehicle> userVehicles = vehicleService.getVehiclesByUser(CurrentUser.getInstance().getUser());
        ObservableList<Vehicle> vehicleObservableList = FXCollections.observableArrayList(userVehicles);
        vehicleListView.setItems(vehicleObservableList);

        vehicleListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Vehicle vehicle, boolean empty) {
                super.updateItem(vehicle, empty);
                if (empty || vehicle == null) {
                    setText(null);
                } else {
                    String listItem = String.format("%s %s - %d - VIN: %s",
                            vehicle.getMake(), vehicle.getModel(), vehicle.getYear(), vehicle.getVin());
                    setText(listItem);
                }
            }
        });


        List<CarPart> parts = partService.getAllParts();
        partsCheckComboBox.getItems().addAll(parts.stream().map(CarPart::getPartName).collect(Collectors.toList()));


        datePicker.setDayCellFactory(getCustomDateCellFactory());


        scheduleAppointmentButton.setDisable(true);


        vehicleListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> checkFieldsAndUpdateButtonState());
        partsCheckComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) change -> {
            while (change.next()) {
                calculateRepairTimeAndCost();
            }
        });
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> checkFieldsAndUpdateButtonState());
    }

    private void checkFieldsAndUpdateButtonState() {
        boolean allFieldsSelected = vehicleListView.getSelectionModel().getSelectedItem() != null &&
                !partsCheckComboBox.getCheckModel().getCheckedItems().isEmpty() &&
                datePicker.getValue() != null;

        scheduleAppointmentButton.setDisable(!allFieldsSelected);
    }

    private Callback<DatePicker, DateCell> getCustomDateCellFactory() {

        bookedDates.setAll(serviceAppointmentService.getAllAppointments().stream()
                .map(ServiceAppointment::getAppointmentDate)
                .collect(Collectors.toList()));

        return datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (item.getDayOfWeek() == DayOfWeek.SATURDAY ||
                        item.getDayOfWeek() == DayOfWeek.SUNDAY ||
                        bookedDates.contains(item) ||
                        item.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        };
    }

    private void calculateRepairTimeAndCost() {
        List<String> selectedParts = partsCheckComboBox.getCheckModel().getCheckedItems();
        double totalCost = 0.0;
        int totalTime = 0;

        for (String partName : selectedParts) {
            CarPart part = partService.getPartByName(partName);
            if (part != null) {
                totalCost += part.getPrice();
                totalTime += part.getEstimatedRepairTime();
            }
        }

        repairCostLabel.setText(String.format("%.2f €", totalCost));
        repairTimeLabel.setText(totalTime + " hours");


        checkFieldsAndUpdateButtonState();
    }

    @FXML
    private void handleScheduleAppointment() {
        Vehicle selectedVehicle = vehicleListView.getSelectionModel().getSelectedItem();
        List<String> selectedParts = partsCheckComboBox.getCheckModel().getCheckedItems();
        LocalDate selectedDate = datePicker.getValue();

        if (selectedVehicle == null || selectedParts.isEmpty() || selectedDate == null) {
            showAlert("Error", "Please select a vehicle, parts, and a valid date.");
            return;
        }
        double totalCost = 0.0;
        try {
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            Number number = format.parse(repairCostLabel.getText().replace("€", "").trim());
            totalCost = number.doubleValue() / 100;
        } catch (ParseException e) {
            System.err.println("Invalid number format: " + e.getMessage());
        }
        int totalTime = Integer.parseInt(repairTimeLabel.getText().replace("hours", "").trim());

        ServiceAppointment appointment = ServiceAppointment.create(
                selectedDate,
                totalCost,
                totalTime,
                "Scheduled",
                selectedVehicle,
                CurrentUser.getInstance().getUser()
        );

        serviceAppointmentService.saveAppointment(appointment);

        bookedDates.add(selectedDate);
        datePicker.setDayCellFactory(getCustomDateCellFactory());

        showAlert("Success", "Your service appointment has been scheduled!");
        clearForm();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        vehicleListView.getSelectionModel().clearSelection();
        partsCheckComboBox.getCheckModel().clearChecks();
        datePicker.setValue(null);
        repairCostLabel.setText("");
        repairTimeLabel.setText("");
    }
}
