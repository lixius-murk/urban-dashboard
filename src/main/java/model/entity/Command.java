package model.entity;

import jakarta.persistence.*;
import org.json.JSONObject; // requires org.json:json dependency
import java.time.LocalDateTime;

@Entity
@Table(name = "commands")
public class Command {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCommand;

    @ManyToOne @JoinColumn(name = "id_plant")
    private PlantInstance plant;

    @ManyToOne @JoinColumn(name = "id_event")
    private Event event;

    private String commandType;

    @Column(columnDefinition = "TEXT")
    private String payload; // stored as JSON string, see note below

    private String status; // PENDING, SENT, ACKNOWLEDGED, FAILED, CANCELLED
    private Integer retryCount = 0;
    private Integer maxRetries = 3;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private LocalDateTime acknowledgedAt;
    private LocalDateTime completedAt;

    public Long getIdCommand() { return idCommand; }
    public PlantInstance getPlant() { return plant; }
    public void setPlant(PlantInstance plant) { this.plant = plant; }
    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }
    public String getCommandType() { return commandType; }
    public void setCommandType(String commandType) { this.commandType = commandType; }
    public String getPayload() { return payload; }
    public void setPayload(JSONObject payload) { this.payload = payload.toString(); }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }
    public Integer getMaxRetries() { return maxRetries; }
    public void setMaxRetries(Integer maxRetries) { this.maxRetries = maxRetries; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getSentAt() { return sentAt; }
    public LocalDateTime getAcknowledgedAt() { return acknowledgedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
}