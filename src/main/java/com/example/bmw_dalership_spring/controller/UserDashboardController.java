package com.example.bmw_dalership_spring.controller;

import com.example.bmw_dalership_spring.configuration.CurrentUser;
import com.example.bmw_dalership_spring.model.Marketplace;
import com.example.bmw_dalership_spring.model.ServiceAppointment;
import com.example.bmw_dalership_spring.service.MarketplaceService;
import com.example.bmw_dalership_spring.service.ServiceAppointmentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserDashboardController extends BaseController {

    @FXML
    private Label greetingTextLabel;

    @FXML
    private ListView<String> appointmentListView;

    @FXML
    private ListView<String> marketplaceListView;

    @FXML
    private Button cancelAppointmentButton;

    @FXML
    private Button deleteListingButton;

    private final ServiceAppointmentService serviceAppointmentService;
    private final MarketplaceService marketplaceService;

    private ObservableList<ServiceAppointment> serviceAppointments;
    private ObservableList<Marketplace> marketplaceListings;

    @Autowired
    public UserDashboardController(ServiceAppointmentService serviceAppointmentService, MarketplaceService marketplaceService) {
        this.serviceAppointmentService = serviceAppointmentService;
        this.marketplaceService = marketplaceService;
    }

    @FXML
    private void initialize() {
        greetingTextLabel.setText("Good evening, " + CurrentUser.getInstance().getUser().getFirstName() + ", welcome!");

        appointmentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        marketplaceListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        loadAppointments();
        loadMarketplaceListings();


        cancelAppointmentButton.setDisable(true);
        deleteListingButton.setDisable(true);


        appointmentListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cancelAppointmentButton.setDisable(newValue == null);
        });

        marketplaceListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            deleteListingButton.setDisable(newValue == null);
        });
    }

    private void loadAppointments() {
        List<ServiceAppointment> appointments = serviceAppointmentService.getAppointmentsByUser(CurrentUser.getInstance().getUser());
        serviceAppointments = FXCollections.observableArrayList(appointments);
        ObservableList<String> appointmentDisplayList = FXCollections.observableArrayList();

        for (ServiceAppointment appointment : appointments) {
            String displayText = String.format("Appointment for: %s %s VIN: %s\nDate: %s\nStatus: %s\nTotal Invoice: %.2f €",
                    appointment.getVehicle().getMake(),
                    appointment.getVehicle().getModel(),
                    appointment.getVehicle().getVin(),
                    appointment.getAppointmentDate().toString(),
                    appointment.getStatus(),
                    appointment.getRepairPrice());
            appointmentDisplayList.add(displayText);
        }
        appointmentListView.setItems(appointmentDisplayList);
    }

    private void loadMarketplaceListings() {
        List<Marketplace> listings = marketplaceService.getListingsByUser(CurrentUser.getInstance().getUser());
        marketplaceListings = FXCollections.observableArrayList(listings);
        ObservableList<String> listingDisplayList = FXCollections.observableArrayList();

        for (Marketplace listing : listings) {
            String displayText = String.format("%s %s, VIN: %s\nListed for: %.2f €",
                    listing.getVehicle().getMake(),
                    listing.getVehicle().getModel(),
                    listing.getVehicle().getVin(),
                    listing.getVehicle().getPrice());
            listingDisplayList.add(displayText);
        }
        marketplaceListView.setItems(listingDisplayList);
    }

    @FXML
    private void handleCancelAppointment() {
        int selectedIndex = appointmentListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            ServiceAppointment selectedAppointment = serviceAppointments.get(selectedIndex);


            boolean confirmation = showConfirmationDialog("Cancel Appointment", "Are you sure you want to cancel this appointment?");
            if (!confirmation) return;

            serviceAppointmentService.deleteAppointment(selectedAppointment);
            showAlert(Alert.AlertType.INFORMATION, "Appointment Canceled", "The appointment has been successfully canceled.");


            loadAppointments();
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an appointment to cancel.");
        }
    }

    @FXML
    private void handleDeleteListing() {
        int selectedIndex = marketplaceListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Marketplace selectedListing = marketplaceListings.get(selectedIndex);


            boolean confirmation = showConfirmationDialog("Delete Listing", "Are you sure you want to delete this marketplace listing?");
            if (!confirmation) return;

            marketplaceService.deleteMarketplace(selectedListing);
            showAlert(Alert.AlertType.INFORMATION, "Listing Deleted", "The marketplace listing has been successfully deleted.");


            loadMarketplaceListings();
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a listing to delete.");
        }
    }

    @FXML
    private void handleLogOut(ActionEvent event) {
        switchToScene("/fxml/LoginPage.fxml", event);
    }

    private boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        return alert.showAndWait().isPresent() && alert.getResult().getText().equalsIgnoreCase("OK");
    }
}
