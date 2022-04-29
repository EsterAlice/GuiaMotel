package br.ester.sp.guiademotel.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.ester.sp.guiademotel.model.Motel;
import br.ester.sp.guiademotel.repository.MotelRepository;

@RestController
@RequestMapping("/api/motel")
public class MotelRestController {
	@Autowired
	private MotelRepository repository;
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Motel> getMoteis(){
		return repository.findAll();
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Motel> getMotel(@PathVariable("id") Long idMotel){
		// tenta buscar o motel no repository
		Optional<Motel> optional = repository.findById(idMotel);
		// se o motel existir
		if(optional.isPresent()) {
			return ResponseEntity.ok(optional.get());
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	@RequestMapping(value = "/tipo/{id}", method = RequestMethod.GET)
	public List<Motel> getTipo(@PathVariable("id") Long id){
		return repository.findByTipoId(id);
	}
}
