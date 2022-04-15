package br.ester.sp.guiademotel.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.api.Http;

import br.ester.sp.guiademotel.model.Administrador;
import br.ester.sp.guiademotel.repository.AdminRepository;
import br.ester.sp.guiademotel.util.HashUtil;

@Controller
public class AdministradorController {
	// Variavel para persistência dos dados
	@Autowired
	private AdminRepository repository;

	@RequestMapping(value = "formularioA", method = RequestMethod.GET)
	public String form() {
		return "formAdm";
	}

	@RequestMapping(value = "salvarAdmin", method = RequestMethod.POST)
	public String salvarAdmin(@Valid Administrador admin, BindingResult result, RedirectAttributes attr) {
		// verifica se houveram erros na validação
		if (result.hasErrors()) {
			// adiciona uma mensagem de erro
			attr.addFlashAttribute("mensagemErro", "Verifica os campos...");
			// redireciona para o formulário
			return "redirect:formularioA";
		}
		// variável para descobrir alteração ou inserção
		boolean alteracao = admin.getId() != null ? true : false;
		// verifica se a semha está vazia
		if (admin.getSenha().equals(HashUtil.hash(""))) {
			if (!alteracao) {
				// retirar a parte antes do @ no e-mail
				String parte = admin.getEmail().substring(0, admin.getEmail().indexOf("@"));
				// "setar" a parte na senha do admin
				admin.setSenha(parte);
			} else {
				// buscar a senha atual no banco de dados
				String hash = repository.findById(admin.getId()).get().getSenha();
				// "setar" o hash na senha
				admin.setSenhaComHash(hash);
			}
		}

		try {
			// Salva no bd a entidade
			repository.save(admin);
			// adiciona uma mensagem de sucesso
			attr.addFlashAttribute("mensagemSucesso", "Administrador cadastrado com sucesso. ID: " + admin.getId());
		} catch (Exception e) {
			// Adiciona uma mensagem de erro
			attr.addFlashAttribute("mensagemErro", "Houve um erro ao cadastrar: " + e.getMessage());
		}
		// Redireciona para o formulário
		return "redirect:formularioA";

	}

	// Request mapping para listar administradores
	@RequestMapping("listaAdmin/{page}")
	public String listaAdmin(Model model, @PathVariable("page") int page) {
		// Cria um pageable informando os parâmetros da página
		PageRequest pageable = PageRequest.of(page - 1, 6, Sort.by(Sort.Direction.ASC, "nome"));
		// Cria um page de administrador através dos parâmetros passados ao repository
		Page<Administrador> pagina = repository.findAll(pageable);
		// Adiciona a model, a lista com os admins
		model.addAttribute("admins", pagina.getContent());
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
		return "listaAdm";
	}

	@RequestMapping("alterarAdm")
	public String altera(Long id, Model model) {
		Administrador administrador = repository.findById(id).get();
		model.addAttribute("administrador", administrador);
		return "forward:formularioA";
	}

	@RequestMapping("excluirAdm")
	public String excluir(Long id) {
		repository.deleteById(id);
		return "redirect:listaAdmin/1";
	}
	@RequestMapping("login")
	public String login(Administrador admLogin, RedirectAttributes attr, HttpSession session) {
		// buscar o administrador no Banco
		Administrador admin = repository.findByEmailAndSenha(admLogin.getEmail(), admLogin.getSenha());
		// verifica se existe
		if(admin == null) {
			attr.addFlashAttribute("mensagemErro", "Login e/ou senha inválido(s)");
			return "redirect:/";
		}else {
			// salva o adm na sessão
			session.setAttribute("usuarioLogado", admin);
			return "redirect:/listaMotel/1";
		}
	}
}
