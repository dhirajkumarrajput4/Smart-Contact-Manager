package com.smart.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfig {

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailServiceImpl();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}

	@Bean
	public AuthenticationManager configureAuthentication(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder =
				http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.authenticationProvider(authenticationProvider());
		return authenticationManagerBuilder.build();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
						.authorizeHttpRequests((authorizeRequests)->
								authorizeRequests
										.requestMatchers("/admin/**")
								.hasRole("ADMIN")
								.requestMatchers("/user/**")
										.hasRole("USER")
								.requestMatchers("/**")
										.permitAll())
				.formLogin( form -> form
						.loginPage("/signin")
						.loginProcessingUrl("/signin")
						.defaultSuccessUrl("/user/index")
						.permitAll() );
		return http.build();

	}


}