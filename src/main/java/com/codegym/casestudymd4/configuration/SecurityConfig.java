package com.codegym.casestudymd4.configuration;

import com.codegym.casestudymd4.controller.CustomAccessDeniedHandler;
import com.codegym.casestudymd4.controller.CustomSuccessHandler;
import com.codegym.casestudymd4.service.*;
import com.codegym.casestudymd4.service.implement.CustomerAccountService;
import com.codegym.casestudymd4.service.implement.StaffAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private IStaffAccountService iStaffAccountService;

    @Autowired
    private IRoleService iRoleService;

    @Bean
    public CustomSuccessHandler customSuccessHandle() {
        return new CustomSuccessHandler();
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    //    xac thuc

    @Bean
    public AuthenticationProvider staffAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService((UserDetailsService) iStaffAccountService);
        authenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        //authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain staffFilterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(staffAuthenticationProvider())
                .securityMatcher("/staff_account/**","/products/**", "/order_staff/**", "/brand_category/**", "/brands/**", "/categories/**", "/role/**", "/departments/**", "/home/list", "/user/list", "/staffs/**")
                .formLogin(form -> form
                        .loginPage("/staff_account/login")
                        .failureUrl("/staff_account/login")
                        .loginProcessingUrl("/staff_account/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(customSuccessHandle())
                )
                // Phân quyền cho các URL
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/staff_account/login","/staff_account/register").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(customAccessDeniedHandler())
                )
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(staffAuthenticationProvider())
                .build();
    }

}
