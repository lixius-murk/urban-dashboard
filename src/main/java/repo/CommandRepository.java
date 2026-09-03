package repo;

import  model.entity.Command;
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
public interface CommandRepository extends JpaRepository<Command, Long> {


    List<Command> findByPlant_IdPlantOrderByCreatedAtDesc(Long plantId);

    //PENDING, SENT, ACKNOWLEDGED, FAILED, CANCELLED
    List<Command> findByStatusOrderByCreatedAtAsc(String status);

    List<Command> findByCommandTypeOrderByCreatedAtDesc(String commandType);


    // PENDING for what needs to be sent
    @Query("SELECT c FROM Command c WHERE c.status = 'PENDING' " +
            "AND c.retryCount < c.maxRetries " +
            "ORDER BY c.createdAt ASC")
    List<Command> findPendingCommandsToSend();


    @Modifying
    @Transactional
    @Query("UPDATE Command c SET c.status = :status, c.sentAt = CURRENT_TIMESTAMP " +
            "WHERE c.idCommand = :id")
    int markAsSent(@Param("id") Long id, @Param("status") String status);

    @Modifying
    @Transactional
    @Query("UPDATE Command c SET c.status = 'ACKNOWLEDGED', " +
            "c.acknowledgedAt = CURRENT_TIMESTAMP, " +
            "c.completedAt = CURRENT_TIMESTAMP " +
            "WHERE c.idCommand = :id")
    int markAsAcknowledged(@Param("id") Long id);


    @Modifying
    @Transactional
    @Query("UPDATE Command c SET c.status = 'CANCELLED' WHERE c.idCommand = :id")
    int cancel(@Param("id") Long id);


    @Modifying
    @Transactional
    @Query("DELETE FROM Command c WHERE c.completedAt < :olderThan AND c.status IN ('ACKNOWLEDGED', 'FAILED', 'CANCELLED')")
    int deleteOldCompletedCommands(@Param("olderThan") LocalDateTime olderThan);
}