import java.util.*;

public class StableManager {
  private final Map<String, Stable> stables = new HashMap<>();

  public void addStable(String name, int capacity) {
    if (stables.containsKey(name)) {
      System.out.printf("Stable '%s' already exists.%n", name);
      return;
    }
    stables.put(name, new Stable(name, capacity));
  }

  public boolean removeStable(String name) {
    return stables.remove(name) != null;
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

  public Stable getStable(String name) { return stables.get(name); }
}
