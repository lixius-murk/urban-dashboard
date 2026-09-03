package controllers;

import model.entity.Recommendation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.RecommendationService;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/unresolved")
    public ResponseEntity<List<Recommendation>> getUnresolved(@RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(recommendationService.getUnresolved(limit));
    }

    @GetMapping("/plant/{plantId}")
    public ResponseEntity<List<Recommendation>> getByPlant(@PathVariable Long plantId) {
        return ResponseEntity.ok(recommendationService.getByPlant(plantId));
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<Void> resolve(@PathVariable Long id, @RequestBody(required = false) String feedback) {
        recommendationService.resolve(id, feedback);
        return ResponseEntity.ok().build();
    }
}
