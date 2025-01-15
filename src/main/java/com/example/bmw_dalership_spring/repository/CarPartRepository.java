package com.example.bmw_dalership_spring.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.bmw_dalership_spring.model.CarPart;

@Repository
public interface CarPartRepository extends JpaRepository<CarPart, Long>{
    CarPart findCarPartByPartName(String partName);
}