package springboot.controller;

import exceptions.HorseNotFoundException;
import model.Horse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springboot.dto.HorseRequest;
import springboot.dto.HorseResponse;
import springboot.dto.RatingRequest;
import springboot.service.HorseService;
import springboot.service.RatingService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/horse")
public class HorseController {

    @Autowired
    private HorseService horseService;

    @Autowired
    private RatingService ratingService;

    /**
     * POST /api/horse - Add horse to stable
     */
    @PostMapping
    public ResponseEntity<HorseResponse> addHorse(@RequestBody HorseRequest request) {
        try {
            HorseResponse response = horseService.addHorse(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * DELETE /api/horse/:id - Remove horse from stable
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteHorse(@PathVariable Long id) {
        try {
            horseService.deleteHorse(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Horse deleted successfully");
            return ResponseEntity.ok(response);
        } catch (HorseNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * GET /api/horse/rating/:id - Get average rating of horse
     */
    @GetMapping("/rating/{id}")
    public ResponseEntity<Map<String, Object>> getHorseRating(@PathVariable Long id) {
        try {
            Double averageRating = ratingService.getAverageRating(id);
            Map<String, Object> response = new HashMap<>();
            response.put("horseId", id);
            response.put("averageRating", averageRating);
            return ResponseEntity.ok(response);
        } catch (HorseNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * POST /api/horse/rating - Add rating for horse
     */
    @PostMapping("/rating")
    public ResponseEntity<Map<String, String>> addRating(@RequestBody RatingRequest request) {
        try {
            ratingService.addRating(request);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Rating added successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
