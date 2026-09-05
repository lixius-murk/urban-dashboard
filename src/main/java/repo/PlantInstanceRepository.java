package repo;

import model.entity.PlantInstance;
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
import java.util.Optional;



@Repository
public interface PlantInstanceRepository extends JpaRepository<PlantInstance, Long> {

    // Using 'active' instead of 'isActive'
    List<PlantInstance> findByActiveTrue();

    List<PlantInstance> findBySpecies_Id(Long speciesId);

    List<PlantInstance> findByState(Integer state);

    List<PlantInstance> findByNameContainingIgnoreCase(String name);

    @Query("SELECT p FROM PlantInstance p " +
            "LEFT JOIN FETCH p.species " +
            "LEFT JOIN FETCH p.sensors " +
            "WHERE p.id = :id")
    Optional<PlantInstance> findById(@Param("id") Long id);

    @Query("SELECT p, t FROM PlantInstance p " +
            "LEFT JOIN Telemetry t ON t.plant = p " +
            "AND t.timestamp = (SELECT MAX(t2.timestamp) FROM Telemetry t2 WHERE t2.plant = p) " +
            "WHERE p.active = true")
    List<Object[]> findAllWithLatestTelemetry();

    @Query("SELECT DISTINCT p FROM PlantInstance p " +
            "JOIN p.species s " +
            "JOIN Telemetry t ON t.plant = p " +
            "WHERE t.timestamp = (SELECT MAX(t2.timestamp) FROM Telemetry t2 WHERE t2.plant = p) " +
            "AND t.soilMoisture < COALESCE(p.soilMoistureMin, s.soilMoistureMin)*0.9")
    List<PlantInstance> findPlantsNeedingWatering();

    @Query("SELECT p FROM PlantInstance p WHERE p.state = 1 AND p.active = true")
    List<PlantInstance> findPlantsNeedingAttention();

    @Query("SELECT p.species.name, COUNT(p) FROM PlantInstance p GROUP BY p.species.name")
    List<Object[]> countBySpecies();


    @Query("SELECT COUNT(p) FROM PlantInstance p WHERE p.active = true")
    long countByIsActiveTrue();
    @Query("SELECT p FROM PlantInstance p WHERE p.active = true")
    List<PlantInstance> findByIsActiveTrue();


    @Modifying
    @Transactional
    @Query("UPDATE PlantInstance p SET p.state = :state, p.lastChecked = CURRENT_TIMESTAMP WHERE p.id = :id")
    int updateState(@Param("id") Long id, @Param("state") Integer state);

    @Modifying
    @Transactional
    @Query("UPDATE PlantInstance p SET p.lastWatered = :time WHERE p.id = :id")
    int updateLastWatered(@Param("id") Long id, @Param("time") LocalDateTime time);

    @Modifying
    @Transactional
    @Query("UPDATE PlantInstance p SET p.height = :height WHERE p.id = :id")
    int updateHeight(@Param("id") Long id, @Param("height") Double height);

    @Query("SELECT COUNT(p) FROM PlantInstance p WHERE p.active = true")
    long countByActiveTrue();

    @Query("SELECT COUNT(p) FROM PlantInstance p WHERE p.state = :state")
    long countByState(@Param("state") Integer state);

    @Query("SELECT AVG(p.height) FROM PlantInstance p WHERE p.active = true")
    Double getAverageHeight();

    @Query("SELECT p FROM PlantInstance p WHERE p.active = true ORDER BY p.height DESC LIMIT 1")
    Optional<PlantInstance> findTallestPlant();

    Page<PlantInstance> findByActiveTrue(Pageable pageable);

    Page<PlantInstance> findByState(Integer state, Pageable pageable);
}


//    List<PlantInstance> findByIsActiveTrue();
//
//    List<PlantInstance> findBySpecies_IdSpecies(Long speciesId);
//
//    List<PlantInstance> findByState(Integer state);
//
//    List<PlantInstance> findByNameContainingIgnoreCase(String name);
//
//    @Query("SELECT p FROM PlantInstance p " +
//            "LEFT JOIN FETCH p.species " +
//            "LEFT JOIN FETCH p.sensors " +
//            "WHERE p.id = :id")
//    Optional<PlantInstance> findById(@Param("id") Long id);
//
//    @Query("SELECT p, t FROM PlantInstance p " +
//            "LEFT JOIN Telemetry t ON t.plant = p " +
//            "AND t.timestamp = (SELECT MAX(t2.timestamp) FROM Telemetry t2 WHERE t2.plant = p) " +
//            "WHERE p.isActive = true")
//    List<Object[]> findAllWithLatestTelemetry();
//
//    @Query("SELECT DISTINCT p FROM PlantInstance p " +
//            "JOIN p.species s " +
//            "JOIN Telemetry t ON t.plant = p " +
//            "WHERE t.timestamp = (SELECT MAX(t2.timestamp) FROM Telemetry t2 WHERE t2.plant = p) " +
//            "AND t.soilMoisture < COALESCE(p.soilMoistureMin, s.soilMoistureMin)*0.9")
//    List<PlantInstance> findPlantsNeedingWatering();
//
//    @Query("SELECT p FROM PlantInstance p WHERE p.state = 1 AND p.isActive = true")
//    List<PlantInstance> findPlantsNeedingAttention();
//
//    @Query("SELECT p.species.name, COUNT(p) FROM PlantInstance p GROUP BY p.species.name")
//    List<Object[]> countBySpecies();
//
//    @Modifying
//    @Transactional
//    @Query("UPDATE PlantInstance p SET p.state = :state, p.lastChecked = CURRENT_TIMESTAMP WHERE p.id = :id")
//    int updateState(@Param("id") Long id, @Param("state") Integer state);
//
//    @Modifying
//    @Transactional
//    @Query("UPDATE PlantInstance p SET p.lastWatered = :time WHERE p.id = :id")
//    int updateLastWatered(@Param("id") Long id, @Param("time") LocalDateTime time);
//
//    @Modifying
//    @Transactional
//    @Query("UPDATE PlantInstance p SET p.height = :height WHERE p.id = :id")
//    int updateHeight(@Param("id") Long id, @Param("height") Double height);
//
//    @Query("SELECT COUNT(p) FROM PlantInstance p WHERE p.isActive = true")
//    long countByIsActiveTrue();
//
//    @Query("SELECT COUNT(p) FROM PlantInstance p WHERE p.state = :state")
//    long countByState(@Param("state") Integer state);
//
//    @Query("SELECT AVG(p.height) FROM PlantInstance p WHERE p.isActive = true")
//    Double getAverageHeight();
//
//    @Query("SELECT p FROM PlantInstance p WHERE p.isActive = true ORDER BY p.height DESC LIMIT 1")
//    Optional<PlantInstance> findTallestPlant();
//
//    Page<PlantInstance> findByIsActiveTrue(Pageable pageable);
//
//    Page<PlantInstance> findByState(Integer state, Pageable pageable);
