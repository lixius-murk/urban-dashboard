package controllers;

import model.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/recent")
    public ResponseEntity<List<Event>> getRecent(@RequestParam(defaultValue = "50") int limit) {
        return ResponseEntity.ok(eventService.getRecent(limit));
    }

    @GetMapping("/plant/{plantId}")
    public ResponseEntity<List<Event>> getByPlant(@PathVariable Long plantId) {
        return ResponseEntity.ok(eventService.getByPlant(plantId));
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<Void> resolve(@PathVariable Long id) {
        eventService.markResolved(id);
        return ResponseEntity.ok().build();
    }
}
