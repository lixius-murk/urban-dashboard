package model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_plant")
    private Long plantId;

    private String type; // WATERING, HEATING, LIGHT_CONTROL
    private String action;
    private LocalDateTime time;

    //0-pending, 1-completed, 2-failed
    private Integer status = 0;

    public Event() {}

    public Event(Long plantId, String type, String action) {
        this.plantId = plantId;
        this.type = type;
        this.action = action;
        this.time = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPlantId() { return plantId; }
    public void setPlantId(Long plantId) { this.plantId = plantId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}