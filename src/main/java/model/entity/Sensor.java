package model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sensors")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_plant")
    private PlantInstance plant;

    private String type; // TEMPERATURE, HUMIDITY, SOIL_MOISTURE, LIGHT
    private String label;
    private Boolean active = true;

    public Sensor() {}

    public Sensor(PlantInstance plant, String type, String label) {
        this.plant = plant;
        this.type = type;
        this.label = label;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PlantInstance getPlant() { return plant; }
    public void setPlant(PlantInstance plant) { this.plant = plant; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Long getIdSensor() { return id; }
    public Boolean getIsActive() { return active; }
}