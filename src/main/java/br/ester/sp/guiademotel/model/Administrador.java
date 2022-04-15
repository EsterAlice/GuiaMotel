package br.ester.sp.guiademotel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import br.ester.sp.guiademotel.util.HashUtil;
import lombok.Data;

// cria os getters & setters
@Data
//mapeia a entidade para o JPA
@Entity
public class Administrador {
	// chave primária e auto-incremento
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty
	@Column(nullable = false)
	private String nome;
	//define a coluna email com um índice único
	@Column(unique = true, nullable = false)
	@Email
	private String email;
	@Column( nullable = false)
	@NotEmpty
	private String senha;
	
	// método set que aplica o hash na senha
	public void setSenha(String senha) {
		this.senha = HashUtil.hash(senha);
	}
	// método que "seta" o hash na senha
	public void setSenhaComHash(String hash) {
		this.senha = hash;
	}
}
