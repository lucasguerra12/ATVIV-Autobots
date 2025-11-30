package com.autobots.automanager.security;

import com.autobots.automanager.jwt.TokenService;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.model.entidades.Usuario;
import com.autobots.automanager.enumeracoes.Perfil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = recuperarToken(request);

        if (token != null) {
            try {
                var login = tokenService.extrairUsername(token);
                Usuario usuario = repositorioUsuario.findByCredencialNomeUsuario(login);
                
                if (usuario != null && tokenService.validarToken(token, login)) {
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    for (Perfil perfil : usuario.getPerfis()) {
                        authorities.add(new SimpleGrantedAuthority(perfil.name()));
                    }
                    
                    UserDetails userDetails = new User(
                        usuario.getCredencial().getNomeUsuario(), 
                        usuario.getCredencial().getSenha(), 
                        authorities
                    );
                    
                    var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}