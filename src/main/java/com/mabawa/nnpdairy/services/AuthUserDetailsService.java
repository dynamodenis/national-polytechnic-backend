package com.mabawa.nnpdairy.services;

import com.mabawa.nnpdairy.models.Role;
import com.mabawa.nnpdairy.models.User;
import com.mabawa.nnpdairy.models.UserPrincipal;
import com.mabawa.nnpdairy.repository.RoleRepository;
import com.mabawa.nnpdairy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> user = this.repository.getUserzByName(s);

        (user.get()).setRoles(this.getUserRoles((user.get()).getRole()));

        user.orElseThrow(() -> new UsernameNotFoundException("Not Found: " + s));
        return user.map(UserPrincipal::new).get();
    }

    private List<Role> getUserRoles(UUID roleId) {
        List<Role> roleList = new ArrayList();
        Optional<Role> roleOptional = this.roleRepository.findById(roleId);
        if (roleOptional.isPresent()) {
            roleList.add((Role)roleOptional.get());
        }

        return roleList;
    }
}
