package br.com.freteex.domain.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import br.com.freteex.domain.model.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
}
