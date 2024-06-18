package org.swp.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.Md4PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.swp.enums.UserRole;
import org.swp.service.UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    @Autowired
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request

//                        .requestMatchers("api/v1/**").permitAll()
//                        .requestMatchers("/api/v1/auth/**").permitAll()
//                        .requestMatchers("api/v1/admin").hasAnyAuthority(UserRole.ADMIN.name())
//                        .requestMatchers("api/v1/shop-owner").hasAnyAuthority(UserRole.SHOP_OWNER.name())
//                        .requestMatchers("api/v1/customer").hasAnyAuthority(UserRole.CUSTOMER.name())
//                        .anyRequest().authenticated()
                                .anyRequest().permitAll()

                )

                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
//
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(request -> request
//                        .requestMatchers("/api/v1/auth/signup").permitAll()
//                        .requestMatchers("/api/v1/auth/signin").permitAll()
//                        .requestMatchers("/api/v1/auth/refresh").permitAll()
//                        .requestMatchers("/api/v1/auth/**").authenticated() // Require authentication for other endpoints under /api/v1/auth
//                        .anyRequest().permitAll() // Permit all other requests not specified above
//                )
//                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authenticationProvider(authenticationProvider())
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService.userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
