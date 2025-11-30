package com.autobots.automanager.controle;

import com.autobots.automanager.model.dto.ServicoDTO;
import com.autobots.automanager.model.entidades.Servico;
import com.autobots.automanager.service.ServicoService;
import com.autobots.automanager.model.converter.ServicoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {
    @Autowired private ServicoService service;
    @Autowired private ServicoConverter converter;

    @GetMapping
    public ResponseEntity<List<ServicoDTO>> listar() {
        return ResponseEntity.ok(converter.entidadeParaDto(service.listar()));
    }

    @PostMapping("/cadastrar")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_GERENTE')")
    public ResponseEntity<ServicoDTO> cadastrar(@RequestBody ServicoDTO dto) {
        Servico s = converter.dtoParaEntidade(dto);
        return ResponseEntity.ok(converter.entidadeParaDto(service.salvar(s)));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}