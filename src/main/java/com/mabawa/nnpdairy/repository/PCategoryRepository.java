package com.mabawa.nnpdairy.repository;

import com.mabawa.nnpdairy.models.PCategoryz;
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
public interface PCategoryRepository extends JpaRepository<PCategoryz, UUID> {
    @Query(value = "SELECT * FROM pcategoryz ORDER BY name", nativeQuery = true)
    List<PCategoryz> getAllCategory();

    @Query(value = "SELECT * FROM pcategoryz WHERE id = :id", nativeQuery = true)
    Optional<PCategoryz> getCategoryById(@Param("id") UUID id);

    @Query(value = "SELECT * FROM pcategoryz WHERE name = :name ORDER BY name", nativeQuery = true)
    Optional<PCategoryz> getCategoryByName(@Param("name") String name);

    @Query(value = "SELECT * FROM pcategoryz WHERE name ILIKE '%'||:name||'%' ORDER BY name", nativeQuery = true)
    List<PCategoryz> getCategoryByNameLike(@Param("name") String name, Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE PCategoryz pcategoryz SET pcategoryz.name = :name, pcategoryz.type = :typ WHERE pcategoryz.id = :id")
    void updateCategory(@Param("name") String name, @Param("typ") Integer typ, @Param("id") UUID id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM PCategoryz pcategoryz WHERE pcategoryz.id = :id")
    void deleteCategoryById(@Param("id") UUID id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM PCategoryz pcategoryz")
    void deleteAllCategory();

    List<PCategoryz> findByNameContaining(String name);
}
