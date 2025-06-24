package com.aluracursos.Screenmatch.service;

import com.aluracursos.Screenmatch.dto.SerieDTO;
import com.aluracursos.Screenmatch.model.Serie;
import com.aluracursos.Screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repository;

    public List<SerieDTO> obtenerSeries(){
        return convertirDatos(repository.findAll());
    }

    public List<SerieDTO> obtenerTop5() {
        return convertirDatos(repository.findTop5ByOrderByEvaluacionDesc());
    }

    public List<SerieDTO> convertirDatos( List<Serie> serie){
        return  serie.stream()
                .map(s -> new SerieDTO(s.getTitulo(), s.getTotalDeTemporadas(), s.getEvaluacion(), s.getActores(), s.getGenero(), s.getSinopsis(), s.getPoster()))
                .collect(Collectors.toList());
    }
}
