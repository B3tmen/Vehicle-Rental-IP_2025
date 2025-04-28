package org.unibl.etf.carrentalbackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.unibl.etf.carrentalbackend.security.CustomUserDetailsService;
import org.unibl.etf.carrentalbackend.security.jwt.JwtFilter;

import static org.unibl.etf.carrentalbackend.util.Constants.EndpointUrls.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final int BCRYPT_STRENGTH = 12;
    private final CustomUserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;


    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(req -> req
//                        .requestMatchers("/**").permitAll()
                        .requestMatchers(API_AUTH_URL + "/login").permitAll()       // Public login endpoint
                        //.requestMatchers(API_VEHICLES_URL).permitAll()                 // Example public endpoint
                        .requestMatchers(API_RSS_URL).permitAll()

                        .requestMatchers(RESOURCES_CLIENT_IMAGES_URL).permitAll()
                        .requestMatchers(RESOURCES_VEHICLE_IMAGES_URL).permitAll()

                        .anyRequest().authenticated()                               // All other endpoints require auth
                )
                //.httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        //authProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());   // FOR TESTING!
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService);

        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCRYPT_STRENGTH);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
