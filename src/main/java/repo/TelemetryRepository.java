package repo;

import model.entity.Telemetry;
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


    List<Telemetry> findByPlantIdAndTimestampBetweenOrderByTimestampAsc(Long plantId, LocalDateTime from, LocalDateTime to);

    List<Telemetry> findBySensor_IdOrderByTimestampDesc(Long sensorId);

    @Query("SELECT t FROM Telemetry t WHERE t.plant.id = :plantId " +
            "AND t.sensor.type = :sensorType " +
            "ORDER BY t.timestamp DESC LIMIT 1")
    Optional<Telemetry> findLatestByPlantAndSensorType(@Param("plantId") Long plantId, @Param("sensorType") String sensorType);

    @Query("SELECT DISTINCT t FROM Telemetry t " +
            "WHERE t.timestamp = (SELECT MAX(t2.timestamp) FROM Telemetry t2 WHERE t2.plant = t.plant)")
    List<Telemetry> findAllLatestReadings();

    @Query("SELECT t FROM Telemetry t WHERE t.plant.id = :plantId ORDER BY t.timestamp DESC LIMIT 1")
    Optional<Telemetry> findLatestByPlantId(@Param("plantId") Long plantId);

    @Query("SELECT AVG(t.temp), AVG(t.humidity), AVG(t.soilMoisture), AVG(t.light) " +
            "FROM Telemetry t WHERE t.plant.id = :plantId AND t.timestamp >= :since")
    Object[] getAveragesSince(@Param("plantId") Long plantId, @Param("since") LocalDateTime since);

    @Modifying
    @Transactional
    @Query("DELETE FROM Telemetry t WHERE t.timestamp < :olderThan")
    int deleteOlderThan(@Param("olderThan") LocalDateTime olderThan);

    @Modifying
    @Transactional
    void deleteByPlant_Id(Long plantId);

    @Query("SELECT t.temp, t.humidity, t.soilMoisture, t.light " +
            "FROM Telemetry t WHERE t.plant.id = :plantId AND t.timestamp >= :since")
    List<Telemetry> findByPlantIdDesc(Long plantId, LocalDateTime since);

}