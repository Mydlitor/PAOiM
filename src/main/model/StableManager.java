package model;

import dao.StableDAO;
import dao.StableDAOImpl;
import exceptions.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.*;

public class StableManager {
  private final StableDAO stableDAO;

  public StableManager() {
    this.stableDAO = new StableDAOImpl();
  }

  public void addStable(String name, int capacity) throws DuplicateStableException, InvalidDataException {
    Stable existing = stableDAO.findByName(name);
    if (existing != null) {
      throw new DuplicateStableException(name);
    }
    if (capacity <= 0) {
      throw new InvalidDataException("Capacity must be positive");
    }
    Stable stable = new Stable(name, capacity);
    stableDAO.save(stable);
  }

  public void removeStable(String name) throws StableNotFoundException {
    Stable stable = stableDAO.findByName(name);
    if (stable == null) {
      throw new StableNotFoundException(name);
    }
    stableDAO.delete(stable);
  }

  public List<Stable> findEmpty() {
    List<Stable> allStables = stableDAO.findAll();
    List<Stable> result = new ArrayList<>();
    for (Stable s : allStables) {
      if (s.getHorseList().isEmpty()) {
        result.add(s);
      }
    }
    return result;
  }

  public void summary() {
    List<Stable> allStables = stableDAO.findAll();
    for (Stable s : allStables) {
      System.out.printf("Stable '%s': %.1f%%%n", s.getStableName(), s.occupancyPercent());
    }
  }

  public Stable getStable(String name) throws StableNotFoundException {
    Stable stable = stableDAO.findByName(name);
    if (stable == null) {
      throw new StableNotFoundException(name);
    }
    return stable;
  }

  public List<Stable> getAllStables() {
    return stableDAO.findAll();
  }

  public List<Stable> sortStablesByLoad() {
    List<Stable> sorted = stableDAO.findAll();
    sorted.sort(Comparator.comparingDouble(Stable::occupancyPercent).reversed());
    return sorted;
  }
}
