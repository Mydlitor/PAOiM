# Lab 5 - Spring Boot REST API

## Overview
This implementation provides a RESTful API for the Stable Manager application using Spring Boot 3.1.5, with full CRUD operations for horses, stables, and ratings.

## Technologies Used
- **Java 17**
- **Spring Boot 3.1.5**
  - Spring Web (REST controllers)
  - Spring Data JPA (database integration)
  - Spring Boot Test (testing framework)
- **Hibernate 5.6.15** (ORM, compatible with existing codebase)
- **H2 Database** (embedded, file-based persistence)
- **Maven** (build and dependency management)
- **JUnit 5** & **Mockito** (unit testing)

## API Endpoints

### Horse Endpoints

#### 1. **POST /api/horse** - Add horse to stable
**Request Body:**
```json
{
  "name": "Bella",
  "breed": "Arabian",
  "type": "HOT_BLOODED",
  "condition": "HEALTHY",
  "age": 6,
  "price": 15000.0,
  "weightKg": 450.0,
  "stableId": 1
}
```
**Response:** `201 CREATED`
```json
{
  "id": 1,
  "name": "Bella",
  "breed": "Arabian",
  "type": "HOT_BLOODED",
  "condition": "HEALTHY",
  "age": 6,
  "price": 15000.0,
  "weightKg": 450.0,
  "stableId": 1,
  "stableName": "North Farm"
}
```

#### 2. **DELETE /api/horse/{id}** - Remove horse
**Response:** `200 OK`
```json
{
  "message": "Horse deleted successfully"
}
```

#### 3. **GET /api/horse/rating/{id}** - Get average rating of horse
**Response:** `200 OK`
```json
{
  "horseId": 1,
  "averageRating": 4.5
}
```

#### 4. **POST /api/horse/rating** - Add rating for horse
**Request Body:**
```json
{
  "horseId": 1,
  "ratingValue": 5,
  "description": "Excellent performance"
}
```
**Response:** `201 CREATED`
```json
{
  "message": "Rating added successfully"
}
```

### Stable Endpoints

#### 5. **GET /api/stable** - Get all stables
**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "stableName": "North Farm",
    "maxCapacity": 10,
    "currentOccupancy": 3
  }
]
```

#### 6. **GET /api/stable/{id}** - Get all horses in a stable
**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Bella",
    "breed": "Arabian",
    "type": "HOT_BLOODED",
    "condition": "HEALTHY",
    "age": 6,
    "price": 15000.0,
    "weightKg": 450.0,
    "stableId": 1,
    "stableName": "North Farm"
  }
]
```

#### 7. **GET /api/stable/{id}/csv** - Export horses to CSV
**Response:** `200 OK` (Content-Type: text/csv)
```csv
ID,Name,Breed,Type,Condition,Age,Price,Weight
1,Bella,Arabian,HOT_BLOODED,HEALTHY,6,15000.0,450.0
```

#### 8. **POST /api/stable** - Add new stable
**Request Body:**
```json
{
  "stableName": "West Ranch",
  "maxCapacity": 8
}
```
**Response:** `201 CREATED`
```json
{
  "id": 2,
  "stableName": "West Ranch",
  "maxCapacity": 8,
  "currentOccupancy": 0
}
```

#### 9. **DELETE /api/stable/{id}** - Remove stable
**Response:** `200 OK`
```json
{
  "message": "Stable deleted successfully"
}
```

#### 10. **GET /api/stable/{id}/fill** - Get stable occupancy percentage
**Response:** `200 OK`
```json
{
  "stableId": 1,
  "stableName": "North Farm",
  "currentOccupancy": 3,
  "maxCapacity": 10,
  "fillPercentage": 30.0
}
```

## Error Responses

All errors return appropriate HTTP status codes with error messages:

- **404 NOT FOUND** - Resource not found
```json
{
  "message": "Horse with id 999 not found",
  "status": 404
}
```

- **400 BAD REQUEST** - Invalid data or capacity exceeded
```json
{
  "message": "Stable capacity exceeded",
  "status": 400
}
```

- **409 CONFLICT** - Duplicate entry
```json
{
  "message": "Stable with name 'North Farm' already exists",
  "status": 409
}
```

- **500 INTERNAL SERVER ERROR** - Unexpected server error
```json
{
  "message": "An unexpected error occurred",
  "status": 500
}
```

## Building the Project

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Compile
```bash
mvn clean compile
```

### Run Tests
```bash
mvn test
```

All controller tests pass (10 tests):
- HorseControllerTest: 4 tests
- StableControllerTest: 6 tests

## Running the Application

### 1. Start Spring Boot Application
```bash
mvn spring-boot:run
```

The application will start on **port 8080**.

You should see:
```
Started StableManagerApplication in X seconds
```

### 2. Verify Server is Running
Open browser or use curl:
```bash
curl http://localhost:8080/api/stable
```

## Testing with Postman

