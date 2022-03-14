package com.mabawa.nnpdairy.repository;

import com.mabawa.nnpdairy.models.Vendors;
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
public interface VendorsRepository extends JpaRepository<Vendors, UUID> {
    @Query(value = "SELECT * FROM vendors WHERE name = :name ORDER BY name", nativeQuery = true)
    Optional<Vendors> getVendorByName(@Param("name") String name);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Vendors vendors WHERE vendors.id = :id")
    void deleteVendorById(@Param("id") UUID id);

    List<Vendors> findByNameContaining(String name);
}
