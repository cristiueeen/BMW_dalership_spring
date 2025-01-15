package com.example.bmw_dalership_spring.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ScreenManager {

    private Stage primaryStage;
    private final Map<String, Parent> screens = new HashMap<>();
    private final ApplicationContext applicationContext;

    @Autowired
    public ScreenManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void loadScreen(String name, String resource) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));

            loader.setControllerFactory(applicationContext::getBean);
            Parent screen = loader.load();
            if (!screens.containsKey(name)) {
                BaseController controller = loader.getController();
                controller.setSceneManager(this);
                screens.put(name, screen);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load screen: " + resource);
        }
    }

    public void setScreen(String name) {
        if (screens.containsKey(name)) {
            Scene scene = new Scene(screens.get(name));
            if (primaryStage != null) {
                primaryStage.setScene(scene);
                primaryStage.show();
            } else {
                System.err.println("Primary stage is not set!");
            }
        } else {
            System.err.println("Screen not found: " + name);
        }
    }
}