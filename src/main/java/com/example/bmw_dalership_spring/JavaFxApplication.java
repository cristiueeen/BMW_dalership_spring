package com.example.bmw_dalership_spring;

import com.example.bmw_dalership_spring.controller.BaseController;
import com.example.bmw_dalership_spring.controller.ScreenManager;
import com.example.bmw_dalership_spring.model.User;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext springContext;
    private ScreenManager screenManager;
    private User currentUser;
    private BaseController baseController;

    @Override
    public void init() {
        // Initialize Spring context
        springContext = new SpringApplicationBuilder(JavaFxApplication.class).run();
    }
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize ScreenManager
        // Get ScreenManager bean from Spring context
        screenManager = springContext.getBean(ScreenManager.class);


        // Inject primaryStage manually
        primaryStage.setTitle("BMW Dealership Management System");
        screenManager.setPrimaryStage(primaryStage);
        // Load the initial login screen
        screenManager.loadScreen("Login", "/fxml/LoginPage.fxml");
        screenManager.setScreen("Login");

    }

    @Override
    public void stop() {
        springContext.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
