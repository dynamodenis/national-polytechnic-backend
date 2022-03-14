package com.mabawa.nnpdairy.repository;

import com.mabawa.nnpdairy.models.TCategoryz;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
public interface TCategoryzRepository extends JpaRepository<TCategoryz, UUID> {
    @Query(value = "SELECT * FROM tcategoryz ORDER BY name", nativeQuery = true)
    List<TCategoryz> getAllCategory();

    @Query(value = "SELECT * FROM tcategoryz WHERE id = :id", nativeQuery = true)
    Optional<TCategoryz> getCategoryById(@Param("id") UUID id);

    @Query(value = "SELECT * FROM tcategoryz WHERE name = :name ORDER BY name", nativeQuery = true)
    Optional<TCategoryz> getCategoryByName(@Param("name") String name);

    @Query(value = "SELECT * FROM tcategoryz WHERE name ILIKE '%'||:name||'%' ORDER BY name", nativeQuery = true)
    List<TCategoryz> getCategoryByNameLike(@Param("name") String name, Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE TCategoryz tcategoryz SET tcategoryz.name = :name WHERE tcategoryz.id = :id")
    void updateCategory(@Param("name") String name, @Param("id") UUID id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM TCategoryz tcategoryz WHERE tcategoryz.id = :id")
    void deleteCategoryById(@Param("id") UUID id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM TCategoryz tcategoryz")
    void deleteAllCategory();

    List<TCategoryz> findByNameContaining(String name);
}
