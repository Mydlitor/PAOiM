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
}
