package com.autobots.automanager.filtros;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.autobots.automanager.adaptadores.UserDetailsServiceImpl;
import com.autobots.automanager.jwt.ProvedorJwt;

public class Autorizador extends BasicAuthenticationFilter {

	private ProvedorJwt provedorJwt;
	private UserDetailsServiceImpl servicoUsuario;

	public Autorizador(AuthenticationManager authenticationManager, ProvedorJwt provedorJwt, UserDetailsServiceImpl servicoUsuario) {
		super(authenticationManager);
		this.provedorJwt = provedorJwt;
		this.servicoUsuario = servicoUsuario;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String cabecalho = request.getHeader("Authorization");

		if (cabecalho == null || !cabecalho.startsWith("Bearer ")) {
			chain.doFilter(request, response);
			return;
		}

		String jwt = cabecalho.replace("Bearer ", "");

		if (provedorJwt.validarJwt(jwt)) {
			String nomeUsuario = provedorJwt.obterNomeUsuario(jwt);
			UserDetails usuario = servicoUsuario.loadUserByUsername(nomeUsuario);
			
			UsernamePasswordAuthenticationToken autenticacao = 
					new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
			
			SecurityContextHolder.getContext().setAuthentication(autenticacao);
		}

		chain.doFilter(request, response);
	}
}