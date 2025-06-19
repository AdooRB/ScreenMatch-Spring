package com.aluracursos.Screenmatch.dto;

import com.aluracursos.Screenmatch.model.Categoria;

public record SerieDTO(
        String titulo,
        Integer totalDeTemporadas,
        Double evaluacion,
        String actores,
        Categoria genero,
        String sinopsis,
        String poster
    ) {

}
