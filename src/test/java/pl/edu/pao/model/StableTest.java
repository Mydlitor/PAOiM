package pl.edu.pao.model;

import org.junit.jupiter.api.*;
import pl.edu.pao.exceptions.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class StableTest {
    private Stable stable;
    private Horse horse1, horse2, horse3;
    
    @BeforeEach
    void setUp() {
        stable = new Stable("Test Stable", 5);
        horse1 = new Horse("Bella", "Arabian", HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 6, 15000.0, 450.0);
        horse2 = new Horse("Argo", "Shire", HorseType.COLD_BLOODED, HorseCondition.TRAINING, 8, 8000.0, 700.0);
        horse3 = new Horse("Coco", "Thoroughbred", HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 4, 22000.0, 380.0);
    }
    
    @Test
    @DisplayName("Test stable creation")
    void testStableCreation() {
        assertEquals("Test Stable", stable.getStableName());
        assertEquals(5, stable.getMaxCapacity());
        assertTrue(stable.getHorseList().isEmpty());
    }
    
    @Test
    @DisplayName("Test adding horse to stable")
    void testAddHorse() throws StableException {
        stable.addHorse(horse1);
        assertEquals(1, stable.getHorseList().size());
        assertTrue(stable.getHorseList().contains(horse1));
    }
    
    @Test
    @DisplayName("Test adding duplicate horse throws exception")
    void testAddDuplicateHorse() throws StableException {
        stable.addHorse(horse1);
        assertThrows(DuplicateHorseException.class, () -> stable.addHorse(horse1));
    }
    
    @Test
    @DisplayName("Test adding horse to full stable throws exception")
    void testAddHorseToFullStable() throws StableException {
        Stable smallStable = new Stable("Small", 2);
        smallStable.addHorse(horse1);
        smallStable.addHorse(horse2);
        assertThrows(StableCapacityException.class, () -> smallStable.addHorse(horse3));
    }
    
    @Test
    @DisplayName("Test removing horse from stable")
    void testRemoveHorse() throws StableException {
        stable.addHorse(horse1);
        stable.removeHorse(horse1);
        assertEquals(0, stable.getHorseList().size());
    }
    
    @Test
    @DisplayName("Test removing non-existent horse throws exception")
    void testRemoveNonExistentHorse() {
        assertThrows(HorseNotFoundException.class, () -> stable.removeHorse(horse1));
    }
    
    @Test
    @DisplayName("Test sick horse functionality")
    void testSickHorse() throws StableException {
        stable.addHorse(horse1);
        stable.sickHorse(horse1);
        assertEquals(0, stable.getHorseList().size());
    }
    
    @Test
    @DisplayName("Test change horse condition")
    void testChangeCondition() throws StableException {
        stable.addHorse(horse1);
        stable.changeCondition(horse1, HorseCondition.QUARANTINE);
        assertEquals(HorseCondition.QUARANTINE, horse1.getCondition());
    }
    
    @Test
    @DisplayName("Test change horse weight")
    void testChangeWeight() throws StableException {
        stable.addHorse(horse1);
        stable.changeWeight(horse1, 470.0);
        assertEquals(470.0, horse1.getWeightKg());
    }
    
    @Test
    @DisplayName("Test count by status")
    void testCountByStatus() throws StableException {
        stable.addHorse(horse1);
        stable.addHorse(horse2);
        stable.addHorse(horse3);
        assertEquals(2, stable.countByStatus(HorseCondition.HEALTHY));
        assertEquals(1, stable.countByStatus(HorseCondition.TRAINING));
    }
    
    @Test
    @DisplayName("Test sort by name")
    void testSortByName() throws StableException {
        stable.addHorse(horse3); // Coco
        stable.addHorse(horse1); // Bella
        stable.addHorse(horse2); // Argo
        List<Horse> sorted = stable.sortByName();
        assertEquals("Argo", sorted.get(0).getName());
        assertEquals("Bella", sorted.get(1).getName());
        assertEquals("Coco", sorted.get(2).getName());
    }
    
    @Test
    @DisplayName("Test sort by price")
    void testSortByPrice() throws StableException {
        stable.addHorse(horse1); // 15000
        stable.addHorse(horse2); // 8000
        stable.addHorse(horse3); // 22000
        List<Horse> sorted = stable.sortByPrice();
        assertEquals(horse2, sorted.get(0)); // Cheapest
        assertEquals(horse3, sorted.get(2)); // Most expensive
    }
    
    @Test
    @DisplayName("Test search horse by name")
    void testSearch() throws StableException {
        stable.addHorse(horse1);
        stable.addHorse(horse2);
        Horse found = stable.search("Bella");
        assertEquals(horse1, found);
        assertNull(stable.search("NonExistent"));
    }
    
    @Test
    @DisplayName("Test partial search")
    void testSearchPartial() throws StableException {
        stable.addHorse(horse1);
        stable.addHorse(horse2);
        List<Horse> results = stable.searchPartial("bel");
        assertEquals(1, results.size());
        assertEquals(horse1, results.get(0));
    }
    
    @Test
    @DisplayName("Test get quarantined horses")
    void testGetQuarantinedHorses() throws StableException {
        stable.addHorse(horse1);
        stable.changeCondition(horse1, HorseCondition.QUARANTINE);
        stable.addHorse(horse2);
        List<Horse> quarantined = stable.getQuarantinedHorses();
        assertEquals(1, quarantined.size());
        assertEquals(horse1, quarantined.get(0));
    }
    
    @Test
    @DisplayName("Test max price horse")
    void testMax() throws StableException {
        stable.addHorse(horse1);
        stable.addHorse(horse2);
        stable.addHorse(horse3);
        Horse max = stable.max();
        assertEquals(horse3, max);
    }
    
    @Test
    @DisplayName("Test occupancy percent")
    void testOccupancyPercent() throws StableException {
        stable.addHorse(horse1);
        stable.addHorse(horse2);
        assertEquals(40.0, stable.occupancyPercent(), 0.01);
    }
}
