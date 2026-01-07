# Instrukcja Uruchomienia Projektu - Lab 5

## Przegl ąd
Projekt implementuje REST API dla systemu zarządzania stadninami koni używając Spring Boot 2.7.17, Hibernate 5.6 i bazy danych H2.

## Wymagania
- Java 17 lub wyższa
- Maven 3.6 lub wyższy

## Sprawdzenie Wersji
```bash
java -version   # Powinno pokazać Java 17+
mvn -version    # Powinno pokazać Maven 3.6+
```

## Kompilacja Projektu

### Pełna kompilacja
```bash
cd /scieżka/do/projektu/PAOiM
mvn clean compile
```

Powinno zakończyć się komunikatem:
```
[INFO] BUILD SUCCESS
```

## Uruchomienie Testów

### Wszystkie testy (łącznie z testami Spring Boot)
```bash
mvn test
```

Wynik:
- **Testy kontrolerów Spring Boot:** 10 testów (wszystkie przechodzą)
  - HorseControllerTest: 4 testy
  - StableControllerTest: 6 testów
- **Testy modelu:** 33 testy
- **Testy facade:** 12 testów  
- **Testy service:** 5 testów
- **Łącznie:** ~60 testów, wszystkie PASS ✓

### Tylko testy Spring Boot
```bash
mvn test -Dtest=HorseControllerTest,StableControllerTest
```

## Uruchomienie Aplikacji

### Opcja 1: Spring Boot REST API (Lab 5)

#### Uruchomienie serwera
```bash
mvn spring-boot:run
```

**Oczekiwany wynik:**
```
Started StableManagerApplication in X seconds (JVM running for Y)
Tomcat started on port(s): 8080 (http)
```

Serwer będzie dostępny na: `http://localhost:8080`

#### Sprawdzenie czy działa
Otwórz nową kartę terminala i wykonaj:
```bash
curl http://localhost:8080/api/stable
```

Powinno zwrócić JSON z listą stadnin (może być pusta na początku).

#### Zatrzymanie serwera
Naciśnij `Ctrl+C` w terminalu gdzie działa aplikacja.

### Opcja 2: JavaFX UI (Lab 3 & 4)
```bash
mvn javafx:run
```

Otworzy się okno GUI aplikacji.

**Logowanie:**
- **Admin:** login: `admin`, hasło: `admin`
- **User:** dowolny login (bez hasła)

## Testowanie REST API

### Używając curl

#### 1. Dodaj stadninę
```bash
curl -X POST http://localhost:8080/api/stable \
  -H "Content-Type: application/json" \
  -d '{"stableName":"Stadnina Północ","maxCapacity":10}'
```

Zwróci ID nowej stadniny (np. `"id":1`).

#### 2. Pobierz wszystkie stadniny
```bash
curl http://localhost:8080/api/stable
```

#### 3. Dodaj konia
```bash
curl -X POST http://localhost:8080/api/horse \
  -H "Content-Type: application/json" \
  -d '{
    "name":"Bella",
    "breed":"Arabski",
    "type":"HOT_BLOODED",
    "condition":"HEALTHY",
    "age":6,
    "price":15000.0,
    "weightKg":450.0,
    "stableId":1
  }'
```

#### 4. Pobierz konie w stadninie
```bash
curl http://localhost:8080/api/stable/1
```

#### 5. Pobierz zapełnienie stadniny
```bash
curl http://localhost:8080/api/stable/1/fill
```

#### 6. Dodaj ocenę konia
```bash
curl -X POST http://localhost:8080/api/horse/rating \
  -H "Content-Type: application/json" \
  -d '{
    "horseId":1,
    "ratingValue":5,
    "description":"Doskonała forma"
  }'
```

#### 7. Pobierz średnią ocenę konia
```bash
curl http://localhost:8080/api/horse/rating/1
```

#### 8. Eksport do CSV
```bash
curl http://localhost:8080/api/stable/1/csv
```

#### 9. Usuń konia
```bash
curl -X DELETE http://localhost:8080/api/horse/1
```

#### 10. Usuń stadninę
```bash
curl -X DELETE http://localhost:8080/api/stable/1
```

### Używając Postman

1. **Otwórz Postman**
2. **Utwórz nową kolekcję** o nazwie "Stable Manager API"
3. **Dodaj requesty** zgodnie z przykładami powyżej
4. **Ustaw:**
   - Method: GET/POST/DELETE (zgodnie z endpointem)
   - URL: `http://localhost:8080/api/...`
   - Headers: `Content-Type: application/json` (dla POST)
   - Body: Wybierz "raw" i "JSON", wklej JSON z przykładu

### Używając przeglądarki

#### Konsola H2 Database
1. Uruchom aplikację Spring Boot
2. Otwórz: `http://localhost:8080/h2-console`
3. Ustawienia połączenia:
   - JDBC URL: `jdbc:h2:./stable_db`
   - User Name: `sa`
   - Password: (zostaw puste)
4. Kliknij "Connect"

Możesz wykonywać zapytania SQL:
```sql
SELECT * FROM stables;
SELECT * FROM horses;
SELECT * FROM ratings;
```

## Baza Danych

### Lokalizacja pliku
- **Plik:** `./stable_db.mv.db`
- **Automatycznie tworzony** przy pierwszym uruchomieniu
- **Dane są zachowane** między uruchomieniami

