package model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "telemetry")
public class Telemetry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTelemetry;

    @ManyToOne
    @JoinColumn(name = "id_plant")
    private PlantInstance plant;

    @ManyToOne
    @JoinColumn(name = "id_sensor")
    private Sensor sensor;

    private BigDecimal temperature;
    private Integer humidityAir;
    private Integer soilMoisture;
    private BigDecimal ec;
    private Integer lightLux;

    private LocalDateTime timestamp;
    private String source;  // SIMULATOR, GATEWAY


    public Long getIdTelemetry() {
        return idTelemetry;
    }

    public void setIdTelemetry(Long idTelemetry) {
        this.idTelemetry = idTelemetry;
    }

    public PlantInstance getPlant() {
        return plant;
    }

    public void setPlant(PlantInstance plant) {
        this.plant = plant;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public Integer getHumidityAir() {
        return humidityAir;
    }

    public void setHumidityAir(Integer humidityAir) {
        this.humidityAir = humidityAir;
    }

    public Integer getSoilMoisture() {
        return soilMoisture;
    }

    public void setSoilMoisture(Integer soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public BigDecimal getEc() {
        return ec;
    }

    public void setEc(BigDecimal ec) {
        this.ec = ec;
    }

    public Integer getLightLux() {
        return lightLux;
    }

    public void setLightLux(Integer lightLux) {
        this.lightLux = lightLux;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
