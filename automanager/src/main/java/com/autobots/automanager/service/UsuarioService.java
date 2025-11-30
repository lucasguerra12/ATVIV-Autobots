package com.autobots.automanager.service;

import com.autobots.automanager.model.entidades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private RepositorioUsuario repositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listarTodos() {
        return repositorio.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return repositorio.findById(id).orElse(null);
    }

    @Transactional
    public Usuario salvar(Usuario usuario) {
        if (usuario.getCadastro() == null) {
            usuario.setCadastro(new Date());
        }
        if (usuario.getCredencial() != null) {
            String senhaPura = usuario.getCredencial().getSenha();
            String senhaCodificada = passwordEncoder.encode(senhaPura);
            usuario.getCredencial().setSenha(senhaCodificada);
        }
        return repositorio.save(usuario);
    }

    @Transactional
    public Usuario atualizar(Long id, Usuario atualizacao) {
        Usuario usuario = buscarPorId(id);
        if (usuario != null) {
            usuario.setNome(atualizacao.getNome());
            usuario.setNomeSocial(atualizacao.getNomeSocial());
            if (atualizacao.getEndereco() != null) {
                usuario.setEndereco(atualizacao.getEndereco());
            }
            return repositorio.save(usuario);
        }
        return null;
    }

    @Transactional
    public void excluir(Long id) {
        repositorio.deleteById(id);
    }
}