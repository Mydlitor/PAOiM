package pao.model;

import java.util.Comparator;
import java.util.Objects;

public class Horse implements Comparable<Horse> {
  private final String name;
  private final String breed;
  private HorseType type;
  private HorseCondition condition;
  private int age;
  private double price;
  private double weightKg;

  public Horse(String name, String breed, HorseType type, HorseCondition condition, int age, double price, double weightKg) {
    this.name = Objects.requireNonNull(name);
    this.breed = Objects.requireNonNull(breed);
    this.type = Objects.requireNonNull(type);
    this.condition = Objects.requireNonNull(condition);
    this.age = age;
    this.price = price;
    this.weightKg = weightKg;
  }

  public String getName() { return name; }
  public String getBreed() { return breed; }
  public HorseType getType() { return type; }
  public void setType(HorseType type) { this.type = type; }
  public HorseCondition getCondition() { return condition; }
  public void setCondition(HorseCondition condition) { this.condition = condition; }
  public int getAge() { return age; }
  public void setAge(int age) { this.age = age; }
  public double getPrice() { return price; }
  public void setPrice(double price) { this.price = price; }
  public double getWeightKg() { return weightKg; }
  public void setWeightKg(double weightKg) { this.weightKg = weightKg; }

  public void print() {
    System.out.printf("Horse{name='%s', breed='%s', type=%s, condition=%s, age=%d, weight=%.1fkg, price=%.2f PLN}%n",
      name, breed, type, condition, age, weightKg, price);
  }

  @Override
  public int compareTo(Horse other) {
    int cmp = this.name.compareToIgnoreCase(other.name);
    if (cmp != 0) return cmp;
    cmp = this.breed.compareToIgnoreCase(other.breed);
    if (cmp != 0) return cmp;
    return Integer.compare(this.age, other.age);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Horse)) return false;
    Horse horse = (Horse) o;
    return age == horse.age && name.equalsIgnoreCase(horse.name) && breed.equalsIgnoreCase(horse.breed);
  }

  @Override
  public int hashCode() { return Objects.hash(name.toLowerCase(), breed.toLowerCase(), age); }

  public static final Comparator<Horse> BY_NAME = (h1, h2) -> h1.getName().compareToIgnoreCase(h2.getName());
  public static final Comparator<Horse> BY_PRICE = Comparator.comparingDouble(Horse::getPrice);
  public static final Comparator<Horse> BY_AGE = Comparator.comparingInt(Horse::getAge);
  public static final Comparator<Horse> BY_BREED = (h1, h2) -> h1.getBreed().compareToIgnoreCase(h2.getBreed());

  @Override
  public String toString() {
    return String.format("%s (%s) - %s, %d years, %.1fkg, %.2f PLN, %s",
      name, breed, type, age, weightKg, price, condition);
  }
}
