package br.com.freteex.interfaces.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.freteex.application.service.FreteService;
import br.com.freteex.domain.model.Usuario;

@RestController
@RequestMapping("/freteex")
public class FreteController {

    @Autowired
    private FreteService freteService;

    @PostMapping("/calcular")
    public ResponseEntity<?> calcularFrete(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioProcessado = freteService.processarPedido(usuario);
            if ("FALHA".equals(usuarioProcessado.getStatus())) {
                return ResponseEntity.badRequest().body("Frete não realizado para o estado: " + usuario.getEstado());
            }
            return ResponseEntity.ok(usuarioProcessado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar pedido: " + e.getMessage());
        }
    }

    @GetMapping("/pedidos")
    public ResponseEntity<List<Usuario>> listarPedidos() {
        List<Usuario> pedidos = freteService.listarTodosPedidos();
        return ResponseEntity.ok(pedidos);
    }

    @PutMapping("/pedidos/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable String id, @RequestBody String novoStatus) {
        try {
            Usuario atualizado = freteService.atualizarStatus(id, novoStatus);
            if (atualizado == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Status inválido: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao atualizar status: " + e.getMessage());
        }
    }
}
