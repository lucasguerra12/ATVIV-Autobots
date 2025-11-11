package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody; 
import com.autobots.automanager.modelo.UsuarioAtualizador;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.modelo.AdicionadorLinkUsuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import org.springframework.web.bind.annotation.DeleteMapping; 

import com.autobots.automanager.servicos.UsuarioServico; 

@RestController
@RequestMapping("/usuario")
public class UsuarioControle {

    @Autowired
    private RepositorioUsuario repositorio;
    @Autowired
    private UsuarioServico servico; 

    @Autowired
    private AdicionadorLinkUsuario adicionadorLink; 
    
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obterUsuario(@PathVariable long id) {
        Optional<Usuario> usuarioOpcional = repositorio.findById(id);
        
        if (usuarioOpcional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Usuario usuario = usuarioOpcional.get();
            adicionadorLink.adicionarLink(usuario); 
            return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
        }
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> obterUsuarios() {
        List<Usuario> usuarios = repositorio.findAll();
        
        if (usuarios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            adicionadorLink.adicionarLink(usuarios); 
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(
            @PathVariable long id,
            @RequestBody Usuario atualizacao) {
        
        Optional<Usuario> usuarioOpcional = repositorio.findById(id);
        
        if (usuarioOpcional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Usuario usuario = usuarioOpcional.get();
            
            UsuarioAtualizador atualizador = new UsuarioAtualizador();
            atualizador.atualizar(usuario, atualizacao);
            
            Usuario usuarioAtualizado = repositorio.save(usuario);
            
            adicionadorLink.adicionarLink(usuarioAtualizado);
            
            return new ResponseEntity<>(usuarioAtualizado, HttpStatus.OK);
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluirUsuario(@PathVariable long id) {
        Optional<Usuario> usuarioOpcional = repositorio.findById(id);
        
        if (usuarioOpcional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Usuario usuario = usuarioOpcional.get();
            servico.excluirUsuario(usuario); 
            
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}