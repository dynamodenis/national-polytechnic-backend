package com.mabawa.nnpdairy.repository;

import com.mabawa.nnpdairy.models.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Integer> {
    @Query(value = "SELECT * from config ", nativeQuery = true)
    Optional<Config> getConfigBy();
}
