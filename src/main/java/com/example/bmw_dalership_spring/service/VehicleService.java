package com.example.bmw_dalership_spring.service;

import com.example.bmw_dalership_spring.model.*;
import com.example.bmw_dalership_spring.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final MarketplaceService marketplaceService;
    private final ServiceAppointmentService serviceAppointmentService;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository ,@Lazy MarketplaceService marketplaceService, ServiceAppointmentService serviceAppointmentService) {
        this.vehicleRepository = vehicleRepository;
        this.marketplaceService = marketplaceService;
        this.serviceAppointmentService = serviceAppointmentService;
    }
    public void saveVehicle(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }
    public List<Vehicle> getVehiclesFromDatabase() {
        return vehicleRepository.findAll();
    }
    @Transactional
    public void removeVehicle(Vehicle selectedVehicle) {
        Vehicle vehicle = this.getVehicleWithOptions(selectedVehicle.getId());
        Marketplace linkedMarketplace = marketplaceService.getMarketplaceByVehicle(vehicle);
        if (linkedMarketplace != null) {
            marketplaceService.deleteMarketplace(linkedMarketplace);
        }
        List<ServiceAppointment> linkedServiceAppointments = serviceAppointmentService.getAppointmentsByVehicle(vehicle);
        if (linkedServiceAppointments != null) {
            for (ServiceAppointment serviceAppointment : linkedServiceAppointments) {
                serviceAppointmentService.deleteAppointment(serviceAppointment);
            }
        }
        vehicle.getOptions().clear();
        vehicleRepository.delete(vehicle);
    }
    @Transactional
    public Vehicle getVehicleWithOptions(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        vehicle.getOptions().size();  // This triggers the lazy loading

        return vehicle;
    }

    @Transactional
    public List<Vehicle> getVehiclesByUser(User user) {
        return vehicleRepository.findAllByOwner(user);
    }
    @Transactional
    public Vehicle getVehicleByVin(String vin)
    {
        return vehicleRepository.findByVin(vin);
    }
}
