package simulator;

import model.entity.Telemetry;
import org.springframework.stereotype.Service;

/**
 * Stands in for the physical gateway device that would normally relay
 * telemetry from real sensors over MQTT/HTTP. In simulation mode it's just
 * a pass-through hook so DataCollection's call site has somewhere to go.
 */
@Service
public class GatewaySimulator {

    public void receiveTelemetry(Telemetry telemetry) {
        // No-op in simulation: DataSimulator already produced the reading,
        // so there's nothing to relay. Kept as an extension point for when
        // a real gateway integration replaces the simulator.
    }
}
