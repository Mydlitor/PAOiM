# Lab 04 - Hibernate ORM Integration

## Overview
This lab implements complete Hibernate ORM integration for the stable management system, replacing in-memory data storage with a persistent H2 database.

## Implementation Summary

### 1. Database Schema
- **Entities**: Horse, Stable, Rating
- **Relationships**: 
  - Stable (1) ←→ (n) Horse (bidirectional @OneToMany/@ManyToOne)
  - Horse (1) ←→ (n) Rating (unidirectional @OneToMany)
- **Database**: H2 in-memory database with file persistence
- **ORM**: Hibernate 5.6.15.Final

### 2. Architecture

#### Entity Layer (`model/`)
- **Horse**: JPA entity with fields: id, name, breed, type, condition, age, price, weightKg, stable
- **Stable**: JPA entity with fields: id, stableName, maxCapacity, horseList
- **Rating**: JPA entity with fields: id, ratingValue (0-5), horse, ratingDate, description

#### DAO Layer (`dao/`)
- **Interfaces**: HorseDAO, StableDAO, RatingDAO
- **Implementations**: HorseDAOImpl, StableDAOImpl, RatingDAOImpl
- All DAOs implement:
  - CRUD operations (save, update, delete, findById)
  - Custom queries using HQL
  - Proper transaction management

#### Service Layer (`service/`)
- **SerializationService**: Binary serialization for Horse and Stable entities
- **CSVService**: CSV import/export using HQL queries
- **RatingStatisticsService**: Rating statistics using Criteria API (GROUP BY)

#### Utility Layer (`util/`)
- **HibernateUtil**: SessionFactory management (singleton pattern)

### 3. Key Features

#### Hibernate Configuration (`hibernate.cfg.xml`)
```xml
- Driver: org.h2.Driver
- URL: jdbc:h2:./stable_db;AUTO_SERVER=TRUE
- Dialect: org.hibernate.dialect.H2Dialect
- hbm2ddl.auto: update (creates/updates schema automatically)
```

#### DAO Pattern Implementation
All database operations are isolated in DAO classes:
- Transaction boundaries clearly defined
- Automatic rollback on errors
- Session management using try-with-resources
- HQL queries for complex operations

#### Serialization
```java
SerializationService service = new SerializationService();
service.saveStable(stable, "stable.ser");
Stable loaded = service.loadStable("stable.ser");
```

#### CSV Export/Import
```java
CSVService csvService = new CSVService();
csvService.exportStablesToCSV("stables.csv");
csvService.exportHorsesToCSV("horses.csv");
csvService.importStablesFromCSV("stables.csv");
csvService.importHorsesFromCSV("horses.csv");
```

#### Rating Statistics (Criteria API)
```java
RatingStatisticsService statsService = new RatingStatisticsService();
Map<String, Object> stats = statsService.getHorseRatingStatistics();
// Returns: Map<HorseName, Map<"average"|"count", value>>
statsService.displayRatingStatistics(); // Prints to console
```

### 4. Database Operations

#### Adding a Stable with Horses
```java
StableFacade facade = new StableFacade();
facade.addStable("North Farm", 10);
facade.addHorseToStable("North Farm", "Bella", "Arabian", 
    HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 6, 15000.0, 450.0);
```

#### Adding Ratings
```java
RatingDAO ratingDAO = new RatingDAOImpl();
HorseDAO horseDAO = new HorseDAOImpl();
Horse horse = horseDAO.findByName("Bella");
Rating rating = new Rating(5, horse, new Date(), "Excellent performance");
ratingDAO.save(rating);
```

### 5. Testing

#### Test Infrastructure
- **TestDatabaseUtil**: Clears database before each test
- **50 tests total** (all passing):
  - Model tests: 33 tests
  - Facade tests: 12 tests
  - Service tests: 5 tests

#### Service Integration Tests
- Serialization/deserialization of Stable and Horse
- CSV export for stables and horses
- Rating statistics with Criteria API
- All tests verify database persistence

