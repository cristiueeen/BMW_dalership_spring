package com.example.bmw_dalership_spring.repository;
import com.example.bmw_dalership_spring.model.User;
import com.example.bmw_dalership_spring.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.bmw_dalership_spring.model.Marketplace;

import java.util.List;

@Repository
public interface MarketplaceRepository extends JpaRepository<Marketplace, Long> {
    Marketplace findByVehicle(Vehicle vehicle);
    List<Marketplace> findByOwner(User owner);
}