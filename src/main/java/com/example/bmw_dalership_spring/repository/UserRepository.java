package com.example.bmw_dalership_spring.repository;
import com.example.bmw_dalership_spring.model.ServiceAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.bmw_dalership_spring.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String Email);
    Optional<User> findByRole(String roleSuperadvisor);
}