package springboot.service;

import dao.HorseDAO;
import dao.HorseDAOImpl;
import dao.StableDAO;
import dao.StableDAOImpl;
import exceptions.*;
import model.Horse;
import model.Stable;
import org.springframework.stereotype.Service;
import springboot.dto.HorseRequest;
import springboot.dto.HorseResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HorseService {
    
    private final HorseDAO horseDAO;
    private final StableDAO stableDAO;

    public HorseService() {
        this.horseDAO = new HorseDAOImpl();
        this.stableDAO = new StableDAOImpl();
    }

    public HorseResponse addHorse(HorseRequest request) throws StableNotFoundException, StableCapacityException, DuplicateHorseException {
        Stable stable = stableDAO.findById(request.getStableId());
        if (stable == null) {
            throw new StableNotFoundException("Stable with id " + request.getStableId() + " not found");
        }

        Horse horse = new Horse(
            request.getName(),
            request.getBreed(),
            request.getType(),
            request.getCondition(),
            request.getAge(),
            request.getPrice(),
            request.getWeightKg()
        );

        stable.addHorse(horse);
        stableDAO.update(stable);
        horseDAO.save(horse);

        return new HorseResponse(horse);
    }

    public void deleteHorse(Long id) throws HorseNotFoundException {
        Horse horse = horseDAO.findById(id);
        if (horse == null) {
            throw new HorseNotFoundException("Horse with id " + id + " not found");
        }

        Stable stable = horse.getStable();
        if (stable != null) {
            stable.removeHorse(horse);
            stableDAO.update(stable);
        }

        horseDAO.delete(horse);
    }

    public HorseResponse getHorseById(Long id) throws HorseNotFoundException {
        Horse horse = horseDAO.findById(id);
        if (horse == null) {
            throw new HorseNotFoundException("Horse with id " + id + " not found");
        }
        return new HorseResponse(horse);
    }

    public List<HorseResponse> getAllHorses() {
        return horseDAO.findAll().stream()
            .map(HorseResponse::new)
            .collect(Collectors.toList());
    }

    public List<HorseResponse> getHorsesByStableId(Long stableId) {
        return horseDAO.findByStableId(stableId).stream()
            .map(HorseResponse::new)
            .collect(Collectors.toList());
    }
}
