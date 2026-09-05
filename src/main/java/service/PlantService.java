package service;

import model.entity.PlantInstance;
import model.entity.PlantSpecies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repo.PlantInstanceRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PlantService {

    @Autowired
    private PlantInstanceRepository plantRepository;

    public long countActive() {
        return plantRepository.countByIsActiveTrue();
    }

    public long countByState(int state) {
        return plantRepository.countByState(state);
    }

    public long countByHealthStatus(String healthStatus) {
        if ("HEALTHY".equals(healthStatus)) {
            return plantRepository.countByState(0);
        } else {
            return plantRepository.countByState(1);
        }
    }

    public List<PlantInstance> getAllPlants() {
        return plantRepository.findAll();
    }

    public List<PlantInstance> getAllActive() {
        return plantRepository.findByIsActiveTrue();
    }

    public PlantInstance getPlantById(Long id) {
        return plantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plant not found"));
    }

    public Optional<PlantInstance> findById(Long id) {
        return plantRepository.findById(id);
    }

    public PlantInstance save(PlantInstance plant) {
        return plantRepository.save(plant);
    }

    public void updateState(Long id, Integer state) {
        plantRepository.updateState(id, state);
    }

    public void updateLastWatered(Long id, LocalDateTime time) {
        plantRepository.updateLastWatered(id, time);
    }
}