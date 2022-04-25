package com.mabawa.nnpdairy.repository;

import com.mabawa.nnpdairy.models.Trainings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrainersRepository extends JpaRepository<Trainings, UUID> {
    @Query(value = "SELECT * FROM trainings WHERE description = :descr", nativeQuery = true)
    Optional<Trainings> getTrainingsByDescription(@Param("descr") String descr);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Trainings trainings WHERE trainings.id = :id")
    void deleteTrainingsById(@Param("id") UUID id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Trainings trainings")
    void deleteAllTrainings();

    List<Trainings> findTrainingsByCategory(UUID category);
}
