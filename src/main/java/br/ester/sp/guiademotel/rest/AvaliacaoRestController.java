package br.ester.sp.guiademotel.rest;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.ester.sp.guiademotel.Avaliacao;
import br.ester.sp.guiademotel.annotation.Privado;
import br.ester.sp.guiademotel.annotation.Publico;
import br.ester.sp.guiademotel.repository.AvaliacaoRepository;

@RestController
@RequestMapping("/api/avaliacao")
public class AvaliacaoRestController {
	@Autowired
	private AvaliacaoRepository repository;
	
	@Privado
	@RequestMapping(value="", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Avaliacao> criarAvaliacao(@RequestBody Avaliacao avaliacao){
		repository.save(avaliacao);
		return ResponseEntity.created(URI.create("/api/avaliacao/"+avaliacao.getId())).body(avaliacao);
	}
	@Publico
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public Avaliacao getById(@PathVariable("id") Long idAvaliacao) {
		return repository.findById(idAvaliacao).get();
	}
	@Publico
	@RequestMapping(value="/motel/{id}", method = RequestMethod.GET)
	public List<Avaliacao> getByMotel(@PathVariable("id") Long idMotel) {
		return repository.findByMotelId(idMotel);
	}
}
