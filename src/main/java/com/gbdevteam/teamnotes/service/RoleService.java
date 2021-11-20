package com.gbdevteam.teamnotes.service;

import com.gbdevteam.teamnotes.model.Role;
import com.gbdevteam.teamnotes.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    private Role addNewRole(Role role){
        return roleRepository.save(role);
    }

    @PostConstruct
    private void initRoles(){
        roleRepository.save(new Role("USER","user of Team Notes service"));
        roleRepository.save(new Role("ADMIN","admin for Team Notes service"));
//        roleRepository.save(new Role("writer","writer of private board, can manage notes"));
//        roleRepository.save(new Role("reader","reader of private board, only can read notes"));
//        roleRepository.save(new Role("manager","can manage users of board, except owner, includes writer rights"));
//        roleRepository.save(new Role("owner","owns private board, have all rights"));
    }

    public List<Role> findAll(){
        return roleRepository.findAll();
    }

    public Role findByName(String name){
        return roleRepository.findByName(name).orElseThrow(()->  new ObjectNotFoundException(Role.class, "Role '%s' not found"));
    }
}
