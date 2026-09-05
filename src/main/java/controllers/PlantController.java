package controllers;

import model.entity.PlantInstance;
import model.entity.Recommendation;
import model.entity.Telemetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.PlantService;
import service.RecommendationService;
import service.TelemetryService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

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
    public ResponseEntity<PlantInstance> getPlantById(@PathVariable Long id) {
        return ResponseEntity.ok(plantService.getPlantById(id));
    }

    @GetMapping("/{id}/telemetry/latest")
    public ResponseEntity<Telemetry> getLatestTelemetry(@PathVariable Long id) {
        return telemetryService.getLatestByPlant(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/recommendations")
    public ResponseEntity<List<Recommendation>> getRecommendations(@PathVariable Long id) {
        return ResponseEntity.ok(recommendationService.getUnresByPlant(id));
    }

    @PostMapping("/{id}/water")
    public ResponseEntity<?> waterPlant(@PathVariable Long id) {
        plantService.updateLastWatered(id, LocalDateTime.now());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/recommendations/{recId}/resolve")
    public ResponseEntity<?> resolveRecommendation(
            @PathVariable Long id,
            @PathVariable Long recId) {
        recommendationService.resolve(recId);
        return ResponseEntity.ok().build();
    }
}