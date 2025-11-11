package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.autobots.automanager.modelo.EmpresaAtualizador;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.modelo.AdicionadorLinkEmpresa;


@RestController
@RequestMapping("/empresa") 
public class EmpresaControle {

    @Autowired
    private RepositorioEmpresa repositorio;

    @Autowired
    private AdicionadorLinkEmpresa adicionadorLink;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> obterEmpresa(@PathVariable long id) {
        Optional<Empresa> empresaOpcional = repositorio.findById(id);
        
        if (empresaOpcional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Empresa empresa = empresaOpcional.get();
            adicionadorLink.adicionarLink(empresa);
            return new ResponseEntity<Empresa>(empresa, HttpStatus.OK);
        }
    }

    @GetMapping("/empresas")
    public ResponseEntity<List<Empresa>> obterEmpresas() {
        List<Empresa> empresas = repositorio.findAll();
        
        if (empresas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            adicionadorLink.adicionarLink(empresas);
            return new ResponseEntity<>(empresas, HttpStatus.OK);
        }
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Empresa> cadastrarEmpresa(@RequestBody Empresa empresa) {
        try {
            empresa.setCadastro(new Date());

            Empresa novaEmpresa = repositorio.save(empresa);

            adicionadorLink.adicionarLink(novaEmpresa);
            
          
            return new ResponseEntity<>(novaEmpresa, HttpStatus.CREATED);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Empresa> atualizarEmpresa(@PathVariable long id, @RequestBody Empresa atualizacao) {
        Optional<Empresa> empresaOpcional = repositorio.findById(id);
        
        if (empresaOpcional.isEmpty()) {
            // Se não encontrar, retorna 404 Not Found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Empresa empresa = empresaOpcional.get();

            EmpresaAtualizador atualizador = new EmpresaAtualizador();
            atualizador.atualizar(empresa, atualizacao);

            Empresa empresaAtualizada = repositorio.save(empresa);

            adicionadorLink.adicionarLink(empresaAtualizada);
            
            return new ResponseEntity<>(empresaAtualizada, HttpStatus.OK);
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluirEmpresa(@PathVariable long id) {
        Optional<Empresa> empresaOpcional = repositorio.findById(id);
        
        if (empresaOpcional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            
            repositorio.deleteById(id);
            
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PostMapping("/empresa/{id}/cadastrarUsuario")
    public ResponseEntity<Empresa> cadastrarUsuarioNaEmpresa(
            @PathVariable long id,
            @RequestBody Usuario novoUsuario) {
        
        Optional<Empresa> empresaOpcional = repositorio.findById(id);
        
        if (empresaOpcional.isEmpty()) {
            // Se a empresa não existe, não podemos adicionar o usuário
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Empresa empresa = empresaOpcional.get();
            
            // Adiciona o novo usuário à lista de usuários da empresa
            empresa.getUsuarios().add(novoUsuario);
            
            // O CascadeType.ALL na entidade Empresa fará com que o 
            // novoUsuario seja salvo no banco quando salvarmos a empresa.
            Empresa empresaAtualizada = repositorio.save(empresa);

            // Adicionamos os links HATEOAS à empresa atualizada
            adicionadorLink.adicionarLink(empresaAtualizada);
            
            // Retorna 201 Created com a empresa atualizada
            return new ResponseEntity<>(empresaAtualizada, HttpStatus.CREATED);
        }
    }

}