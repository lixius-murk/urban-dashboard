package service;

import model.entity.Event;
import model.entity.PlantInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repo.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getRecent(int limit) {
        return eventRepository.findLast20Events();
    }

    public List<Event> getByPlantId(Long plantId) {
        return eventRepository.findByPlantId(plantId);
    }

    public Event logEvent(String type, String action, Long plantId) {
        Event event = new Event();
        event.setType(type);
        event.setAction(action);
        event.setPlantId(plantId);
        event.setTime(LocalDateTime.now());
        event.setStatus(0); // PENDING
        return eventRepository.save(event);
    }

    public List<Event> saveAll(List<Event> events) {
        return eventRepository.saveAll(events);
    }

    public void markCommandSent(Long eventId) {
        eventRepository.markCommandSent(eventId);
    }

    public void markResolved(Long eventId) {
        eventRepository.updateState(eventId, 2); // 2 = COMPLETED
    }
}