package com.mabawa.nnpdairy.repository;

import com.mabawa.nnpdairy.models.Research;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResearchRepository extends JpaRepository<Research, UUID> {
    @Query(value = "SELECT * FROM research WHERE description = :descr", nativeQuery = true)
    Optional<Research> getResearchByDescription(@Param("descr") String descr);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Research research WHERE research.id = :id")
    void deleteResearchById(@Param("id") UUID id);
}
