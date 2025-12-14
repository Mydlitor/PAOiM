package model;

import exceptions.*;
import java.util.*;
import java.util.stream.Collectors;

public class Stable {
  private final String stableName;
  private final List<Horse> horseList;
  private final int maxCapacity;

  public Stable(String stableName, int maxCapacity) {
    this.stableName = Objects.requireNonNull(stableName);
    this.maxCapacity = maxCapacity;
    this.horseList = new ArrayList<>();
  }

  public String getStableName() { return stableName; }
  public int getMaxCapacity() { return maxCapacity; }
  public List<Horse> getHorseList() { return Collections.unmodifiableList(horseList); }

  public void addHorse(Horse horse) throws DuplicateHorseException, StableCapacityException {
    if (horseList.contains(horse)) {
      throw new DuplicateHorseException(horse.getName(), stableName);
    }
    if (horseList.size() >= maxCapacity) {
      throw new StableCapacityException(stableName, maxCapacity);
    }
    horseList.add(horse);
  }

  public void removeHorse(Horse horse) throws HorseNotFoundException {
    if (!horseList.remove(horse)) {
      throw new HorseNotFoundException(horse.getName());
    }
  }

  public void sickHorse(Horse horse) throws HorseNotFoundException {
    int idx = horseList.indexOf(horse);
    if (idx == -1) {
      throw new HorseNotFoundException(horse.getName());
    }
    horseList.get(idx).setCondition(HorseCondition.SICK);
    horseList.remove(idx);
  }

  public void changeCondition(Horse horse, HorseCondition condition) throws HorseNotFoundException {
    int idx = horseList.indexOf(horse);
    if (idx == -1) {
      throw new HorseNotFoundException(horse.getName());
    }
    horseList.get(idx).setCondition(condition);
  }

  public void changeWeight(Horse horse, double kg) throws HorseNotFoundException {
    int idx = horseList.indexOf(horse);
    if (idx == -1) {
      throw new HorseNotFoundException(horse.getName());
    }
    horseList.get(idx).setWeightKg(kg);
  }

  public long countByStatus(HorseCondition condition) {
    return horseList.stream().filter(h -> h.getCondition() == condition).count();
  }

  public List<Horse> sortByName() {
    List<Horse> copy = new ArrayList<>(horseList);
    copy.sort(Horse.BY_NAME);
    return copy;
  }

  public List<Horse> sortByPrice() {
    List<Horse> copy = new ArrayList<>(horseList);
    copy.sort(Horse.BY_PRICE);
    return copy;
  }

  public Horse search(String name) {
    return horseList.stream().filter(h -> h.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
  }

  public List<Horse> searchPartial(String frag) {
    String low = frag.toLowerCase();
    return horseList.stream().filter(h -> h.getName().toLowerCase().contains(low) || h.getBreed().toLowerCase().contains(low)).collect(Collectors.toList());
  }

  public List<Horse> getQuarantinedHorses() {
    return horseList.stream().filter(h -> h.getCondition().equals(HorseCondition.QUARANTINE)).toList();
  }

  public void summary() {
    System.out.printf("Summary for '%s' (%d/%d):%n", stableName, horseList.size(), maxCapacity);
    for (Horse h : horseList) h.print();
  }

  public Horse max() {
    return horseList.isEmpty() ? null : Collections.max(horseList, Horse.BY_PRICE);
  }

  public double occupancyPercent() {
    return (100.0 * horseList.size()) / maxCapacity;
  }
}
