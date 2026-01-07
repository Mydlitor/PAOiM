package springboot.dto;

public class RatingRequest {
    private Long horseId;
    private int ratingValue;
    private String description;

    public RatingRequest() {}

    public RatingRequest(Long horseId, int ratingValue, String description) {
        this.horseId = horseId;
        this.ratingValue = ratingValue;
        this.description = description;
    }

    public Long getHorseId() { return horseId; }
    public void setHorseId(Long horseId) { this.horseId = horseId; }

    public int getRatingValue() { return ratingValue; }
    public void setRatingValue(int ratingValue) { this.ratingValue = ratingValue; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
