package controllers;


import model.entity.*;
import service.RecommendationService;
import service.SensorService;
import simulator.DataSimulator;
import picocli.CommandLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plants")
public class SensorController {

    @Autowired
    private PlantService plantService;

    @Autowired
    private TelemetryService telemetryService;

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping
    public ResponseEntity<List<PlantInstance>> getAllPlants() {
        return ResponseEntity.ok(plantService.getAllActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlantInstance> getPlant(@PathVariable Long id) {
        return plantService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PlantInstance> createPlant(@RequestBody PlantInstance plant) {
        return ResponseEntity.ok(plantService.save(plant));
    }

    @PutMapping("/{id}/settings")
    public ResponseEntity<PlantInstance> updateSettings(
            @PathVariable Long id,
            @RequestBody PlantSettingsDto settings) {
        PlantInstance plant = plantService.updateSettings(id, settings);
        return ResponseEntity.ok(plant);
    }

    @GetMapping("/{id}/telemetry/latest")
    public ResponseEntity<Telemetry> getLatestTelemetry(@PathVariable Long id) {
        return telemetryService.getLatestByPlant(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/telemetry/history")
    public ResponseEntity<List<Telemetry>> getTelemetryHistory(
            @PathVariable Long id,
            @RequestParam(defaultValue = "24") int hours) {
        return ResponseEntity.ok(
                telemetryService.getHistory(id, hours)
        );
    }

    @GetMapping("/{id}/recommendations")
    public ResponseEntity<List<Recommendation>> getRecommendations(@PathVariable Long id) {
        return ResponseEntity.ok(recommendationService.getByPlant(id));
    }

    @PostMapping("/{id}/water")
    public ResponseEntity<Command> manualWater(@PathVariable Long id) {
        PlantInstance plant = plantService.findById(id).orElseThrow();

        Event event = eventService.createManualEvent(plant, "WATERING", "Ручной полив");
        CommandLine.Command command = commandService.createManualCommand(plant, event, "WATERING");
        commandService.sendCommand(command);

        return ResponseEntity.ok(command);
    }

    @PostMapping("/{id}/recommendations/{recId}/resolve")
    public ResponseEntity<Void> resolveRecommendation(
            @PathVariable Long id,
            @PathVariable Long recId,
            @RequestBody(required = false) String feedback) {
        recommendationService.resolve(recId, feedback);
        return ResponseEntity.ok().build();
    }
}
