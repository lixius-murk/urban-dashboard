package model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    private Boolean isActive = true;
    private String healthStatus = "HEALTHY";
    private LocalDateTime lastCheckAt;
    private LocalDateTime lastWateredAt;

    @OneToMany(mappedBy = "plant")
    private List<Sensor> sensors;

    public LocalDateTime getPlantedAt() {
        return plantedAt;
    }

    public void setPlantedAt(LocalDateTime plantedAt) {
        this.plantedAt = plantedAt;
    }

    public PlantSpecies getSpecies() {
        return species;
    }

    public void setSpecies(PlantSpecies species) {
        this.species = species;
    }

    public BigDecimal getCurrentHeightCm() {
        return currentHeightCm;
    }

    public void setCurrentHeightCm(BigDecimal currentHeightCm) {
        this.currentHeightCm = currentHeightCm;
    }

    public Long getIdPlant() {
        return idPlant;
    }

    public BigDecimal getCustomTempMax() {
        return customTempMax;
    }

    public void setCustomTempMax(BigDecimal customTempMax) {
        this.customTempMax = customTempMax;
    }

    public BigDecimal getCustomTempMin() {
        return customTempMin;
    }

    public void setCustomTempMin(BigDecimal customTempMin) {
        this.customTempMin = customTempMin;
    }

    public Integer getCustomHumMin() {
        return customHumMin;
    }

    public void setCustomHumMin(Integer customHumMin) {
        this.customHumMin = customHumMin;
    }

    public Integer getCustomHumMax() {
        return customHumMax;
    }

    public void setCustomHumMax(Integer customHumMax) {
        this.customHumMax = customHumMax;
    }

    public Integer getCustomSoilMoistureMin() {
        return customSoilMoistureMin;
    }

    public void setCustomSoilMoistureMin(Integer customSoilMoistureMin) {
        this.customSoilMoistureMin = customSoilMoistureMin;
    }

    public Integer getCustomSoilMoistureMax() {
        return customSoilMoistureMax;
    }

    public void setCustomSoilMoistureMax(Integer customSoilMoistureMax) {
        this.customSoilMoistureMax = customSoilMoistureMax;
    }

    public Integer getCustomLightMin() {
        return customLightMin;
    }

    public void setCustomLightMin(Integer customLightMin) {
        this.customLightMin = customLightMin;
    }

    public Integer getCurrentPotSizeCm() {
        return currentPotSizeCm;
    }

    public void setCurrentPotSizeCm(Integer currentPotSizeCm) {
        this.currentPotSizeCm = currentPotSizeCm;
    }

    public Integer getCurrentState() {
        return currentState;
    }

    public void setCurrentState(Integer currentState) {
        this.currentState = currentState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public LocalDateTime getLastCheckAt() {
        return lastCheckAt;
    }

    public LocalDateTime getLastWateredAt() {
        return lastWateredAt;
    }

    public void setLastWateredAt(LocalDateTime lastWateredAt) {
        this.lastWateredAt = lastWateredAt;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }
}
