package simulator;

import model.entity.PlantInstance;
import model.entity.Sensor;
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

    //last values for smooth changes
    private final Map<Long, DeviceState> deviceStates = new ConcurrentHashMap<>();

    public Telemetry generateTelemetry(PlantInstance plant, Sensor sensor) {
        DeviceState state = deviceStates.computeIfAbsent(
                plant.getId(),
                k -> new DeviceState()
        );

        Telemetry telemetry = new Telemetry();
        telemetry.setPlant(plant);
        telemetry.setSensor(sensor);
        telemetry.setTimestamp(LocalDateTime.now());
        telemetry.setSource("SIMULATOR");

        //all values on every telemetry record
        telemetry.setTemp(generateTemperature(plant, state));
        telemetry.setHumidity(generateHumidityAir(plant, state));
        telemetry.setSoilMoisture(generateSoilMoisture(plant, state));
        telemetry.setLight(generateLight(plant, state));

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

    private Integer generateHumidityAir(PlantInstance plant, DeviceState state) {
        Integer prevHum = state.getLastHumidityAir();
        int min = 30;
        int max = 80;

        int base = prevHum != null ? prevHum : (min + max) / 2;
        int delta = random.nextInt(20) - 10;
        int newHum = Math.max(min, Math.min(max, base + delta));

        state.setLastHumidityAir(newHum);
        return newHum;
    }

    private Integer generateSoilMoisture(PlantInstance plant, DeviceState state) {
        Integer prevMoisture = state.getLastSoilMoisture();
        Integer min = getEffectiveSoilMoistureMin(plant);
        Integer max = getEffectiveSoilMoistureMax(plant);

        double evaporationRate = 0.98;
        int newMoisture = prevMoisture != null
                ? (int)(prevMoisture * evaporationRate)
                : (min + max) / 2;

        if (state.isWateringActive()) {
            newMoisture = Math.min(newMoisture + 40, max);
            state.decrementWateringTimer();
        }

        newMoisture += random.nextInt(20) - 10;
        newMoisture = Math.max(min, Math.min(max, newMoisture));

        state.setLastSoilMoisture(newMoisture);
        return newMoisture;
    }

    private Integer generateLight(PlantInstance plant, DeviceState state) {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();

        if (hour < 7 || hour > 20) {
            return 50;
        }

        int peakHour = 13;
        int maxLight = 12000;
        int minLight = 500;

        double factor = 1 - Math.pow((hour - peakHour) / 7.0, 2);
        factor = Math.max(0.1, factor);

        int light = (int)(minLight + (maxLight - minLight) * factor);
        light *= (0.8 + random.nextDouble() * 0.4);

        return Math.min(maxLight, Math.max(50, light));
    }

    //for effective thresholds
    private BigDecimal getEffectiveTempMin(PlantInstance plant) {
        return plant.getTempMin() != null
                ? plant.getTempMin()
                : plant.getSpecies().getTempMin();
    }

    private BigDecimal getEffectiveTempMax(PlantInstance plant) {
        return plant.getSpecies().getTempMax();
    }

    private int getEffectiveSoilMoistureMin(PlantInstance plant) {
        return plant.getSoilMoistureMin() != null
                ? plant.getSoilMoistureMin()
                : plant.getSpecies().getSoilMoistureMin();
    }

    private int getEffectiveSoilMoistureMax(PlantInstance plant) {
        return plant.getSpecies().getSoilMoistureMax();
    }

    private static class DeviceState {
        private BigDecimal lastTemperature;
        private Integer lastSoilMoisture;
        private Integer lastHumidityAir;
        private boolean wateringActive;
        private int wateringTimer;

        public BigDecimal getLastTemperature() { return lastTemperature; }
        public void setLastTemperature(BigDecimal lastTemperature) { this.lastTemperature = lastTemperature; }

        public Integer getLastSoilMoisture() { return lastSoilMoisture; }
        public void setLastSoilMoisture(Integer lastSoilMoisture) { this.lastSoilMoisture = lastSoilMoisture; }

        public Integer getLastHumidityAir() { return lastHumidityAir; }
        public void setLastHumidityAir(Integer lastHumidityAir) { this.lastHumidityAir = lastHumidityAir; }

        public boolean isWateringActive() { return wateringActive; }
        public void setWateringActive(boolean wateringActive) { this.wateringActive = wateringActive; }

        public void decrementWateringTimer() {
            if (wateringTimer > 0) wateringTimer--;
            if (wateringTimer == 0) wateringActive = false;
        }

        public void startWatering(int duration) {
            wateringActive = true;
            wateringTimer = duration;
        }
    }
}