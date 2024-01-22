package com.sdu.usermanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.sdu.usermanagement.model.Role;
import com.sdu.usermanagement.service.UserService;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Autowired
	private JwtFilter jwtFilter;

	@Autowired
	private LogoutHandler logoutHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(request -> request
				.requestMatchers("/auth/**")
						.permitAll()
						.requestMatchers("/roles/**").hasRole(Role.RoleName.ADMIN.toString())
						.requestMatchers("/api/v1/admin").hasAuthority(Role.RoleName.ADMIN.toString())
				.requestMatchers("/genders/**").hasAuthority(Role.RoleName.ADMIN.toString())
				.requestMatchers(HttpMethod.GET,"/departments").hasAnyAuthority(Role.RoleName.ADMIN.toString(), Role.RoleName.USER.toString())
                .requestMatchers(HttpMethod.POST, "/departments").hasAuthority(Role.RoleName.ADMIN.toString())
                .requestMatchers(HttpMethod.PUT, "/departments").hasAuthority(Role.RoleName.ADMIN.toString())
				.requestMatchers(HttpMethod.DELETE, "/departments").hasAuthority(Role.RoleName.ADMIN.toString())
				.requestMatchers(HttpMethod.GET,"/sections").hasAnyAuthority(Role.RoleName.ADMIN.toString(), Role.RoleName.USER.toString())
				.requestMatchers(HttpMethod.POST, "/sections").hasAuthority(Role.RoleName.ADMIN.toString())
				.requestMatchers(HttpMethod.PUT, "/sections").hasAuthority(Role.RoleName.ADMIN.toString())
				.requestMatchers(HttpMethod.DELETE, "/sections").hasAuthority(Role.RoleName.ADMIN.toString())
				.requestMatchers(HttpMethod.GET,"/users").hasAnyAuthority(Role.RoleName.ADMIN.toString(), Role.RoleName.USER.toString())
				.requestMatchers(HttpMethod.POST, "/users").hasAuthority(Role.RoleName.ADMIN.toString())
				.requestMatchers(HttpMethod.PUT, "/users").hasAuthority(Role.RoleName.ADMIN.toString())
				.requestMatchers(HttpMethod.DELETE, "/users").hasAuthority(Role.RoleName.ADMIN.toString())
				.requestMatchers("/profile_images").hasAnyAuthority(Role.RoleName.ADMIN.toString(), Role.RoleName.USER.toString())
				.requestMatchers("/genders").hasAuthority(Role.RoleName.ADMIN.toString())
				.requestMatchers("/roles").hasAuthority(Role.RoleName.ADMIN.toString())
				.anyRequest().authenticated())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authenticationProvider(authenticationProvider())
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
			.logout(logout -> logout
				.logoutUrl("/auth/logout")
				.addLogoutHandler(logoutHandler)
				.logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
				
			);

		
		return http.build();
					
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserService();
	}
	
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
	
}
