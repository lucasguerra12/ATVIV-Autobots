package com.autobots.automanager.modelo;

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.modelo.StringVerificadorNulo; 

public class EmpresaAtualizador {
    
    private StringVerificadorNulo verificador = new StringVerificadorNulo();
    

    public void atualizar(Empresa empresa, Empresa atualizacao) {
        
        if (!verificador.verificar(atualizacao.getRazaoSocial())) {
            empresa.setRazaoSocial(atualizacao.getRazaoSocial());
        }
        if (!verificador.verificar(atualizacao.getNomeFantasia())) {
            empresa.setNomeFantasia(atualizacao.getNomeFantasia());
        }
    }
}