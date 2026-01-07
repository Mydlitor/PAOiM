package springboot.dto;

import model.HorseCondition;
import model.HorseType;

public class HorseRequest {
    private String name;
    private String breed;
    private HorseType type;
    private HorseCondition condition;
    private int age;
    private double price;
    private double weightKg;
    private Long stableId;

    public HorseRequest() {}

    public HorseRequest(String name, String breed, HorseType type, HorseCondition condition, 
                       int age, double price, double weightKg, Long stableId) {
        this.name = name;
        this.breed = breed;
        this.type = type;
        this.condition = condition;
        this.age = age;
        this.price = price;
        this.weightKg = weightKg;
        this.stableId = stableId;
    }

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

    public Long getStableId() { return stableId; }
    public void setStableId(Long stableId) { this.stableId = stableId; }
}
