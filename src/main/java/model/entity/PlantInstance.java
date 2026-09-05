package model.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "plant_instances")
public class PlantInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "id_species")
    private PlantSpecies species;

    private BigDecimal height;
    private Integer potSize;
    private Integer state = 0;  // 0-healthy, 1-needs attention

    //all custom pars
    private BigDecimal tempMin;
    private Integer soilMoistureMin;
    private Integer lightMin;

    private LocalDateTime lastWatered;
    private LocalDateTime lastChecked;

    @OneToMany(mappedBy = "plant")
    @JsonIgnore
    private List<Sensor> sensors;

    public PlantInstance() {}

    public PlantInstance(String name, PlantSpecies species) {
        this.name = name;
        this.species = species;
        this.state = 0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public PlantSpecies getSpecies() { return species; }
    public void setSpecies(PlantSpecies species) { this.species = species; }

    public BigDecimal getHeight() { return height; }
    public void setHeight(BigDecimal height) { this.height = height; }

    public Integer getPotSize() { return potSize; }
    public void setPotSize(Integer potSize) { this.potSize = potSize; }

    public Integer getState() { return state; }
    public void setState(Integer state) { this.state = state; }

    public BigDecimal getTempMin() { return tempMin; }
    public void setTempMin(BigDecimal tempMin) { this.tempMin = tempMin; }

    public Integer getSoilMoistureMin() { return soilMoistureMin; }
    public void setSoilMoistureMin(Integer soilMoistureMin) { this.soilMoistureMin = soilMoistureMin; }

    public Integer getLightMin() { return lightMin; }
    public void setLightMin(Integer lightMin) { this.lightMin = lightMin; }

    public LocalDateTime getLastWatered() { return lastWatered; }
    public void setLastWatered(LocalDateTime lastWatered) { this.lastWatered = lastWatered; }

    public LocalDateTime getLastChecked() { return lastChecked; }
    public void setLastChecked(LocalDateTime lastChecked) { this.lastChecked = lastChecked; }

    public List<Sensor> getSensors() { return sensors; }
    public void setSensors(List<Sensor> sensors) { this.sensors = sensors; }

    public String getHealth() {
        return state == 0 ? "HEALTHY" : "NEEDS_ATTENTION";
    }

    public Long getIdPlant() { return id; }
}