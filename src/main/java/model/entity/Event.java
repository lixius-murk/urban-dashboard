package model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvent;

    @ManyToOne @JoinColumn(name = "id_plant")
    private PlantInstance plant;

    @ManyToOne @JoinColumn(name = "id_telemetry")
    private Telemetry telemetry;

    private String eventType;
    private String triggerType;
    private Integer priority;
    private Integer eventState; // 0-ожидание,1-обработка,2-выполнено,3-ошибка
    private String actionTaken;
    private String errorMessage;
    private Boolean commandSent = false;
    private Boolean commandAcked = false;
    private LocalDateTime timestamp;

    public Long getIdEvent() { return idEvent; }
    public PlantInstance getPlant() { return plant; }
    public void setPlant(PlantInstance plant) { this.plant = plant; }
    public Telemetry getTelemetry() { return telemetry; }
    public void setTelemetry(Telemetry telemetry) { this.telemetry = telemetry; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getTriggerType() { return triggerType; }
    public void setTriggerType(String triggerType) { this.triggerType = triggerType; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public Integer getEventState() { return eventState; }
    public void setEventState(Integer eventState) { this.eventState = eventState; }
    public String getActionTaken() { return actionTaken; }
    public void setActionTaken(String actionTaken) { this.actionTaken = actionTaken; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public Boolean getCommandSent() { return commandSent; }
    public void setCommandSent(Boolean commandSent) { this.commandSent = commandSent; }
    public Boolean getCommandAcked() { return commandAcked; }
    public void setCommandAcked(Boolean commandAcked) { this.commandAcked = commandAcked; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}