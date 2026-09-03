package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.EventService;
import service.PlantService;
import service.RecommendationService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private PlantService plantService;

    @Autowired
    private EventService eventService;

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();

        long totalPlants = plantService.countActive();
        long healthyPlants = plantService.countByHealthStatus("HEALTHY");
        long plantsNeedingAttention = plantService.countByState(1);
        long pendingRecommendations = recommendationService.countUnresolved();

        summary.put("totalPlants", totalPlants);
        summary.put("healthyPlants", healthyPlants);
        summary.put("plantsNeedingAttention", plantsNeedingAttention);
        summary.put("pendingRecommendations", pendingRecommendations);

        summary.put("recentEvents", eventService.getRecent(10));
        summary.put("recommendations", recommendationService.getUnresolved(5));

        return ResponseEntity.ok(summary);
    }
}
