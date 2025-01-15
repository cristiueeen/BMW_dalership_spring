package com.example.bmw_dalership_spring.repository;
import com.example.bmw_dalership_spring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.bmw_dalership_spring.model.Vehicle;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findAllByOwner(User owner);
    Vehicle findByVin(String vin);
}
