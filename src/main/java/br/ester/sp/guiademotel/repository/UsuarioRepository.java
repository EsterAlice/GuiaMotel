package br.ester.sp.guiademotel.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.ester.sp.guiademotel.model.Usuario;

public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Long> {
	public Usuario findByEmailAndSenha(String email, String senha);
}
