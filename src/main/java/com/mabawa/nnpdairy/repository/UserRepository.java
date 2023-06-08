package com.mabawa.nnpdairy.repository;

import com.mabawa.nnpdairy.models.User;
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
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query(value = "SELECT * FROM userz WHERE name = :name order by name", nativeQuery = true)
    Optional<User> getUserzByName(@Param("name") String name);

    @Query(value = "SELECT * FROM userz WHERE phone = :fone order by name", nativeQuery = true)
    Optional<User> getUserzByPhone(@Param("fone") String fone);

    @Query(value = "SELECT * FROM userz WHERE mail = :mail order by name", nativeQuery = true)
    Optional<User> getUserzByMail(@Param("mail") String mail);

    @Query(value = "SELECT * FROM userz WHERE phone = :fone AND otpnumber = :otp", nativeQuery = true)
    Optional<User> verifyOtp(@Param("fone") String fone, @Param("otp") Integer otp);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User userz SET userz.password = :password WHERE userz.id = :id")
    void updateUserPassword(@Param("password") String password, @Param("id") UUID id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM User userz WHERE userz.id = :id")
    void deleteUserById(@Param("id") UUID id);

    List<User> findAllByRole(UUID roleId);

    List<User> findByTypeNot(Integer type);

    List<User> findByType(Integer type);

    Optional<User> findByName(String name);

    Optional<User> findByNameContaining(String name);
}
