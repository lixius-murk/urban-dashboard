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

    // These field names MUST match what the frontend expects
    private BigDecimal temp;          // Frontend expects 'temp'
    private Integer humidity;         // Frontend expects 'humidity'
    private Integer soilMoisture;     // Frontend expects 'soilMoisture'
    private Integer light;            // Frontend expects 'light'
    private LocalDateTime timestamp;  // Frontend expects 'time'

    private String source;

    // Getters and Setters
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

    // Add this method for frontend compatibility
    public LocalDateTime getTime() { return timestamp; }
}