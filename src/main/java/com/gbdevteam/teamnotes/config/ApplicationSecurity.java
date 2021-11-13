package com.gbdevteam.teamnotes.config;

import com.gbdevteam.teamnotes.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
@Slf4j
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    //-- Uncomment the block below to disable security (32-35)

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/**");
//    }

    //-- comment the block below to disable security (40-57)

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("Dao Authentication Provider");
        http.csrf().disable()
//        http.httpBasic()
//                .and()
//                .csrf().csrfTokenRepository(csrfTokenRepository()).and()
                .authorizeRequests()
                .antMatchers("/*.js").permitAll()
                .antMatchers("/*.css").permitAll()
                .antMatchers("/promo.html").permitAll()
                .antMatchers("/signup").permitAll()
                .anyRequest().authenticated().and()
                .formLogin().and()
                .logout()
                .logoutSuccessUrl("/promo.html");
//                .and()
//                .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);

        http.headers().disable();//for h2-console frame, disable in production
    }



    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(userService.passwordEncoder());
        authenticationProvider.setUserDetailsService(userService);
        return authenticationProvider;
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }
}
