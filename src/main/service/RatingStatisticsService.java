package service;

import dao.RatingDAO;
import dao.RatingDAOImpl;
import java.util.Map;

public class RatingStatisticsService {
    private final RatingDAO ratingDAO;

    public RatingStatisticsService() {
        this.ratingDAO = new RatingDAOImpl();
    }

    /**
     * Gets average rating and count for each horse using Criteria API (GROUP BY)
     * @return Map with horse name as key and a map of statistics (average, count) as value
     */
    public Map<String, Object> getHorseRatingStatistics() {
        return ratingDAO.getAverageRatingByHorse();
    }

    /**
     * Prints rating statistics to console
     */
    public void displayRatingStatistics() {
        Map<String, Object> stats = getHorseRatingStatistics();
        
        if (stats.isEmpty()) {
            System.out.println("No ratings found.");
            return;
        }

        System.out.println("\n=== Horse Rating Statistics ===");
        System.out.printf("%-20s %-15s %-10s%n", "Horse Name", "Average Rating", "Count");
        System.out.println("-".repeat(50));

        for (Map.Entry<String, Object> entry : stats.entrySet()) {
            String horseName = entry.getKey();
            @SuppressWarnings("unchecked")
            Map<String, Object> horseStats = (Map<String, Object>) entry.getValue();
            Double average = (Double) horseStats.get("average");
            Long count = (Long) horseStats.get("count");

            System.out.printf("%-20s %-15.2f %-10d%n", horseName, average, count);
        }
        System.out.println();
    }
}
