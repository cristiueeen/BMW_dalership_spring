package com.example.bmw_dalership_spring.configuration;

import com.example.bmw_dalership_spring.controller.BaseController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.InputStream;

public class SpringFXMLLoader {

    private final ApplicationContext context;

    public SpringFXMLLoader(ApplicationContext context) {
        this.context = context;
    }

    public FXMLLoader loadFXMLLoader(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(context::getBean);
        try (InputStream fxmlStream = getClass().getResourceAsStream(fxmlPath)) {
            if (fxmlStream == null) {
                throw new IllegalArgumentException("FXML file not found: " + fxmlPath);
            }
            loader.load(fxmlStream);
        }
        return loader;
    }
}
