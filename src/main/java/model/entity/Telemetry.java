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


    public void setIdTelemetry(Long idTelemetry) {
        this.idTelemetry = idTelemetry;
    }

    public void setHumidityAir(Integer humidityAir) {
        this.humidityAir = humidityAir;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public void setLightLux(Integer lightLux) {
        this.lightLux = lightLux;
    }

    public void setSoilMoisture(Integer soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public void setEc(BigDecimal ec) {
        this.ec = ec;
    }

    public void setPlant(PlantInstance plant) {
        this.plant = plant;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}