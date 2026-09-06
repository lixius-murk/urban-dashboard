package model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import model.entity.RecommendationMsg;

@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_plant")
    private PlantInstance plant;


    @ManyToOne
    @JoinColumn(name = "msg_id")
    private RecommendationMsg message;

    private String severity;
    private Boolean resolved = false;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;

    public Recommendation() {
        this.createdAt = LocalDateTime.now();
    }

    public Recommendation(PlantInstance plant, RecommendationMsg message, String severity) {
        this.plant = plant;
        this.message = message;
        this.severity = severity;
        this.resolved = false;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PlantInstance getPlant() { return plant; }
    public void setPlant(PlantInstance plant) { this.plant = plant; }

    public RecommendationMsg getMessage() { return message; }
    public void setMessage(RecommendationMsg message) { this.message = message; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public Boolean getResolved() { return resolved; }
    public void setResolved(Boolean resolved) { this.resolved = resolved; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }

    public Long getIdRecommendation() { return id; }
    public Long getIdPlant() { return plant != null ? plant.getId() : null; }
    public Boolean getIsResolved() { return resolved; }
}