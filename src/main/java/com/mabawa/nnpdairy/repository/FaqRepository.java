package com.mabawa.nnpdairy.repository;

import com.mabawa.nnpdairy.models.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface FaqRepository extends JpaRepository<Faq, UUID> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Faq faq WHERE faq.id = :id")
    void deleteFaqById(@Param("id") UUID id);

    List<Faq> findByQuestionContainingIgnoreCase(String qstn);
}
