package model;

import java.math.BigDecimal;

public class PlantSettingsDto {

    private String name;
    private BigDecimal customTempMin;
    private BigDecimal customTempMax;
    private Integer customHumMin;
    private Integer customHumMax;
    private Integer customSoilMoistureMin;
    private Integer customSoilMoistureMax;
    private Integer customLightMin;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCustomTempMin() {
        return customTempMin;
    }

    public void setCustomTempMin(BigDecimal customTempMin) {
        this.customTempMin = customTempMin;
    }

    public BigDecimal getCustomTempMax() {
        return customTempMax;
    }

    public void setCustomTempMax(BigDecimal customTempMax) {
        this.customTempMax = customTempMax;
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
}
