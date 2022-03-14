package com.mabawa.nnpdairy.services;

import com.mabawa.nnpdairy.models.Role;
import com.mabawa.nnpdairy.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getUserRoles(){
        return roleRepository.getAllRoles();
    }

    public Optional<Role> get(final UUID id)
    {
        return roleRepository.findById(id);
    }

    public void delete(final UUID id){
        roleRepository.deleteRolesById(id);
    }

    public void deleteAllRoles()
    {
        roleRepository.deleteAllRoles();
    }
}
