package repo;

import model.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {

    List<Sensor> findByPlant_IdAndActiveTrue(Long plantId);

    List<Sensor> findByPlant_Id(Long plantId);
}