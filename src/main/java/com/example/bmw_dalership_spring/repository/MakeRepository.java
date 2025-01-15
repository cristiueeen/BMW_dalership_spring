package com.example.bmw_dalership_spring.repository;
import com.example.bmw_dalership_spring.model.Make;
import com.example.bmw_dalership_spring.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MakeRepository extends JpaRepository<Make, Long> {
    Make findByMake(String makeName);
}