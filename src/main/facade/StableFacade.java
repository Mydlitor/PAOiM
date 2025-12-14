package facade;

import exceptions.*;
import model.*;
import java.util.List;

/**
 * Facade pattern to separate UI layer from business logic
 */
public class StableFacade {
    private final StableManager stableManager;

    public StableFacade() {
        this.stableManager = new StableManager();
    }

    // Stable operations
    public void addStable(String name, int capacity) throws StableException {
        stableManager.addStable(name, capacity);
    }

    public void removeStable(String name) throws StableException {
        stableManager.removeStable(name);
    }

    public List<Stable> getAllStables() {
        return stableManager.getAllStables();
    }

    public List<Stable> sortStablesByLoad() {
        return stableManager.sortStablesByLoad();
    }

    public List<Stable> findEmptyStables() {
        return stableManager.findEmpty();
    }

    public Stable getStable(String name) throws StableException {
        return stableManager.getStable(name);
    }

    // Horse operations
    public void addHorseToStable(String stableName, String horseName, String breed, 
                                  HorseType type, HorseCondition condition, 
                                  int age, double price, double weight) throws StableException {
        Stable stable = stableManager.getStable(stableName);
        Horse horse = new Horse(horseName, breed, type, condition, age, price, weight);
        stable.addHorse(horse);
    }

    public void removeHorseFromStable(String stableName, String horseName) throws StableException {
        Stable stable = stableManager.getStable(stableName);
        Horse horse = stable.search(horseName);
        if (horse == null) {
            throw new HorseNotFoundException(horseName);
        }
        stable.removeHorse(horse);
    }

    public List<Horse> getHorsesInStable(String stableName) throws StableException {
        Stable stable = stableManager.getStable(stableName);
        return stable.getHorseList();
    }

    public List<Horse> searchHorsesByName(String stableName, String fragment) throws StableException {
        Stable stable = stableManager.getStable(stableName);
        return stable.searchPartial(fragment);
    }

    public List<Horse> filterHorsesByCondition(String stableName, HorseCondition condition) throws StableException {
        Stable stable = stableManager.getStable(stableName);
        return stable.getHorseList().stream()
                .filter(h -> h.getCondition() == condition)
                .toList();
    }

    public List<Horse> sortHorsesByName(String stableName) throws StableException {
        Stable stable = stableManager.getStable(stableName);
        return stable.sortByName();
    }

    public List<Horse> sortHorsesByPrice(String stableName) throws StableException {
        Stable stable = stableManager.getStable(stableName);
        return stable.sortByPrice();
    }

    public void changeHorseCondition(String stableName, String horseName, HorseCondition condition) throws StableException {
        Stable stable = stableManager.getStable(stableName);
        Horse horse = stable.search(horseName);
        if (horse == null) {
            throw new HorseNotFoundException(horseName);
        }
        stable.changeCondition(horse, condition);
    }

    public void changeHorseWeight(String stableName, String horseName, double weight) throws StableException {
        Stable stable = stableManager.getStable(stableName);
        Horse horse = stable.search(horseName);
        if (horse == null) {
            throw new HorseNotFoundException(horseName);
        }
        stable.changeWeight(horse, weight);
    }
}
