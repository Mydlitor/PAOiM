package service;

import dao.HorseDAO;
import dao.HorseDAOImpl;
import dao.RatingDAO;
import dao.RatingDAOImpl;
import dao.StableDAO;
import dao.StableDAOImpl;
import model.*;
import org.junit.jupiter.api.*;
import util.TestDatabaseUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ServiceIntegrationTest {
    private SerializationService serializationService;
    private CSVService csvService;
    private RatingStatisticsService ratingStatsService;
    private StableDAO stableDAO;
    private HorseDAO horseDAO;
    private RatingDAO ratingDAO;
    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        TestDatabaseUtil.clearDatabase();
        serializationService = new SerializationService();
        csvService = new CSVService();
        ratingStatsService = new RatingStatisticsService();
        stableDAO = new StableDAOImpl();
        horseDAO = new HorseDAOImpl();
        ratingDAO = new RatingDAOImpl();
        // Create a temporary directory for test files
        tempDir = Files.createTempDirectory("stable-manager-test");
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up temporary directory and its contents
        if (tempDir != null && Files.exists(tempDir)) {
            Files.walk(tempDir)
                .sorted(java.util.Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        // Ignore cleanup errors
                    }
                });
        }
    }

    @Test
    @DisplayName("Test serialization and deserialization of Stable")
    void testStableSerialization() throws IOException, ClassNotFoundException {
        String filename = tempDir.resolve("test_stable.ser").toString();
        
        Stable stable = new Stable("Test Stable", 10);
        Horse horse = new Horse("Test Horse", "Arabian", HorseType.HOT_BLOODED, 
                                HorseCondition.HEALTHY, 5, 10000.0, 400.0);
        try {
            stable.addHorse(horse);
        } catch (Exception e) {
            fail("Failed to add horse: " + e.getMessage());
        }
        
        serializationService.saveStable(stable, filename);
        Stable loaded = serializationService.loadStable(filename);
        
        assertNotNull(loaded);
        assertEquals("Test Stable", loaded.getStableName());
        assertEquals(10, loaded.getMaxCapacity());
        assertEquals(1, loaded.getHorseList().size());
    }

    @Test
    @DisplayName("Test serialization and deserialization of Horse")
    void testHorseSerialization() throws IOException, ClassNotFoundException {
        String filename = tempDir.resolve("test_horse.ser").toString();
        
        Horse horse = new Horse("Test Horse", "Arabian", HorseType.HOT_BLOODED, 
                                HorseCondition.HEALTHY, 5, 10000.0, 400.0);
        
        serializationService.saveHorse(horse, filename);
        Horse loaded = serializationService.loadHorse(filename);
        
        assertNotNull(loaded);
        assertEquals("Test Horse", loaded.getName());
        assertEquals("Arabian", loaded.getBreed());
        assertEquals(HorseType.HOT_BLOODED, loaded.getType());
    }

    @Test
    @DisplayName("Test CSV export for stables")
    void testStableCSVExport() throws Exception {
        String filename = tempDir.resolve("test_stables.csv").toString();
        
        // Create test data
        Stable stable1 = new Stable("Stable1", 10);
        Stable stable2 = new Stable("Stable2", 5);
        stableDAO.save(stable1);
        stableDAO.save(stable2);
        
        // Export to CSV
        csvService.exportStablesToCSV(filename);
        
        // Verify file exists
        File csvFile = new File(filename);
        assertTrue(csvFile.exists());
        assertTrue(csvFile.length() > 0);
    }

    @Test
    @DisplayName("Test CSV export for horses")
    void testHorseCSVExport() throws Exception {
        String filename = tempDir.resolve("test_horses.csv").toString();
        
        // Create test data
        Stable stable = new Stable("Test Stable", 10);
        stableDAO.save(stable);
        
        Horse horse1 = new Horse("Horse1", "Arabian", HorseType.HOT_BLOODED, 
                                 HorseCondition.HEALTHY, 5, 10000.0, 400.0);
        horse1.setStable(stable);
        horseDAO.save(horse1);
        
        // Export to CSV
        csvService.exportHorsesToCSV(filename);
        
        // Verify file exists
        File csvFile = new File(filename);
        assertTrue(csvFile.exists());
        assertTrue(csvFile.length() > 0);
    }

    @Test
    @DisplayName("Test rating statistics with Criteria API")
    void testRatingStatistics() {
        // Create test data
        Stable stable = new Stable("Test Stable", 10);
        stableDAO.save(stable);
        
        Horse horse1 = new Horse("Horse1", "Arabian", HorseType.HOT_BLOODED, 
                                 HorseCondition.HEALTHY, 5, 10000.0, 400.0);
        Horse horse2 = new Horse("Horse2", "Shire", HorseType.COLD_BLOODED, 
                                 HorseCondition.HEALTHY, 8, 8000.0, 700.0);
        horse1.setStable(stable);
        horse2.setStable(stable);
        horseDAO.save(horse1);
        horseDAO.save(horse2);
        
        // Add ratings
        Rating r1 = new Rating(5, horse1, new Date(), "Excellent");
        Rating r2 = new Rating(4, horse1, new Date(), "Very good");
        Rating r3 = new Rating(3, horse2, new Date(), "Good");
        
        ratingDAO.save(r1);
        ratingDAO.save(r2);
        ratingDAO.save(r3);
        
        // Get statistics
        Map<String, Object> stats = ratingStatsService.getHorseRatingStatistics();
        
        assertNotNull(stats);
        assertEquals(2, stats.size());
        assertTrue(stats.containsKey("Horse1"));
        assertTrue(stats.containsKey("Horse2"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> horse1Stats = (Map<String, Object>) stats.get("Horse1");
        assertEquals(4.5, (Double) horse1Stats.get("average"), 0.01);
        assertEquals(2L, horse1Stats.get("count"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> horse2Stats = (Map<String, Object>) stats.get("Horse2");
        assertEquals(3.0, (Double) horse2Stats.get("average"), 0.01);
        assertEquals(1L, horse2Stats.get("count"));
    }
}
