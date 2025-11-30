package com.autobots.automanager.service;

import com.autobots.automanager.model.entidades.Venda;
import com.autobots.automanager.repositorios.RepositorioVenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class VendaService {
    @Autowired private RepositorioVenda repositorio;

    public List<Venda> listar() { return repositorio.findAll(); }

    @Transactional
    public Venda salvar(Venda venda) {
        if(venda.getCadastro() == null) venda.setCadastro(new Date());
        return repositorio.save(venda);
    }
    
    @Transactional
    public void excluir(Long id) { repositorio.deleteById(id); }
}