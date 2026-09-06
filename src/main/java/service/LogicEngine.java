package service;

import model.entity.Command;
import model.entity.Event;
import model.entity.PlantInstance;
import model.entity.Recommendation;
import model.entity.RecommendationMsg;
import model.entity.Telemetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LogicEngine {

    @Autowired
    private EventService eventService;

    @Autowired
    private CommandService commandService;

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private MockDeviceGateway deviceGateway;

    @Autowired
    private PlantService plantService;

    @Autowired
    private TelemetryService telemetryService;

    public void evaluate(Telemetry telemetry, PlantInstance plant) {
        List<Event> triggeredEvents = new ArrayList<>();

        if (telemetry.getSoilMoisture() != null) {
            int minMoisture = getEffectiveSoilMoistureMin(plant);

            if (telemetry.getSoilMoisture() < minMoisture) {
                Event event = createEvent(plant, "WATERING",
                        "Низкая влажность почвы: " + telemetry.getSoilMoisture() + "%");
                triggeredEvents.add(event);
            }
        }

        if (telemetry.getTemp() != null) {
            BigDecimal minTemp = getEffectiveTempMin(plant);

            if (telemetry.getTemp().compareTo(minTemp) < 0) {
                Event event = createEvent(plant, "HEATING",
                        "Низкая температура: " + telemetry.getTemp() + "°C");
                triggeredEvents.add(event);
            }
        }

        if (telemetry.getLight() != null) {
            int minLight = getEffectiveLightMin(plant);

            if (telemetry.getLight() < minLight) {
                Event event = createEvent(plant, "LIGHT_CONTROL",
                        "Недостаточно света: " + telemetry.getLight() + " lux");
                triggeredEvents.add(event);
            }
        }

        List<Event> savedEvents = eventService.saveAll(triggeredEvents);
        for (Event event : savedEvents) {
            dispatchCommandFor(plant, event);
        }

        Integer newState = savedEvents.isEmpty() ? 0 : 1;
        plant.setState(newState);
        plantService.updateState(plant.getId(), newState);
    }

    private void dispatchCommandFor(PlantInstance plant, Event event) {
        Command command;
        switch (event.getType()) {
            case "WATERING":
                command = commandService.createCommand(plant, event, "WATERING");
                break;
            case "HEATING":
                command = commandService.createCommand(plant, event, "HEATING");
                break;
            case "LIGHT_CONTROL":
                command = commandService.createCommand(plant, event, "CURTAINS_OPEN");
                break;
            default:
                return;
        }
        commandService.sendCommand(command);
        deviceGateway.sendCommand(command);
        eventService.markCommandSent(event.getId());
    }

    private Event createEvent(PlantInstance plant, String type, String action) {
        Event event = new Event();
        event.setPlantId(plant.getId());
        event.setType(type);
        event.setAction(action);
        event.setTime(LocalDateTime.now());
        event.setStatus(0); // PENDING
        return event;
    }

    private int getEffectiveSoilMoistureMin(PlantInstance plant) {
        return plant.getSoilMoistureMin() != null
                ? plant.getSoilMoistureMin()
                : plant.getSpecies().getSoilMoistureMin();
    }

    private BigDecimal getEffectiveTempMin(PlantInstance plant) {
        return plant.getTempMin() != null
                ? plant.getTempMin()
                : plant.getSpecies().getTempMin();
    }

    private int getEffectiveLightMin(PlantInstance plant) {
        return plant.getLightMin() != null
                ? plant.getLightMin()
                : plant.getSpecies().getLightMin();
    }

    @Scheduled(cron = "0 0 12 * * *")
    public void checkGrowth() {
        List<PlantInstance> plants = plantService.getAllActive();
        for (PlantInstance plant : plants) {
            Telemetry latestTelemetry = telemetryService.getLatestByPlant(plant.getId()).orElse(null);
            if (latestTelemetry == null || latestTelemetry.getSoilMoisture() == null) continue;
            if (plant.getHeight() == null || plant.getPotSize() == null) continue;

            BigDecimal currentHeight = plant.getHeight();
            BigDecimal potSize = BigDecimal.valueOf(plant.getPotSize());
            BigDecimal recommendedSize = plant.getSpecies().getRecommendedPotSize() != null
                    ? BigDecimal.valueOf(plant.getSpecies().getRecommendedPotSize())
                    : BigDecimal.valueOf(15);

            if (currentHeight.compareTo(potSize.multiply(BigDecimal.valueOf(0.9))) > 0) {
                Recommendation rec = new Recommendation();
                rec.setPlant(plant);
                rec.setMessage(new RecommendationMsg(String.format("Растение достигло высоты %.1f см при размере горшка %d см. " +
                                "Рекомендуется пересадка в горшок %d см.",
                        currentHeight, plant.getPotSize(), recommendedSize.intValue())));
                rec.setSeverity("INFO");
                rec.setCreatedAt(LocalDateTime.now());
                rec.setResolved(false);
                recommendationService.save(rec);
            }
        }
    }
}