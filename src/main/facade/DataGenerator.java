package facade;

import exceptions.StableException;
import model.*;

/**
 * Singleton Data Generator for populating test data
 */
public class DataGenerator {
    private static DataGenerator instance;
    
    private DataGenerator() {}
    
    public static DataGenerator getInstance() {
        if (instance == null) {
            instance = new DataGenerator();
        }
        return instance;
    }
    
    public void generateSampleData(StableFacade facade) throws StableException {
        // Check if sample data already exists
        if (!facade.getAllStables().isEmpty()) {
            return; // Sample data already generated
        }
        
        // Add stables
        facade.addStable("North Farm", 10);
        facade.addStable("East Barn", 5);
        facade.addStable("South Stable", 8);
        facade.addStable("West Ranch", 3);
        
        // Add horses to North Farm
        facade.addHorseToStable("North Farm", "Bella", "Arabian", 
            HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 6, 15000.0, 450.0);
        facade.addHorseToStable("North Farm", "Argo", "Shire", 
            HorseType.COLD_BLOODED, HorseCondition.TRAINING, 8, 8000.0, 700.0);
        facade.addHorseToStable("North Farm", "Coco", "Thoroughbred", 
            HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 4, 22000.0, 380.0);
        facade.addHorseToStable("North Farm", "Zorro", "Mustang", 
            HorseType.HOT_BLOODED, HorseCondition.QUARANTINE, 5, 5000.0, 420.0);
        
        // Add horses to East Barn
        facade.addHorseToStable("East Barn", "Tiny", "Pony", 
            HorseType.COLD_BLOODED, HorseCondition.HEALTHY, 3, 1200.0, 200.0);
        facade.addHorseToStable("East Barn", "Thunder", "Friesian", 
            HorseType.COLD_BLOODED, HorseCondition.HEALTHY, 7, 18000.0, 650.0);
        
        // Add horses to South Stable
        facade.addHorseToStable("South Stable", "Luna", "Andalusian", 
            HorseType.HOT_BLOODED, HorseCondition.TRAINING, 5, 12000.0, 480.0);
        facade.addHorseToStable("South Stable", "Spirit", "Appaloosa", 
            HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 6, 9500.0, 500.0);
        facade.addHorseToStable("South Stable", "Shadow", "Quarter Horse", 
            HorseType.HOT_BLOODED, HorseCondition.SICK, 9, 7000.0, 520.0);
        
        // Add horses to West Ranch
        facade.addHorseToStable("West Ranch", "Blaze", "Paint", 
            HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 4, 6500.0, 470.0);
    }
}
