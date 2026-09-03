package controllers;

import model.entity.Telemetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.TelemetryService;

import java.util.List;

@RestController
@RequestMapping("/api/telemetry")
public class TelemetryController {

    @Autowired
    private TelemetryService telemetryService;

    @GetMapping("/plant/{plantId}/latest")
    public ResponseEntity<Telemetry> getLatest(@PathVariable Long plantId) {
        return telemetryService.getLatestByPlant(plantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/plant/{plantId}/history")
    public ResponseEntity<List<Telemetry>> getHistory(
            @PathVariable Long plantId,
            @RequestParam(defaultValue = "24") int hours) {
        return ResponseEntity.ok(telemetryService.getHistory(plantId, hours));
    }

    @GetMapping("/plant/{plantId}/averages")
    public ResponseEntity<Object[]> getAverages(
            @PathVariable Long plantId,
            @RequestParam(defaultValue = "24") int hours) {
        return ResponseEntity.ok(telemetryService.getAveragesSince(plantId, hours));
    }
}
