package com.autobots.automanager.service;

import com.autobots.automanager.model.entidades.Empresa;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class EmpresaService {

    @Autowired
    private RepositorioEmpresa repositorio;

    public List<Empresa> listar() {
        return repositorio.findAll();
    }

    public Empresa buscar(Long id) {
        return repositorio.findById(id).orElse(null);
    }

    @Transactional
    public Empresa salvar(Empresa empresa) {
        if (empresa.getCadastro() == null) empresa.setCadastro(new Date());
        return repositorio.save(empresa);
    }
    
    @Transactional
    public void excluir(Long id) {
        repositorio.deleteById(id);
    }
}