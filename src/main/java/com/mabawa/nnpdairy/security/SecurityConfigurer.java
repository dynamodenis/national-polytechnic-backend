package com.mabawa.nnpdairy.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    protected void configure(HttpSecurity http) throws Exception {
        ((HttpSecurity) ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) ((HttpSecurity) ((HttpSecurity) http.cors().and()).csrf().disable()).authorizeRequests().antMatchers(new String[]{"/api/v1/user/login", "/api/v1/user/user-registration", "/api/v1/user/verify-phoneNumber", "/api/v1/user/contact-us", "/api/v1/user/sendSMSSema/{phone}", "/api/v1/user/resendCode", "/api/v1/", "/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/ui", "/configuration/security", "/nnpdairy/", "/swagger-ui.html", "/webjars/**", "/configuration/**"})).permitAll().anyRequest()).authenticated().and()).sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(this.jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
