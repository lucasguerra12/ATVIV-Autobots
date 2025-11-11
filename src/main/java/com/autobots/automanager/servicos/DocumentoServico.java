package com.autobots.automanager.servicos;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@Service
public class DocumentoServico {

	@Autowired
	private ClienteRepositorio clienteRepositorio;

	@Autowired
	private DocumentoRepositorio documentoRepositorio;

	public boolean adicionarDocumento(long clienteId, Documento novoDocumento) {
		Optional<Cliente> clienteOpcional = clienteRepositorio.findById(clienteId);
		if (clienteOpcional.isPresent()) {
			Cliente cliente = clienteOpcional.get();
			cliente.getDocumentos().add(novoDocumento);
			clienteRepositorio.save(cliente); 
			return true;
		}
		return false;
	}

	public boolean atualizarDocumento(long documentoId, Documento atualizacao) {
		Optional<Documento> documentoOpcional = documentoRepositorio.findById(documentoId);
		if (documentoOpcional.isPresent()) {
			Documento documento = documentoOpcional.get();
			DocumentoAtualizador atualizador = new DocumentoAtualizador();
			atualizador.atualizar(documento, atualizacao);
			
			documentoRepositorio.save(documento);
			return true;
		}
		return false;
	}

	public boolean excluirDocumento(long clienteId, long documentoId) {
		Optional<Cliente> clienteOpcional = clienteRepositorio.findById(clienteId);
		
		if (clienteOpcional.isPresent()) {
			Cliente cliente = clienteOpcional.get();
			Optional<Documento> documentoOpcional = cliente.getDocumentos().stream()
					.filter(doc -> doc.getId().equals(documentoId))
					.findFirst();
			
			if (documentoOpcional.isPresent()) {
				Documento documento = documentoOpcional.get();
				cliente.getDocumentos().remove(documento);
				clienteRepositorio.save(cliente);
				return true;
			}
		}
		return false; 
	}
}