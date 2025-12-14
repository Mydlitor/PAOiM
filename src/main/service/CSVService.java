package service;

import dao.HorseDAO;
import dao.HorseDAOImpl;
import dao.StableDAO;
import dao.StableDAOImpl;
import model.Horse;
import model.HorseCondition;
import model.HorseType;
import model.Stable;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVService {
    private static final String CSV_SEPARATOR = ",";
    private final StableDAO stableDAO;
    private final HorseDAO horseDAO;

    public CSVService() {
        this.stableDAO = new StableDAOImpl();
        this.horseDAO = new HorseDAOImpl();
    }

    public void exportStablesToCSV(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Write header
            writer.write("StableName,MaxCapacity,CurrentLoad");
            writer.newLine();

            // Use HQL to fetch stables
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Query<Stable> query = session.createQuery("FROM Stable", Stable.class);
                List<Stable> stables = query.list();

                for (Stable stable : stables) {
                    writer.write(String.format("%s,%d,%d",
                            escapeCSV(stable.getStableName()),
                            stable.getMaxCapacity(),
                            stable.getHorseList().size()));
                    writer.newLine();
                }
            }
        }
    }

    public void exportHorsesToCSV(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Write header
            writer.write("Name,Breed,Type,Condition,Age,Price,Weight,StableName");
            writer.newLine();

            // Use HQL to fetch horses
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Query<Horse> query = session.createQuery("FROM Horse h LEFT JOIN FETCH h.stable", Horse.class);
                List<Horse> horses = query.list();

                for (Horse horse : horses) {
                    String stableName = horse.getStable() != null ? horse.getStable().getStableName() : "";
                    writer.write(String.format("%s,%s,%s,%s,%d,%.2f,%.2f,%s",
                            escapeCSV(horse.getName()),
                            escapeCSV(horse.getBreed()),
                            horse.getType(),
                            horse.getCondition(),
                            horse.getAge(),
                            horse.getPrice(),
                            horse.getWeightKg(),
                            escapeCSV(stableName)));
                    writer.newLine();
                }
            }
        }
    }

    public void importStablesFromCSV(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }

                String[] parts = line.split(CSV_SEPARATOR);
                if (parts.length >= 2) {
                    String name = unescapeCSV(parts[0]);
                    int capacity = Integer.parseInt(parts[1].trim());

                    Stable stable = new Stable(name, capacity);
                    stableDAO.save(stable);
                }
            }
        }
    }

    public void importHorsesFromCSV(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }

                String[] parts = line.split(CSV_SEPARATOR);
                if (parts.length >= 7) {
                    String name = unescapeCSV(parts[0]);
                    String breed = unescapeCSV(parts[1]);
                    HorseType type = HorseType.valueOf(parts[2].trim());
                    HorseCondition condition = HorseCondition.valueOf(parts[3].trim());
                    int age = Integer.parseInt(parts[4].trim());
                    double price = Double.parseDouble(parts[5].trim());
                    double weight = Double.parseDouble(parts[6].trim());

                    Horse horse = new Horse(name, breed, type, condition, age, price, weight);

                    // If stable name is provided, associate with stable
                    if (parts.length >= 8 && !parts[7].trim().isEmpty()) {
                        String stableName = unescapeCSV(parts[7]);
                        Stable stable = stableDAO.findByName(stableName);
                        if (stable != null) {
                            horse.setStable(stable);
                        }
                    }

                    horseDAO.save(horse);
                }
            }
        }
    }

    private String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String unescapeCSV(String value) {
        if (value == null) {
            return "";
        }
        value = value.trim();
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
            value = value.replace("\"\"", "\"");
        }
        return value;
    }
}
