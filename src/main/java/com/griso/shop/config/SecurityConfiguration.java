package com.griso.shop.config;

import com.griso.shop.filter.JwtRequestFilter;
import com.griso.shop.service.UserSecurityDetailsService;
import com.griso.shop.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserSecurityDetailsService userSecurityDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider(userSecurityDetailsService));
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // Secure the endpoints with Bearer authentication
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()

                // user
                .antMatchers(HttpMethod.GET, Constants.ENDPOINT.URL_USER).authenticated()
                .antMatchers(HttpMethod.PUT, Constants.ENDPOINT.URL_USER).authenticated()
                .antMatchers(HttpMethod.DELETE, Constants.ENDPOINT.URL_USER).authenticated()

                // Admin services
                .antMatchers(Constants.ENDPOINT.URL_ADMIN + "/**").hasAnyRole(Constants.ROLE.ADMIN)

                .and()
                .exceptionHandling()

                .and()
                .sessionManagement()

                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(UserSecurityDetailsService userSecurityDetailsService) {
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setPasswordEncoder(passwordEncoder());
        dao.setUserDetailsService(userSecurityDetailsService);

        return dao;
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
