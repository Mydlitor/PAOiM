package springboot.service;

import dao.StableDAO;
import dao.StableDAOImpl;
import exceptions.*;
import model.Horse;
import model.Stable;
import org.springframework.stereotype.Service;
import service.CSVService;
import springboot.dto.StableRequest;
import springboot.dto.StableResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StableService {
    
    private final StableDAO stableDAO;
    private final CSVService csvService;

    public StableService() {
        this.stableDAO = new StableDAOImpl();
        this.csvService = new CSVService();
    }

    public StableResponse addStable(StableRequest request) throws DuplicateStableException, InvalidDataException {
        if (request.getStableName() == null || request.getStableName().trim().isEmpty()) {
            throw new InvalidDataException("Stable name cannot be empty");
        }
        if (request.getMaxCapacity() <= 0) {
            throw new InvalidDataException("Max capacity must be positive");
        }

        Stable existing = stableDAO.findByName(request.getStableName());
        if (existing != null) {
            throw new DuplicateStableException(request.getStableName());
        }

        Stable stable = new Stable(request.getStableName(), request.getMaxCapacity());
        stableDAO.save(stable);

        return new StableResponse(stable);
    }

    public void deleteStable(Long id) throws StableNotFoundException {
        Stable stable = stableDAO.findById(id);
        if (stable == null) {
            throw new StableNotFoundException("Stable with id " + id + " not found");
        }

        stableDAO.delete(stable);
    }

    public StableResponse getStableById(Long id) throws StableNotFoundException {
        Stable stable = stableDAO.findById(id);
        if (stable == null) {
            throw new StableNotFoundException("Stable with id " + id + " not found");
        }
        return new StableResponse(stable);
    }

    public List<StableResponse> getAllStables() {
        return stableDAO.findAll().stream()
            .map(StableResponse::new)
            .collect(Collectors.toList());
    }

    public double getStableFillPercentage(Long id) throws StableNotFoundException {
        Stable stable = stableDAO.findById(id);
        if (stable == null) {
            throw new StableNotFoundException("Stable with id " + id + " not found");
        }

        int currentOccupancy = stable.getHorseList().size();
        int maxCapacity = stable.getMaxCapacity();

        return maxCapacity > 0 ? (currentOccupancy * 100.0) / maxCapacity : 0.0;
    }

    public String exportStableHorsesToCSV(Long id) throws StableNotFoundException {
        Stable stable = stableDAO.findById(id);
        if (stable == null) {
            throw new StableNotFoundException("Stable with id " + id + " not found");
        }

        StringBuilder csv = new StringBuilder();
        csv.append("ID,Name,Breed,Type,Condition,Age,Price,Weight\n");

        for (Horse horse : stable.getHorseList()) {
            csv.append(horse.getId()).append(",")
               .append(horse.getName()).append(",")
               .append(horse.getBreed()).append(",")
               .append(horse.getType()).append(",")
               .append(horse.getCondition()).append(",")
               .append(horse.getAge()).append(",")
               .append(horse.getPrice()).append(",")
               .append(horse.getWeightKg()).append("\n");
        }

        return csv.toString();
    }
}
