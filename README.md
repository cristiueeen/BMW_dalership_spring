# BMW Dealership Spring + JavaFX Project

A JavaFX + Spring Boot application for managing a virtual BMW dealership.  
Features include listing, filtering, and selling vehicles, plus price estimation and user management.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Running the Application](#running-the-application)
- [Tests](#tests)
- [Usage](#usage)

---

## Overview

This application provides a platform where users can:
1. Manage vehicles (CRUD operations on Make, Model, Options, etc.).
2. List vehicles in a **Marketplace** where potential buyers can view details (pricing, mileage, fuel type, etc.).
3. Filter vehicles by make, model, year, mileage, price, and more.
4. Estimate vehicle prices using depreciation and mileage models.
5. Validate user logins (basic scenario).
6. Combine **Spring Boot** (backend) and **JavaFX** (UI) for a desktop-like experience.

---

## Features

- **Add Vehicles** with full details (make, model, year, fuel type, etc.).
- **List & Filter Vehicles** in a marketplace.
- **Dynamic UI** with JavaFX (check combo boxes for make/model).
- **Price Estimation** logic factoring age, mileage, and options.
- **User Login** validation (sample logic).
- **Simple Integration and Unit Tests** for services and data access.

---

## Technologies Used

- **Java 17**
- **Spring Boot 3**
- **JavaFX 17**
- **Maven** 
- **Hibernate / JPA**
- **ControlsFX**
- **PostgreSQL** database (configurable in `application.properties`)

- The aplication's back-end is built using SpringBoot, while the front-end is build with JavaFX.
- The two technologies are linked togather in memory, without the use of RESTapi

---

## Project Structure
- **`controller/`** – JavaFX controllers.
- **`service/`** – Business logic (e.g., `VehicleService`, `MarketplaceService`).
- **`model/`** – JPA entities (`Vehicle`, `Marketplace`, `Make`, `Model`, etc.).
- **`repository/`** – Spring Data JPA repositories.
- **`fxml/`** – FXML files for JavaFX UI layouts.

---

## Getting Started

### Prerequisites

- YOU NEED TO SETUP A POSTGRESQL DATABASE IN THE APPLICATION PROPERTIES!.
- create a new postres database or connect to a test one you have created already by pasting the link to it, and login credentials
 - Configure your database in `src/main/resources/application.properties`
### Installation

**Clone** this repository:
   ```bash
   git clone https://github.com/yourusername/BMW_dalership_spring.git
   ```
## Running the Application
- Open the project in your IDE.
- Locate the main application class `BMWDalershipSpringApplication.java`).
- Run it.
- The JavaFX UI should launch, with Spring Boot logs in the console.
## Usage
- Launch the JavaFX application.
- `Login page`: here you can log in into your account, reset your password by the password reset token (`admin` for Supervisor), or register a new account
  - The main app will open, if authentification is succesfull. Here you can choose from the tabs between `Marketplace`, `Service Appointments`, `Vehicle` and `Dashboard`
### Marketplace
- In the Marketplace tab, you can:
- View a list of vehicles available for sale.
- Filter them by make, model, year, mileage, price, etc.
- Add new listings via the “Sell Vehicle” button or a separate tab.
- Select any listing to see details (price, owner info, image, etc.).
### Adding a Vehicle
- You can select a vehicle from your garage that hasn't been listed yet, the app filling the fields automatically
- Add a new vehicle by filling the form, this vehicle will also be added to your garage.
- You will get a dynamic price estimation after all the necessay fileds have been filled
### Vehicle garage
- You can see your vehicles in a table, along with options as well
- You can add a new one by filling the form
### Service Appointments
- You can schedule a service appointment for your vehicles.
- Only available dates will be shown
- You can select multiple faulty parts to be replaced, and get a total price
### Dashboard
- Here you can log out, see and delete your listings, as well as upcoming  service appointments.
---

## Tests
- specific SpringBoot tests can be found and run in `src/test/java/com/example/bmw_dalership_spring`
- `WARNING` Some tests may appear ignored because the way SpringBoot integrates with the JavaFX UI, if you want to see the particular ignored test working, remove @SpringBootAplication adnotation from `com/example/bmw_dalership_spring/JavaFxApplication.java`, note that if you want to run the app again, you will need to insert it back.
