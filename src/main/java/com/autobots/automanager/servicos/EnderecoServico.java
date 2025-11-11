package com.autobots.automanager.servicos;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelo.EnderecoAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;

// Não precisamos do EnderecoRepositorio aqui, pois o Cliente gerencia o Endereco
// via CascadeType.ALL e orphanRemoval=true.

@Service
public class EnderecoServico {

	@Autowired
	private ClienteRepositorio clienteRepositorio;

	/**
	 * Atualiza ou define o endereço de um cliente.
	 * Se o cliente não tiver endereço, o novo é definido.
	 * Se o cliente já tiver, o existente é atualizado.
	 * * Retorna true se o cliente foi encontrado e o endereço atualizado/definido.
	 * Retorna false se o cliente não foi encontrado.
	 */
	public boolean atualizarEndereco(long clienteId, Endereco atualizacao) {
		Optional<Cliente> clienteOpcional = clienteRepositorio.findById(clienteId);
		
		if (clienteOpcional.isPresent()) {
			Cliente cliente = clienteOpcional.get();
			Endereco enderecoAtual = cliente.getEndereco();
			
			if (enderecoAtual != null) {
				// Cliente já tem endereço, vamos atualizá-lo
				EnderecoAtualizador atualizador = new EnderecoAtualizador();
				atualizador.atualizar(enderecoAtual, atualizacao);
			} else {
				// Cliente não tem endereço, vamos definir o novo
				cliente.setEndereco(atualizacao);
			}
			
			// O CascadeType.ALL cuidará de salvar o endereço junto com o cliente
			clienteRepositorio.save(cliente);
			return true;
		}
		return false; // Cliente não encontrado
	}

	/**
	 * Exclui o endereço de um cliente (define como nulo).
	 * * Retorna true se o cliente foi encontrado.
	 * Retorna false se o cliente não foi encontrado.
	 */
	public boolean excluirEndereco(long clienteId) {
		Optional<Cliente> clienteOpcional = clienteRepositorio.findById(clienteId);
		
		if (clienteOpcional.isPresent()) {
			Cliente cliente = clienteOpcional.get();
			// Definir o endereço como nulo
			cliente.setEndereco(null);
			// O orphanRemoval=true cuidará de excluir o endereço antigo do banco
			clienteRepositorio.save(cliente);
			return true;
		}
		return false; // Cliente não encontrado
	}
}