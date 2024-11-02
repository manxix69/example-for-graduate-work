package ru.skypro.homework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.utils.LogShifter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class WebSecurityConfig {

    private final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
    private final LogShifter shifter = LogShifter.getLogShifter();

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**"
            ,"/swagger-ui.html"
            ,"/v3/api-docs"
            ,"/webjars/**"
            ,"/login"
            ,"/register"
            ,"/ads"
//            ,"/ads/**"
            ,"/photo/**"
//            ,"/ads/**/comments/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        shifter.log(logger,"Started method filterChain: {}", http.getSharedObjects().values());

        http.csrf( (csrf) -> csrf.disable())
                .authorizeHttpRequests(
                        authorization ->
                                authorization
//                                        .mvcMatchers("/ads/**")
//                                        .hasRole("ADMIN")
//                                        .hasAuthority("ROLE_ADMIN")
                                        .mvcMatchers(AUTH_WHITELIST)
                                        .permitAll()
                                        .mvcMatchers("/ads/**", "/users/**")
                                        .authenticated()
                )
                .cors(withDefaults())
//                .and()
                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        shifter.log(logger, "Started method passwordEncoder");
        return new BCryptPasswordEncoder();
    }
}