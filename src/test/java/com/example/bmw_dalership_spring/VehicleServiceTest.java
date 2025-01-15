package com.example.bmw_dalership_spring;

import com.example.bmw_dalership_spring.model.Marketplace;
import com.example.bmw_dalership_spring.model.Option;
import com.example.bmw_dalership_spring.model.ServiceAppointment;
import com.example.bmw_dalership_spring.model.Vehicle;
import com.example.bmw_dalership_spring.repository.VehicleRepository;
import com.example.bmw_dalership_spring.service.MarketplaceService;
import com.example.bmw_dalership_spring.service.ServiceAppointmentService;
import com.example.bmw_dalership_spring.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private MarketplaceService marketplaceService;

    @Mock
    private ServiceAppointmentService serviceAppointmentService;

    // This is the class under test
    @InjectMocks
    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        // Any setup needed before each test
    }

    @Test
    void testRemoveVehicle() {
        // Arrange
        Vehicle selectedVehicle = new Vehicle();
        selectedVehicle.setId(1L);

        // The "fully loaded" vehicle with options
        Vehicle vehicleWithOptions = new Vehicle();
        vehicleWithOptions.setId(1L);
        // Add some dummy options
        Set<Option> options = new HashSet<>();
        options.add(new Option());
        vehicleWithOptions.setOptions(options);

        // Mock the repository call that happens inside getVehicleWithOptions(...)
        when(vehicleRepository.findById(1L))
                .thenReturn(Optional.of(vehicleWithOptions));

        // Mock marketplace, appointments, etc.
        Marketplace linkedMarketplace = new Marketplace();
        linkedMarketplace.setId(10L);
        when(marketplaceService.getMarketplaceByVehicle(vehicleWithOptions))
                .thenReturn(linkedMarketplace);

        ServiceAppointment appointment1 = new ServiceAppointment();
        appointment1.setId(100L);
        List<ServiceAppointment> linkedAppointments = List.of(appointment1);
        when(serviceAppointmentService.getAppointmentsByVehicle(vehicleWithOptions))
                .thenReturn(linkedAppointments);

        // Act
        vehicleService.removeVehicle(selectedVehicle);

        // Assert
        verify(marketplaceService).deleteMarketplace(linkedMarketplace);
        verify(serviceAppointmentService).deleteAppointment(appointment1);
        assertTrue(vehicleWithOptions.getOptions().isEmpty(),
                "Options should have been cleared before deleting the vehicle");
        verify(vehicleRepository).delete(vehicleWithOptions);
    }
    @Test
    void testRemoveVehicle_whenNoMarketplaceOrAppointments() {
        Vehicle selectedVehicle = new Vehicle();
        selectedVehicle.setId(2L);

        Vehicle vehicleWithOptions = new Vehicle();
        vehicleWithOptions.setId(2L);
        vehicleWithOptions.setOptions(new HashSet<>()); //empty opt list

        when(vehicleRepository.findById(2L))
                .thenReturn(Optional.of(vehicleWithOptions));

        // If no marketplace is found
        when(marketplaceService.getMarketplaceByVehicle(vehicleWithOptions))
                .thenReturn(null);
        // If no service appointments are found
        when(serviceAppointmentService.getAppointmentsByVehicle(vehicleWithOptions))
                .thenReturn(new ArrayList<>());

        // Act
        vehicleService.removeVehicle(selectedVehicle);

        // Assert
        verify(marketplaceService, never()).deleteMarketplace(any());
        verify(serviceAppointmentService, never()).deleteAppointment(any());
        verify(vehicleRepository).delete(vehicleWithOptions);
    }
}
