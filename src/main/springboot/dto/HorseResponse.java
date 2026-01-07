package springboot.dto;

import model.Horse;
import model.HorseCondition;
import model.HorseType;

public class HorseResponse {
    private Long id;
    private String name;
    private String breed;
    private HorseType type;
    private HorseCondition condition;
    private int age;
    private double price;
    private double weightKg;
    private Long stableId;
    private String stableName;

    public HorseResponse() {}

    public HorseResponse(Horse horse) {
        this.id = horse.getId();
        this.name = horse.getName();
        this.breed = horse.getBreed();
        this.type = horse.getType();
        this.condition = horse.getCondition();
        this.age = horse.getAge();
        this.price = horse.getPrice();
        this.weightKg = horse.getWeightKg();
        if (horse.getStable() != null) {
            this.stableId = horse.getStable().getId();
            this.stableName = horse.getStable().getStableName();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public String getStableName() { return stableName; }
    public void setStableName(String stableName) { this.stableName = stableName; }
}
