package br.com.freteex.application.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.com.freteex.domain.model.Usuario;
import br.com.freteex.domain.repository.UsuarioRepository;

@Service
public class FreteService {

    private static final List<String> STATUS_VALIDOS = Arrays.asList("PROCESSANDO", "SAIU PARA ENTREGA", "ENTREGADO", "FALHA");

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Cacheable(value = "freteCache", key = "#usuario.estado", cacheManager = "cacheManager")
    public Usuario processarPedido(Usuario usuario) {
        usuario.setStatus("PROCESSANDO");
        usuario.setDataPedido(LocalDateTime.now());
        Double valorFrete = calcularFrete(usuario.getEstado());

        if (valorFrete == null) {
            usuario.setStatus("FALHA");
            usuario.setValorFrete(null);
        } else {
            usuario.setValorFrete(valorFrete);
        }
        return usuarioRepository.save(usuario);
    }

    private Double calcularFrete(String estado) {
        if (estado == null) return null;

        switch (estado.toUpperCase()) {
            case "SP":
            case "PR":
                return 0.0;
            case "RJ":
            case "SC":
            case "RS":
                return 20.0;
            case "MG":
            case "MT":
            case "MS":
            case "ES":
                return 50.0;
            default:
                return null;
        }
    }

    public List<Usuario> listarTodosPedidos() {
    return usuarioRepository.findAll();
    }


    public Usuario atualizarStatus(String id, String novoStatus) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isEmpty()) {
            return null;
        }
        novoStatus = novoStatus.toUpperCase().replace("\"", ""); // limpar aspas

        if (!STATUS_VALIDOS.contains(novoStatus)) {
            throw new IllegalArgumentException("Status deve ser um dos seguintes: " + STATUS_VALIDOS);
        }

        Usuario usuario = optionalUsuario.get();
        usuario.setStatus(novoStatus);
        return usuarioRepository.save(usuario);
    }
}
