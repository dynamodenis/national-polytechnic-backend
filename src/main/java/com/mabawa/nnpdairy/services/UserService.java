package com.mabawa.nnpdairy.services;

import com.mabawa.nnpdairy.models.Role;
import com.mabawa.nnpdairy.models.User;
import com.mabawa.nnpdairy.repository.RoleRepository;
import com.mabawa.nnpdairy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<User> getAllUsers()
    {
        List<User> userz = userRepository.findAll();
        userz.forEach(user -> {
            user.setRoles(this.getUserRoles(user.getRole()));
        });

        return userz;
    }

    public Optional<User> getUserById(UUID id){
        return userRepository.findById(id);
    }

    public Optional<User> getUserByName(String name){
        return userRepository.findByName(name);
    }

    public Optional<User> getUserByPhone(String name){
        return userRepository.getUserzByPhone(name);
    }

    public Optional<User> getUserByMail(String name){
        return userRepository.getUserzByMail(name);
    }

    public List<User> getNNpUsers(Integer typ){
        return  userRepository.findByType(typ);
    }

    public List<User> getEndUsers(Integer typ){
        return userRepository.findByTypeNot(typ);
    }

    public User create(User saveUser){
        return  userRepository.saveAndFlush(saveUser);
    }

    public User update(User updateUser){
        return userRepository.save(updateUser);
    }

    public List<User> findAllByRole(UUID roleId){
        return userRepository.findAllByRole(roleId);
    }

    public void updateUserPassword(String psw, UUID userId){
        updateUserPassword(psw, userId);
    }

    public void deleteUserById(UUID userId){
        userRepository.deleteUserById(userId);
    }

    public Optional<User> verifyOtp(String fone, Integer otp){
        return userRepository.verifyOtp(fone, otp);
    }

    public User getUserviaParam(int prm, UUID id, String nme) {
        new User();
        User userz = null;
        Optional<User> nameOptional;
        if (prm == 1) {
            nameOptional = this.userRepository.findById(id);
            if (nameOptional.isPresent()) {
                userz = (User)nameOptional.get();
                userz.setRoles(this.getUserRoles(userz.getRole()));
            }
        } else if (prm == 2) {
            nameOptional = this.userRepository.getUserzByPhone(nme);
            if (nameOptional.isPresent()) {
                userz = nameOptional.get();
                userz.setRoles(this.getUserRoles(userz.getRole()));
            }
        }else if (prm == 3) {
            nameOptional = this.userRepository.findByName(nme);
            if (nameOptional.isPresent()) {
                userz = nameOptional.get();
                userz.setRoles(this.getUserRoles(userz.getRole()));
            }
        }
        else if (prm == 4) {
            nameOptional = this.userRepository.getUserzByMail(nme);
            if (nameOptional.isPresent()) {
                userz = nameOptional.get();
                userz.setRoles(this.getUserRoles(userz.getRole()));
            }
        }

        return userz;
    }

    public List<Role> getUserRoles(UUID roleId) {
        List<Role> roleList = new ArrayList();
        Optional<Role> roleOptional = this.roleRepository.findById(roleId);
        if (roleOptional.isPresent()) {
            roleList.add((Role)roleOptional.get());
        }

        return roleList;
    }


}
