package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; // Importante
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.modelo.AdicionadorLinkUsuario;
import com.autobots.automanager.modelo.UsuarioAtualizador;
import com.autobots.automanager.repositorios.RepositorioUsuario;
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
            
            // --- PRIVACIDADE: Cliente só vê o próprio cadastro ---
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String userLogado = auth.getName();
            
            boolean isStaff = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ADMINISTRADOR") || 
                               r.getAuthority().equals("GERENTE") || 
                               r.getAuthority().equals("VENDEDOR"));
            
            if (!isStaff) {
                // Se não é staff, verifica se o usuário buscado é ele mesmo
                boolean eOProprio = usuario.getCredenciais().stream()
                    .filter(c -> c instanceof CredencialUsuarioSenha)
                    .map(c -> (CredencialUsuarioSenha) c)
                    .anyMatch(c -> c.getNomeUsuario().equals(userLogado));
                
                if (!eOProprio) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            // ----------------------------------------------------

            adicionadorLink.adicionarLink(usuario); 
            return new ResponseEntity<>(usuario, HttpStatus.OK);
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

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
        if (usuario.getId() == null) {
            repositorio.save(usuario);
            adicionadorLink.adicionarLink(usuario);
            return new ResponseEntity<>(usuario, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable long id, @RequestBody Usuario atualizacao) {
        Optional<Usuario> usuarioOpcional = repositorio.findById(id);
        if (usuarioOpcional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Usuario usuario = usuarioOpcional.get();
            UsuarioAtualizador atualizador = new UsuarioAtualizador();
            atualizador.atualizar(usuario, atualizacao);
            repositorio.save(usuario);
            adicionadorLink.adicionarLink(usuario);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
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