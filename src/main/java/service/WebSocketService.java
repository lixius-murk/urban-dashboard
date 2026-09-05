package service;

import model.entity.Event;
import model.entity.PlantInstance;
import model.entity.Recommendation;
import model.entity.Telemetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    // required = false: SimpMessagingTemplate only exists as a bean if
    // spring-boot-starter-websocket + @EnableWebSocketMessageBroker are on
    // the classpath. Without it, this stays null and pushes are skipped
    // instead of breaking autowiring for every bean that depends on us
    // (EventService, RecommendationService, ...).
    @Autowired(required = false)
    private SimpMessagingTemplate messagingTemplate;

    public void sendRecommendation(PlantInstance plant, Recommendation recommendation) {
        if (plant == null || messagingTemplate == null) return;
        messagingTemplate.convertAndSend(
                "/topic/plants/" + plant.getIdPlant() + "/recommendations",
                recommendation
        );
    }

    public void sendEvent(Event event) {
        if (event == null || event.getPlantId() == null || messagingTemplate == null) return;
        messagingTemplate.convertAndSend(
                "/topic/plants/" + event.getPlantId() + "/events",
                event
        );
    }

    public void sendTelemetry(Telemetry telemetry) {
        if (telemetry == null || telemetry.getPlant() == null || messagingTemplate == null) return;
        messagingTemplate.convertAndSend(
                "/topic/plants/" + telemetry.getPlant().getIdPlant() + "/telemetry",
                telemetry
        );
    }
}