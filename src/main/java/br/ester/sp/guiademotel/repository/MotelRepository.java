package br.ester.sp.guiademotel.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.ester.sp.guiademotel.model.Motel;

public interface MotelRepository extends PagingAndSortingRepository<Motel, Long>{
		
}
