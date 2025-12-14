package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ratings")
public class Rating implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int ratingValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "horse_id", nullable = false)
    private Horse horse;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date ratingDate;

    @Column(nullable = false)
    private String description;

    public Rating() {
        // Default constructor for JPA
    }

    public Rating(int ratingValue, Horse horse, Date ratingDate, String description) {
        this.ratingValue = ratingValue;
        this.horse = Objects.requireNonNull(horse);
        this.ratingDate = Objects.requireNonNull(ratingDate);
        this.description = Objects.requireNonNull(description, "Description cannot be null");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        if (ratingValue < 0 || ratingValue > 5) {
            throw new IllegalArgumentException("Rating value must be between 0 and 5");
        }
        this.ratingValue = ratingValue;
    }

    public Horse getHorse() {
        return horse;
    }

    public void setHorse(Horse horse) {
        this.horse = horse;
    }

    public Date getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(Date ratingDate) {
        this.ratingDate = ratingDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rating)) return false;
        Rating rating = (Rating) o;
        return Objects.equals(id, rating.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Rating{id=%d, value=%d, date=%s, description='%s'}",
                id, ratingValue, ratingDate, description);
    }
}
