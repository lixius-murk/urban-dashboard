package model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sensors")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSensor;

    @ManyToOne
    @JoinColumn(name = "id_plant")
    private PlantInstance plant;

    @ManyToOne
    @JoinColumn(name = "id_sensor_type")
    private SensorType sensorType;

    private String label;
    private Boolean isActive = true;

    public Long getIdSensor() {
        return idSensor;
    }

    public PlantInstance getPlant() {
        return plant;
    }

    public void setPlant(PlantInstance plant) {
        this.plant = plant;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
