package model;

import javax.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

@Entity
@Table(name = "horses")
public class Horse implements Comparable<Horse>, Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String breed;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private HorseType type;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private HorseCondition condition;

  @Column(nullable = false)
  private int age;

  @Column(nullable = false)
  private double price;

  @Column(nullable = false)
  private double weightKg;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "stable_id")
  @Cascade(CascadeType.SAVE_UPDATE)
  private Stable stable;

  public Horse() {
    // Default constructor for JPA
  }

  public Horse(String name, String breed, HorseType type, HorseCondition condition, int age, double price, double weightKg) {
    this.name = Objects.requireNonNull(name);
    this.breed = Objects.requireNonNull(breed);
    this.type = Objects.requireNonNull(type);
    this.condition = Objects.requireNonNull(condition);
    this.age = age;
    this.price = price;
    this.weightKg = weightKg;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  
  public Stable getStable() { return stable; }
  public void setStable(Stable stable) { this.stable = stable; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  
  public String getBreed() { return breed; }
  public void setBreed(String breed) { this.breed = breed; }
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
