package com.example.bmw_dalership_spring.service;
import com.example.bmw_dalership_spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.stereotype.Service;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

import com.example.bmw_dalership_spring.model.User;
import org.springframework.validation.annotation.Validated;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsServiceAutoConfiguration userDetailsServiceAutoConfiguration;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void updatePassword(User user,String password){
        user.setPassword(password);
        userRepository.save(user);
    }
    public Boolean validateUserLogIn(String email, String password)
    {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            return user.getPassword().equals(password);
        }
        return false;
    }
    public void registerUser(@Valid User user)
    {
        userRepository.save(user);
    }

    public User getUserByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }
}
