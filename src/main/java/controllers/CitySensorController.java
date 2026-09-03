package controllers;

import model.CitySensorReading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.SensorService;

import java.util.List;

@RestController
@RequestMapping("/api/sensors")
public class CitySensorController {

    @Autowired
    private SensorService sensorService;

    @GetMapping
    public ResponseEntity<List<CitySensorReading>> getCitySensors() {
        return ResponseEntity.ok(sensorService.generateCityReadings());
    }
}
