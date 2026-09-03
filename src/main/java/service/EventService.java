package service;

import model.entity.Event;
import model.entity.PlantInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import repo.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    @Autowired(required = false)
    private EventRepository eventRepository;

    @Autowired(required = false)
    private WebSocketService webSocketService;

    public List<Event> saveAll(List<Event> events) {
        if (events == null || events.isEmpty()) {
            return events;
        }
        List<Event> saved = eventRepository.saveAll(events);
        saved.forEach(webSocketService::sendEvent);
        return saved;
    }

    public Event save(Event event) {
        Event saved = eventRepository.save(event);
        webSocketService.sendEvent(saved);
        return saved;
    }

    public List<Event> getRecent(int limit) {
        List<Event> events = eventRepository.findLast50Events();
        return events.size() > limit ? events.subList(0, limit) : events;
    }

    public List<Event> getByPlant(Long plantId) {
        return eventRepository.findByPlant_IdPlantOrderByTimestampDesc(plantId);
    }

    public Event createManualEvent(PlantInstance plant, String eventType, String action) {
        Event event = new Event();
        event.setPlant(plant);
        event.setEventType(eventType);
        event.setTriggerType("MANUAL");
        event.setPriority(1);
        event.setEventState(0); // ожидание
        event.setActionTaken(action);
        event.setTimestamp(LocalDateTime.now());
        return save(event);
    }

    public void markResolved(Long id) {
        eventRepository.updateState(id, 2, null);
    }

    public void markFailed(Long id, String error) {
        eventRepository.updateState(id, 3, error);
    }

    public void markCommandSent(Long id) {
        eventRepository.markCommandSent(id);
    }
}
