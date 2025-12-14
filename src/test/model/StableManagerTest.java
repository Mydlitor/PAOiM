package model;

import org.junit.jupiter.api.*;
import exceptions.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class StableManagerTest {
    private StableManager manager;
    
    @BeforeEach
    void setUp() {
        manager = new StableManager();
    }
    
    @Test
    @DisplayName("Test adding stable")
    void testAddStable() throws StableException {
        manager.addStable("North Farm", 10);
        Stable stable = manager.getStable("North Farm");
        assertNotNull(stable);
        assertEquals("North Farm", stable.getStableName());
        assertEquals(10, stable.getMaxCapacity());
    }
    
    @Test
    @DisplayName("Test adding duplicate stable throws exception")
    void testAddDuplicateStable() throws StableException {
        manager.addStable("North Farm", 10);
        assertThrows(DuplicateStableException.class, () -> manager.addStable("North Farm", 5));
    }
    
    @Test
    @DisplayName("Test adding stable with invalid capacity throws exception")
    void testAddStableInvalidCapacity() {
        assertThrows(InvalidDataException.class, () -> manager.addStable("Test", 0));
        assertThrows(InvalidDataException.class, () -> manager.addStable("Test", -5));
    }
    
    @Test
    @DisplayName("Test removing stable")
    void testRemoveStable() throws StableException {
        manager.addStable("North Farm", 10);
        manager.removeStable("North Farm");
        assertThrows(StableNotFoundException.class, () -> manager.getStable("North Farm"));
    }
    
    @Test
    @DisplayName("Test removing non-existent stable throws exception")
    void testRemoveNonExistentStable() {
        assertThrows(StableNotFoundException.class, () -> manager.removeStable("NonExistent"));
    }
    
    @Test
    @DisplayName("Test get stable throws exception when not found")
    void testGetNonExistentStable() {
        assertThrows(StableNotFoundException.class, () -> manager.getStable("NonExistent"));
    }
    
    @Test
    @DisplayName("Test find empty stables")
    void testFindEmpty() throws StableException {
        manager.addStable("Empty1", 5);
        manager.addStable("Empty2", 3);
        manager.addStable("Full", 2);
        
        Stable fullStable = manager.getStable("Full");
        Horse horse = new Horse("Test", "Arabian", HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 5, 10000.0, 400.0);
        fullStable.addHorse(horse);
        
        List<Stable> empty = manager.findEmpty();
        assertEquals(2, empty.size());
    }
    
    @Test
    @DisplayName("Test get all stables")
    void testGetAllStables() throws StableException {
        manager.addStable("North", 10);
        manager.addStable("South", 5);
        manager.addStable("East", 8);
        
        List<Stable> all = manager.getAllStables();
        assertEquals(3, all.size());
    }
    
    @Test
    @DisplayName("Test sort stables by load")
    void testSortStablesByLoad() throws StableException {
        manager.addStable("Empty", 10);
        manager.addStable("Half", 4);
        manager.addStable("Full", 2);
        
        Stable half = manager.getStable("Half");
        Stable full = manager.getStable("Full");
        
        Horse h1 = new Horse("H1", "Arabian", HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 5, 10000.0, 400.0);
        Horse h2 = new Horse("H2", "Arabian", HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 5, 10000.0, 400.0);
        Horse h3 = new Horse("H3", "Arabian", HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 5, 10000.0, 400.0);
        
        half.addHorse(h1);
        half.addHorse(h2); // 50% load
        full.addHorse(h3); // 50% load
        
        List<Stable> sorted = manager.sortStablesByLoad();
        assertEquals(3, sorted.size());
        // First two should have higher load than empty
        assertTrue(sorted.get(0).occupancyPercent() >= sorted.get(2).occupancyPercent());
    }
}
