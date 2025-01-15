package com.example.bmw_dalership_spring.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bmw_dalership_spring.model.Option;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    // Add custom queries if needed
//    Option findByName(String name);
    // OptionRepository.java
    @Query("SELECT o FROM Option o WHERE o.name = :name")
    Option findByName(@Param("name") String name);

}