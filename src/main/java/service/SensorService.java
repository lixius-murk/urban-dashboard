package service;

import  simulator.DataSimulator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class SensorService {

    private final Random random = new Random();

    public List<DataSimulator> generateSensors() {

        List<DataSimulator> sensors = new ArrayList<>();

        sensors.add(new DataSimulator(
                "manhole",
                56.326093, 44.016003,
                random.nextBoolean() ? "OPEN" : "SAFE",
                random.nextInt(100)
        ));

        sensors.add(new DataSimulator(
                "noise",
                56.325349, 44.006651,
                "LOUD",
                40 + random.nextInt(50)
        ));

        sensors.add(new DataSimulator(
                "bus",
                56.325170, 44.007487,
                "CROWDED",
                70 + random.nextInt(30)
        ));

        return sensors;
    }
}