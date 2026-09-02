package repo;

import  model.entity.Telemetry;
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
public interface TelemetryRepository extends JpaRepository<Telemetry, Long> {

    List<Telemetry> findTop10ByPlant_IdPlantOrderByTimestampDesc(Long plantId);

    List<Telemetry> findByPlant_IdPlantAndTimestampBetweenOrderByTimestampAsc(
            Long plantId, LocalDateTime from, LocalDateTime to);

    List<Telemetry> findBySensor_IdSensorOrderByTimestampDesc(Long sensorId);

    @Query("SELECT t FROM Telemetry t WHERE t.plant.idPlant = :plantId " +
            "AND t.sensor.sensorType.name = :sensorType " +
            "ORDER BY t.timestamp DESC LIMIT 1")
    Optional<Telemetry> findLatestByPlantAndSensorType(
            @Param("plantId") Long plantId,
            @Param("sensorType") String sensorType);


    @Query("SELECT DISTINCT t FROM Telemetry t " +
            "WHERE t.timestamp = (SELECT MAX(t2.timestamp) FROM Telemetry t2 WHERE t2.plant = t.plant)")
    List<Telemetry> findAllLatestReadings();

    @Query("SELECT t FROM Telemetry t WHERE t.plant.idPlant = :plantId " +
            "ORDER BY t.timestamp DESC LIMIT 1")
    Optional<Telemetry> findLatestByPlantId(@Param("plantId") Long plantId);

    //средние показатели за последние 24 часа
    @Query("SELECT AVG(t.temperature), AVG(t.humidityAir), AVG(t.soilMoisture), AVG(t.lightLux) " +
            "FROM Telemetry t WHERE t.plant.idPlant = :plantId " +
            "AND t.timestamp >= :since")
    Object[] getAveragesSince(
            @Param("plantId") Long plantId,
            @Param("since") LocalDateTime since);

    //выход за пределы норм
    @Query("SELECT t FROM Telemetry t WHERE t.plant.idPlant = :plantId " +
            "AND t.temperature IS NOT NULL " +
            "AND (t.temperature < :minTemp OR t.temperature > :maxTemp)")
    List<Telemetry> findTemperatureAnomalies(
            @Param("plantId") Long plantId,
            @Param("minTemp") Double minTemp,
            @Param("maxTemp") Double maxTemp);


    @Modifying
    @Transactional
    @Query("DELETE FROM Telemetry t WHERE t.timestamp < :olderThan")
    int deleteOlderThan(@Param("olderThan") LocalDateTime olderThan);

    @Modifying
    @Transactional
    void deleteByPlant_IdPlant(Long plantId);

    //паганация для бд
    Page<Telemetry> findByPlant_IdPlantOrderByTimestampDesc(Long plantId, Pageable pageable);

    Page<Telemetry> findBySensor_IdSensorOrderByTimestampDesc(Long sensorId, Pageable pageable);
}