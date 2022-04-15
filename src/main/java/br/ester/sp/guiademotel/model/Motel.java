package br.ester.sp.guiademotel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Motel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	@Column(columnDefinition = "TEXT")
	private String descricao;
	private String cep;
	private String endereco;
	private String numero;
	private String bairro;
	private String cidade;
	private String estado;
	private String evento;
	private String formasPagamento;
	private String horario;
	private String site;
	private String telefone;
	private boolean estacionamento;
	@ManyToOne
	private TipoMotel tipo;
	@Column(columnDefinition = "TEXT")
	private String fotos;
	private int preco;
	private String qtdDePessoas;
	private boolean cafeDaManha;
	private boolean tematico;
	private boolean banheira;
	private boolean brinquedo;
	private boolean abafadorSom;
	private boolean espelhoTeto;
	private boolean hidromassagem;
	private boolean sauna;
	private boolean piscina;
	private boolean poleDance;
	private boolean preservativo;
	private boolean acessorioMulher;
	
	// retorna as fotos na forma de vetor de String
	public String[] verFotos() {
		return fotos.split(";");
	}
}
