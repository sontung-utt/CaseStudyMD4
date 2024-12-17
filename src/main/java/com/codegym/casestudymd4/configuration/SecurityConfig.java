//package com.codegym.casestudymd4.configuration;
//
//import com.codegym.casestudymd4.controller.CustomAccessDeniedHandler;
//import com.codegym.casestudymd4.controller.CustomSuccessHandler;
//import com.codegym.casestudymd4.service.*;
//import com.codegym.casestudymd4.service.implement.CustomUserDetailService;
//import com.codegym.casestudymd4.service.implement.CustomerAccountService;
//import com.codegym.casestudymd4.service.implement.StaffAccountService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//    @Autowired
//    private CustomUserDetailService customUserDetailService;
//
//    @Autowired
//    private CustomerAccountService customerAccountService;
//
//    @Autowired
//    private StaffAccountService staffAccountService;
//
//    @Autowired
//    private IRoleService iRoleService;
//
//    @Bean
//    public CustomSuccessHandler customSuccessHandle() {
//        return new CustomSuccessHandler();
//    }
//
//    @Bean
//    public CustomAccessDeniedHandler customAccessDeniedHandler() {
//        return new CustomAccessDeniedHandler();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(10);
//    }
//
//    //    xac thuc
//    @Bean
//    public AuthenticationProvider customerAuthenticationProvider() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService((customUserDetailService));
//        authenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
//        //authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }
//
//    @Bean
//    public AuthenticationProvider staffAuthenticationProvider() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService((customerAccountService));
//        authenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
//        //authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }
//
//    @Bean
//    public SecurityFilterChain customerFilterChain(HttpSecurity http) throws Exception {
//        http
//                // Trang login cho khách hàng
//                .securityMatcher("/user/**").formLogin(form -> form
//                        .loginPage("/user/login")
//                        .loginProcessingUrl("/user/login")
//                        .usernameParameter("username")
//                        .passwordParameter("password")
//                        .successHandler(customSuccessHandle())
//                )
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // Cho phép truy cập tài nguyên tĩnh
//                        .requestMatchers("/user/login", "/user/register").permitAll()
//                        .requestMatchers("/staff_account/login").permitAll()
//                        .requestMatchers("/home/**").hasRole("USER") // Chỉ khách hàng
//                        .requestMatchers("/products/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SALE") // Chỉ nhân viên
//                        .anyRequest().authenticated()
//                )
//                .exceptionHandling(ex -> ex
//                        .accessDeniedHandler(customAccessDeniedHandler())
//                )
//                .csrf(AbstractHttpConfigurer::disable);
//
//        return http.build();
//    }
//
//    @Bean
//    public SecurityFilterChain staffFilterChain(HttpSecurity http) throws Exception {
//        http
//                // Trang login cho nhân viên
//                .securityMatcher("/staff_account/**").formLogin(form -> form
//                        .loginPage("/staff_account/login")
//                        .loginProcessingUrl("/staff_account/login")
//                        .usernameParameter("username")
//                        .passwordParameter("password")
//                        .successHandler(customSuccessHandle())
//                )
//                // Phân quyền cho các URL
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // Cho phép truy cập tài nguyên tĩnh
//                        .requestMatchers("/user/login", "/user/register").permitAll()
//                        .requestMatchers("/staff_account/login").permitAll()
//                        .requestMatchers("/home/**").hasRole("USER") // Chỉ khách hàng
//                        .requestMatchers("/products/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SALE") // Chỉ nhân viên
//                        .anyRequest().authenticated()
//                )
//                .exceptionHandling(ex -> ex
//                        .accessDeniedHandler(customAccessDeniedHandler())
//                )
//                .csrf(AbstractHttpConfigurer::disable);
//
//        return http.build();
//    }
//
//}
