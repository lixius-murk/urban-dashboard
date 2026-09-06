package repo;

import model.entity.Recommendation;
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


    @Query("SELECT r FROM Recommendation r WHERE r.resolved = false " +
            "AND r.severity IN ('WARNING', 'CRITICAL') " +
            "ORDER BY r.severity DESC, r.createdAt ASC")
    List<Recommendation> findByResolvedFalseOrderByCreatedAtAsc();


    @Query("SELECT r FROM Recommendation r WHERE r.resolved = false " +
            "AND r.severity IN ('WARNING', 'CRITICAL') " +
            "ORDER BY r.severity DESC, r.createdAt ASC")
    List<Recommendation> findUnresolvedCritical();

    @Query("SELECT r FROM Recommendation r WHERE r.plant.id = :plantId " +
            "ORDER BY r.severity DESC, r.createdAt ASC")
    List<Recommendation> getByPlantId(@Param("plantId") Long plantId);

    @Query("SELECT r FROM Recommendation r WHERE r.resolved = false AND r.plant.id = :plantId " +
            "ORDER BY r.severity DESC, r.createdAt ASC")
    List<Recommendation> getUnresByPlantId(@Param("plantId") Long plantId);

    @Query("SELECT r.plant.id, COUNT(r) FROM Recommendation r " +
            "WHERE r.resolved = false " +
            "GROUP BY r.plant.id")
    List<Object[]> countUnresolvedByPlant();

    @Query("SELECT r.id, r.plant.id FROM Recommendation r WHERE r.resolved = false")
    List<Object[]> getUnresolved();

    @Modifying
    @Transactional
    @Query("UPDATE Recommendation r SET r.resolved = true, r.resolvedAt = CURRENT_TIMESTAMP WHERE r.id = :id")
    int resolve(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Recommendation r SET r.resolved = true, r.resolvedAt = CURRENT_TIMESTAMP " +
            "WHERE r.plant.id = :plantId AND r.resolved = false")
    int resolveAllByPlant(@Param("plantId") Long plantId);

    @Query("SELECT COUNT(r) FROM Recommendation r WHERE r.resolved = false")
    long countByResolvedFalse();

    long countByPlant_IdAndResolvedFalse(Long plantId);

    @Query("SELECT r.severity, COUNT(r) FROM Recommendation r " +
            "WHERE r.resolved = false GROUP BY r.severity")
    List<Object[]> countUnresolvedBySeverity();

    @Modifying
    @Transactional
    @Query("DELETE FROM Recommendation r WHERE r.resolvedAt < :olderThan")
    int deleteOldResolved(@Param("olderThan") LocalDateTime olderThan);
}