package dao;

import model.Rating;
import java.util.List;
import java.util.Map;

public interface RatingDAO {
    void save(Rating rating);
    void update(Rating rating);
    void delete(Rating rating);
    Rating findById(Long id);
    List<Rating> findAll();
    List<Rating> findByHorseId(Long horseId);
    Map<String, Object> getAverageRatingByHorse();
}
