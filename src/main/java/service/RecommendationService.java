package service;

import  model.entity.PlantInstance;
import  model.entity.Recommendation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repo.RecommendationRepository;

import java.time.LocalDateTime;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private WebSocketService webSocketService;

    public Recommendation save(Recommendation recommendation) {
        Recommendation saved = recommendationRepository.save(recommendation);

        PlantInstance plant = saved.getPlant();
        if (plant != null && plant.getIsActive()) {
            webSocketService.sendRecommendation(plant, saved);
        }
        return saved;
    }

    public void resolve(Long id, String feedback) {
        Recommendation rec = findById(id);
        rec.setIsResolved(true);
        rec.setResolvedAt(LocalDateTime.now());
        rec.setUserFeedback(feedback);
        recommendationRepository.save(rec);
        webSocketService.sendRecommendation(rec.getPlant(), rec);
    }
}