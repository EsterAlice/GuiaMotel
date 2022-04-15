package br.ester.sp.guiademotel.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.ester.sp.guiademotel.model.Administrador;

public interface AdminRepository extends PagingAndSortingRepository<Administrador, Long> {
	public Administrador findByEmailAndSenha(String email, String senha);
}
