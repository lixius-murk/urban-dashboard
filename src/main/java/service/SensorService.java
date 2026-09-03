package service;

import model.CitySensorReading;
import model.entity.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repo.SensorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class SensorService {

    private final Random random = new Random();

    @Autowired(required = false)
    private SensorRepository sensorRepository;

    // ===== Plant sensors (real IoT devices attached to a PlantInstance) =====

    public List<Sensor> getSensorsByPlant(Long plantId) {
        return sensorRepository.findByPlant_IdPlantAndIsActiveTrue(plantId);
    }

    // ===== City dashboard mock readings, used by /api/sensors + dashboard.js =====
    // NOTE: this used to reuse the DataSimulator class name/constructor, which
    // collided with the unrelated plant-telemetry DataSimulator. Split into its
    // own small model (CitySensorReading) instead of overloading DataSimulator.

    public List<CitySensorReading> generateCityReadings() {
        List<CitySensorReading> sensors = new ArrayList<>();

        sensors.add(new CitySensorReading(
                "manhole",
                56.326093, 44.016003,
                random.nextBoolean() ? "OPEN" : "SAFE",
                random.nextInt(100)
        ));

        sensors.add(new CitySensorReading(
                "noise",
                56.325349, 44.006651,
                "LOUD",
                40 + random.nextInt(50)
        ));

        sensors.add(new CitySensorReading(
                "bus",
                56.325170, 44.007487,
                "CROWDED",
                70 + random.nextInt(30)
        ));

        return sensors;
    }
}
