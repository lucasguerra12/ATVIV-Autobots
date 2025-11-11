package com.autobots.automanager.servicos;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@Service
public class TelefoneServico {

	@Autowired
	private ClienteRepositorio clienteRepositorio;

	@Autowired
	private TelefoneRepositorio telefoneRepositorio;

	/**
	 * Adiciona um telefone a um cliente.
	 * Retorna true se o cliente foi encontrado e o telefone adicionado.
	 * Retorna false se o cliente não foi encontrado.
	 */
	public boolean adicionarTelefone(long clienteId, Telefone novoTelefone) {
		Optional<Cliente> clienteOpcional = clienteRepositorio.findById(clienteId);
		if (clienteOpcional.isPresent()) {
			Cliente cliente = clienteOpcional.get();
			cliente.getTelefones().add(novoTelefone);
			clienteRepositorio.save(cliente); // Cascade.ALL salva o novoTelefone
			return true;
		}
		return false;
	}

	/**
	 * Atualiza um telefone.
	 * Retorna true se o telefone foi encontrado e atualizado.
	 * Retorna false se o telefone não foi encontrado.
	 */
	public boolean atualizarTelefone(long telefoneId, Telefone atualizacao) {
		Optional<Telefone> telefoneOpcional = telefoneRepositorio.findById(telefoneId);
		if (telefoneOpcional.isPresent()) {
			Telefone telefone = telefoneOpcional.get();
			// Usamos o atualizador que já existia
			TelefoneAtualizador atualizador = new TelefoneAtualizador();
			atualizador.atualizar(telefone, atualizacao);
			
			telefoneRepositorio.save(telefone);
			return true;
		}
		return false;
	}

	/**
	 * Exclui um telefone de um cliente.
	 * Retorna true se ambos foram encontrados e o telefone excluído.
	 * Retorna false se o cliente ou o telefone não foi encontrado.
	 */
	public boolean excluirTelefone(long clienteId, long telefoneId) {
		Optional<Cliente> clienteOpcional = clienteRepositorio.findById(clienteId);
		
		if (clienteOpcional.isPresent()) {
			Cliente cliente = clienteOpcional.get();
			// Procuramos o telefone na lista do cliente
			Optional<Telefone> telefoneOpcional = cliente.getTelefones().stream()
					.filter(tel -> tel.getId().equals(telefoneId))
					.findFirst();
			
			if (telefoneOpcional.isPresent()) {
				Telefone telefone = telefoneOpcional.get();
				cliente.getTelefones().remove(telefone);
				clienteRepositorio.save(cliente); // orphanRemoval=true exclui o telefone
				return true;
			}
		}
		return false; // Cliente ou telefone não encontrado
	}
}