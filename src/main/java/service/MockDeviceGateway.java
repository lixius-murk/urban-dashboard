package service;

import model.entity.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Stands in for the real hardware gateway. In production this would publish
 * to MQTT/HTTP and wait for a device ACK; here we simulate that round trip
 * so the rest of the command pipeline (retry, ack, fail) has something to drive it.
 */
@Service
public class MockDeviceGateway {

    private final Random random = new Random();

    @Autowired(required = false)
    private CommandService commandService;

    @Async
    public void sendCommand(Command command) {
        try {
            // simulate network/device latency
            Thread.sleep(300 + random.nextInt(700));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        // simulate an occasional device failure (~10%)
        if (random.nextInt(10) == 0) {
            commandService.fail(command.getIdCommand(), "Device did not respond");
        } else {
            commandService.acknowledge(command.getIdCommand());
        }
    }
}
