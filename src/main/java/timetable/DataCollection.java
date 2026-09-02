package timetable;


import model.entity.Telemetry;
import model.entity.PlantInstance;
import service.SensorService;
import simulator.DataSimulator;
import simulator.GatewaySimulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Scheduled;
import org.springframework.stereotype.Component;
import service.LogicEngine;

import java.util.List;

@Component
public class DataCollection {

        @Autowired
        private PlantService plantService;

        @Autowired
        private SensorService sensorService;

        @Autowired
        private DataSimulator dataSimulator;

        @Autowired
        private GatewaySimulator gateway;

        @Autowired
        private TelemetryService telemetryService;

        @Autowired
        private LogicEngine logicEngine;

        // Каждые 30 секунд генерируем новые показания
        @Scheduled(fixedDelay = 30000)
        public void collectData() {
            List<PlantInstance> activePlants = plantService.getAllActive();

            for (PlantInstance plant : activePlants) {
                // Получаем все сенсоры, привязанные к растению
                List<Sensor> sensors = sensorService.getSensorsByPlant(plant.getIdPlant());

                for (Sensor sensor : sensors) {
                    Telemetry telemetry = dataSimulator.generateTelemetry(plant, sensor);

                    gateway.receiveTelemetry(telemetry);
                    telemetryService.save(telemetry);
                    logicEngine.evaluate(telemetry, plant);
                }
            }
        }
    }

