package repo;

import  model.entity.PlantInstance;
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

     List<PlantInstance> findByIsActiveTrue();

    // Растения по виду
    List<PlantInstance> findBySpecies_IdSpecies(Long speciesId);

    // Растения по состоянию (0 - хорошо, 1 - требует внимания)
    List<PlantInstance> findByCurrentState(Integer state);

    // Растения по статусу здоровья
    List<PlantInstance> findByHealthStatus(String healthStatus);

    // Поиск по имени (содержит)
    List<PlantInstance> findByNameContainingIgnoreCase(String name);

    // Получить растение со всеми связанными данными (вид, сенсоры)
    @Query("SELECT p FROM PlantInstance p " +
            "LEFT JOIN FETCH p.species " +
            "LEFT JOIN FETCH p.sensors " +
            "WHERE p.idPlant = :id")
    Optional<PlantInstance> findByIdWithDetails(@Param("id") Long id);

    // Получить все растения с их последней телеметрией
    @Query("SELECT p, t FROM PlantInstance p " +
            "LEFT JOIN Telemetry t ON t.plant = p " +
            "AND t.timestamp = (SELECT MAX(t2.timestamp) FROM Telemetry t2 WHERE t2.plant = p) " +
            "WHERE p.isActive = true")
    List<Object[]> findAllWithLatestTelemetry();

    // Растения, требующие полива (влажность почвы ниже порога)
    @Query("SELECT DISTINCT p FROM PlantInstance p " +
            "JOIN p.species s " +
            "JOIN Telemetry t ON t.plant = p " +
            "WHERE t.timestamp = (SELECT MAX(t2.timestamp) FROM Telemetry t2 WHERE t2.plant = p) " +
            "AND t.soilMoisture < COALESCE(p.customSoilMoistureMin, s.soilMoistureMin)")
    List<PlantInstance> findPlantsNeedingWatering();

    // Растения, требующие внимания (currentState = 1)
    @Query("SELECT p FROM PlantInstance p WHERE p.currentState = 1 AND p.isActive = true")
    List<PlantInstance> findPlantsNeedingAttention();

    // Подсчет растений по видам (JPQL)
    @Query("SELECT p.species.name, COUNT(p) FROM PlantInstance p GROUP BY p.species.name")
    List<Object[]> countBySpecies();

    // Подсчет по состоянию здоровья
    @Query("SELECT p.healthStatus, COUNT(p) FROM PlantInstance p GROUP BY p.healthStatus")
    List<Object[]> countByHealthStatus();

    // ========== Обновления ==========

    // Обновить состояние растения
    @Modifying
    @Transactional
    @Query("UPDATE PlantInstance p SET p.currentState = :state, p.lastCheckAt = CURRENT_TIMESTAMP WHERE p.idPlant = :id")
    int updateState(@Param("id") Long id, @Param("state") Integer state);

    // Обновить время последнего полива
    @Modifying
    @Transactional
    @Query("UPDATE PlantInstance p SET p.lastWateredAt = :time WHERE p.idPlant = :id")
    int updateLastWatered(@Param("id") Long id, @Param("time") LocalDateTime time);

    // Обновить высоту растения
    @Modifying
    @Transactional
    @Query("UPDATE PlantInstance p SET p.currentHeightCm = :height WHERE p.idPlant = :id")
    int updateHeight(@Param("id") Long id, @Param("height") Double height);

    // ========== Статистика ==========

    // Количество активных растений
    long countByIsActiveTrue();

    // Количество растений в определенном состоянии
    long countByCurrentState(Integer state);

    // Количество растений по виду
    long countBySpecies_IdSpecies(Long speciesId);

    // Средняя высота всех активных растений
    @Query("SELECT AVG(p.currentHeightCm) FROM PlantInstance p WHERE p.isActive = true")
    Double getAverageHeight();

    // Самое высокое растение
    @Query("SELECT p FROM PlantInstance p WHERE p.isActive = true ORDER BY p.currentHeightCm DESC LIMIT 1")
    Optional<PlantInstance> findTallestPlant();

    // ========== Пагинация ==========

    Page<PlantInstance> findByIsActiveTrue(Pageable pageable);

    Page<PlantInstance> findByCurrentState(Integer state, Pageable pageable);
}