package springboot.dto;

import model.Stable;

public class StableResponse {
    private Long id;
    private String stableName;
    private int maxCapacity;
    private int currentOccupancy;

    public StableResponse() {}

    public StableResponse(Stable stable) {
        this.id = stable.getId();
        this.stableName = stable.getStableName();
        this.maxCapacity = stable.getMaxCapacity();
        this.currentOccupancy = stable.getHorseList().size();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStableName() { return stableName; }
    public void setStableName(String stableName) { this.stableName = stableName; }

    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public int getCurrentOccupancy() { return currentOccupancy; }
    public void setCurrentOccupancy(int currentOccupancy) { this.currentOccupancy = currentOccupancy; }
}
