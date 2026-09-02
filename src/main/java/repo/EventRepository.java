package repo;


import  model.entity.Event;
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
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByPlant_IdPlantOrderByTimestampDesc(Long plantId);
    List<Event> findTop10ByPlant_IdPlantOrderByTimestampDesc(Long plantId);
    List<Event> findByEventTypeOrderByTimestampDesc(String eventType);
    List<Event> findByTriggerTypeOrderByTimestampDesc(String triggerType);

    //0-ожидание, 1-обработка, 2-выполнено, 3-ошибка
    List<Event> findByEventState(Integer state);

    @Query("SELECT e FROM Event e WHERE e.eventState IN (0, 1) ORDER BY e.priority DESC, e.timestamp ASC")
    List<Event> findPendingEvents();

    List<Event> findByPlant_IdPlantAndTimestampBetweenOrderByTimestampDesc(
            Long plantId, LocalDateTime from, LocalDateTime to);

    // Последние события по всем растениям
    @Query("SELECT e FROM Event e ORDER BY e.timestamp DESC LIMIT 50")
    List<Event> findLast50Events();

    // Подсчет событий по типам
    @Query("SELECT e.eventType, COUNT(e) FROM Event e " +
            "WHERE e.timestamp >= :since GROUP BY e.eventType")
    List<Object[]> countByEventTypeSince(@Param("since") LocalDateTime since);

    // События с высоким приоритетом
    List<Event> findByPriorityGreaterThanEqualOrderByTimestampDesc(Integer priority);


    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.eventState = :state, e.errorMessage = :error " +
            "WHERE e.idEvent = :id")
    int updateState(@Param("id") Long id,
                    @Param("state") Integer state,
                    @Param("error") String error);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.commandSent = true, e.commandAcked = false WHERE e.idEvent = :id")
    int markCommandSent(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Event e WHERE e.timestamp < :olderThan AND e.eventState = 2")
    int deleteOldResolvedEvents(@Param("olderThan") LocalDateTime olderThan);



    Page<Event> findByPlant_IdPlantOrderByTimestampDesc(Long plantId, Pageable pageable);

    Page<Event> findByEventTypeOrderByTimestampDesc(String eventType, Pageable pageable);
}