package com.gbdevteam.teamnotes.service;

import com.gbdevteam.teamnotes.dto.UserRegAuthDto;
import com.gbdevteam.teamnotes.model.Role;
import com.gbdevteam.teamnotes.model.User;
import com.gbdevteam.teamnotes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService implements GenericService<User>, UserDetailsService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RoleService roleService;

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    public List<User> findAllByBoardId(UUID boardId) {
        List<User> users = new ArrayList<>(userRepository.findAllByBoards_Id(boardId));
        User owner = userRepository.findByMyBoards_Id(boardId);
        users.add(owner);
        return users;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public UUID create(User user) {
        user.setRoles(List.of(roleService.findByName("USER")));
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        return userRepository.save(user).getId();
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

    public User save(User user) {
        return userRepository.save(user);
    }


    public User addNewUser(UserRegAuthDto userRegAuthDto) {
        User user = new User();
        modelMapper.map(userRegAuthDto, user);
        create(user);
        UserDetails userDetails = loadUserByUsername(user.getEmail());
        Authentication authenticatedUser = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),userDetails.getPassword(),userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
        return user;
    }

}
