package com.example.bmw_dalership_spring;

import com.example.bmw_dalership_spring.model.Option;
import com.example.bmw_dalership_spring.repository.OptionRepository;
import com.example.bmw_dalership_spring.service.OptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OptionServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @InjectMocks
    private OptionService optionService;

    @BeforeEach
    void setup() {
        // Any additional setup before each test goes here
    }

    @Test
    void testGetOptionList_withValidOptions() {
        // Arrange
        List<String> optionStrings = List.of("Sunroof", "Leather Seats");

        // Create mock Option objects
        Option option1 = new Option();
        option1.setName("Sunroof");
        Option option2 = new Option();
        option2.setName("Leather Seats");

        // Mock repository calls
        when(optionRepository.findByName("Sunroof")).thenReturn(option1);
        when(optionRepository.findByName("Leather Seats")).thenReturn(option2);

        // Act
        Set<Option> result = optionService.getOptionList(optionStrings);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "Should return exactly 2 options");
        assertTrue(result.contains(option1), "Should include 'Sunroof' option");
        assertTrue(result.contains(option2), "Should include 'Leather Seats' option");
    }

    @Test
    void testGetOptionList_withInvalidOptions() {
        // Arrange
        List<String> optionStrings = List.of("FictitiousOption1", "FictitiousOption2");

        // Mock repository calls to return null, since these options don't exist
        when(optionRepository.findByName("FictitiousOption1")).thenReturn(null);
        when(optionRepository.findByName("FictitiousOption2")).thenReturn(null);

        // Act
        Set<Option> result = optionService.getOptionList(optionStrings);

        // Assert
        assertNotNull(result, "Result should not be null even if options don't exist");
        assertTrue(result.isEmpty(), "No valid options found, so the set should be empty");
    }

    @Test
    void testGetOptionList_withMixedExistingAndNonExisting() {
        // Arrange
        List<String> optionStrings = List.of("Sunroof", "NonExistentOption", "Leather Seats");

        Option optionSunroof = new Option();
        optionSunroof.setName("Sunroof");

        Option optionLeather = new Option();
        optionLeather.setName("Leather Seats");

        // Mock some found, some not
        when(optionRepository.findByName("Sunroof")).thenReturn(optionSunroof);
        when(optionRepository.findByName("NonExistentOption")).thenReturn(null);
        when(optionRepository.findByName("Leather Seats")).thenReturn(optionLeather);

        // Act
        Set<Option> result = optionService.getOptionList(optionStrings);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size(), "We only expect 2 valid options in the returned set");
        assertTrue(result.contains(optionSunroof));
        assertTrue(result.contains(optionLeather));
    }

    @Test
    void testGetOptionList_withEmptyList() {
        // Arrange
        List<String> optionStrings = List.of(); // no options

        // Act
        Set<Option> result = optionService.getOptionList(optionStrings);

        // Assert
        assertNotNull(result, "Should return a non-null empty set");
        assertTrue(result.isEmpty(), "Result should be empty when no option strings are provided");
    }
}
