package demo;

import dao.*;
import model.*;
import service.*;
import util.HibernateUtil;

import java.util.Date;
import java.util.List;

/**
 * Demonstration of Lab 04 Hibernate ORM features
 */
public class HibernateDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Lab 04: Hibernate ORM Integration Demo ===\n");
        
        try {
            // Initialize DAOs
            StableDAO stableDAO = new StableDAOImpl();
            HorseDAO horseDAO = new HorseDAOImpl();
            RatingDAO ratingDAO = new RatingDAOImpl();
            
            // Clear database for demo
            clearDatabase();
            
            // 1. Demonstrate DAO Pattern - Create Stables
            System.out.println("1. Creating stables using DAO...");
            Stable northFarm = new Stable("North Farm", 10);
            Stable southStable = new Stable("South Stable", 8);
            stableDAO.save(northFarm);
            stableDAO.save(southStable);
            System.out.println("   ✓ Created 2 stables");
            
            // 2. Demonstrate Bidirectional Relationship - Add Horses
            System.out.println("\n2. Adding horses to stables (bidirectional relationship)...");
            Horse bella = new Horse("Bella", "Arabian", HorseType.HOT_BLOODED, 
                                   HorseCondition.HEALTHY, 6, 15000.0, 450.0);
            Horse argo = new Horse("Argo", "Shire", HorseType.COLD_BLOODED, 
                                  HorseCondition.TRAINING, 8, 8000.0, 700.0);
            Horse coco = new Horse("Coco", "Thoroughbred", HorseType.HOT_BLOODED, 
                                  HorseCondition.HEALTHY, 4, 22000.0, 480.0);
            
            northFarm.addHorse(bella);
            northFarm.addHorse(argo);
            southStable.addHorse(coco);
            
            stableDAO.update(northFarm);
            stableDAO.update(southStable);
            System.out.println("   ✓ Added 3 horses to stables");
            
            // 3. Demonstrate HQL Queries
            System.out.println("\n3. Querying database with HQL...");
            List<Stable> allStables = stableDAO.findAll();
            System.out.println("   Found " + allStables.size() + " stables:");
            for (Stable s : allStables) {
                System.out.println("   - " + s.getStableName() + " (" + 
                                 s.getHorseList().size() + "/" + s.getMaxCapacity() + ")");
            }
            
            // 4. Demonstrate Rating Entity
            System.out.println("\n4. Creating ratings for horses...");
            Rating r1 = new Rating(5, bella, new Date(), "Excellent performer");
            Rating r2 = new Rating(4, bella, new Date(), "Very good temperament");
            Rating r3 = new Rating(5, coco, new Date(), "Outstanding speed");
            Rating r4 = new Rating(3, argo, new Date(), "Good for heavy work");
            
            ratingDAO.save(r1);
            ratingDAO.save(r2);
            ratingDAO.save(r3);
            ratingDAO.save(r4);
            System.out.println("   ✓ Created 4 ratings");
            
            // 5. Demonstrate Criteria API - Rating Statistics
            System.out.println("\n5. Rating statistics using Criteria API (GROUP BY):");
            RatingStatisticsService statsService = new RatingStatisticsService();
            statsService.displayRatingStatistics();
            
            // 6. Demonstrate CSV Export
            System.out.println("6. Exporting data to CSV files...");
            CSVService csvService = new CSVService();
            csvService.exportStablesToCSV("demo_stables.csv");
            csvService.exportHorsesToCSV("demo_horses.csv");
            System.out.println("   ✓ Exported to demo_stables.csv and demo_horses.csv");
            
            // 7. Demonstrate Serialization
            System.out.println("\n7. Testing binary serialization...");
            SerializationService serService = new SerializationService();
            serService.saveStable(northFarm, "demo_stable.ser");
            Stable loaded = serService.loadStable("demo_stable.ser");
            System.out.println("   ✓ Serialized and deserialized: " + loaded.getStableName());
            System.out.println("   ✓ Horses preserved: " + loaded.getHorseList().size());
            
            // 8. Summary
            System.out.println("\n=== Demo Summary ===");
            System.out.println("✓ DAO Pattern: CRUD operations working");
            System.out.println("✓ JPA Entities: Horse, Stable, Rating");
            System.out.println("✓ Relationships: Bidirectional @OneToMany/@ManyToOne");
            System.out.println("✓ HQL Queries: Database queries working");
            System.out.println("✓ Criteria API: GROUP BY statistics working");
            System.out.println("✓ CSV Export: Data exported successfully");
            System.out.println("✓ Serialization: Binary serialization working");
            System.out.println("\nAll Lab 04 requirements demonstrated successfully!");
            
        } catch (Exception e) {
            System.err.println("Error during demo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
    
    private static void clearDatabase() {
        org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try {
            session.createQuery("DELETE FROM Rating").executeUpdate();
            session.createQuery("DELETE FROM Horse").executeUpdate();
            session.createQuery("DELETE FROM Stable").executeUpdate();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