### Import Collection
Create a new Postman collection and add the endpoints listed above.

### Example: Add a Stable
1. **Method:** POST
2. **URL:** `http://localhost:8080/api/stable`
3. **Headers:** `Content-Type: application/json`
4. **Body (raw JSON):**
```json
{
  "stableName": "Test Stable",
  "maxCapacity": 5
}
```
5. **Expected Response:** 201 CREATED with stable details

### Example: Add a Horse
1. **Method:** POST
2. **URL:** `http://localhost:8080/api/horse`
3. **Headers:** `Content-Type: application/json`
4. **Body (raw JSON):**
```json
{
  "name": "Thunder",
  "breed": "Mustang",
  "type": "HOT_BLOODED",
  "condition": "HEALTHY",
  "age": 4,
  "price": 12000.0,
  "weightKg": 480.0,
  "stableId": 1
}
```
5. **Expected Response:** 201 CREATED with horse details

### Example: Get Stable Occupancy
1. **Method:** GET
2. **URL:** `http://localhost:8080/api/stable/1/fill`
3. **Expected Response:** 200 OK with occupancy details

## Database Access

### H2 Console
The H2 console is enabled for debugging:

**URL:** `http://localhost:8080/h2-console`

**Connection Settings:**
- JDBC URL: `jdbc:h2:./stable_db`
- User: `sa`
- Password: (leave empty)

### Database File
- Location: `./stable_db.mv.db`
- Automatically created on first run
- Persists between application restarts
- Delete to reset database

## Project Structure

```
src/main/
├── springboot/
│   ├── StableManagerApplication.java     # Spring Boot main class
│   ├── controller/
│   │   ├── HorseController.java          # Horse endpoints
│   │   ├── StableController.java         # Stable endpoints
│   │   └── GlobalExceptionHandler.java   # Error handling
│   ├── service/
│   │   ├── HorseService.java             # Horse business logic
│   │   ├── StableService.java            # Stable business logic
│   │   └── RatingService.java            # Rating business logic
│   └── dto/
│       ├── HorseRequest.java             # Request DTOs
│       ├── HorseResponse.java            # Response DTOs
│       ├── StableRequest.java
│       ├── StableResponse.java
│       ├── RatingRequest.java
│       └── ErrorResponse.java
├── resources/
│   └── application.properties            # Spring Boot configuration
```

## Key Features Implemented

### ✅ RESTful Architecture
- Proper HTTP methods (GET, POST, DELETE)
- Correct status codes (200, 201, 400, 404, 409, 500)
- JSON request/response format
- CSV export capability

### ✅ Separation of Concerns
- **Controllers:** Handle HTTP requests/responses only
- **Services:** Business logic and DAO interaction
- **DTOs:** Data transfer objects for API
- **Exception Handler:** Centralized error handling

### ✅ Best Practices
- GET methods don't modify state
- Proper exception handling (no 500 errors for expected cases)
- Meaningful error messages
- Input validation

### ✅ Testing
- Unit tests for all endpoints
- MockMvc for controller testing
- Mocked services for isolation
- All tests passing

## Lab Requirements Checklist

✅ **Implemented REST controller with required endpoints**
- POST /api/horse ✓
- DELETE /api/horse/:id ✓
- GET /api/horse/rating/:id ✓
- POST /api/horse/rating ✓
- GET /api/stable ✓
- GET /api/stable/:id ✓
- GET /api/stable/:id/csv ✓
- POST /api/stable ✓
- DELETE /api/stable/:id ✓
- GET /api/stable/:id/fill ✓

✅ **Data storage in database** (uses existing Hibernate setup from Lab 4)

✅ **Unit tests for endpoints** (10 tests, all passing)

✅ **Controller follows best practices:**
- Handles request/response objects
- Database operations in service layer
- CSV generation in service layer
- Proper status codes (404 for not found)
- GET methods don't modify state
- Exception handling (avoid 500 errors)
- Meaningful error messages with status codes

✅ **Maven for dependencies and testing**

## Running with JavaFX UI (Previous Labs)

The JavaFX UI from previous labs still works:

```bash
mvn javafx:run
```

## Additional Notes

### Compatibility
- Uses Hibernate 5.6 for compatibility with existing code (javax.persistence)
- Maintains all Lab 4 functionality
- Database schema unchanged
- Existing DAOs reused

### Future Enhancements
- Add authentication/authorization
- Implement pagination for large datasets
- Add Swagger/OpenAPI documentation
- Add integration tests
- Implement caching

## Troubleshooting

### Port Already in Use
If port 8080 is already in use, change it in `application.properties`:
```properties
server.port=8081
```

### Database Lock Error
If you see a database lock error, ensure no other instance is running and delete `stable_db.mv.db.lock`.

### Compilation Errors
Ensure Java 17 and Maven are properly installed:
```bash
java -version
mvn -version
```

## Author
Implementation for Lab 5 - Spring Boot REST API

## License
Educational project for PAOiM course
