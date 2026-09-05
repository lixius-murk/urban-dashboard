package service;

import model.entity.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repo.SensorRepository;

import java.util.List;
import java.util.Random;

@Service
public class SensorService {

    private final Random random = new Random();

    @Autowired(required = false)
    private SensorRepository sensorRepository;

    public List<Sensor> getSensorsByPlant(Long plantId) {
        return sensorRepository.findByPlant_IdAndActiveTrue(plantId);
    }


}
