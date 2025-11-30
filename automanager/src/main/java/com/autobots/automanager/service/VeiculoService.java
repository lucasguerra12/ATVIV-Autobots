package com.autobots.automanager.service;

import com.autobots.automanager.model.entidades.Usuario;
import com.autobots.automanager.model.entidades.Veiculo;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VeiculoService {

    @Autowired
    private RepositorioVeiculo repositorio;
    
    @Autowired
    private RepositorioUsuario repositorioUsuario;

    public List<Veiculo> listar() {
        return repositorio.findAll();
    }

    public Veiculo buscar(Long id) {
        return repositorio.findById(id).orElse(null);
    }

    @Transactional
    public Veiculo salvar(Veiculo veiculo, Long donoId) {
        if (donoId != null) {
            Usuario dono = repositorioUsuario.findById(donoId).orElse(null);
            veiculo.setProprietario(dono);
        }
        return repositorio.save(veiculo);
    }

    @Transactional
    public void excluir(Long id) {
        repositorio.deleteById(id);
    }
}