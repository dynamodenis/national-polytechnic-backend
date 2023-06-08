package com.mabawa.nnpdairy.services;

import com.mabawa.nnpdairy.models.Appointments;
import com.mabawa.nnpdairy.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Appointments> findAllAppointments(){
        return  appointmentRepository.findAll();
    }

    public Optional<Appointments> findById(UUID id){
        return appointmentRepository.findById(id);
    }

    public Optional<Appointments> getUserAppointment(UUID uzer, UUID consultant, Integer status){
        return appointmentRepository.getUserAppointment(uzer, consultant, status);
    }

    public List<Appointments> getConsultantAppointments(UUID conId)
    {
        return appointmentRepository.findAllByConsultant(conId);
    }

    public List<Appointments> getUserAppointments(UUID userId)
    {
        return appointmentRepository.findAllByAppuser(userId);
    }

    public List<Appointments> filterConsultantAppointments(UUID conId, Integer status){
        return  appointmentRepository.findAllByConsultantAndStatus(conId, status);
    }

    public List<Appointments> filterUserAppointments(UUID appuser, Integer status){
        return  appointmentRepository.findAllByAppuserAndStatus(appuser, status);
    }

    public List<Appointments> getAppointmentListBetweenStime(UUID consultant, Timestamp stime, Timestamp etime, Integer status){
        return appointmentRepository.findAppointmentsByConsultantAndStimeBetweenAndStatusIsNot(consultant, stime, etime, status);
    }

    public Appointments create(Appointments appointments){
        return  appointmentRepository.saveAndFlush(appointments);
    }

    public Appointments update(Appointments appointments){
        return  appointmentRepository.save(appointments);
    }

    public void updateAppointmentStatus(Integer status, UUID id){
        appointmentRepository.updateAppointmentsById(status, id);
    }

    public void deleteById(UUID id){
        appointmentRepository.deleteAppointmentsById(id);
    }

    public void deleteAppointmentByConsultant(UUID consultant){
        appointmentRepository.deleteAppointmentsByConsultant(consultant);
    }

    public void  deleteAppointmentByUser(UUID uzer){
        appointmentRepository.deleteAppointmentsByAppuser(uzer);
    }

    public void  deleteAllAppointment(){
        appointmentRepository.deleteAllAppointments();
    }
}
