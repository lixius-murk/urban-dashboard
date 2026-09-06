package model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "plant_species")
public class PlantSpecies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal tempMin = BigDecimal.valueOf(18);
    private BigDecimal tempMax = BigDecimal.valueOf(30);
    private Integer soilMoistureMin = 40;
    private Integer soilMoistureMax = 80;
    private Integer lightMin = 1000;
    private Integer recommendedPotSize = 15;
    private Integer humMin = 40;
    private Integer humMax = 80;

    public PlantSpecies() {}

    public PlantSpecies(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getTempMin() { return tempMin; }
    public void setTempMin(BigDecimal tempMin) { this.tempMin = tempMin; }

    public BigDecimal getTempMax() { return tempMax; }
    public void setTempMax(BigDecimal tempMax) { this.tempMax = tempMax; }

    public Integer getSoilMoistureMin() { return soilMoistureMin; }
    public void setSoilMoistureMin(Integer soilMoistureMin) { this.soilMoistureMin = soilMoistureMin; }

    public Integer getSoilMoistureMax() { return soilMoistureMax; }
    public void setSoilMoistureMax(Integer soilMoistureMax) { this.soilMoistureMax = soilMoistureMax; }

    public Integer getLightMin() { return lightMin; }
    public void setLightMin(Integer lightMin) { this.lightMin = lightMin; }

    public Integer getRecommendedPotSize() { return recommendedPotSize; }
    public void setRecommendedPotSize(Integer recommendedPotSize) { this.recommendedPotSize = recommendedPotSize; }

    public Integer getHumMin() { return humMin; }
    public void setHumMin(Integer humMin) { this.humMin = humMin; }

    public Integer getHumMax() { return humMax; }
    public void setHumMax(Integer humMax) { this.humMax = humMax; }

    public Long getIdSpecies() { return id; }
}