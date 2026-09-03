package service;

import model.entity.Command;
import model.entity.Event;
import model.entity.PlantInstance;
import model.entity.Recommendation;
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

    @Autowired(required = false)
    private EventService eventService;

    @Autowired(required = false)
    private CommandService commandService;

    @Autowired(required = false)
    private RecommendationService recommendationService;

    @Autowired
    private MockDeviceGateway deviceGateway;

    @Autowired(required = false)
    private PlantService plantService;

    @Autowired(required = false)
    private TelemetryService telemetryService;

    public void evaluate(Telemetry telemetry, PlantInstance plant) {
        List<Event> triggeredEvents = new ArrayList<>();

        if (telemetry.getSoilMoisture() != null) {
            int minMoisture = getEffectiveSoilMoistureMin(plant);
            int maxMoisture = getEffectiveSoilMoistureMax(plant);

            if (telemetry.getSoilMoisture() < minMoisture) {
                Event event = createEvent(plant, telemetry,
                        "WATERING", "Низкая влажность почвы: " + telemetry.getSoilMoisture() + "%");
                triggeredEvents.add(event);
            }
        }

        if (telemetry.getTemperature() != null) {
            BigDecimal minTemp = getEffectiveTempMin(plant);

            if (telemetry.getTemperature().compareTo(minTemp) < 0) {
                Event event = createEvent(plant, telemetry,
                        "HEATING", "Низкая температура: " + telemetry.getTemperature() + "°C");
                triggeredEvents.add(event);
            }
        }

        if (telemetry.getLightLux() != null) {
            int minLight = getEffectiveLightMin(plant);

            if (telemetry.getLightLux() < minLight) {
                Event event = createEvent(plant, telemetry,
                        "LIGHT_CONTROL", "Недостаточно света: " + telemetry.getLightLux() + " lux");
                triggeredEvents.add(event);
            }
        }

        if (telemetry.getEc() != null && telemetry.getEc().compareTo(BigDecimal.valueOf(0.8)) < 0) {
            Recommendation rec = new Recommendation();
            rec.setPlant(plant);
            rec.setRecommendationType("FERTILIZE");
            rec.setMessage("Низкий уровень питательных веществ (EC=" + telemetry.getEc() + " мСм/см).");
            rec.setSeverity("WARNING");
            recommendationService.save(rec);
        }

        //saving the event first
        List<Event> savedEvents = eventService.saveAll(triggeredEvents);
        for (Event event : savedEvents) {
            dispatchCommandFor(plant, event);
        }

        Integer newState = savedEvents.isEmpty() ? 0 : 1; // 0 - все хорошо, 1 - требует действия
        plant.setCurrentState(newState);
        plantService.updateState(plant.getIdPlant(), newState);
    }

    private void dispatchCommandFor(PlantInstance plant, Event event) {
        Command command;
        switch (event.getEventType()) {
            case "WATERING":
                command = commandService.createCommand(plant, event, "WATERING",
                        Map.of("duration_seconds", 5, "amount_ml", 200));
                break;
            case "HEATING":
                command = commandService.createCommand(plant, event, "HEATING",
                        Map.of("duration_seconds", 10, "target_temp", getEffectiveTempMin(plant)));
                break;
            case "LIGHT_CONTROL":
                command = commandService.createCommand(plant, event, "CURTAINS_OPEN",
                        Map.of("action", "OPEN"));
                break;
            default:
                return;
        }
        commandService.sendCommand(command);
        deviceGateway.sendCommand(command);
        eventService.markCommandSent(event.getIdEvent());
    }

    private Event createEvent(PlantInstance plant, Telemetry telemetry,
                              String type, String action) {
        Event event = new Event();
        event.setPlant(plant);
        event.setTelemetry(telemetry);
        event.setEventType(type);
        event.setTriggerType("AUTO");
        event.setPriority(2);
        event.setEventState(0);  // ожидание
        event.setActionTaken(action);
        event.setTimestamp(LocalDateTime.now());
        return event;
    }


    //keeping up with datasimulator!!!
    private int getEffectiveSoilMoistureMin(PlantInstance plant) {
        return plant.getCustomSoilMoistureMin() != null
                ? plant.getCustomSoilMoistureMin()
                : plant.getSpecies().getSoilMoistureMin();
    }

    private int getEffectiveSoilMoistureMax(PlantInstance plant) {
        return plant.getCustomSoilMoistureMax() != null
                ? plant.getCustomSoilMoistureMax()
                : plant.getSpecies().getSoilMoistureMax();
    }

    private BigDecimal getEffectiveTempMin(PlantInstance plant) {
        return plant.getCustomTempMin() != null
                ? plant.getCustomTempMin()
                : plant.getSpecies().getTempMin();
    }

    private int getEffectiveLightMin(PlantInstance plant) {
        return plant.getCustomLightMin() != null
                ? plant.getCustomLightMin()
                : plant.getSpecies().getLightMin();
    }


    //checking in 12:00
    @Scheduled(cron = "0 0 12 * * *")
    public void checkGrowth() {
        List<PlantInstance> plants = plantService.getAllActive();
        for (PlantInstance plant : plants) {
            Telemetry latestTelemetry = telemetryService.getLatestByPlant(plant.getIdPlant()).orElse(null);
            if (latestTelemetry == null || latestTelemetry.getSoilMoisture() == null) continue;
            if (plant.getCurrentHeightCm() == null || plant.getCurrentPotSizeCm() == null) continue;

            BigDecimal currentHeight = plant.getCurrentHeightCm();
            BigDecimal potSize = BigDecimal.valueOf(plant.getCurrentPotSizeCm());
            BigDecimal recommendedSize = plant.getSpecies().getRecommendedPotSizeCm() != null
                    ? BigDecimal.valueOf(plant.getSpecies().getRecommendedPotSizeCm())
                    : BigDecimal.valueOf(15);

            if (currentHeight.compareTo(potSize.multiply(BigDecimal.valueOf(0.9))) > 0) {
                Recommendation rec = new Recommendation();
                rec.setPlant(plant);
                rec.setRecommendationType("REPOT");
                rec.setMessage(String.format("Растение достигло высоты %.1f см при размере горшка %d см. " +  "Рекомендуется пересадка в горшок %d см.",
                        currentHeight, plant.getCurrentPotSizeCm(), recommendedSize.intValue()
                ));
                rec.setSeverity("INFO");
                recommendationService.save(rec);
            }
        }
    }
}
