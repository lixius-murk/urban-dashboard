package service;

import model.entity.Telemetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repo.TelemetryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TelemetryService {

    @Autowired
    private TelemetryRepository telemetryRepository;

    public Telemetry save(Telemetry telemetry) {
        return telemetryRepository.save(telemetry);
    }

    public Optional<Telemetry> getLatestByPlant(Long plantId) {
        return telemetryRepository.findLatestByPlantId(plantId);
    }

    public List<Telemetry> getHistory(Long plantId, int hours) {
        LocalDateTime from = LocalDateTime.now().minusHours(hours);
        return telemetryRepository.findByPlantIdAndTimestampBetweenOrderByTimestampAsc(plantId, from, LocalDateTime.now());
    }

    public Object[] getAveragesSince(Long plantId, int hours) {
        return telemetryRepository.getAveragesSince(plantId, LocalDateTime.now().minusHours(hours));
    }

    public int purgeOlderThan(int days) {
        return telemetryRepository.deleteOlderThan(LocalDateTime.now().minusDays(days));
    }
}