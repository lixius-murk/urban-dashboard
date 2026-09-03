package model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sensor_types")
public class SensorType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSensorType;

    //TEMPERATURE, HUMIDITY_AIR, SOIL_MOISTURE, LIGHT, EC
    private String name;

    public Long getIdSensorType() {
        return idSensorType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
