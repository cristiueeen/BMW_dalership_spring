package com.example.bmw_dalership_spring.service;

import com.example.bmw_dalership_spring.model.ServiceAppointment;
import com.example.bmw_dalership_spring.model.User;
import com.example.bmw_dalership_spring.model.Vehicle;
import com.example.bmw_dalership_spring.repository.ServiceAppointmentRepository;
import com.example.bmw_dalership_spring.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ServiceAppointmentService {
    private final ServiceAppointmentRepository serviceAppointmentRepository;
    private final UserRepository userRepository;

    @Autowired
    public ServiceAppointmentService(ServiceAppointmentRepository serviceAppointmentRepository, UserRepository userRepository) {
        this.serviceAppointmentRepository = serviceAppointmentRepository;
        this.userRepository = userRepository;
    }

    public List<ServiceAppointment> getAllAppointments() {
        return serviceAppointmentRepository.findAll();
    }

    public void saveAppointment(ServiceAppointment appointment) {
        serviceAppointmentRepository.save(appointment);
    }
    public List<ServiceAppointment> getAppointmentsByVehicle(Vehicle vehicle) {
        return serviceAppointmentRepository.findByVehicle(vehicle);
    }
    public void deleteAppointment(ServiceAppointment appointment) {
        serviceAppointmentRepository.delete(appointment);
    }

    @Transactional
    public List<ServiceAppointment> getAppointmentsByUser(User user) {
        return serviceAppointmentRepository.findByUser(user);
    }
}
