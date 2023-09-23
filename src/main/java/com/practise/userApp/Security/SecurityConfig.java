package com.practise.userApp.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity

@EnableMethodSecurity			//- to provide Method Level Security   (//@EnableGlobalMethodSecurity     -deprecated)
public class SecurityConfig {
	
	
	//AUTHENTICATION
	
	//INMemory userDetails DB  ,PasswordEncoder is a interface ,BCryptPasswordEncoder is one of the implementation
	@Bean   
	public InMemoryUserDetailsManager detailsManager (PasswordEncoder p) {
		UserDetails user=User.withUsername("springer1")   //org.springframework.security.core.userdetails.User
							.password(p.encode("secret"))
							.roles("ADMIN")
							.build();
		UserDetails admin=User.withUsername("springer2")
							  .password(p.encode("secret"))
							  .roles("USER")
							  .build();
		
		return new InMemoryUserDetailsManager(user,admin);
	}
	
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

	
	//AUTHORIZATION
	
	 @Bean
	 SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
			
					 // If you are only creating a service that is used by non-browser clients, you will likely want to disable CSRF protection.
//						http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
//							.requestMatchers(HttpMethod.POST,"/users").hasRole("admin") 
//							.requestMatchers(HttpMethod.GET,"/users","/users/**").hasAnyRole("admin","user")
//							.requestMatchers(HttpMethod.GET,"/v1/users").authenticated()
//							
//							.requestMatchers("/users/**").hasRole("admin")
//							.requestMatchers("/" ,"/h2-console/**").permitAll()
//								.anyRequest().permitAll()); // to permit all to access the security page. orelse the
															// initial page itself wont be loaded
		 
		 http.csrf().disable();
		 http.headers().frameOptions().disable();
		 
		 http
		 	.csrf().disable()
			.authorizeHttpRequests((authorize) -> authorize
				.anyRequest().authenticated()
				
			  ).httpBasic(Customizer.withDefaults());     //.formLogin(withDefaults());  
			
					
			 
			return http.build();
					
	}

}
