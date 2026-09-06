package model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "telemetry")
public class Telemetry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_plant")
    private PlantInstance plant;

    @ManyToOne
    @JoinColumn(name = "id_sensor")
    private Sensor sensor;

    private BigDecimal temp;
    private Integer humidity;
    private Integer soilMoisture;
    private Integer light;
    private LocalDateTime timestamp;

    private String source;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PlantInstance getPlant() { return plant; }
    public void setPlant(PlantInstance plant) { this.plant = plant; }

    public Sensor getSensor() { return sensor; }
    public void setSensor(Sensor sensor) { this.sensor = sensor; }

    public BigDecimal getTemp() { return temp; }
    public void setTemp(BigDecimal temp) { this.temp = temp; }

    public Integer getHumidity() { return humidity; }
    public void setHumidity(Integer humidity) { this.humidity = humidity; }

    public Integer getSoilMoisture() { return soilMoisture; }
    public void setSoilMoisture(Integer soilMoisture) { this.soilMoisture = soilMoisture; }

    public Integer getLight() { return light; }
    public void setLight(Integer light) { this.light = light; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public LocalDateTime getTime() { return timestamp; }
}