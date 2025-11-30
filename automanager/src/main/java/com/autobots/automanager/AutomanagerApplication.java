package com.autobots.automanager;

import java.util.Date;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.autobots.automanager.model.entidades.Credencial;
import com.autobots.automanager.model.entidades.Usuario;
import com.autobots.automanager.enumeracoes.Perfil;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@SpringBootApplication
public class AutomanagerApplication implements CommandLineRunner {

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }

    public static void main(String[] args) {
        SpringApplication.run(AutomanagerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (repositorioUsuario.findByCredencialNomeUsuario("admin") == null) {
            Usuario admin = new Usuario();
            admin.setNome("Administrador Master");
            admin.setNomeSocial("Admin");
            admin.setCadastro(new Date());
            admin.getPerfis().add(Perfil.ROLE_ADMIN);

            Credencial cred = new Credencial();
            cred.setNomeUsuario("admin");
            cred.setSenha(passwordEncoder.encode("admin123")); 
            
            admin.setCredencial(cred);
            repositorioUsuario.save(admin);
            System.out.println("Usuário ADMIN criado.");
        }
    }
}