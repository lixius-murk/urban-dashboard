package model.entity;
import jakarta.persistence.*;

import javax.xml.crypto.Data;
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
    private Data plantedAt;
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

    private Data lastWateredAt;

    public Data getPlantedAt() {
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

}
