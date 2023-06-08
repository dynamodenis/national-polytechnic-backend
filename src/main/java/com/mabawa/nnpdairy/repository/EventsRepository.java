package com.mabawa.nnpdairy.repository;

import com.mabawa.nnpdairy.models.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventsRepository extends JpaRepository<Events, UUID> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Events events SET events.status = :status WHERE events.id = :id")
    void updateEventsById(@Param("status") Integer status, @Param("id") UUID id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Events events WHERE events.id = :id")
    void deleteEventsById(@Param("id") UUID id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Events events")
    void deleteAllEvents();

    Optional<Events> findEventsByTopic(String topic);

    List<Events> findEventsByTopicContainingIgnoreCase(String topic);

    List<Events> findEventsByStimeAndEtime(Timestamp stime, Timestamp etime);

    List<Events> findEventsByStatus(Integer status);
}
