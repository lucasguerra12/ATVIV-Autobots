package com.autobots.automanager.servicos;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@Service // Indica ao Spring que esta é uma classe de serviço
public class UsuarioServico {

    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    /**
     * Lógica de negócio para excluir um usuário.
     * Remove o usuário de todas as empresas que o contêm.
     * O CascadeType.ALL + orphanRemoval=true cuidará da exclusão física no banco.
     */
    public void excluirUsuario(Usuario usuario) {
        // Encontra todas as empresas
        List<Empresa> empresas = repositorioEmpresa.findAll();
            
        // Itera por cada empresa e remove este usuário da lista dela
        for (Empresa empresa : empresas) {
            // O método .removeIf usa o .equals() do usuário para o encontrar
            empresa.getUsuarios().remove(usuario);
        }
            
        // Salva as empresas (o orphanRemoval=true vai apagar o usuário)
        repositorioEmpresa.saveAll(empresas);
    }
}