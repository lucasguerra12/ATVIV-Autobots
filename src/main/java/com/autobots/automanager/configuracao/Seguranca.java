package com.autobots.automanager.configuracao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.autobots.automanager.adaptadores.UserDetailsServiceImpl;
import com.autobots.automanager.filtros.Autenticador;
import com.autobots.automanager.filtros.Autorizador;
import com.autobots.automanager.jwt.ProvedorJwt;

@Configuration
@EnableWebSecurity
public class Seguranca {

	@Autowired
	private UserDetailsServiceImpl servicoUsuario;

	@Autowired
	private ProvedorJwt provedorJwt;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(HttpMethod.POST, "/login").permitAll()
				
				.requestMatchers(HttpMethod.DELETE, "/**").hasAuthority("ADMINISTRADOR") 
				
				.requestMatchers(HttpMethod.POST, "/empresa/**", "/servico/**", "/mercadoria/**").hasAnyAuthority("ADMINISTRADOR", "GERENTE")
				.requestMatchers(HttpMethod.PUT, "/empresa/**", "/servico/**", "/mercadoria/**").hasAnyAuthority("ADMINISTRADOR", "GERENTE")

				.requestMatchers(HttpMethod.POST, "/venda/**").hasAnyAuthority("ADMINISTRADOR", "GERENTE", "VENDEDOR")
				.anyRequest().authenticated()
			)
			.addFilter(new Autenticador(authenticationManager, provedorJwt))
			.addFilter(new Autorizador(authenticationManager, provedorJwt, servicoUsuario));

		return http.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@SuppressWarnings("deprecation")
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}