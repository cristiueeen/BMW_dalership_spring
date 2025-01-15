package com.example.bmw_dalership_spring.controller;

import com.example.bmw_dalership_spring.configuration.SpringFXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {
    protected ScreenManager screenManager;
    @Autowired
    private SpringFXMLLoader springFXMLLoader;

    void setSceneManager(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    protected void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void switchToScene(String fxmlPath, ActionEvent event) {
        try {
            FXMLLoader loader = springFXMLLoader.loadFXMLLoader(fxmlPath);
            Parent root = loader.getRoot();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to switch scenes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}