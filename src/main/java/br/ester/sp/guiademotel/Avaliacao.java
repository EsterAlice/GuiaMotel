package br.ester.sp.guiademotel;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.ester.sp.guiademotel.model.Motel;
import br.ester.sp.guiademotel.model.Usuario;
import lombok.Data;

@Data
@Entity
public class Avaliacao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private Motel motel;
	private double nota;
	private String comentario;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Calendar dataVisita;
	@ManyToOne
	private Usuario usuario;
}