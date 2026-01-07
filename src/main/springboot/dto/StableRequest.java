package springboot.dto;

public class StableRequest {
    private String stableName;
    private int maxCapacity;

    public StableRequest() {}

    public StableRequest(String stableName, int maxCapacity) {
        this.stableName = stableName;
        this.maxCapacity = maxCapacity;
    }

    public String getStableName() { return stableName; }
    public void setStableName(String stableName) { this.stableName = stableName; }

    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }
}
