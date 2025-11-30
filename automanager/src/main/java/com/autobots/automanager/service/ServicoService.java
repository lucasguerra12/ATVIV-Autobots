package com.autobots.automanager.service;

import com.autobots.automanager.model.entidades.Servico;
import com.autobots.automanager.repositorios.RepositorioServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ServicoService {
    @Autowired private RepositorioServico repositorio;

    public List<Servico> listar() { return repositorio.findAll(); }

    @Transactional
    public Servico salvar(Servico servico) { return repositorio.save(servico); }
    
    @Transactional
    public void excluir(Long id) { repositorio.deleteById(id); }
}