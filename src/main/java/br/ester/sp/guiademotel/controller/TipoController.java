package br.ester.sp.guiademotel.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import br.ester.sp.guiademotel.model.TipoMotel;
import br.ester.sp.guiademotel.repository.TipoRepository;

@Controller
public class TipoController {
	@Autowired
	private TipoRepository repository;

	@RequestMapping(value = "formularioT", method = RequestMethod.GET)
	public String formTipo() {
		return "formTipo";
	}

	@RequestMapping(value = "salvarT", method = RequestMethod.POST)
	public String salvarTipo(TipoMotel tipo) {
		repository.save(tipo);
		return "redirect:formularioT";
	}

	// Request mapping para listar tipos de moteis
	@RequestMapping("listaTipo/{page}")
	public String listaTipo(Model model, @PathVariable("page") int page) {
		// Cria um pageable informando os parâmetros da página
		PageRequest pageable = PageRequest.of(page - 1, 6, Sort.by(Sort.Direction.ASC, "id"));
		// Cria um page de administrador através dos parâmetros passados ao repository
		Page<TipoMotel> pagina = repository.findAll(pageable);
		// Adiciona a model, a lista com os admins
		model.addAttribute("tipos", pagina.getContent());
		// Variável para o total de páginas
		int totalPages = pagina.getTotalPages();
		// Cria um List de inteiros para armazenar os nºs das páginas
		List<Integer> numPaginas = new ArrayList<Integer>();
		// Preencher o list com as páginas
		for (int i = 1; i <= totalPages; i++) {
			// adiciona a página ao list
			numPaginas.add(i);
		}
		// Adiciona os valores á model
		model.addAttribute("numPaginas", numPaginas);
		model.addAttribute("totalPags", totalPages);
		model.addAttribute("pagAtual", page);
		// Retorna para o html da lista
		return "listaTipo";
	}

	@RequestMapping("alterarTipo")
	public String alteraT(Long id, Model model) {
		TipoMotel tipo = repository.findById(id).get();
		model.addAttribute("tipo", tipo);
		return "forward:formularioT";
	}

	@RequestMapping("excluirTipo")
	public String excluirT(Long id) {
		repository.deleteById(id);
		return "redirect:listaTipo/1";
	}

	@RequestMapping("buscar")
	public String buscar(String nome, String select, Model model) {
		if (select == null) {
			model.addAttribute("tipos", repository.palavra(nome));
		} else if (select.equals("nome")) {
			model.addAttribute("tipos", repository.nome(nome));
		} else if (select.equals("descricao")) {
			model.addAttribute("tipos", repository.descricao(nome));
		} else {
			model.addAttribute("tipos", repository.palavra(nome));
		}
		return "listaTipo";
	}
}
