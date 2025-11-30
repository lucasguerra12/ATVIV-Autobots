package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.autobots.automanager.model.entidades.Mercadoria;

public interface RepositorioMercadoria extends JpaRepository<Mercadoria, Long> {
}