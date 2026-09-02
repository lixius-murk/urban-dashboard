package simulator;

import model.entity.PlantInstance;
import model.entity.Telemetry;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DataSimulator {
    private final Random random = new Random();

    // Храним последние значения для плавного изменения
    private final Map<Long, DeviceState> deviceStates = new ConcurrentHashMap<>();

    public Telemetry generateTelemetry(PlantInstance plant, Sensor sensor) {
        DeviceState state = deviceStates.computeIfAbsent(
                plant.getIdPlant(),
                k -> new DeviceState()
        );

        Telemetry telemetry = new Telemetry();
        telemetry.setPlant(plant);
        telemetry.setSensor(sensor);
        telemetry.setTimestamp(LocalDateTime.now());
        telemetry.setSource("SIMULATOR");

        // Определяем тип сенсора и генерируем соответствующее значение
        String sensorType = sensor.getSensorType().getName();

        switch (sensorType) {
            case "TEMPERATURE":
                telemetry.setTemperature(generateTemperature(plant, state));
                break;
            case "HUMIDITY_AIR":
                telemetry.setHumidityAir(generateHumidityAir(plant, state));
                break;
            case "SOIL_MOISTURE":
                telemetry.setSoilMoisture(generateSoilMoisture(plant, state));
                break;
            case "LIGHT":
                telemetry.setLightLux(generateLight(plant, state));
                break;
            case "EC":
                telemetry.setEc(generateEc(plant, state));
                break;
        }

        return telemetry;
    }

    private BigDecimal generateTemperature(PlantInstance plant, DeviceState state) {
        BigDecimal prevTemp = state.getLastTemperature();
        BigDecimal min = getEffectiveTempMin(plant);
        BigDecimal max = getEffectiveTempMax(plant);

        LocalTime now = LocalTime.now();
        double timeFactor = Math.sin((now.getHour() - 6) * Math.PI / 12);
        BigDecimal baseTemp = min.add(max.subtract(min).multiply(BigDecimal.valueOf(0.5 + timeFactor * 0.3)));

        BigDecimal randomDelta = BigDecimal.valueOf(random.nextDouble() * 3 - 1.5);
        BigDecimal newTemp = baseTemp.add(randomDelta);

        if (prevTemp != null) {
            newTemp = prevTemp.add(newTemp.subtract(prevTemp).multiply(BigDecimal.valueOf(0.3)));
        }

        state.setLastTemperature(newTemp);
        return newTemp.setScale(1, RoundingMode.HALF_UP);
    }

    private Integer generateSoilMoisture(PlantInstance plant, DeviceState state) {
        Integer prevMoisture = state.getLastSoilMoisture();
        Integer min = getEffectiveSoilMoistureMin(plant);
        Integer max = getEffectiveSoilMoistureMax(plant);

        // Испарение: медленно снижается
        double evaporationRate = 0.98; // 2% потери за цикл
        int newMoisture = prevMoisture != null
                ? (int)(prevMoisture * evaporationRate)
                : (min + max) / 2;

        // Если был недавний полив — резко повышаем
        if (state.isWateringActive()) {
            newMoisture = Math.min(newMoisture + 25, max);
            state.decrementWateringTimer();
        }

        // Если был нагрев — ускоряем испарение
        if (state.isHeatingActive()) {
            newMoisture = (int)(newMoisture * 0.95);
        }

        // Дополнительное случайное колебание
        newMoisture += random.nextInt(5) - 2;
        newMoisture = Math.max(min, Math.min(max, newMoisture));

        state.setLastSoilMoisture(newMoisture);
        return newMoisture;
    }

    private Integer generateLight(PlantInstance plant, DeviceState state) {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();

        if (hour < 7 || hour > 20) {
            return 50;  //минимальное освещение (луна/подсветка)
        }

        // Дневной цикл: пик в 12-14 часов
        int peakHour = 13;
        int maxLight = 12000;
        int minLight = 500;

        double factor = 1 - Math.pow((hour - peakHour) / 7.0, 2);
        factor = Math.max(0.1, factor);

        int light = (int)(minLight + (maxLight - minLight) * factor);

        // Случайная облачность ±20%
        light *= (0.8 + random.nextDouble() * 0.4);

        if (!state.isCurtainsOpen()) {
            light = (int)(light * 0.3);
        }

        return Math.min(maxLight, Math.max(50, light));
    }

    // Вспомогательные методы для получения эффективных порогов
    private BigDecimal getEffectiveTempMin(PlantInstance plant) {
        return plant.getCustomTempMin() != null
                ? plant.getCustomTempMin()
                : plant.getSpecies().getTempMin();
    }

    private BigDecimal getEffectiveTempMax(PlantInstance plant) {
        return plant.getCustomTempMax() != null
                ? plant.getCustomTempMax()
                : plant.getSpecies().getTempMax();
    }

    @Data
    private static class DeviceState {
        private BigDecimal lastTemperature;
        private Integer lastSoilMoisture;
        private Integer lastHumidityAir;
        private BigDecimal lastEc;

        private boolean wateringActive;
        private int wateringTimer;

        private boolean heatingActive;
        private int heatingTimer;

        private boolean curtainsOpen = true;

        public void decrementWateringTimer() {
            if (wateringTimer > 0) wateringTimer--;
            if (wateringTimer == 0) wateringActive = false;
        }

        public boolean isCurtainsOpen(){
            return curtainsOpen;
        }
        public boolean isHeatingActive(){
            return heatingActive;
        }
        public boolean isWateringActive(){
            return wateringActive;
        }
        public void startWatering(int duration) {
            wateringActive = true;
            wateringTimer = duration;
        }
        public void startHeating(int duration) {
            heatingActive = true;
            heatingTimer = duration;
        }
        public void startCurtains(int duration) {
            curtainsOpen = true;
        }
    }
}