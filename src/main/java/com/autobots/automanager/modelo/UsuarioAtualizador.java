package com.autobots.automanager.modelo;

import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.modelo.StringVerificadorNulo; 

public class UsuarioAtualizador {

    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    public void atualizar(Usuario usuario, Usuario atualizacao) {
        
        if (!verificador.verificar(atualizacao.getNome())) {
            usuario.setNome(atualizacao.getNome());
        }
        if (!verificador.verificar(atualizacao.getNomeSocial())) {
            usuario.setNomeSocial(atualizacao.getNomeSocial());
        }

        if (atualizacao.getPerfis() != null && !atualizacao.getPerfis().isEmpty()) {
            usuario.getPerfis().clear();
            usuario.getPerfis().addAll(atualizacao.getPerfis());
        }
    }
}