### Reset bazy danych
Aby wyczyścić wszystkie dane:
```bash
rm stable_db.mv.db
```

Przy następnym uruchomieniu zostanie utworzona nowa, pusta baza.

## Wszystkie Endpointy API

| Nr | Metoda | Endpoint | Opis |
|----|--------|----------|------|
| 1  | POST   | /api/horse | Dodaj konia do stadniny |
| 2  | DELETE | /api/horse/:id | Usuń konia |
| 3  | GET    | /api/horse/rating/:id | Pobierz średnią ocenę konia |
| 4  | POST   | /api/horse/rating | Dodaj ocenę dla konia |
| 5  | GET    | /api/stable | Pobierz wszystkie stadniny |
| 6  | GET    | /api/stable/:id | Pobierz konie w stadninie |
| 7  | GET    | /api/stable/:id/csv | Eksportuj konie do CSV |
| 8  | POST   | /api/stable | Dodaj nową stadninę |
| 9  | DELETE | /api/stable/:id | Usuń stadninę |
| 10 | GET    | /api/stable/:id/fill | Pobierz zapełnienie stadniny |

Szczegółowa dokumentacja endpointów znajduje się w pliku **LAB05_README.md**.

## Typy Danych

### HorseType (Typ konia)
- `HOT_BLOODED` - Konie gorącokrwiste
- `COLD_BLOODED` - Konie zimnokrwiste

### HorseCondition (Stan konia)
- `HEALTHY` - Zdrowy
- `SICK` - Chory
- `TRAINING` - Na treningu
- `QUARANTINE` - Kwarantanna
- `SOLD` - Sprzedany

## Kody Błędów HTTP

| Kod | Znaczenie | Przykład |
|-----|-----------|----------|
| 200 | OK | Operacja powiodła się |
| 201 | Created | Zasób utworzony |
| 400 | Bad Request | Nieprawidłowe dane |
| 404 | Not Found | Zasób nie znaleziony |
| 409 | Conflict | Duplikat (np. ta sama nazwa) |
| 500 | Server Error | Nieoczekiwany błąd serwera |

## Struktura Projektu

```
PAOiM/
├── pom.xml                          # Konfiguracja Maven
├── LAB05_README.md                  # Dokumentacja (EN)
├── INSTRUKCJA_PL.md                 # Ta instrukcja (PL)
├── src/
│   ├── main/
│   │   ├── springboot/              # Spring Boot (Lab 5)
│   │   │   ├── StableManagerApplication.java
│   │   │   ├── controller/          # Kontrolery REST
│   │   │   ├── service/             # Logika biznesowa
│   │   │   └── dto/                 # Obiekty transferu danych
│   │   ├── model/                   # Encje Hibernate (Lab 4)
│   │   ├── dao/                     # Warstwa dostępu do danych
│   │   ├── service/                 # Serwisy (CSV, Serializacja)
│   │   ├── ui/                      # JavaFX UI (Lab 3)
│   │   └── resources/
│   │       ├── application.properties  # Konfiguracja Spring Boot
│   │       └── hibernate.cfg.xml       # Konfiguracja Hibernate
│   └── test/
│       ├── springboot/controller/   # Testy Spring Boot
│       ├── model/                   # Testy modelu
│       └── facade/                  # Testy facade
└── stable_db.mv.db                  # Plik bazy danych H2
```

## Rozwiązywanie Problemów

### Problem: Port 8080 jest zajęty
**Rozwiązanie:** Zmień port w `src/main/resources/application.properties`:
```properties
server.port=8081
```

### Problem: Błąd kompilacji
**Rozwiązanie:**
```bash
mvn clean compile
```

### Problem: Baza danych zablokowana
**Rozwiązanie:**
```bash
rm stable_db.mv.db.lock
```

### Problem: Aplikacja nie startuje
**Sprawdź:**
1. Czy port 8080 jest wolny
2. Czy Java 17 jest zainstalowana: `java -version`
3. Czy Maven działa poprawnie: `mvn -version`

### Problem: 404 Not Found na wszystkich endpointach
**Sprawdź:**
1. Czy aplikacja się uruchomiła (zobacz logi)
2. Czy używasz poprawnego portu (domyślnie 8080)
3. Czy endpoint zaczyna się od `/api/`

## Podsumowanie Komend

### Szybki start
```bash
# 1. Kompilacja
mvn clean compile

# 2. Testy
mvn test

# 3. Uruchomienie REST API
mvn spring-boot:run

# 4. W nowym terminalu - test API
curl http://localhost:8080/api/stable
```

### Zatrzymanie
- **Spring Boot:** `Ctrl+C`
- **JavaFX:** Zamknij okno

## Dodatkowe Informacje

### Dokumentacja
- **LAB05_README.md** - Pełna dokumentacja API (EN)
- **README.md** - Dokumentacja Lab 1-4
- **LAB04_IMPLEMENTATION.md** - Dokumentacja Hibernate

### Testy
- Wszystkie testy: `mvn test`
- Pokrycie kodu: `mvn clean test jacoco:report`
- Raport: `target/site/jacoco/index.html`

### Czyszczenie
```bash
mvn clean                    # Usuń skompilowane pliki
rm stable_db.mv.db          # Wyczyść bazę danych
```

## Wsparcie
Projekt edukacyjny dla kursu PAOiM - Lab 5.

Wszystkie wymagania z Lab 5 zostały zaimplementowane i przetestowane ✓
