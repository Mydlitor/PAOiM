package pao.model;

import pao.exceptions.*;
import java.util.*;

public class StableManager {
  private final Map<String, Stable> stables = new HashMap<>();

  public void addStable(String name, int capacity) throws DuplicateStableException, InvalidDataException {
    if (stables.containsKey(name)) {
      throw new DuplicateStableException(name);
    }
    if (capacity <= 0) {
      throw new InvalidDataException("Capacity must be positive");
    }
    stables.put(name, new Stable(name, capacity));
  }

  public void removeStable(String name) throws StableNotFoundException {
    if (stables.remove(name) == null) {
      throw new StableNotFoundException(name);
    }
  }

  public List<Stable> findEmpty() {
    List<Stable> result = new ArrayList<>();
    for (Stable s : stables.values())
      if (s.getHorseList().isEmpty()) result.add(s);
    return result;
  }

  public void summary() {
    for (Stable s : stables.values())
      System.out.printf("Stable '%s': %.1f%%%n", s.getStableName(), s.occupancyPercent());
  }

  public Stable getStable(String name) throws StableNotFoundException {
    Stable stable = stables.get(name);
    if (stable == null) {
      throw new StableNotFoundException(name);
    }
    return stable;
  }

  public List<Stable> getAllStables() {
    return new ArrayList<>(stables.values());
  }

  public List<Stable> sortStablesByLoad() {
    List<Stable> sorted = new ArrayList<>(stables.values());
    sorted.sort(Comparator.comparingDouble(Stable::occupancyPercent).reversed());
    return sorted;
  }
}
