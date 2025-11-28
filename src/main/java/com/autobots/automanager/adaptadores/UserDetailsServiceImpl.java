package com.autobots.automanager.adaptadores;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entitades.Credencial;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private RepositorioUsuario repositorio;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuarioOp = repositorio.findByNomeUsuario(username);

		if (usuarioOp.isPresent()) {
			Usuario usuario = usuarioOp.get();
			for (Credencial cred : usuario.getCredenciais()) {
				if (cred instanceof CredencialUsuarioSenha) {
					CredencialUsuarioSenha credSenha = (CredencialUsuarioSenha) cred;
					if (credSenha.getNomeUsuario().equals(username)) {
						return new UserDetailsImpl(usuario, credSenha);
					}
				}
			}
		}
		throw new UsernameNotFoundException("Usuário não encontrado: " + username);
	}
}