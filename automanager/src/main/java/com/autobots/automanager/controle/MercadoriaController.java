package com.autobots.automanager.controle;

import com.autobots.automanager.model.dto.MercadoriaDTO;
import com.autobots.automanager.model.entidades.Mercadoria;
import com.autobots.automanager.service.MercadoriaService;
import com.autobots.automanager.model.converter.MercadoriaConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mercadorias")
public class MercadoriaController {
    @Autowired private MercadoriaService service;
    @Autowired private MercadoriaConverter converter;

    @GetMapping
    public ResponseEntity<List<MercadoriaDTO>> listar() {
        return ResponseEntity.ok(converter.entidadeParaDto(service.listar()));
    }

    @PostMapping("/cadastrar")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_GERENTE')")
    public ResponseEntity<MercadoriaDTO> cadastrar(@RequestBody MercadoriaDTO dto) {
        Mercadoria m = converter.dtoParaEntidade(dto);
        return ResponseEntity.ok(converter.entidadeParaDto(service.salvar(m)));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}