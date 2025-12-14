package facade;

import org.junit.jupiter.api.*;
import exceptions.StableException;
import util.TestDatabaseUtil;
import static org.junit.jupiter.api.Assertions.*;

class DataGeneratorTest {
    private DataGenerator generator;
    private StableFacade facade;
    
    @BeforeEach
    void setUp() {
        TestDatabaseUtil.clearDatabase();
        generator = DataGenerator.getInstance();
        facade = new StableFacade();
    }
    
    @Test
    @DisplayName("Test singleton instance")
    void testSingletonInstance() {
        DataGenerator instance1 = DataGenerator.getInstance();
        DataGenerator instance2 = DataGenerator.getInstance();
        assertSame(instance1, instance2);
    }
    
    @Test
    @DisplayName("Test sample data generation")
    void testGenerateSampleData() throws StableException {
        generator.generateSampleData(facade);
        
        // Check that stables were created
        var stables = facade.getAllStables();
        assertTrue(stables.size() >= 4);
        
        // Check that horses were added
        var horses = facade.getHorsesInStable("North Farm");
        assertTrue(horses.size() > 0);
    }
    
    @Test
    @DisplayName("Test repeated data generation does not throw exception")
    void testRepeatedGenerateSampleData() throws StableException {
        // Generate sample data first time
        generator.generateSampleData(facade);
        
        // Check that stables were created
        var stables = facade.getAllStables();
        int initialStableCount = stables.size();
        assertTrue(initialStableCount >= 4);
        
        // Generate sample data again - should not throw exception
        assertDoesNotThrow(() -> generator.generateSampleData(facade));
        
        // Verify data wasn't duplicated
        var stablesAfter = facade.getAllStables();
        assertEquals(initialStableCount, stablesAfter.size());
    }
    
    @Test
    @DisplayName("Test data generation with other stables present")
    void testGenerateDataWithOtherStables() throws StableException {
        // Add a different stable first
        facade.addStable("My Custom Stable", 7);
        
        // Generate sample data - should still work since "North Farm" doesn't exist
        assertDoesNotThrow(() -> generator.generateSampleData(facade));
        
        // Verify both the custom stable and sample data exist
        assertDoesNotThrow(() -> facade.getStable("My Custom Stable"));
        assertDoesNotThrow(() -> facade.getStable("North Farm"));
        
        var stables = facade.getAllStables();
        assertTrue(stables.size() >= 5); // 1 custom + 4 sample stables
    }
}
