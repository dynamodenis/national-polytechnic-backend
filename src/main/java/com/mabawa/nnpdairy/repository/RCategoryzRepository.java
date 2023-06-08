package com.mabawa.nnpdairy.repository;

import com.mabawa.nnpdairy.models.RCategoryz;
import com.mabawa.nnpdairy.models.TCategoryz;
import org.springframework.data.domain.Pageable;
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
public interface RCategoryzRepository extends JpaRepository<RCategoryz, UUID> {
    @Query(value = "SELECT * FROM rcategoryz ORDER BY name", nativeQuery = true)
    List<RCategoryz> getAllCategory();

    @Query(value = "SELECT * FROM rcategoryz WHERE id = :id", nativeQuery = true)
    Optional<RCategoryz> getCategoryById(@Param("id") UUID id);

    @Query(value = "SELECT * FROM rcategoryz WHERE name = :name ORDER BY name", nativeQuery = true)
    Optional<RCategoryz> getCategoryByName(@Param("name") String name);

    @Query(value = "SELECT * FROM rcategoryz WHERE name ILIKE '%'||:name||'%' ORDER BY name", nativeQuery = true)
    List<RCategoryz> getCategoryByNameLike(@Param("name") String name, Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE RCategoryz rcategoryz SET rcategoryz.name = :name WHERE rcategoryz.id = :id")
    void updateCategory(@Param("name") String name, @Param("id") UUID id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM RCategoryz rcategoryz WHERE rcategoryz.id = :id")
    void deleteCategoryById(@Param("id") UUID id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM RCategoryz rcategoryz")
    void deleteAllCategory();

    List<RCategoryz> findByNameContaining(String name);
}
