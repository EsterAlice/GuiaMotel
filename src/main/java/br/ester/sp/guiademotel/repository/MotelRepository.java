package br.ester.sp.guiademotel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.ester.sp.guiademotel.model.Motel;

public interface MotelRepository extends PagingAndSortingRepository<Motel, Long>{
		@Query("SELECT m FROM Motel m WHERE m.tipo.id = :id")
		public List<Motel> findByTipoId(@Param("id")Long id);
}
