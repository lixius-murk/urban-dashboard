package repo;

import  model.entity.Recommendation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {


    List<Recommendation> findByPlant_IdPlantOrderByCreatedAtDesc(Long plantId);

    List<Recommendation> findByIsResolvedFalseOrderByCreatedAtAsc();

    List<Recommendation> findBySeverityOrderByCreatedAtDesc(String severity);


    @Query("SELECT r FROM Recommendation r WHERE r.isResolved = false " +
            "AND r.severity IN ('WARNING', 'CRITICAL') " +
            "ORDER BY r.severity DESC, r.createdAt ASC")
    List<Recommendation> findUnresolvedCritical();

    List<Recommendation> findByPlant_IdPlantAndCreatedAtBetweenOrderByCreatedAtDesc(
            Long plantId, LocalDateTime from, LocalDateTime to);

    @Query("SELECT r.plant.idPlant, COUNT(r) FROM Recommendation r " +
            "WHERE r.isResolved = false " +
            "GROUP BY r.plant.idPlant")
    List<Object[]> countUnresolvedByPlant();


    @Modifying
    @Transactional
    @Query("UPDATE Recommendation r SET r.isResolved = true, " +
            "r.resolvedAt = CURRENT_TIMESTAMP, " +
            "r.userFeedback = :feedback " +
            "WHERE r.idRecommendation = :id")
    int resolve(@Param("id") Long id, @Param("feedback") String feedback);

    //разрешение всех рекомендаций для растения
    @Modifying
    @Transactional
    @Query("UPDATE Recommendation r SET r.isResolved = true, r.resolvedAt = CURRENT_TIMESTAMP " +
            "WHERE r.plant.idPlant = :plantId AND r.isResolved = false")
    int resolveAllByPlant(@Param("plantId") Long plantId);


    long countByIsResolvedFalse();

    long countByPlant_IdPlantAndIsResolvedFalse(Long plantId);


    @Query("SELECT r.severity, COUNT(r) FROM Recommendation r " +
            "WHERE r.isResolved = false GROUP BY r.severity")
    List<Object[]> countUnresolvedBySeverity();

    @Modifying
    @Transactional
    @Query("DELETE FROM Recommendation r WHERE r.resolvedAt < :olderThan")
    int deleteOldResolved(@Param("olderThan") LocalDateTime olderThan);

    Page<Recommendation> findByPlant_IdPlantOrderByCreatedAtDesc(Long plantId, Pageable pageable);

    Page<Recommendation> findByIsResolvedFalseOrderByCreatedAtAsc(Pageable pageable);
}