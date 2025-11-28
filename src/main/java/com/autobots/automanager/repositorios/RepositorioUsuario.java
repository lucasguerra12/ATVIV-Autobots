package com.autobots.automanager.repositorios;

import java.util.Optional; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.autobots.automanager.entitades.Usuario;

@Repository
public interface RepositorioUsuario extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u JOIN u.credenciais c WHERE c.nomeUsuario = :nomeUsuario")
    Optional<Usuario> findByNomeUsuario(String nomeUsuario);
}