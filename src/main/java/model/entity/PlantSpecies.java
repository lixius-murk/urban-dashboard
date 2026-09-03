package model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "plant_species")
public class PlantSpecies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSpecies;

    private String name;

    private BigDecimal tempMin;
    private BigDecimal tempMax;
    private Integer humMin;
    private Integer humMax;
    private Integer soilMoistureMin;
    private Integer soilMoistureMax;
    private Integer lightMin;
    private Integer recommendedPotSizeCm;
    private Boolean isActive = true;

    @OneToMany(mappedBy = "species")
    private List<PlantInstance> instances;

    public Long getIdSpecies() {
        return idSpecies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTempMin() {
        return tempMin;
    }

    public void setTempMin(BigDecimal tempMin) {
        this.tempMin = tempMin;
    }

    public BigDecimal getTempMax() {
        return tempMax;
    }

    public void setTempMax(BigDecimal tempMax) {
        this.tempMax = tempMax;
    }

    public Integer getHumMin() {
        return humMin;
    }

    public void setHumMin(Integer humMin) {
        this.humMin = humMin;
    }

    public Integer getHumMax() {
        return humMax;
    }

    public void setHumMax(Integer humMax) {
        this.humMax = humMax;
    }

    public Integer getSoilMoistureMin() {
        return soilMoistureMin;
    }

    public void setSoilMoistureMin(Integer soilMoistureMin) {
        this.soilMoistureMin = soilMoistureMin;
    }

    public Integer getSoilMoistureMax() {
        return soilMoistureMax;
    }

    public void setSoilMoistureMax(Integer soilMoistureMax) {
        this.soilMoistureMax = soilMoistureMax;
    }

    public Integer getLightMin() {
        return lightMin;
    }

    public void setLightMin(Integer lightMin) {
        this.lightMin = lightMin;
    }

    public Integer getRecommendedPotSizeCm() {
        return recommendedPotSizeCm;
    }

    public void setRecommendedPotSizeCm(Integer recommendedPotSizeCm) {
        this.recommendedPotSizeCm = recommendedPotSizeCm;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<PlantInstance> getInstances() {
        return instances;
    }
}