### 6. Project Structure
```
src/main/
├── dao/
│   ├── HorseDAO.java
│   ├── HorseDAOImpl.java
│   ├── RatingDAO.java
│   ├── RatingDAOImpl.java
│   ├── StableDAO.java
│   └── StableDAOImpl.java
├── model/
│   ├── Horse.java (JPA Entity)
│   ├── Stable.java (JPA Entity)
│   ├── Rating.java (JPA Entity)
│   ├── HorseCondition.java
│   ├── HorseType.java
│   └── StableManager.java
├── service/
│   ├── SerializationService.java
│   ├── CSVService.java
│   └── RatingStatisticsService.java
├── util/
│   └── HibernateUtil.java
└── resources/
    └── hibernate.cfg.xml

src/test/
├── service/
│   └── ServiceIntegrationTest.java
└── util/
    └── TestDatabaseUtil.java
```

### 7. Dependencies
```xml
<!-- Hibernate Core -->
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>5.6.15.Final</version>
</dependency>

<!-- H2 Database -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.1.214</version>
</dependency>
```

### 8. Running the Application

#### Build and Test
```bash
mvn clean test
```

#### Run with UI
```bash
mvn javafx:run
```

#### Database File
- Location: `./stable_db.mv.db`
- Automatically created on first run
- Persists between application restarts
- Can be deleted to reset database

### 9. Technical Highlights

#### Transaction Management
All database operations use proper transaction management:
```java
Transaction transaction = null;
try (Session session = HibernateUtil.getSessionFactory().openSession()) {
    transaction = session.beginTransaction();
    // Database operations
    transaction.commit();
} catch (Exception e) {
    if (transaction != null) {
        transaction.rollback();
    }
    throw e;
}
```

#### Bidirectional Relationship
```java
// In Stable:
@OneToMany(mappedBy = "stable", fetch = FetchType.EAGER, orphanRemoval = true)
@Cascade({CascadeType.ALL})
private List<Horse> horseList;

// In Horse:
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "stable_id")
@Cascade(CascadeType.SAVE_UPDATE)
private Stable stable;
```

#### HQL Queries
```java
Query<Stable> query = session.createQuery("FROM Stable", Stable.class);
List<Stable> stables = query.list();
```

#### Criteria API for GROUP BY
```java
CriteriaBuilder cb = session.getCriteriaBuilder();
CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
Root<Rating> root = cq.from(Rating.class);
Join<Rating, Horse> horseJoin = root.join("horse");

cq.multiselect(
    horseJoin.get("name"),
    cb.avg(root.get("ratingValue")),
    cb.count(root)
);
cq.groupBy(horseJoin.get("name"));
```

### 10. Security & Quality

#### Security
- ✅ No vulnerabilities found (CodeQL scan)
- ✅ No security advisories for dependencies
- ✅ Proper input validation
- ✅ No SQL injection (using parameterized queries)

#### Code Quality
- ✅ All code review issues addressed
- ✅ Protected setters for immutable fields
- ✅ Proper exception handling
- ✅ 50 tests passing (100% success rate)

### 11. Lab Requirements Checklist

✅ **Database Schema with Hibernate**
- Horse and Stable converted to JPA entities
- 1:n bidirectional relationship implemented
- Rating entity created with all required fields

✅ **DAO Pattern Implementation**
- DAO interfaces and implementations for all entities
- Database operations separated from business logic
- SessionFactory used for database operations

✅ **Serialization Functionality**
- Binary serialization implemented
- Java Serialization API used
- Service class created

✅ **CSV Import/Export**
- CSV export implemented using HQL
- CSV import implemented
- Uses java.io package (portable)

✅ **Refactor Existing Methods**
- All CRUD operations use database
- Filtering/searching queries database
- Exceptions handled at UI level

✅ **HQL Queries**
- CSV export uses HQL queries
- Efficient database queries implemented

✅ **Criteria API Usage**
- GROUP BY implemented using Criteria API
- Average rating and count calculated
- Results displayable in console

✅ **Maven Configuration**
- Hibernate 5.6.15.Final added
- H2 database driver configured
- Proper Maven build

### 12. Future Enhancements

Potential improvements for future iterations:
- Add UI components for rating management
- Implement CSV import UI
- Add database backup/restore functionality
- Implement lazy loading optimizations
- Add caching layer (second-level cache)
- Create database migration scripts
