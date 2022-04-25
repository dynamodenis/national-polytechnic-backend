package com.mabawa.nnpdairy.repository;

import com.mabawa.nnpdairy.models.Consultants;
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
public interface ConsultantsRepository extends JpaRepository<Consultants, UUID> {
    @Query(value = "SELECT * FROM consultants WHERE name = :name ORDER BY name", nativeQuery = true)
    Optional<Consultants> getConsultantByName(@Param("name") String name);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Consultants consultants WHERE consultants.id = :id")
    void deleteConsultantsById(@Param("id") UUID id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Consultants consultants")
    void deleteAllConsultants();

    List<Consultants> findByNameContaining(String name);

    List<Consultants> findConsultantsByUserid(UUID userId);
}
