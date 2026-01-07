package springboot.controller;

import exceptions.StableNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springboot.dto.HorseResponse;
import springboot.dto.StableRequest;
import springboot.dto.StableResponse;
import springboot.service.HorseService;
import springboot.service.StableService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stable")
public class StableController {

    @Autowired
    private StableService stableService;

    @Autowired
    private HorseService horseService;

    /**
     * GET /api/stable - Get all stables
     */
    @GetMapping
    public ResponseEntity<List<StableResponse>> getAllStables() {
        List<StableResponse> stables = stableService.getAllStables();
        return ResponseEntity.ok(stables);
    }

    /**
     * GET /api/stable/:id - Get all horses in a stable
     */
    @GetMapping("/{id}")
    public ResponseEntity<List<HorseResponse>> getStableHorses(@PathVariable Long id) {
        try {
            // First verify stable exists
            stableService.getStableById(id);
            List<HorseResponse> horses = horseService.getHorsesByStableId(id);
            return ResponseEntity.ok(horses);
        } catch (StableNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * GET /api/stable/:id/csv - Export horses to CSV
     */
    @GetMapping("/{id}/csv")
    public ResponseEntity<String> exportStableHorsesToCSV(@PathVariable Long id) {
        try {
            String csv = stableService.exportStableHorsesToCSV(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", "stable_" + id + "_horses.csv");
            return ResponseEntity.ok().headers(headers).body(csv);
        } catch (StableNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * POST /api/stable - Add new stable
     */
    @PostMapping
    public ResponseEntity<StableResponse> addStable(@RequestBody StableRequest request) {
        try {
            StableResponse response = stableService.addStable(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * DELETE /api/stable/:id - Delete stable
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteStable(@PathVariable Long id) {
        try {
            stableService.deleteStable(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Stable deleted successfully");
            return ResponseEntity.ok(response);
        } catch (StableNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * GET /api/stable/:id/fill - Get stable occupancy percentage
     */
    @GetMapping("/{id}/fill")
    public ResponseEntity<Map<String, Object>> getStableFill(@PathVariable Long id) {
        try {
            StableResponse stable = stableService.getStableById(id);
            double fillPercentage = stableService.getStableFillPercentage(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("stableId", id);
            response.put("stableName", stable.getStableName());
            response.put("currentOccupancy", stable.getCurrentOccupancy());
            response.put("maxCapacity", stable.getMaxCapacity());
            response.put("fillPercentage", fillPercentage);
            
            return ResponseEntity.ok(response);
        } catch (StableNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
