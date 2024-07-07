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
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.swp.entity.User;
import org.swp.enums.UserRole;
import org.swp.repository.IUserRepository;
import org.swp.service.UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    @Autowired
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private final UserService userService;
    @Autowired
    private IUserRepository userRepository;

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

                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/google")
                        .successHandler(new SimpleUrlAuthenticationSuccessHandler("https://pet-spa-391.vercel.app"))// trả ve gì
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(this.oidcUserService())
                                .userService(this.oauth2UserService())
                        )
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

    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return request -> {
            OAuth2User oAuth2User = delegate.loadUser(request);

            String email = oAuth2User.getAttribute("email");
            String Name = oAuth2User.getAttribute("given_name");
            String phoneNumber = oAuth2User.getAttribute("phone_number");

            // Kiểm tra và tạo mới người dùng nếu chưa tồn tại

            userRepository.findByEmail(email).orElseGet(() -> {
                User user = new User();
                user.setEmail(email);
                user.setFirstName(Name);
                user.setPhone(phoneNumber);
                user.setUsername(email);
                user.setPassword("123456");
                user.setRole(UserRole.CUSTOMER);
                return userRepository.save(user);
            });

            return oAuth2User;
        };
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        OidcUserService delegate = new OidcUserService();
        return request -> {
            OidcUser oidcUser = delegate.loadUser(request);

            String email = oidcUser.getAttribute("email");
            String Name = oidcUser.getAttribute("given_name");
            String phoneNumber = oidcUser.getAttribute("phone_number");

            // Kiểm tra và tạo mới người dùng nếu chưa tồn tại
            userRepository.findByEmail(email).orElseGet(() -> {
                User user = new User();
                user.setEmail(email);
                user.setFirstName(Name);
                user.setPhone(phoneNumber);
                user.setUsername(email);
                user.setPassword("123456");
                user.setRole(UserRole.CUSTOMER);
                return userRepository.save(user);
            });
            return oidcUser;
        };
    }

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
