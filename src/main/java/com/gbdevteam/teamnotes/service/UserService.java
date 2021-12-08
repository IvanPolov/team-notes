package com.gbdevteam.teamnotes.service;

import com.gbdevteam.teamnotes.dto.UserAuthDto;
import com.gbdevteam.teamnotes.dto.UserDTO;
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
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final EmailSender emailSender;

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    public List<UserDTO> findAllByBoardId(UUID boardId) {
        List<UserDTO> users = userRepository.findAllByBoards_Id(boardId).stream().map(this::convertToDTO).collect(Collectors.toList());
        UserDTO owner = convertToDTO(userRepository.findByMyBoards_Id(boardId));
        users.add(owner);
        return users;
    }

    public UserDTO findById(UUID id) {
        return convertToDTO(findUserById(id));
    }

    public User findUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("user with that id not found"));
    }

    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email);
//        }
    }

    public UUID create(User user) {
        user.setRoles(List.of(roleService.findByName("USER")));
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        user.setDateRegistration(new Date());
        return userRepository.save(user).getId();
    }

    public void update(UserDTO user) {
        userRepository.save(convertToEntity(user));
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


    public UserDTO addNewUser(UserRegAuthDto userRegAuthDto) throws MessagingException, UnsupportedEncodingException {
        User user = new User();
        modelMapper.map(userRegAuthDto, user);
        create(user);
        UserDetails userDetails = loadUserByUsername(user.getEmail());
        Authentication authenticatedUser = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
        generateConfirmUUID(user);
        emailSender.sendEmail(user.getEmail(), user.getConfirmUUID());
        return convertToDTO(user);
    }

    @Transactional
    public boolean login(UserAuthDto userAuthDto) {
        log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Login >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        User user = findByEmail(userAuthDto.getEmail());
        if (user != null) {
            log.info("User is :" + user.toString());
            boolean isPasswordOk = passwordEncoder().matches(userAuthDto.getPassword().subSequence(0, userAuthDto.getPassword().length()), user.getPassword());
            log.info("Result of password check: " + isPasswordOk);
            if (isPasswordOk) {
                UserDetails userDetails = loadUserByUsername(user.getEmail());
                Authentication authenticatedUser = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
                return true;
            }
        }
        return false;
    }

    private void generateConfirmUUID(User user) {
        user.setConfirmUUID(UUID.randomUUID());
        log.error(String.valueOf(UUID.randomUUID()));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private User convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public boolean verifyNewUserEmail(String email, UUID uuId) {
        User newUser = findByEmail(email);
        if (Boolean.FALSE.equals(newUser.getIsVerified())) {
            log.info("\n");
            log.info("\n******************** Confirming a new User with a verification key **********************");
            log.info("Field IsVerified =" + newUser.getIsVerified());
            if (newUser.getConfirmUUID() != null) {
                log.info("******************************************");
                log.info("Field ConfirmUUID =" + newUser.getConfirmUUID());
                if (newUser.getConfirmUUID().compareTo(uuId) == 0) {
                    log.info("******************************************");
                    log.info("Compare uuids result = " + (newUser.getConfirmUUID().compareTo(uuId) == 0 ? "uuId's are equivalent" : "uuId's are not equivalent "));
                    findByEmail(email).setIsVerified(true);
                    findByEmail(email).setConfirmUUID(null);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isExpiredUnverifiedUser(User user) {
        if (Boolean.FALSE.equals(user.getIsVerified())) {
            Date currentDate = new Date();
            return currentDate.getTime() - user.getDateRegistration().getTime() >= 20000L;
        }
        return false;
    }
}
