package com.autobots.automanager;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.enumeracoes.Perfil;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@SpringBootApplication
public class AutomanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutomanagerApplication.class, args);
	}

	@Component
	public static class Runner implements ApplicationRunner {
		@Autowired
		public RepositorioUsuario repositorio;

		@Override
		public void run(ApplicationArguments args) throws Exception {
			if (repositorio.findAll().isEmpty()) {
				Calendar calendario = Calendar.getInstance();
				calendario.set(2002, 05, 15);

				Usuario usuario = new Usuario();
				usuario.setNome("Pedro Alcântara de Bragança e Bourbon");
				usuario.setNomeSocial("Dom Pedro");
				
				usuario.getPerfis().add(Perfil.ADMINISTRADOR); 
				usuario.getPerfis().add(Perfil.CLIENTE);

				Telefone telefone = new Telefone();
				telefone.setDdd("21");
				telefone.setNumero("981234576");
				usuario.getTelefones().add(telefone);

				Endereco endereco = new Endereco();
				endereco.setEstado("Rio de Janeiro");
				endereco.setCidade("Rio de Janeiro");
				endereco.setBairro("Copacabana");
				endereco.setRua("Avenida Atlântica");
				endereco.setNumero("1702");
				endereco.setCodigoPostal("22021001");
				endereco.setInformacoesAdicionais("Hotel Copacabana palace");
				usuario.setEndereco(endereco);

				CredencialUsuarioSenha credencial = new CredencialUsuarioSenha();
				credencial.setCriacao(Calendar.getInstance().getTime());
				credencial.setInativo(false);
				credencial.setUltimoAcesso(Calendar.getInstance().getTime());
				credencial.setNomeUsuario("admin");
				credencial.setSenha("admin"); 
				
				usuario.getCredenciais().add(credencial);

				repositorio.save(usuario);
				System.out.println("--- USUÁRIO DE TESTE CRIADO: admin / admin ---");
			}
		}
	}
}