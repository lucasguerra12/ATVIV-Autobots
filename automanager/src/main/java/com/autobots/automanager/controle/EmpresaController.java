package com.autobots.automanager.controle;

import com.autobots.automanager.model.dto.EmpresaDTO;
import com.autobots.automanager.model.entidades.Empresa;
import com.autobots.automanager.service.EmpresaService;
import com.autobots.automanager.model.converter.EmpresaConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService service;
    
    @Autowired
    private EmpresaConverter converter;

    @GetMapping
    public ResponseEntity<List<EmpresaDTO>> listar() {
        return ResponseEntity.ok(converter.entidadeParaDto(service.listar()));
    }

    @PostMapping("/cadastrar")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<EmpresaDTO> cadastrar(@RequestBody EmpresaDTO dto) {
        Empresa empresa = converter.dtoParaEntidade(dto);
        return ResponseEntity.ok(converter.entidadeParaDto(service.salvar(empresa)));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}