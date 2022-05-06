package br.ester.sp.guiademotel.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.ester.sp.guiademotel.Avaliacao;

public interface AvaliacaoRepository extends PagingAndSortingRepository<Avaliacao, Long>  {
	public List<Avaliacao> findByMotelId(Long idMotel);
}
