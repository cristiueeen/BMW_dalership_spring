package com.example.bmw_dalership_spring.repository;
import com.example.bmw_dalership_spring.controller.VehicleTabController;
import com.example.bmw_dalership_spring.model.User;
import com.example.bmw_dalership_spring.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.bmw_dalership_spring.model.ServiceAppointment;

import java.util.List;

@Repository
public interface ServiceAppointmentRepository extends JpaRepository<ServiceAppointment, Long> {
    List<ServiceAppointment> findByVehicle(Vehicle vehicle);
    List<ServiceAppointment> findByUser(User user);
}