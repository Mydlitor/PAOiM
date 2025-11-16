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

  public void addHorse(Horse horse) {
    if (horseList.contains(horse)) {
      System.out.printf("Horse already exists in '%s': %s%n", stableName, horse.getName());
      return;
    }
    if (horseList.size() >= maxCapacity) {
      System.err.printf("Cannot add horse '%s' — capacity exceeded in '%s'.%n", horse.getName(), stableName);
      return;
    }
    horseList.add(horse);
  }

  public boolean removeHorse(Horse horse) { return horseList.remove(horse); }

  public boolean sickHorse(Horse horse) {
    int idx = horseList.indexOf(horse);
    if (idx == -1) return false;
    horseList.get(idx).setCondition(HorseCondition.SICK);
    horseList.remove(idx);
    return true;
  }

  public boolean changeCondition(Horse horse, HorseCondition condition) {
    int idx = horseList.indexOf(horse);
    if (idx == -1) return false;
    horseList.get(idx).setCondition(condition);
    return true;
  }

  public boolean changeWeight(Horse horse, double kg) {
    int idx = horseList.indexOf(horse);
    if (idx == -1) return false;
    horseList.get(idx).setWeightKg(kg);
    return true;
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
