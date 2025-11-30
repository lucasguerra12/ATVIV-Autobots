package com.autobots.automanager.controle;

import com.autobots.automanager.model.dto.VendaDTO;
import com.autobots.automanager.model.entidades.Venda;
import com.autobots.automanager.service.VendaService;
import com.autobots.automanager.model.converter.VendaConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/vendas")
public class VendaController {
    @Autowired private VendaService service;
    @Autowired private VendaConverter converter;

    @GetMapping
    public ResponseEntity<List<VendaDTO>> listar() {
        return ResponseEntity.ok(converter.entidadeParaDto(service.listar()));
    }

    @PostMapping("/cadastrar")
    @PreAuthorize("hasRole('ROLE_VENDEDOR') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<VendaDTO> cadastrar(@RequestBody VendaDTO dto) {
        Venda v = converter.dtoParaEntidade(dto);
        return ResponseEntity.ok(converter.entidadeParaDto(service.salvar(v)));
    }
}