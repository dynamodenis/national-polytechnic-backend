package com.mabawa.nnpdairy.services;

import com.mabawa.nnpdairy.models.Appointments;
import com.mabawa.nnpdairy.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Appointments create(Appointments appointments){
        return  appointmentRepository.saveAndFlush(appointments);
    }

    public Appointments update(Appointments appointments){
        return  appointmentRepository.save(appointments);
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
}
