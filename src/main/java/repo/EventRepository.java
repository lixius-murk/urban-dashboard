package repo;

import model.entity.Event;
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

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {


    @Query("SELECT e FROM Event e WHERE e.status IN (0, 1) ORDER BY e.id DESC")
    List<Event> findPendingEvents();


    @Query("SELECT e FROM Event e ORDER BY e.time DESC LIMIT 20")
    List<Event> findLast20Events();

    @Query("SELECT e.type, COUNT(e) FROM Event e WHERE e.time >= :since GROUP BY e.type")
    List<Object[]> countByTypeSince(@Param("since") LocalDateTime since);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.status = :status WHERE e.id = :id")
    int updateState(@Param("id") Long id, @Param("status") Integer status);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.status = 1 WHERE e.id = :id")
    int markCommandSent(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Event e WHERE e.time < :olderThan AND e.status = 2")
    int deleteOldResolvedEvents(@Param("olderThan") LocalDateTime olderThan);


    @Query("SELECT e FROM Event e WHERE e.plantId = :plantId")
    List<Event> findByPlantId(@Param("plantId") Long plantId);
}