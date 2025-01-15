package com.example.bmw_dalership_spring.configuration;

import com.example.bmw_dalership_spring.model.*;
import com.example.bmw_dalership_spring.repository.OptionRepository;
import com.example.bmw_dalership_spring.repository.UserRepository;
import com.example.bmw_dalership_spring.repository.VehicleRepository;
import com.example.bmw_dalership_spring.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.print.attribute.standard.MediaPrintableArea;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.example.bmw_dalership_spring.configuration.VehicleModelPriceReference.MODEL_REFERENCE_PRICES;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private CarPartService carPartService;
    @Autowired
    private MarketplaceService marketplaceService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private UserService userService;
    @Autowired
    private OptionService optionService;
    @Autowired
    private MakeService makeService;
    @Autowired
    private ModelService modelService;

    @Override
    public void run(String... args) throws Exception {

        Optional<User> existingSupervisor = userRepository.findByRole("ROLE_SUPERADVISOR");

        if (existingSupervisor.isEmpty()) {

            User supervisor = new User("BMW","Administration","contact@bmw.com","admin","ROLE_SUPERADVISOR","admin");

            userRepository.save(supervisor);
            System.out.println("Supervisor user created: " + supervisor.getEmail());
        } else {
            System.out.println("Supervisor already exists in the database.");
        }


        Map<String, Double> featureWeights = new HashMap<>();

        featureWeights.put("Sunroof", 0.8);
        featureWeights.put("Leather Seats", 0.7);
        featureWeights.put("Navigation", 0.9);
        featureWeights.put("Backup Camera", 0.8);
        featureWeights.put("Heated Seats", 0.6);
        featureWeights.put("Heated Mirrors", 0.5);
        featureWeights.put("iDrive Navigation System", 1.0);
        featureWeights.put("CD Changer", 0.3);
        featureWeights.put("Sports Package", 0.7);
        featureWeights.put("Bluetooth Connectivity", 0.9);
        featureWeights.put("Harman Kardon Sound System", 0.8);
        featureWeights.put("Keyless Entry", 0.7);
        featureWeights.put("Xenon Headlights", 0.7);
        featureWeights.put("Panoramic Roof", 0.7);
        featureWeights.put("Parking Sensors", 0.9);
        featureWeights.put("Heads-Up Display", 0.9);
        featureWeights.put("Adaptive Cruise Control", 1.0);
        featureWeights.put("Lane Departure Warning", 0.8);
        featureWeights.put("Premium Package", 0.8);
        featureWeights.put("M Sport Package", 0.9);
        featureWeights.put("Power Tailgate", 0.6);
        featureWeights.put("Ventilated Seats", 0.6);
        featureWeights.put("Automatic Climate Control", 0.7);
        featureWeights.put("Memory Seats", 0.5);
        featureWeights.put("Surround View Camera", 0.9);
        featureWeights.put("Apple CarPlay", 0.8);
        featureWeights.put("Android Auto", 0.8);
        featureWeights.put("Wireless Charging", 0.6);
        featureWeights.put("Ambient Lighting", 0.5);
        featureWeights.put("All-Wheel Drive (xDrive)", 1.0);
        featureWeights.put("Dynamic Stability Control", 0.9);
        featureWeights.put("Run-Flat Tires", 0.6);
        featureWeights.put("Power Folding Mirrors", 0.5);
        featureWeights.put("Auto-Dimming Mirrors", 0.5);
        featureWeights.put("Hill Descent Control", 0.7);
        featureWeights.put("Rear-Seat Entertainment", 0.6);
        featureWeights.put("Third-Row Seating", 0.4);
        featureWeights.put("Adaptive Headlights", 0.9);
        featureWeights.put("Soft-Close Doors", 0.2);
        featureWeights.put("Digital Instrument Cluster", 0.8);

        featureWeights.forEach((feature, weight) -> {

            Option existingOption = optionRepository.findByName(feature);

            if (existingOption == null) {

                optionRepository.save(new Option(feature, weight));
            }
        });

        List<CarPart> partsList = List.of(
                new CarPart("Engine", 5000, 12),
                new CarPart("Alternator", 600, 3),
                new CarPart("Starter", 400, 2),
                new CarPart("Head Gasket", 1200, 10),
                new CarPart("Water Pump", 250, 2),
                new CarPart("Timing Chain", 1500, 8),
                new CarPart("Engine Oil and Filters", 100, 1),
                new CarPart("ECU", 900, 1),
                new CarPart("Drive Unit", 3000, 6),
                new CarPart("Speaker", 200, 1),
                new CarPart("Wiring Loom", 1200, 8),
                new CarPart("Door", 800, 3),
                new CarPart("Hood", 1000, 4),
                new CarPart("Bumper", 600, 3),
                new CarPart("Headlight", 400, 1),
                new CarPart("Taillight", 300, 1),
                new CarPart("Interior Trim", 150, 2),
                new CarPart("Seat", 600, 2),
                new CarPart("Windshield", 1000, 3),
                new CarPart("Full Vehicle Reprogram", 500, 2),
                new CarPart("Battery", 150, 1),
                new CarPart("AC Compressor", 900, 4),
                new CarPart("Belt", 100, 1),
                new CarPart("Coolant", 80, 1),
                new CarPart("Brake Pads", 250, 1),
                new CarPart("Brake Calipers", 400, 2),
                new CarPart("Brake Disks", 300, 2),
                new CarPart("Brake Fluid", 70, 1),
                new CarPart("Valve Cover Gasket", 200, 3),
                new CarPart("Vanos Unit", 1200, 5),
                new CarPart("Turbocharger", 2500, 10),
                new CarPart("Intercooler", 600, 3),
                new CarPart("Radiator", 900, 3),
                new CarPart("Coolant Hoses", 200, 2),
                new CarPart("Charged Air Piping", 350, 2),
                new CarPart("Frame Repair", 5000, 15),
                new CarPart("Key", 300, 1),
                new CarPart("Airbags", 2500, 6),
                new CarPart("Tires", 600, 2),
                new CarPart("Wheels", 800, 2),
                new CarPart("Pressure Sensors", 150, 1),
                new CarPart("Oil Pump", 700, 6),
                new CarPart("Rod Bearings", 1800, 10),
                new CarPart("Main Bearings", 2000, 10),
                new CarPart("Fuel Injectors", 900, 4),
                new CarPart("Fuel Pump", 600, 3),
                new CarPart("Fuel Lines", 500, 3),
                new CarPart("Fuel Tank", 1200, 6),
                new CarPart("ABS System", 1500, 6),
                new CarPart("Gearbox", 4000, 12),
                new CarPart("Clutch", 2000, 8),
                new CarPart("Driveshaft", 1000, 4),
                new CarPart("Differential", 2500, 10),
                new CarPart("Fenders", 400, 3),
                new CarPart("EGR", 600, 3),
                new CarPart("DPF", 2000, 8),
                new CarPart("Catalytic Converter", 1500, 6),
                new CarPart("Exhaust", 1000, 4),
                new CarPart("Lambda Sensors", 300, 1)
        );
        List<CarPart> existingParts = carPartService.getAllParts();
        Set<String> existingPartNames = new HashSet<>();
        for (CarPart part : existingParts) {
            existingPartNames.add(part.getPartName().toLowerCase());
        }

        for (CarPart carPart : partsList) {
            if (!existingPartNames.contains(carPart.getPartName().toLowerCase())) {
                carPartService.savePart(carPart);
            }
        }
        createMakeAndModelsIfNotPresent("BMW", Arrays.asList(

                "116i", "118i", "118d", "116d", "120d", "120i", "320i", "320d", "320xi", "320xd", "323i", "323xi", "328i", "328xi",
                "330i", "330d", "330xi", "330xd", "335i", "335d", "335xi", "335xd",
                "520i", "520d", "520xi", "520xd", "525i", "525xi", "530i", "530d",
                "530xi", "530xd", "540i", "540xi", "550i", "550xi",
                "730i", "730d", "730xi", "730xd", "740i", "740d", "740xi", "740xd",
                "750i", "750xi",
                "X1", "X3", "X4", "X5", "X6", "X7",
                "M2", "M3", "M4", "M5", "M6", "M8",
                "iX", "i4"
        ));
        createMakeAndModelsIfNotPresent("Mini", Arrays.asList(
                "Cooper", "Cooper S", "John Cooper Works", "Countryman", "Clubman", "Electric"
        ));

        createMakeAndModelsIfNotPresent("Rolls-Royce", Arrays.asList(
                "Ghost", "Wraith", "Dawn", "Phantom", "Cullinan"
        ));

        
        this.checkAndAddListing("BMW", "320d", "WBA320dVIN021", 2023, 48000.0, "0x1e90ff",
            "Diesel", 15000, 190, 400, "B47", "/media/320d-silver.jpg",
            "Low mileage with premium interior", "Munich, Germany", "New",
            "Leather Seats, Navigation, Heated Seats, Sunroof");
        this.checkAndAddListing("BMW", "330i", "WBA330eVIN022", 2022, 56000.0, "0xffd700",
            "Hybrid", 12000, 288, 420, "B48", "/media/330e-gold.jpg",
            "Plug-in hybrid, M-Sport trim", "Stuttgart, Germany", "New",
            "Adaptive Cruise Control, Parking Assist, LED Headlights");
        this.checkAndAddListing("BMW", "435i", "WBA435iVIN023", 2021, 54000.0, "0xff6347",
            "Gasoline", 20000, 306, 400, "N55", "/media/435i-red.jpg",
            "Sport Line, premium sound system", "Berlin, Germany", "Used",
            "Heated Seats, Harman Kardon Audio, Sport Package");
        this.checkAndAddListing("BMW", "M4", "WBSM4VIN024", 2023, 95000.0, "0x990000",
            "Gasoline", 5000, 510, 650, "S58", "/media/m4-competition-red.jpg",
            "M Competition, carbon fiber roof", "Hamburg, Germany", "New",
            "Carbon Fiber Package, Navigation, HUD, M Performance Exhaust, Navigation, Heated Seats");
        this.checkAndAddListing("BMW", "530xd", "WBA530dVIN025", 2022, 63000.0, "0x333333",
            "Diesel", 11000, 286, 650, "B57", "/media/530d-xdrive.jpg",
            "xDrive AWD, luxury line package", "Frankfurt, Germany", "New",
            "Panoramic Roof, Adaptive Suspension, Heated Steering Wheel");
        this.checkAndAddListing("BMW", "540i", "WBA540iVIN026", 2023, 71000.0, "0x228b22",
            "Gasoline", 8000, 382, 500, "B58", "/media/540i-green.jpg",
            "Executive package with leather interior", "Düsseldorf, Germany", "New",
            "Gesture Control, Digital Cockpit, Harman Kardon Audio");
        this.checkAndAddListing("BMW", "750d", "WBA750dVIN027", 2023, 97000.0, "0x000000",
            "Diesel", 5000, 400, 850, "B57", "/media/750d-black.jpg",
            "Luxury edition with adaptive cruise control", "Cologne, Germany", "New",
            "Luxury Seats, Premium Sound System, Rear Seat Entertainment");
        this.checkAndAddListing("BMW", "iX", "WBAiXXDriveVIN028", 2023, 110000.0, "0xffa500",
            "Electric", 2000, 523, 0, "Electric", "/media/ix-orange.jpg",
            "Top-tier electric SUV with dual-motor AWD", "Munich, Germany", "New",
            "Heated Steering Wheel, Wireless Charging, Adaptive Headlights");
        this.checkAndAddListing("BMW", "M5", "WBSM5VIN029", 2023, 130000.0, "0xff4500",
            "Gasoline", 4000, 627, 750, "S63", "/media/m5-competition-orange.jpg",
            "Limited edition M5 with carbon-ceramic brakes", "Berlin, Germany", "New",
            "Carbon Roof, Adaptive Cruise Control, HUD, M Sport Differential");
        this.checkAndAddListing("BMW", "X5", "WBAX5VIN030", 2022, 95000.0, "0x4682b4",
            "Diesel", 10000, 400, 760, "B57", "/media/x5-m50d-blue.jpg",
            "Premium SUV with M Performance trim", "Stuttgart, Germany", "New",
            "360 Camera, Sport Exhaust, Adaptive Chassis");
        this.checkAndAddListing("BMW", "i4", "WBAi4eDriveVIN031", 2023, 70000.0, "0x87cefa",
            "Electric", 3000, 340, 0, "Electric", "/media/i4-blue.jpg",
            "Gran Coupe EV with adaptive damping", "Frankfurt, Germany", "New",
            "Comfort Access, Laser Headlights, Apple CarPlay");
        this.checkAndAddListing("BMW", "X7", "WBAX7VIN032", 2023, 120000.0, "0x696969",
            "Gasoline", 4000, 530, 750, "N63", "/media/x7-grey.jpg",
            "Luxury SUV with third-row seating", "Munich, Germany", "New",
            "Massage Seats, Panoramic Roof, Ambient Lighting");
        this.checkAndAddListing("BMW", "218i", "WBA218iVIN033", 2021, 32000.0, "0x00ff00",
            "Gasoline", 25000, 136, 220, "B38", "/media/218i-green.jpg",
            "Compact coupe with sporty dynamics", "Berlin, Germany", "Used",
            "Parking Assist, Cruise Control, Sport Seats");
        this.checkAndAddListing("BMW", "M2", "WBSM2VIN034", 2023, 85000.0, "0xff0000",
            "Gasoline", 6000, 450, 550, "S55", "/media/m2-cs-red.jpg",
            "Limited edition M2 Coupe", "Leipzig, Germany", "New",
            "Carbon Package, Sport Exhaust, HUD");
        this.checkAndAddListing("BMW", "330d", "WBA330dVIN035", 2023, 62000.0, "0xadd8e6",
            "Diesel", 7000, 265, 580, "B57", "/media/330d-lightblue.jpg",
            "Luxury trim with sport handling", "Hamburg, Germany", "New",
            "Heated Seats, Adaptive M Suspension, Digital Key");

    }
    public void checkAndAddListing(
            String make, String model, String vin, int year, double price, String color,
            String fuelType, int mileage, int power, int torque, String engine, String imagePath,
            String description, String location, String condition, String optionsString
    ) {

        Vehicle existingVehicleOpt = vehicleService.getVehicleByVin(vin);


        if (existingVehicleOpt == null) {
            this.addNewListingWithImage(
                    make, model, vin, year, price, color,
                    fuelType, mileage, power, torque, engine, imagePath,
                    description, location, condition, optionsString
            );
        } else {
            System.out.println("Vehicle with VIN " + vin + " already exists. Skipping creation.");
        }
    }
    public void addNewListingWithImage(
            String make, String model, String vin, int year, double price, String color,
            String fuelType, int mileage, int power, int torque, String engine, String imagePath,
            String description, String location, String condition, String optionsString) {

        List<String> options = Arrays.asList(optionsString.split(",\\s*"));

        System.out.println("Options i got from the input: " + options);


        Set<Option> optionList = optionService.getOptionList(options);

        Vehicle vehicle = new Vehicle();
        vehicle.setMake(make);
        vehicle.setModel(model);
        vehicle.setVin(vin);
        vehicle.setYear(year);
        vehicle.setPrice(price);
        vehicle.setColor(color);
        vehicle.setFuelType(fuelType);
        vehicle.setMileage(mileage);
        vehicle.setPower(power);
        vehicle.setTorque(torque);
        vehicle.setEngine(engine);
        vehicle.setOptions(optionList);
        vehicle.setOwner(userService.getUserByEmail("contact@bmw.com"));


        try {
            Path imageFilePath = Paths.get(getClass().getResource(imagePath).toURI());
            byte[] imageBytes = Files.readAllBytes(imageFilePath);
            vehicle.setImage(imageBytes);
        } catch (Exception e) {
            System.err.println("Could not load image for vehicle " + vin + ": " + e.getMessage());
            vehicle.setImage(null);
        }
        vehicleService.saveVehicle(vehicle);

        Marketplace listing = new Marketplace();
        listing.setDescription(description);
        listing.setLocation(location);
        listing.setCondition(condition);
        listing.setVehicle(vehicle);
        listing.setUser(userService.getUserByEmail("contact@bmw.com"));

        marketplaceService.saveMarketplace(listing);

        System.out.println("New listing added: " + make + " " + model + " (" + vin + ")");
    }
    private void createMakeAndModelsIfNotPresent(String makeName, List<String> models) {

        Make existingMake = makeService.getMakeByName(makeName);
        if (existingMake != null) {
            System.out.println(makeName + " already exists in the database. Skipping creation.");
            return;
        }


        Make make = new Make(makeName);
        makeService.saveMake(make);


        for (String modelName : models) {

            Integer basePrice = MODEL_REFERENCE_PRICES.get(modelName);
            if (basePrice == null) {
                basePrice = 50000;
                System.out.println("Warning: '" + modelName + "' not found in MODEL_REFERENCE_PRICES map.");
            }

            Model model = new Model(modelName, basePrice);
            model.setMake(make);


            modelService.saveModel(model);
        }
        System.out.println("Created " + makeName + " with " + models.size() + " models.");
    }
}
