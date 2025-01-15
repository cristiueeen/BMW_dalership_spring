package com.example.bmw_dalership_spring.repository;
import com.example.bmw_dalership_spring.model.Make;
import com.example.bmw_dalership_spring.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
    List<Model> findModelsByMake(Make make);
    Model findModelByModel(String name);
}