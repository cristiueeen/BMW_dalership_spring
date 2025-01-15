package com.example.bmw_dalership_spring.controller;

import com.example.bmw_dalership_spring.configuration.SpringFXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class MainTabPaneController extends BaseController {

    @FXML
    private TabPane mainTabPane;

    @FXML
    private AnchorPane marketplaceTabPane, serviceAppointmentTabPane, vehicleTabPane, userDashboardTabPane;

    private final SpringFXMLLoader springFXMLLoader;

    @Autowired
    public MainTabPaneController(SpringFXMLLoader springFXMLLoader) {
        this.springFXMLLoader = springFXMLLoader;
    }

    @FXML
    public void initialize() {
        mainTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {
                try {
                    switch (newTab.getText()) {
                        case "Marketplace":
                            loadTabWithScreenManager(marketplaceTabPane, "/fxml/MarketplaceTab.fxml");
                            break;
                        case "Service Appointments":
                            loadTabWithScreenManager(serviceAppointmentTabPane, "/fxml/ServiceAppointmentTab.fxml");
                            break;
                        case "Vehicles":
                            loadTabWithScreenManager(vehicleTabPane, "/fxml/VehicleTab.fxml");
                            break;
                        case "Dashboard":
                            loadTabWithScreenManager(userDashboardTabPane, "/fxml/UserDashboard.fxml");
                            break;
                    }
                } catch (IOException e) {
                    showAlert(Alert.AlertType.ERROR, "Load Error", "Could not load tab: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        try {
            loadTabWithScreenManager(marketplaceTabPane, "/fxml/MarketplaceTab.fxml");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Load Error", "Could not load default tab: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadTabWithScreenManager(AnchorPane pane, String fxmlPath) throws IOException {
        pane.getChildren().clear();
        FXMLLoader loader = springFXMLLoader.loadFXMLLoader(fxmlPath);
        AnchorPane newContent = loader.getRoot();
        BaseController controller = loader.getController();

        if (controller != null) {
            System.out.println("screenManaget is set");
//            controller.setSceneManager(screenManager);
        }
        pane.getChildren().add(newContent);
    }
}
