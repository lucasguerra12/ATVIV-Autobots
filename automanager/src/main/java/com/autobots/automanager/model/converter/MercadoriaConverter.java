package com.autobots.automanager.model.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.autobots.automanager.model.dto.MercadoriaDTO;
import com.autobots.automanager.model.entidades.Mercadoria;

@Component
public class MercadoriaConverter {

    public Mercadoria dtoParaEntidade(MercadoriaDTO dto) {
        Mercadoria entidade = new Mercadoria();

        entidade.setId(dto.getId());
        entidade.setValidade(dto.getValidade());
        entidade.setFabricao(dto.getFabricao());
        entidade.setCadastro(dto.getCadastro());
        entidade.setNome(dto.getNome());
        entidade.setQuantidade(dto.getQuantidade());
        entidade.setValor(dto.getValor());
        entidade.setDescricao(dto.getDescricao());

        return entidade;
    }

    public MercadoriaDTO entidadeParaDto(Mercadoria entidade) {
        MercadoriaDTO dto = new MercadoriaDTO();

        dto.setId(entidade.getId());
        dto.setValidade(entidade.getValidade());
        dto.setFabricao(entidade.getFabricao());
        dto.setCadastro(entidade.getCadastro());
        dto.setNome(entidade.getNome());
        dto.setQuantidade(entidade.getQuantidade());
        dto.setValor(entidade.getValor());
        dto.setDescricao(entidade.getDescricao());

        return dto;
    }

    public List<MercadoriaDTO> entidadeParaDto(List<Mercadoria> entidades) {
        return entidades.stream()
                .map(this::entidadeParaDto)
                .collect(Collectors.toList());
    }
}