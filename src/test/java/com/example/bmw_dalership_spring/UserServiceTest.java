package com.example.bmw_dalership_spring;

import com.example.bmw_dalership_spring.model.User;
import com.example.bmw_dalership_spring.repository.UserRepository;
import com.example.bmw_dalership_spring.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void whenUserExistsAndPasswordMatches_thenValidateReturnsTrue() {
        // Arrange
        String email = "test@example.com";
        String password = "secret";

        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword(password);

        // Mock the repository to return our "existing" user
        when(userRepository.findByEmail(email)).thenReturn(mockUser);

        // Act
        Boolean result = userService.validateUserLogIn(email, password);

        // Assert
        Assertions.assertTrue(result,
                "Expected validateUserLogIn to return true when password matches");
    }

    @Test
    void whenUserExistsButPasswordDoesNotMatch_thenValidateReturnsFalse() {
        // Arrange
        String email = "test@example.com";
        String correctPassword = "secret";
        String wrongPassword = "wrong-secret";

        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword(correctPassword);

        when(userRepository.findByEmail(email)).thenReturn(mockUser);

        // Act
        Boolean result = userService.validateUserLogIn(email, wrongPassword);

        // Assert
        Assertions.assertFalse(result,
                "Expected validateUserLogIn to return false when passwords do not match");
    }

    @Test
    void whenUserDoesNotExist_thenValidateReturnsFalse() {
        // Arrange
        String email = "unknown@example.com";
        String password = "irrelevant";

        // Mock the repository to return null, meaning user not found
        when(userRepository.findByEmail(email)).thenReturn(null);

        // Act
        Boolean result = userService.validateUserLogIn(email, password);

        // Assert
        Assertions.assertFalse(result,
                "Expected validateUserLogIn to return false when the user is not found");
    }
}
