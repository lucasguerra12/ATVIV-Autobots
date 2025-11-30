package com.autobots.automanager.model.dto;

import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MercadoriaDTO extends RepresentationModel<MercadoriaDTO> {
    private Long id;
    private Date validade;
    private Date fabricao;
    private Date cadastro;
    private String nome;
    private long quantidade;
    private double valor;
    private String descricao;
}