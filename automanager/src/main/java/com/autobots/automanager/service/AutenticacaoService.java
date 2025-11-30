package com.autobots.automanager.service;

import com.autobots.automanager.model.entidades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.enumeracoes.Perfil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private RepositorioUsuario repositorio;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repositorio.findByCredencialNomeUsuario(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Perfil perfil : usuario.getPerfis()) {
            authorities.add(new SimpleGrantedAuthority(perfil.name()));
        }

        return new User(usuario.getCredencial().getNomeUsuario(), usuario.getCredencial().getSenha(), authorities);
    }
}