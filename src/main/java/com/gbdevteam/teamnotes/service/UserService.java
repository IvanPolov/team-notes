package com.gbdevteam.teamnotes.service;

import com.gbdevteam.teamnotes.model.Role;
import com.gbdevteam.teamnotes.model.User;
import com.gbdevteam.teamnotes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements GenericService<User> , UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    public List<User> findAllByBoardId(UUID boardId) {
        return userRepository.findAllByBoards_Id(boardId);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    public User findByEmail(String email){
        return userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", email)));
    }

    @Override
    public UUID create(User user) {
        if(findByEmail(user.getEmail()) != null) return null;
        List<Role> roles = new ArrayList<>();
        roles.add(roleService.findByName("USER"));
        user.setRoles(roles);
        user.setPassword(passwordEncoder().encode(user.getPassword()));
      return  userRepository.save(user).getId();
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }

    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public User save(User user){
        return userRepository.save(user);
    }

    @PostConstruct
    private void init() {
        save(new User("test@mail.com", "test", true, passwordEncoder().encode("12345"), Arrays.asList(roleService.findByName("USER"))));
        save(new User("test2@mail.com", "test2", true, passwordEncoder().encode("12345"), Arrays.asList(roleService.findByName("USER"))));
    }
}
