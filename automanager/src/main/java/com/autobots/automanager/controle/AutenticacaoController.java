package com.autobots.automanager.controle;

import com.autobots.automanager.jwt.TokenService;
import com.autobots.automanager.model.entidades.Credencial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Credencial dados) {
        try {
            var tokenAuth = new UsernamePasswordAuthenticationToken(dados.getNomeUsuario(), dados.getSenha());
            var auth = manager.authenticate(tokenAuth);
            var token = tokenService.gerarToken(dados.getNomeUsuario());
            
            return ResponseEntity.ok(new TokenResponse(token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
    
    public record TokenResponse(String token) {}
}