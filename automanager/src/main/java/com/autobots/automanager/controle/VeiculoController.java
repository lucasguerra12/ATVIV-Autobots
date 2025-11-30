package com.autobots.automanager.controle;

import com.autobots.automanager.model.dto.VeiculoDTO;
import com.autobots.automanager.model.entidades.Veiculo;
import com.autobots.automanager.service.VeiculoService;
import com.autobots.automanager.model.converter.VeiculoConverter; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoService service;
    
    @Autowired
    private VeiculoConverter converter;

    @GetMapping
    public ResponseEntity<List<VeiculoDTO>> listar() {
        List<Veiculo> veiculos = service.listar();
        return ResponseEntity.ok(converter.entidadeParaDto(veiculos));
    }

    @PostMapping("/cadastrar")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_GERENTE')")
    public ResponseEntity<VeiculoDTO> cadastrar(@RequestBody VeiculoDTO dto) {
        Veiculo veiculo = converter.dtoParaEntidade(dto);
        Veiculo salvo = service.salvar(veiculo, dto.getProprietarioId());
        return ResponseEntity.ok(converter.entidadeParaDto(salvo));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}