package com.aluracursos.Screenmatch.repository;

import com.aluracursos.Screenmatch.model.Categoria;
import com.aluracursos.Screenmatch.model.Episodio;
import com.aluracursos.Screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie,Long> {

    Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);

    List<Serie> findTop5ByOrderByEvaluacionDesc();

    List<Serie> findByGenero(Categoria categoria);

    //Con Query Derived
   List<Serie> findByTotalDeTemporadasLessThanEqualAndEvaluacionGreaterThanEqual(Integer temporadas, Double evaluacion);

   //Con SQL nativo
   @Query (value = "SELECT * FROM series WHERE series.total_de_temporadas <= 3 AND series.evaluacion >= 8.6", nativeQuery = true)
   List<Serie> seriePorTemporadaYEvaluacion();

   //Con JPQL
    @Query ("SELECT s FROM Serie s WHERE s.totalDeTemporadas <= :temporadas AND s.evaluacion >= :evaluacion")
    List<Serie> seriePorTemporadaYEvaluacionJPQL(Double evaluacion, Integer temporadas);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:nombreEpisodio%")
    List<Episodio> espisodiosPorNombre(String nombreEpisodio);
}
