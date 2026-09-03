package timetable;

import model.entity.PlantInstance;
import model.entity.Sensor;
import model.entity.Telemetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import service.LogicEngine;
import service.PlantService;
import service.SensorService;
import service.TelemetryService;
import simulator.DataSimulator;

import java.util.List;

@Component
@ConditionalOnProperty(name = "app.simulation.enabled", havingValue = "true", matchIfMissing = true)
public class DataCollection {

        @Autowired(required = false)
        private PlantService plantService;

        @Autowired(required = false)
        private SensorService sensorService;

        @Autowired
        private DataSimulator dataSimulator;


        @Autowired(required = false)
        private TelemetryService telemetryService;

        @Autowired
        private LogicEngine logicEngine;

        @Scheduled(fixedDelayString = "${app.simulation.interval-seconds:30}000")
        public void collectData() {
            List<PlantInstance> activePlants = plantService.getAllActive();

            for (PlantInstance plant : activePlants) {
                List<Sensor> sensors = sensorService.getSensorsByPlant(plant.getIdPlant());

                for (Sensor sensor : sensors) {
                    Telemetry telemetry = dataSimulator.generateTelemetry(plant, sensor);

                    telemetryService.save(telemetry);
                    logicEngine.evaluate(telemetry, plant);
                }
            }
        }
    }
