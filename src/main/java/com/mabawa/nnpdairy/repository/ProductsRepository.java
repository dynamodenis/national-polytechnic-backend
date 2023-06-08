package com.mabawa.nnpdairy.repository;

import com.mabawa.nnpdairy.models.Products;
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
public interface ProductsRepository extends JpaRepository<Products, UUID> {
    @Query(value = "SELECT * FROM products WHERE name ILIKE '%'||:name||'%' ORDER BY name", nativeQuery = true)
    List<Products> getStockByNameLike(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT * FROM products WHERE type = :typ ORDER BY name", nativeQuery = true)
    List<Products> filterStockByType(@Param("typ") Integer typ, Pageable pageable);

    @Query(value = "SELECT * FROM products WHERE type = :typ AND category = :category ORDER BY name", nativeQuery = true)
    List<Products> filterStockByTypeAndCategory(@Param("typ") Integer typ, @Param("category") UUID category, Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Products products WHERE products.id = :id")
    void deleteProductsById(@Param("id") UUID id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Products products")
    void deleteAllProducts();

    Optional<Products> findByNameIgnoreCase(String name);

    List<Products> findProductsByNameContaining(String name);
}