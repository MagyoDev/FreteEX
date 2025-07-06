
package br.com.freteex.dto;

import lombok.Data;

@Data
public class UsuarioRequest {
    private String nome;
    private String email;
    private String endereco;
    private String estado;
}
