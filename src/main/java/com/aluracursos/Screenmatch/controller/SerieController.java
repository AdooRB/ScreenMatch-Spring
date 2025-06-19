package com.aluracursos.Screenmatch.controller;

import com.aluracursos.Screenmatch.dto.SerieDTO;
import com.aluracursos.Screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SerieController {
    @Autowired
    private SerieRepository repository;

    @GetMapping("/series")
    public List<SerieDTO> obtenerSeries(){
        return repository.findAll().stream()
                .map(s -> new SerieDTO(s.getTitulo(), s.getTotalDeTemporadas(), s.getEvaluacion(), s.getActores(), s.getGenero(), s.getSinopsis(), s.getPoster()))
                .collect(Collectors.toList());
    }
}