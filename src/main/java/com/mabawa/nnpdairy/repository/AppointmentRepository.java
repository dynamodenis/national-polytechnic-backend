package com.mabawa.nnpdairy.repository;

import com.mabawa.nnpdairy.models.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointments, UUID> {
    @Query(value = "SELECT * FROM appointments WHERE appuser = :uzer AND consultant = :consultant AND status = :status", nativeQuery = true)
    Optional<Appointments> getUserAppointment(@Param("uzer") UUID uzer, @Param("consultant") UUID consultant, @Param("status") Integer status);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Appointments appointments WHERE appointments.id = :id")
    void deleteAppointmentsById(@Param("id") UUID id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Appointments appointments WHERE appointments.consultant = :consultant")
    void deleteAppointmentsByConsultant(@Param("consultant") UUID consultant);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Appointments appointments WHERE appointments.appuser = :appuser")
    void deleteAppointmentsByAppuser(@Param("appuser") UUID appuser);

    List<Appointments> findAllByConsultant(UUID consultant);

    List<Appointments> findAllByAppuser(UUID appuser);

    List<Appointments> findAllByAppuserAndStatus(UUID appuser, Integer status);

    List<Appointments> findAllByConsultantAndStatus(UUID appuser, Integer status);

    List<Appointments> findAppointmentsByConsultantAndStimeBetweenAndStatusIsNot(UUID consultant, Timestamp stime, Timestamp etime, Integer status);
}
