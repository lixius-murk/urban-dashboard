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
    private String description;

    private BigDecimal tempMin = BigDecimal.valueOf(18);
    private Integer soilMoistureMin = 40;
    private Integer lightMin = 1000;
    private Integer recommendedPotSize = 15;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getTempMin() { return tempMin; }
    public void setTempMin(BigDecimal tempMin) { this.tempMin = tempMin; }

    public Integer getSoilMoistureMin() { return soilMoistureMin; }
    public void setSoilMoistureMin(Integer soilMoistureMin) { this.soilMoistureMin = soilMoistureMin; }

    public Integer getLightMin() { return lightMin; }
    public void setLightMin(Integer lightMin) { this.lightMin = lightMin; }

    public Integer getRecommendedPotSize() { return recommendedPotSize; }
    public void setRecommendedPotSize(Integer recommendedPotSize) { this.recommendedPotSize = recommendedPotSize; }
}