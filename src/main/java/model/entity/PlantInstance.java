package model.entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.math.BigDecimal;


@Entity
@Table(name = "plant_instances")
public class PlantInstance {
    @Id
    @GeneratedValue()
    private Long idPlant;

    @ManyToOne
    @JoinColumn(name="id_species")
    private PlantSpecies species;



    String name;
    private LocalDateTime plantedAt;
    private Boolean isActive = true;
    private String healthStatus;
    private LocalDateTime lastCheckAt;
    private BigDecimal currentHeightCm;
    private Integer currentPotSizeCm;
    private Integer currentState;  // 0 - ок, 1 - требует действия
    private BigDecimal customTempMin;
    private BigDecimal customTempMax;
    private Integer customHumMin;
    private Integer customHumMax;
    private Integer customSoilMoistureMin;
    private Integer customSoilMoistureMax;
    private Integer customLightMin;

    private LocalDateTime lastWateredAt;

    public LocalDateTime getPlantedAt() {
        return plantedAt;
    }

    public PlantSpecies getSpecies() {
        return species;
    }

    public BigDecimal getCurrentHeightCm() {
        return currentHeightCm;
    }

    public Long getIdPlant() {
        return idPlant;
    }

    public BigDecimal getCustomTempMax() {
        return customTempMax;
    }

    public BigDecimal getCustomTempMin() {
        return customTempMin;
    }

    public Integer getCurrentPotSizeCm() { return currentPotSizeCm; }
    public void setCurrentState(Integer currentState) { this.currentState = currentState; }
    public Integer getCurrentState() { return currentState; }
    public Boolean getIsActive() { return isActive; }
    public String getHealthStatus() { return healthStatus; }
    public Integer getCustomSoilMoistureMin() { return customSoilMoistureMin; }
    public Integer getCustomSoilMoistureMax() { return customSoilMoistureMax; }
    public Integer getCustomLightMin() { return customLightMin; }
    public String getName() { return name; }

}
