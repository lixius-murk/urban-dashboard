package service;

import  model.entity.PlantInstance;
import  model.entity.Telemetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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

    public void evaluate(Telemetry telemetry, PlantInstance plant) {
        List<Event> triggeredEvents = new ArrayList<>();

        // 1. Проверка влажности почвы
        if (telemetry.getSoilMoisture() != null) {
            int minMoisture = getEffectiveSoilMoistureMin(plant);
            int maxMoisture = getEffectiveSoilMoistureMax(plant);

            if (telemetry.getSoilMoisture() < minMoisture) {
                Event event = createEvent(plant, telemetry,
                        "WATERING", "Низкая влажность почвы: " + telemetry.getSoilMoisture() + "%");
                triggeredEvents.add(event);

                // Отправляем команду на полив
                Command command = createCommand(plant, event, "WATERING",
                        Map.of("duration_seconds", 5, "amount_ml", 200));
                commandService.sendCommand(command);
                deviceGateway.sendCommand(command);
            }
        }

        // 2. Проверка температуры
        if (telemetry.getTemperature() != null) {
            BigDecimal minTemp = getEffectiveTempMin(plant);

            if (telemetry.getTemperature().compareTo(minTemp) < 0) {
                Event event = createEvent(plant, telemetry,
                        "HEATING", "Низкая температура: " + telemetry.getTemperature() + "°C");
                triggeredEvents.add(event);

                Command command = createCommand(plant, event, "HEATING",
                        Map.of("duration_seconds", 10, "target_temp", minTemp));
                commandService.sendCommand(command);
                deviceGateway.sendCommand(command);
            }
        }

        // 3. Проверка освещения
        if (telemetry.getLightLux() != null) {
            int minLight = getEffectiveLightMin(plant);

            if (telemetry.getLightLux() < minLight) {
                Event event = createEvent(plant, telemetry,
                        "LIGHT_CONTROL", "Недостаточно света: " + telemetry.getLightLux() + " lux");
                triggeredEvents.add(event);

                Command command = createCommand(plant, event, "CURTAINS_OPEN",
                        Map.of("action", "OPEN"));
                commandService.sendCommand(command);
                deviceGateway.sendCommand(command);
            }
        }

        // 4. Проверка питания (EC)
        if (telemetry.getEc() != null && telemetry.getEc().compareTo(BigDecimal.valueOf(0.8)) < 0) {
            Recommendation rec = new Recommendation();
            rec.setPlant(plant);
            rec.setRecommendationType("FERTILIZE");
            rec.setMessage("Низкий уровень питательных веществ (EC=" + telemetry.getEc() +
                    " мСм/см). Рекомендуется внесение удобрений.");
            rec.setSeverity("WARNING");
            recommendationService.save(rec);
        }

        // 5. Проверка размеров (если есть данные)
        // Вызывается отдельно из другого места или по расписанию

        // Сохраняем все события
        eventService.saveAll(triggeredEvents);

        // Обновляем состояние растения
        if (!triggeredEvents.isEmpty()) {
            plant.setCurrentState(1);  // требует действия
            // plantService.update(plant);
        } else {
            plant.setCurrentState(0);  // все хорошо
        }
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

    private Command createCommand(PlantInstance plant, Event event,
                                  String type, Map<String, Object> payload) {
        Command command = new Command();
        command.setPlant(plant);
        command.setEvent(event);
        command.setCommandType(type);
        command.setPayload(new JSONObject(payload));
        command.setStatus("PENDING");
        command.setRetryCount(0);
        command.setMaxRetries(3);
        command.setCreatedAt(LocalDateTime.now());
        return command;
    }

    // Метод для периодической проверки роста
    @Scheduled(cron = "0 0 12 * * *")  // каждый день в 12:00
    public void checkGrowth() {
        List<PlantInstance> plants = plantService.getAllActive();
        for (PlantInstance plant : plants) {
            // Получаем последние показания роста
            Telemetry latestTelemetry = telemetryService.getLatestByPlant(plant.getIdPlant());
            if (latestTelemetry == null || latestTelemetry.getSoilMoisture() == null) continue;

            BigDecimal currentHeight = plant.getCurrentHeightCm();
            BigDecimal potSize = BigDecimal.valueOf(plant.getCurrentPotSizeCm());
            BigDecimal recommendedSize = plant.getSpecies().getRecommendedPotSizeCm() != null
                    ? BigDecimal.valueOf(plant.getSpecies().getRecommendedPotSizeCm())
                    : BigDecimal.valueOf(15);

            // Если растение превысило 85% от размера горшка
            if (currentHeight.compareTo(potSize.multiply(BigDecimal.valueOf(0.85))) > 0) {
                Recommendation rec = new Recommendation();
                rec.setPlant(plant);
                rec.setRecommendationType("REPOT");
                rec.setMessage(String.format(
                        "Растение достигло высоты %.1f см при размере горшка %d см. " +
                                "Рекомендуется пересадка в горшок %d см.",
                        currentHeight, plant.getCurrentPotSizeCm(), recommendedSize.intValue()
                ));
                rec.setSeverity("INFO");
                recommendationService.save(rec);
            }
        }
    }
}