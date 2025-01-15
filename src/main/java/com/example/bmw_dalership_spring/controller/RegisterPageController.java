package com.example.bmw_dalership_spring.controller;

import com.example.bmw_dalership_spring.model.User;
import com.example.bmw_dalership_spring.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;


@Controller
public class RegisterPageController extends BaseController {

    private final UserService userService;


    @Autowired
    public RegisterPageController(UserService userService) {
        this.userService = userService;
    }

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailAddressField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField confirmPasswordField;

    @FXML
    private TextField passwordResetTokenField;

    @FXML
    private Button clearFormButton;

    @FXML
    private Button registerButton;

    @FXML
    private Text registerText;



    @FXML
    public void handleClearForm() {
        firstNameField.clear();
        lastNameField.clear();
        emailAddressField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        passwordResetTokenField.clear();
    }
    @FXML
    public void handleBackButton(ActionEvent event)
    {
        System.out.println("Back button clicked");

        switchToScene("/fxml/LoginPage.fxml", event);
    }


    @FXML
    public void handleRegister() {

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailAddressField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String resetToken = passwordResetTokenField.getText();
        User user = new User(firstName, lastName, email, password,"ROLE_USER", resetToken);


        if (!password.equals(confirmPassword)) {

            System.out.println("Passwords do not match!");
        } else {
            try {
                userService.registerUser(user);
                showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Welcome, " + firstName + "!");

            } catch (ConstraintViolationException e) {

                e.getConstraintViolations().forEach(violation ->
                        System.out.println("Validation error: " + violation.getMessage())
                );
                showAlert(Alert.AlertType.ERROR, "Registration Failed", "Invalid data-fields, check entries and try again.");
            }

        }
    }



}
