package facade;

import org.junit.jupiter.api.*;
import exceptions.*;
import model.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class StableFacadeTest {
    private StableFacade facade;
    
    @BeforeEach
    void setUp() {
        facade = new StableFacade();
    }
    
    @Test
    @DisplayName("Test adding stable through facade")
    void testAddStable() throws StableException {
        facade.addStable("North Farm", 10);
        List<Stable> stables = facade.getAllStables();
        assertEquals(1, stables.size());
        assertEquals("North Farm", stables.get(0).getStableName());
    }
    
    @Test
    @DisplayName("Test adding horse through facade")
    void testAddHorse() throws StableException {
        facade.addStable("North Farm", 10);
        facade.addHorseToStable("North Farm", "Bella", "Arabian", 
            HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 6, 15000.0, 450.0);
        
        List<Horse> horses = facade.getHorsesInStable("North Farm");
        assertEquals(1, horses.size());
        assertEquals("Bella", horses.get(0).getName());
    }
    
    @Test
    @DisplayName("Test removing horse through facade")
    void testRemoveHorse() throws StableException {
        facade.addStable("North Farm", 10);
        facade.addHorseToStable("North Farm", "Bella", "Arabian", 
            HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 6, 15000.0, 450.0);
        
        facade.removeHorseFromStable("North Farm", "Bella");
        List<Horse> horses = facade.getHorsesInStable("North Farm");
        assertEquals(0, horses.size());
    }
    
    @Test
    @DisplayName("Test search horses by name")
    void testSearchHorses() throws StableException {
        facade.addStable("North Farm", 10);
        facade.addHorseToStable("North Farm", "Bella", "Arabian", 
            HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 6, 15000.0, 450.0);
        facade.addHorseToStable("North Farm", "Argo", "Shire", 
            HorseType.COLD_BLOODED, HorseCondition.TRAINING, 8, 8000.0, 700.0);
        
        List<Horse> results = facade.searchHorsesByName("North Farm", "bel");
        assertEquals(1, results.size());
        assertEquals("Bella", results.get(0).getName());
    }
    
    @Test
    @DisplayName("Test filter horses by condition")
    void testFilterHorsesByCondition() throws StableException {
        facade.addStable("North Farm", 10);
        facade.addHorseToStable("North Farm", "Bella", "Arabian", 
            HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 6, 15000.0, 450.0);
        facade.addHorseToStable("North Farm", "Argo", "Shire", 
            HorseType.COLD_BLOODED, HorseCondition.TRAINING, 8, 8000.0, 700.0);
        
        List<Horse> healthy = facade.filterHorsesByCondition("North Farm", HorseCondition.HEALTHY);
        assertEquals(1, healthy.size());
        assertEquals("Bella", healthy.get(0).getName());
    }
    
    @Test
    @DisplayName("Test sort horses by name")
    void testSortHorsesByName() throws StableException {
        facade.addStable("North Farm", 10);
        facade.addHorseToStable("North Farm", "Zorro", "Mustang", 
            HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 5, 5000.0, 420.0);
        facade.addHorseToStable("North Farm", "Bella", "Arabian", 
            HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 6, 15000.0, 450.0);
        
        List<Horse> sorted = facade.sortHorsesByName("North Farm");
        assertEquals("Bella", sorted.get(0).getName());
        assertEquals("Zorro", sorted.get(1).getName());
    }
    
    @Test
    @DisplayName("Test sort horses by price")
    void testSortHorsesByPrice() throws StableException {
        facade.addStable("North Farm", 10);
        facade.addHorseToStable("North Farm", "Zorro", "Mustang", 
            HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 5, 15000.0, 420.0);
        facade.addHorseToStable("North Farm", "Bella", "Arabian", 
            HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 6, 5000.0, 450.0);
        
        List<Horse> sorted = facade.sortHorsesByPrice("North Farm");
        assertEquals("Bella", sorted.get(0).getName());
        assertEquals("Zorro", sorted.get(1).getName());
    }
    
    @Test
    @DisplayName("Test change horse condition")
    void testChangeHorseCondition() throws StableException {
        facade.addStable("North Farm", 10);
        facade.addHorseToStable("North Farm", "Bella", "Arabian", 
            HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 6, 15000.0, 450.0);
        
        facade.changeHorseCondition("North Farm", "Bella", HorseCondition.TRAINING);
        
        List<Horse> horses = facade.getHorsesInStable("North Farm");
        assertEquals(HorseCondition.TRAINING, horses.get(0).getCondition());
    }
    
    @Test
    @DisplayName("Test change horse weight")
    void testChangeHorseWeight() throws StableException {
        facade.addStable("North Farm", 10);
        facade.addHorseToStable("North Farm", "Bella", "Arabian", 
            HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 6, 15000.0, 450.0);
        
        facade.changeHorseWeight("North Farm", "Bella", 470.0);
        
        List<Horse> horses = facade.getHorsesInStable("North Farm");
        assertEquals(470.0, horses.get(0).getWeightKg());
    }
    
    @Test
    @DisplayName("Test sort stables by load")
    void testSortStablesByLoad() throws StableException {
        facade.addStable("Empty", 10);
        facade.addStable("Full", 2);
        
        facade.addHorseToStable("Full", "H1", "Arabian", 
            HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 5, 10000.0, 400.0);
        facade.addHorseToStable("Full", "H2", "Arabian", 
            HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 5, 10000.0, 400.0);
        
        List<Stable> sorted = facade.sortStablesByLoad();
        assertEquals(2, sorted.size());
        assertTrue(sorted.get(0).occupancyPercent() >= sorted.get(1).occupancyPercent());
    }
}
