package com.autobots.automanager.adaptadores;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Usuario;

@SuppressWarnings("serial")
public class UserDetailsImpl implements UserDetails {
	private Usuario usuario;
	private CredencialUsuarioSenha credencial;

	public UserDetailsImpl(Usuario usuario, CredencialUsuarioSenha credencial) {
		this.usuario = usuario;
		this.credencial = credencial;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return usuario.getPerfis();
	}

	@Override
	public String getPassword() {
		return credencial.getSenha();
	}

	@Override
	public String getUsername() {
		return credencial.getNomeUsuario();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return !credencial.isInativo();
	}
}