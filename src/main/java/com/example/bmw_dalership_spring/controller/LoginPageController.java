package com.example.bmw_dalership_spring.controller;
import com.example.bmw_dalership_spring.configuration.CurrentUser;
import com.example.bmw_dalership_spring.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class LoginPageController extends BaseController{

    private final UserService userService;


    @Autowired
    public LoginPageController(UserService userService) {
        this.userService = userService;
    }
    @FXML
    private TextField emailAddressField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button signInButton;

    @FXML
    private Button registerButton;

    @FXML
    private Button supervisorModeButton;

    @FXML
    private Hyperlink passwordResetButton;

    @FXML
    void handleSignIn(ActionEvent event) {
        String email = emailAddressField.getText();
        String password = passwordTextField.getText();


        if (email.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Sign In Failed", "Email and password are required.");
            return;
        }
        if (userService.validateUserLogIn(email, password)) {
            showAlert(AlertType.INFORMATION, "Sign In Successful", "Welcome, " + email + "!");
            CurrentUser.getInstance().setUser(userService.getUserByEmail(email));


            switchToScene("/fxml/MainTabPane.fxml", event);
        }
        else
        {
            showAlert(AlertType.ERROR, "Sign In Failed", "Invalid email or password.");
        }
    }

    @FXML
    void handleRegister(ActionEvent event) {


        switchToScene("/fxml/RegisterPage.fxml", event);
    }

    @FXML
    void handleSupervisorMode(ActionEvent event) {

        emailAddressField.setText("contact@bmw.com");
        showAlert(AlertType.INFORMATION, "Supervisor Mode", "Supervisor mode activated, you may use the classic, very secure pass for a admin account.");
    }

    @FXML
    void handlePasswordReset(ActionEvent event) {

        switchToScene("/fxml/ResetPasswordPage.fxml", event);
    }

    @FXML
    private void initialize() {


    }

}
