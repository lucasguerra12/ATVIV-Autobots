package com.autobots.automanager.jwt;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class ProvedorJwt {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;

	private SecretKey getChave() {
		return Keys.hmacShaKeyFor(secret.getBytes());
	}

	public String proverJwt(String nomeUsuario) {
		Date agora = new Date();
		Date dataExpiracao = new Date(agora.getTime() + expiration);

		return Jwts.builder()
				.setSubject(nomeUsuario)
				.setIssuedAt(agora)
				.setExpiration(dataExpiracao)
				.signWith(getChave())
				.compact();
	}

	public boolean validarJwt(String jwt) {
		try {
			Jwts.parserBuilder().setSigningKey(getChave()).build().parseClaimsJws(jwt);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String obterNomeUsuario(String jwt) {
		Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(getChave()).build().parseClaimsJws(jwt);
		return claims.getBody().getSubject();
	}
}