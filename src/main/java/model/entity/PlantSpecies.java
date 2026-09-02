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
    private Integer lightMin;
    private Integer soilMoistureMin;
    private Integer soilMoistureMax;
    private Integer recommendedPotSizeCm;
    private Boolean isActive = true;

    @OneToMany(mappedBy = "species")
    private List<PlantInstance> instances;

    public Long getIdSpecies() { return idSpecies; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getTempMin() { return tempMin; }
    public void setTempMin(BigDecimal tempMin) { this.tempMin = tempMin; }
    public BigDecimal getTempMax() { return tempMax; }
    public void setTempMax(BigDecimal tempMax) { this.tempMax = tempMax; }
    public Integer getLightMin() { return lightMin; }
    public void setLightMin(Integer lightMin) { this.lightMin = lightMin; }
    public Integer getSoilMoistureMin() { return soilMoistureMin; }
    public void setSoilMoistureMin(Integer v) { this.soilMoistureMin = v; }
    public Integer getSoilMoistureMax() { return soilMoistureMax; }
    public void setSoilMoistureMax(Integer v) { this.soilMoistureMax = v; }
    public Integer getRecommendedPotSizeCm() { return recommendedPotSizeCm; }
    public void setRecommendedPotSizeCm(Integer v) { this.recommendedPotSizeCm = v; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public List<PlantInstance> getInstances() { return instances; }
}