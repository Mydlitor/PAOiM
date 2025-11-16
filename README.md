# Stable Manager - Lab 3

A JavaFX application for managing horse stables with admin and user interfaces.

## Features

### Admin Features
- Full CRUD operations for stables and horses
- Add/remove/edit stables
- Add/remove/edit horses
- Sort stables by occupancy load
- Sort horses by name or price
- Filter horses by name and condition
- View detailed information

### User Features
- View all stables and horses (read-only)
- Search horses by name
- Filter horses by condition
- Sort horses by name or price

## Architecture

- **Model**: Core business logic (Horse, Stable, StableManager)
- **Facade**: StableFacade separates UI from business logic
- **UI**: JavaFX views (LoginView, AdminView, UserView)
- **Exceptions**: Custom checked exceptions for error handling
- **Tests**: Comprehensive JUnit 5 tests with 70%+ coverage

## Design Patterns Used

1. **Facade Pattern**: `StableFacade` provides a simplified interface to the model
2. **Singleton Pattern**: `DataGenerator` ensures single instance
3. **MVC Pattern**: Separation of Model, View, and Controller logic

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- JavaFX 17 or higher (included in dependencies)

## Building the Project

```bash
mvn clean compile
```

## Running Tests

```bash
mvn test
```

## Running with Code Coverage

```bash
mvn clean test jacoco:report
```

Coverage report will be generated in `target/site/jacoco/index.html`

## Running the Application

```bash
mvn javafx:run
```

## Login Credentials

### Admin Access
- Username: `admin`
- Password: `admin`

### User Access
- Username: any non-empty string
- Password: not required

## Project Structure

```
src/
├── main/
│   └── java/
│       └── pl/edu/pao/
│           ├── Main.java                    # JavaFX Application entry point
│           ├── exceptions/                   # Custom exceptions
│           │   ├── StableException.java
│           │   ├── DuplicateStableException.java
│           │   ├── DuplicateHorseException.java
│           │   ├── HorseNotFoundException.java
│           │   ├── StableNotFoundException.java
│           │   ├── StableCapacityException.java
│           │   └── InvalidDataException.java
│           ├── facade/                       # Facade pattern
│           │   ├── StableFacade.java
│           │   └── DataGenerator.java       # Singleton
│           ├── model/                        # Business logic
│           │   ├── Horse.java
│           │   ├── HorseCondition.java
│           │   ├── HorseType.java
│           │   ├── Stable.java
│           │   ├── StableManager.java
│           │   └── Main.java               # Console test main
│           └── ui/                          # JavaFX views
│               ├── LoginView.java
│               ├── AdminView.java
│               └── UserView.java
└── test/
    └── java/
        └── pl/edu/pao/
            ├── model/                       # Model tests
            │   ├── HorseTest.java
            │   ├── StableTest.java
            │   └── StableManagerTest.java
            └── facade/                      # Facade tests
                ├── StableFacadeTest.java
                └── DataGeneratorTest.java
```

## Test Coverage

- **HorseTest**: 77% coverage - 7 tests
- **StableTest**: 80% coverage - 17 tests
- **StableManagerTest**: 78% coverage - 9 tests
- **StableFacadeTest**: 90% coverage - 10 tests
- **DataGeneratorTest**: 93% coverage - 2 tests

Total: **45 tests, all passing**

## Exception Handling

The application uses custom checked exceptions (not Runtime exceptions) for:
- Duplicate entries (stables/horses)
- Capacity exceeded
- Not found errors
- Invalid data

All exceptions extend `StableException` and provide clear error messages.

## Sample Data

The application automatically generates sample data on startup:
- 4 stables (North Farm, East Barn, South Stable, West Ranch)
- 10 horses across different stables
- Various breeds, conditions, and prices

## Technologies Used

- **Java 17**
- **JavaFX 17.0.2** - UI framework
- **JUnit 5.9.2** - Testing
- **JaCoCo 0.8.8** - Code coverage
- **Maven** - Build tool

## Lab Requirements Met

✅ JavaFX UI with login, admin, and user modules  
✅ Separation of concerns (Model-View-Facade)  
✅ Custom checked exceptions (not Runtime)  
✅ Facade pattern implementation  
✅ Singleton pattern (DataGenerator)  
✅ CRUD operations for admin  
✅ Read-only view for users  
✅ Search and filter functionality  
✅ Sort functionality  
✅ JUnit tests with 70%+ code coverage  
✅ Proper exception handling throughout  

## Notes

- UI components are not tested (requires UI testing frameworks)
- Core business logic has excellent test coverage (77-90%)
- All model operations properly throw checked exceptions
- Facade pattern successfully separates UI from model
