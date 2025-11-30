package com.autobots.automanager.service;

import com.autobots.automanager.model.entidades.Mercadoria;
import com.autobots.automanager.repositorios.RepositorioMercadoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class MercadoriaService {
    @Autowired private RepositorioMercadoria repositorio;

    public List<Mercadoria> listar() { return repositorio.findAll(); }

    @Transactional
    public Mercadoria salvar(Mercadoria mercadoria) {
        if(mercadoria.getCadastro() == null) mercadoria.setCadastro(new Date());
        return repositorio.save(mercadoria);
    }
    
    @Transactional
    public void excluir(Long id) { repositorio.deleteById(id); }
}