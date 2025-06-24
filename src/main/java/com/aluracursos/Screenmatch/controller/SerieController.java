package com.aluracursos.Screenmatch.controller;

import com.aluracursos.Screenmatch.dto.SerieDTO;
import com.aluracursos.Screenmatch.repository.SerieRepository;
import com.aluracursos.Screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SerieController {
    @Autowired
    private SerieService servicio;

    @GetMapping("/series")
    public List<SerieDTO> obtenerSeries(){
        return servicio.obtenerSeries();
    }

    @GetMapping("/series/top5")
    public List<SerieDTO> obtenerTop5(){
        return servicio.obtenerTop5();
    }
}