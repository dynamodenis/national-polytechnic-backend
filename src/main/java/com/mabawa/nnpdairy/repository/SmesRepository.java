package com.mabawa.nnpdairy.repository;

import com.mabawa.nnpdairy.models.Smes;
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
public interface SmesRepository extends JpaRepository<Smes, UUID> {
    @Query(value = "SELECT * FROM smes WHERE name = :name ORDER BY name", nativeQuery = true)
    Optional<Smes> getSmesByName(@Param("name") String name);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Smes smes WHERE smes.id = :id")
    void deleteSmesById(@Param("id") UUID id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Smes smes")
    void deleteAllSmes();

    List<Smes> findByNameContaining(String name);
}
