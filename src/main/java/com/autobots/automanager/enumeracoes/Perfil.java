package com.autobots.automanager.enumeracoes;

import org.springframework.security.core.GrantedAuthority;

public enum Perfil implements GrantedAuthority {
	ADMINISTRADOR, 
	GERENTE, 
	VENDEDOR, 
	CLIENTE;

	@Override
	public String getAuthority() {
		return this.name();
	}
}