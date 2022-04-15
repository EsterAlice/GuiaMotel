package br.ester.sp.guiademotel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.ester.sp.guiademotel.model.TipoMotel;


public interface TipoRepository extends PagingAndSortingRepository<TipoMotel, Long> {
	@Query("SELECT b FROM TipoMotel b WHERE b.nome LIKE %:n%")
	public List<TipoMotel> nome(@Param("n")String nome);

	@Query("SELECT b FROM TipoMotel b WHERE b.descricao LIKE %:n%")
	public List<TipoMotel> descricao(@Param("n")String descricao);
	
	@Query("SELECT b FROM TipoMotel b WHERE b.palavraChave LIKE %:n%")
	public List<TipoMotel> palavra(@Param("n")String palavraChave);
	
	public List<TipoMotel> findAllByOrderByNomeAsc();
}
