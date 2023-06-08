package com.mabawa.nnpdairy.repository;

import com.mabawa.nnpdairy.models.MarketplaceTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarketplaceTypesRepository extends JpaRepository<MarketplaceTypes, Integer> {
    List<MarketplaceTypes> findAll();
}
