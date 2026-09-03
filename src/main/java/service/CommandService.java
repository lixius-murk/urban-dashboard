package service;

import model.entity.Command;
import model.entity.Event;
import model.entity.PlantInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repo.CommandRepository;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CommandService {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired(required = false)
    private CommandRepository commandRepository;

    public Command createCommand(PlantInstance plant, Event event, String commandType, Map<String, Object> payload) {
        Command command = new Command();
        command.setPlant(plant);
        command.setEvent(event);
        command.setCommandType(commandType);
        command.setPayload(toJson(payload));
        command.setStatus("PENDING");
        command.setRetryCount(0);
        command.setMaxRetries(3);
        command.setCreatedAt(LocalDateTime.now());
        return commandRepository.save(command);
    }

    public Command createManualCommand(PlantInstance plant, Event event, String commandType) {
        return createCommand(plant, event, commandType, Map.of("trigger", "MANUAL"));
    }


    public void sendCommand(Command command) {
        commandRepository.markAsSent(command.getIdCommand(), "SENT");
    }

    public void acknowledge(Long commandId) {
        commandRepository.markAsAcknowledged(commandId);
    }

    public void fail(Long commandId, String error) {
        commandRepository.findById(commandId).ifPresent(c -> {
                c.setStatus("FAILED");
                c.setErrorMessage(error);
                commandRepository.save(c);
            });
    }

    public void cancel(Long commandId) {
        commandRepository.cancel(commandId);
    }

    private String toJson(Map<String, Object> payload) {
        try {
            return MAPPER.writeValueAsString(payload);
        } catch (Exception e) {
            return "{}";
        }
    }
}
