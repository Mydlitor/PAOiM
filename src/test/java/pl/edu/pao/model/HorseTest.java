package pl.edu.pao.model;

import org.junit.jupiter.api.*;
import pl.edu.pao.exceptions.*;
import static org.junit.jupiter.api.Assertions.*;

class HorseTest {
    private Horse horse1;
    private Horse horse2;
    
    @BeforeEach
    void setUp() {
        horse1 = new Horse("Bella", "Arabian", HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 6, 15000.0, 450.0);
        horse2 = new Horse("Argo", "Shire", HorseType.COLD_BLOODED, HorseCondition.TRAINING, 8, 8000.0, 700.0);
    }
    
    @Test
    @DisplayName("Test horse creation with valid data")
    void testHorseCreation() {
        assertNotNull(horse1);
        assertEquals("Bella", horse1.getName());
        assertEquals("Arabian", horse1.getBreed());
        assertEquals(HorseType.HOT_BLOODED, horse1.getType());
        assertEquals(HorseCondition.HEALTHY, horse1.getCondition());
        assertEquals(6, horse1.getAge());
        assertEquals(15000.0, horse1.getPrice());
        assertEquals(450.0, horse1.getWeightKg());
    }
    
    @Test
    @DisplayName("Test horse creation with null name throws exception")
    void testHorseCreationWithNullName() {
        assertThrows(NullPointerException.class, () -> 
            new Horse(null, "Arabian", HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 6, 15000.0, 450.0));
    }
    
    @Test
    @DisplayName("Test horse setters")
    void testHorseSetters() {
        horse1.setCondition(HorseCondition.TRAINING);
        assertEquals(HorseCondition.TRAINING, horse1.getCondition());
        
        horse1.setWeightKg(460.0);
        assertEquals(460.0, horse1.getWeightKg());
        
        horse1.setAge(7);
        assertEquals(7, horse1.getAge());
        
        horse1.setPrice(16000.0);
        assertEquals(16000.0, horse1.getPrice());
    }
    
    @Test
    @DisplayName("Test horse compareTo method")
    void testHorseCompareTo() {
        assertTrue(horse2.compareTo(horse1) < 0); // Argo < Bella
        assertTrue(horse1.compareTo(horse2) > 0); // Bella > Argo
        assertEquals(0, horse1.compareTo(horse1));
    }
    
    @Test
    @DisplayName("Test horse equals and hashCode")
    void testHorseEqualsAndHashCode() {
        Horse horse1Copy = new Horse("Bella", "Arabian", HorseType.HOT_BLOODED, HorseCondition.TRAINING, 6, 20000.0, 500.0);
        assertEquals(horse1, horse1Copy);
        assertEquals(horse1.hashCode(), horse1Copy.hashCode());
        assertNotEquals(horse1, horse2);
    }
    
    @Test
    @DisplayName("Test horse comparators")
    void testHorseComparators() {
        assertEquals(0, Horse.BY_NAME.compare(horse1, horse1));
        assertTrue(Horse.BY_PRICE.compare(horse1, horse2) > 0); // Bella more expensive
        assertTrue(Horse.BY_AGE.compare(horse1, horse2) < 0); // Bella younger
    }
    
    @Test
    @DisplayName("Test horse toString")
    void testHorseToString() {
        String str = horse1.toString();
        assertTrue(str.contains("Bella"));
        assertTrue(str.contains("Arabian"));
        assertTrue(str.contains("hot-blooded"));
    }
}
