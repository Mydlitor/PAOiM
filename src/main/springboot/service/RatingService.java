package springboot.service;

import dao.HorseDAO;
import dao.HorseDAOImpl;
import dao.RatingDAO;
import dao.RatingDAOImpl;
import exceptions.HorseNotFoundException;
import model.Horse;
import model.Rating;
import org.springframework.stereotype.Service;
import springboot.dto.RatingRequest;

import java.util.Date;
import java.util.List;

@Service
public class RatingService {
    
    private final RatingDAO ratingDAO;
    private final HorseDAO horseDAO;

    public RatingService() {
        this.ratingDAO = new RatingDAOImpl();
        this.horseDAO = new HorseDAOImpl();
    }

    public void addRating(RatingRequest request) throws HorseNotFoundException {
        Horse horse = horseDAO.findById(request.getHorseId());
        if (horse == null) {
            throw new HorseNotFoundException("Horse with id " + request.getHorseId() + " not found");
        }

        if (request.getRatingValue() < 0 || request.getRatingValue() > 5) {
            throw new IllegalArgumentException("Rating value must be between 0 and 5");
        }

        Rating rating = new Rating(
            request.getRatingValue(),
            horse,
            new Date(),
            request.getDescription() != null ? request.getDescription() : ""
        );

        ratingDAO.save(rating);
    }

    public Double getAverageRating(Long horseId) throws HorseNotFoundException {
        Horse horse = horseDAO.findById(horseId);
        if (horse == null) {
            throw new HorseNotFoundException("Horse with id " + horseId + " not found");
        }

        List<Rating> ratings = ratingDAO.findByHorseId(horseId);
        if (ratings.isEmpty()) {
            return 0.0;
        }

        double sum = ratings.stream()
            .mapToInt(Rating::getRatingValue)
            .sum();

        return sum / ratings.size();
    }
}
