package com.example.bmw_dalership_spring.controller;

import com.example.bmw_dalership_spring.model.User;
import com.example.bmw_dalership_spring.repository.UserRepository;
import com.example.bmw_dalership_spring.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class ResetPasswordPageController extends BaseController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserDetailsServiceAutoConfiguration userDetailsServiceAutoConfiguration;


    @Autowired
    public ResetPasswordPageController(UserService userService, UserRepository userRepository, UserDetailsServiceAutoConfiguration userDetailsServiceAutoConfiguration) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userDetailsServiceAutoConfiguration = userDetailsServiceAutoConfiguration;
    }
    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordResetTokenField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button resetPasswordButton;

    @FXML
    private Button backButton;

    
    @FXML
    private void handleResetPassword(ActionEvent event) {
       String email = emailField.getText();
       String passwordResetToken = passwordResetTokenField.getText();
       String confirmPassword = confirmPasswordField.getText();
       String password = passwordField.getText();

       User user = userRepository.findByEmail(email);
       if (user != null) {
           if (passwordResetToken.equals(user.getPasswordResetToken())) {
               if (confirmPassword.equals(password)) {
                   userService.updatePassword(user, password);
                   showAlert(Alert.AlertType.INFORMATION, "Password succesfully reset", "Password succesfully reset, login now");

                   switchToScene("/fxml/LoginPage.fxml", event);

               }
               else{
                   showAlert(Alert.AlertType.ERROR, "Password does not match", "Password does not match");
               }
           }
           else{
               showAlert(Alert.AlertType.ERROR, "Password token incorrect", "Password reset token does not match");
           }
       }
       else {
           showAlert(Alert.AlertType.ERROR, "Invalid email", "Invalid email");
       }
    }

    
    @FXML
    private void handleBackButton(ActionEvent event){
        System.out.println("Back button clicked");

        switchToScene("/fxml/LoginPage.fxml", event);
    }
}
