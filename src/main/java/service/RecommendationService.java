package service;

import model.entity.PlantInstance;
import model.entity.Recommendation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repo.RecommendationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecommendationService {

    @Autowired(required = false)
    private RecommendationRepository recommendationRepository;

    @Autowired(required = false)
    private WebSocketService webSocketService;

    public Recommendation save(Recommendation recommendation) {
        Recommendation saved = recommendationRepository.save(recommendation);

        PlantInstance plant = saved.getPlant();
        if (plant != null && Boolean.TRUE.equals(plant.getIsActive())) {
            webSocketService.sendRecommendation(plant, saved);
        }
        return saved;
    }

    public void resolve(Long id, String feedback) {
        Recommendation rec = recommendationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recommendation not found: " + id));
        rec.setIsResolved(true);
        rec.setResolvedAt(LocalDateTime.now());
        rec.setUserFeedback(feedback);
        recommendationRepository.save(rec);
        webSocketService.sendRecommendation(rec.getPlant(), rec);
    }

    public List<Recommendation> getByPlant(Long plantId) {
        return recommendationRepository.findByPlant_IdPlantOrderByCreatedAtDesc(plantId);
    }

    public List<Recommendation> getUnresolved(int limit) {
        List<Recommendation> unresolved = recommendationRepository.findByIsResolvedFalseOrderByCreatedAtAsc();
        return unresolved.size() > limit ? unresolved.subList(0, limit) : unresolved;
    }

    public long countUnresolved() {
        return recommendationRepository.countByIsResolvedFalse();
    }
}
