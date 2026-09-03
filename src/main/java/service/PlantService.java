package service;

import model.PlantSettingsDto;
import model.entity.PlantInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repo.PlantInstanceRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PlantService {

    @Autowired(required = false)
    private PlantInstanceRepository plantInstanceRepository;

    public List<PlantInstance> getAllActive() {
        return plantInstanceRepository.findByIsActiveTrue();
    }

    public Optional<PlantInstance> findById(Long id) {
        return plantInstanceRepository.findById(id);
    }

    public PlantInstance save(PlantInstance plant) {
        if (plant.getIsActive() == null) {
            plant.setIsActive(true);
        }
        if (plant.getCurrentState() == null) {
            plant.setCurrentState(0);
        }
        if (plant.getPlantedAt() == null) {
            plant.setPlantedAt(LocalDateTime.now());
        }
        return plantInstanceRepository.save(plant);
    }

    public PlantInstance updateSettings(Long id, PlantSettingsDto settings) {
        PlantInstance plant = plantInstanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plant not found: " + id));

        if (settings.getName() != null) {
            plant.setName(settings.getName());
        }
        // Custom thresholds override species defaults; null means "use species default"
        plant.setCustomTempMin(settings.getCustomTempMin());
        plant.setCustomTempMax(settings.getCustomTempMax());
        plant.setCustomHumMin(settings.getCustomHumMin());
        plant.setCustomHumMax(settings.getCustomHumMax());
        plant.setCustomSoilMoistureMin(settings.getCustomSoilMoistureMin());
        plant.setCustomSoilMoistureMax(settings.getCustomSoilMoistureMax());
        plant.setCustomLightMin(settings.getCustomLightMin());

        return plantInstanceRepository.save(plant);
    }

    public void updateState(Long id, Integer state) {
        plantInstanceRepository.updateState(id, state);
    }

    public void updateLastWatered(Long id, LocalDateTime time) {
        plantInstanceRepository.updateLastWatered(id, time);
    }

    public void updateHeight(Long id, Double height) {
        plantInstanceRepository.updateHeight(id, height);
    }

    public long countActive() {
        return plantInstanceRepository.countByIsActiveTrue();
    }

    public long countByHealthStatus(String healthStatus) {
        return getAllActive().stream()
                .filter(p -> healthStatus.equals(p.getHealthStatus()))
                .count();
    }

    public long countByState(Integer state) {
        return plantInstanceRepository.countByCurrentState(state);
    }

    public List<PlantInstance> getPlantsNeedingAttention() {
        return plantInstanceRepository.findPlantsNeedingAttention();
    }
}
