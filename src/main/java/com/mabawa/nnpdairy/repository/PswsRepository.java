package com.mabawa.nnpdairy.repository;

import com.mabawa.nnpdairy.models.Psws;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PswsRepository extends JpaRepository<Psws, Integer> {
    @Query(value = "SELECT * FROM psws WHERE userid = :userid", nativeQuery = true)
    Optional<Psws> getPswsByUserid(@Param("userid") UUID userid);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Psws psws SET psws.lastdate = :last, psws.psw1 = :psw1, psws.psw2 = :psw2, psws.psw3 = :psw3, psws.psw4 = :psw4, psws.psw5 = :psw5 WHERE psws.userid = :userid")
    void updatePsws(@Param("last") Timestamp last, @Param("psw1") String psw1, @Param("psw2") String psw2, @Param("psw3") String psw3, @Param("psw4") String psw4, @Param("psw5") String psw5, @Param("userid") UUID userid);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Psws psws WHERE psws.userid = :userid")
    void deletePsws(@Param("userid") UUID userid);
}
