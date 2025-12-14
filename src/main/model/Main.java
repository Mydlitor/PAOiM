package model;

import exceptions.*;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    try {
      StableManager manager = new StableManager();
      manager.addStable("NorthFarm", 5);
      manager.addStable("EastBarn", 2);
      manager.addStable("Emp", 10);
      Stable north = manager.getStable("NorthFarm");
      Stable east = manager.getStable("EastBarn");
      Stable emp = manager.getStable("Emp");

      Horse h1 = new Horse("Bella", "Arabian", HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 6, 15000.0, 450.0);
      Horse h2 = new Horse("Argo", "Shire", HorseType.COLD_BLOODED, HorseCondition.TRAINING, 8, 8000.0, 700.0);
      Horse h3 = new Horse("Coco", "Thoroughbred", HorseType.HOT_BLOODED, HorseCondition.HEALTHY, 4, 22000.0, 380.0);
      Horse h4 = new Horse("Zorro", "Mustang", HorseType.HOT_BLOODED, HorseCondition.QUARANTINE, 5, 5000.0, 420.0);
      Horse h5 = new Horse("Tiny", "Pony", HorseType.COLD_BLOODED, HorseCondition.HEALTHY, 3, 1200.0, 200.0);

      north.addHorse(h1);
      north.addHorse(h2);
      north.addHorse(h3);
      north.addHorse(h4);
      north.addHorse(h5);

      north.summary();
      System.out.println("\nSorted by name:");
      for (Horse h : north.sortByName()) h.print();
      System.out.println("\nSorted by price:");
      for (Horse h : north.sortByPrice()) h.print();

      System.out.println("\nSearch 'Coco':");
      Horse found = north.search("Coco");
      if (found != null) found.print();

      System.out.println("\nSearch partial 'bel':");
      for (Horse h : north.searchPartial("bel")) h.print();

      System.out.printf("\nCount healthy: %d%n", north.countByStatus(HorseCondition.HEALTHY));
      north.changeCondition(h3, HorseCondition.TRAINING);
      north.changeWeight(h3, 390.0);
      north.sickHorse(h2);
      north.summary();

      Horse max = north.max();
      System.out.println("\nMost expensive:");
      if (max != null) max.print();

      east.addHorse(h5);
      manager.summary();

      System.out.println("\nEmpty stables:");
      for (Stable s : manager.findEmpty()) System.out.println(s.getStableName());

      north.changeCondition(h3, HorseCondition.QUARANTINE);

      System.out.println("\nsick horses: ");
      for(Horse h : north.getQuarantinedHorses() ) System.out.println(h);

    } catch (StableException e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
