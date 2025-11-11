package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.modelo.AdicionadorLinkEmpresa;

@RestController
@RequestMapping("/empresa") 
public class EmpresaControle {

    @Autowired
    private RepositorioEmpresa repositorio;

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> obterEmpresa(@PathVariable long id) {
        Optional<Empresa> empresaOpcional = repositorio.findById(id);
        
        if (empresaOpcional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Empresa empresa = empresaOpcional.get();
            return new ResponseEntity<Empresa>(empresa, HttpStatus.OK);
        }
    }

    @GetMapping("/empresas")
    public ResponseEntity<List<Empresa>> obterEmpresas() {
        List<Empresa> empresas = repositorio.findAll();
        
        if (empresas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(empresas, HttpStatus.OK);
        }
    }

}