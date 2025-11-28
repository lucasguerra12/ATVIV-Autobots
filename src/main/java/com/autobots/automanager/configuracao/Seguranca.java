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
				// Login público
				.requestMatchers(HttpMethod.POST, "/login").permitAll()
				
				// --- VENDEDOR (Tabela 1: CRUD em Vendas, Serviços, Mercadorias) ---
				.requestMatchers("/venda/**", "/servico/**", "/mercadoria/**").hasAnyAuthority("ADMINISTRADOR", "GERENTE", "VENDEDOR")
				
				// --- GESTÃO DE USUÁRIOS (Tabela 1) ---
				// Criar/Editar: Admin, Gerente e Vendedor (Vendedor pode criar Clientes)
				.requestMatchers(HttpMethod.POST, "/usuario/**").hasAnyAuthority("ADMINISTRADOR", "GERENTE", "VENDEDOR")
				.requestMatchers(HttpMethod.PUT, "/usuario/**").hasAnyAuthority("ADMINISTRADOR", "GERENTE", "VENDEDOR")
				
				// Excluir Usuários: Admin e Gerente (Gerente pode excluir Vendedores/Clientes)
				.requestMatchers(HttpMethod.DELETE, "/usuario/**").hasAnyAuthority("ADMINISTRADOR", "GERENTE")

				// --- EMPRESA ---
				.requestMatchers("/empresa/**").hasAnyAuthority("ADMINISTRADOR", "GERENTE")

				// --- GLOBAL DELETE (Admin) ---
				.requestMatchers(HttpMethod.DELETE, "/**").hasAuthority("ADMINISTRADOR") 
				
				// Resto requer autenticação (incluindo GET para Clientes)
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