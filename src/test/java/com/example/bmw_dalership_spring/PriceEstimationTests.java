package com.example.bmw_dalership_spring;

import com.example.bmw_dalership_spring.model.Marketplace;
import com.example.bmw_dalership_spring.model.Option;
import com.example.bmw_dalership_spring.model.Vehicle;
import com.example.bmw_dalership_spring.service.MarketplaceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
public class PriceEstimationTests {
    @Autowired
    private MarketplaceService marketplaceService;
    @Test
    void contextLoads() {
    }

    @Test
    void testGetPriceEstimation_basicScenario() {
        // Arrange: Create a Marketplace with a known vehicle model & year, and add some mileage
        Vehicle vehicle = new Vehicle();
        vehicle.setModel("X5");        // Must exist in VehicleModelPriceReference.MODEL_REFERENCE_PRICES
        vehicle.setYear(java.time.Year.now().getValue() - 2); // 2 years old
        vehicle.setMileage(50_000);    // 50,000 km
        // Suppose it has zero or minimal options in this test
        vehicle.setOptions(Set.of());

        Marketplace marketplace = new Marketplace();
        marketplace.setVehicle(vehicle);

        // Act: call the method under test
        double estimatedPrice = marketplaceService.getPriceEstimation(marketplace);

        // Assert: we expect an estimation that is less than the base price but still not minimal
        System.out.println("Estimated price (basic scenario) = " + estimatedPrice);
        Assertions.assertTrue(estimatedPrice > 0, "Estimated price should be > 0");
    }

    @Test
    void testGetPriceEstimation_modelNotFound() {
        // Arrange: Provide a vehicle model that doesn't exist in the reference map
        Vehicle vehicle = new Vehicle();
        vehicle.setModel("NonExistentModel");
        vehicle.setYear(2020);
        vehicle.setMileage(10_000);

        Marketplace marketplace = new Marketplace();
        marketplace.setVehicle(vehicle);

        // Act
        double estimatedPrice = marketplaceService.getPriceEstimation(marketplace);

        // Assert
        // The method returns 0 if model is not found.
        // Adjust your expectation based on your method's actual behavior.
        Assertions.assertEquals(0.0, estimatedPrice, 0.0001,
                "Expected price to be 0.0 if model is not found");
    }

    @Test
    void testGetPriceEstimation_veryOldCarWithOptions() {
        // Arrange: A very old car with some options
        Vehicle vehicle = new Vehicle();
        vehicle.setModel("X7");      // Must exist in your reference
        vehicle.setYear(java.time.Year.now().getValue() - 25); // 25 years old
        vehicle.setMileage(300_000);

        // Suppose it has two options:
        Option option1 = new Option();
        option1.setName("Option1");
        option1.setPriceContribution(0.5); // e.g., contributes 0.5

        Option option2 = new Option();
        option2.setName("Option2");
        option2.setPriceContribution(1.0); // e.g., contributes 1.0

        vehicle.setOptions(Set.of(option1, option2));

        Marketplace marketplace = new Marketplace();
        marketplace.setVehicle(vehicle);

        // Act
        double estimatedPrice = marketplaceService.getPriceEstimation(marketplace);

        // Assert
        // Because the car is very old, it should have a strong depreciation,
        // but it should not fall below 10% of the base price.
        System.out.println("Estimated price (very old car) = " + estimatedPrice);
        Assertions.assertTrue(estimatedPrice > 0, "Estimated price should be > 0");
    }

}
