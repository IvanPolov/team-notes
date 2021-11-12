package com.gbdevteam.teamnotes.config;

import com.gbdevteam.teamnotes.model.Role;
import com.gbdevteam.teamnotes.model.User;
import com.gbdevteam.teamnotes.repository.RoleRepository;
import com.gbdevteam.teamnotes.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
@Slf4j
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

    final UserService userService;

    final RoleRepository roleRepository;

    //-- Uncomment the block below to disable security (32-35)

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/**");
//    }

    //-- comment the block below to disable security (40-57)

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("Dao Authentication Provider");
        http.csrf().disable().authorizeRequests()
//                .antMatchers("/authenticated/**").authenticated()
//                .antMatchers("/board/**").authenticated()
//                .antMatchers("/note/**").authenticated()
//                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/*.js").permitAll()
                .antMatchers("/*.css").permitAll()
                .antMatchers("/promo.html").permitAll()
                .antMatchers("/signup").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .logout()
                .logoutSuccessUrl("/promo.html");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userService);
        return authenticationProvider;
    }

    @PostConstruct
    private void init() {
        roleRepository.save(new Role("USER"));
        userService.save(new User("test@mail.com", "test", false, passwordEncoder().encode("12345"), roleRepository.findAll()));
        userService.save(new User("test2@mail.com", "test2", false, passwordEncoder().encode("12345"), roleRepository.findAll()));
        log.info(passwordEncoder().encode("12345") + " password: 12345");
    }
}
