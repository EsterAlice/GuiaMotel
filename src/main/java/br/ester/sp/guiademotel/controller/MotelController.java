package br.ester.sp.guiademotel.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.ester.sp.guiademotel.model.Motel;
import br.ester.sp.guiademotel.repository.MotelRepository;
import br.ester.sp.guiademotel.repository.TipoRepository;
import br.ester.sp.guiademotel.util.FirebaseUtil;

@Controller
public class MotelController {
	@Autowired
	private TipoRepository repTipo;
	@Autowired
	private MotelRepository repository;
	@Autowired
	private FirebaseUtil fireUtil;
	
	@RequestMapping("formM")
	public String form(Model model) {
		model.addAttribute("tipos", repTipo.findAllByOrderByNomeAsc());
		return "motel/formMotel";
	}
	@RequestMapping("salvarM")
	public String salvar(Motel motel, @RequestParam("fileFotos") MultipartFile[] fileFotos) {
		//String para armazenar as URLs
		String fotos = motel.getFotos();
		// percorre cada arquivo no vetor
		for(MultipartFile arquivo : fileFotos) {
			// verifica se o arquivo existe
			if(arquivo.getOriginalFilename().isEmpty()) {
				// vai para o próximp arquivo
				continue;
			}
		try {
			// faz upload e guarda a URL na string fotos
			fotos += fireUtil.upload(arquivo)+";";
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		}
		//guarda na variável motel as fotos
		motel.setFotos(fotos);
		//salva no BD
		repository.save(motel);
		return "redirect:formM";
	}
	// Request mapping para listar tipos de moteis
		@RequestMapping("listaMotel/{page}")
		public String listaMotel(Model model, @PathVariable("page") int page) {
			// Cria um pageable informando os parâmetros da página
			PageRequest pageable = PageRequest.of(page - 1, 6, Sort.by(Sort.Direction.ASC, "id"));
			// Cria um page de administrador através dos parâmetros passados ao repository
			Page<Motel> pagina = repository.findAll(pageable);
			// Adiciona a model, a lista com os admins
			model.addAttribute("moteis", pagina.getContent());
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
			return "motel/listaMotel";
		}
		@RequestMapping("alterarMotel")
		public String alteraM(Long id, Model model) {
			Motel motel = repository.findById(id).get();
			model.addAttribute("motel", motel);
			return "forward:formM";
		}
		@RequestMapping("/excluirMotel")
		public String excluirM(Long id) {
			Motel rest = repository.findById(id).get();
			if(rest.getFotos().length()> 0) {
				for(String foto : rest.verFotos()) {
					fireUtil.deletar(foto);
				}
			}
			repository.delete(rest);
			return "redirect:listaMotel/1";
		}
		public String excluirFotos(Long idRest, int numFoto, Model model) {
			// Busca o motel
			Motel rest = repository.findById(idRest).get();
			// busca a URL da foto
			String urlFoto = rest.verFotos()[numFoto];
			//deleta a foto
			fireUtil.deletar(urlFoto);
			// remove a url do atributo fotos
			rest.setFotos(rest.getFotos().replace(urlFoto+";", ""));
			//salva no Bd
			repository.save(rest);
			//coloca o rest na Model
			model.addAttribute("motel", rest);
			return "forward:formM";
		}
}
