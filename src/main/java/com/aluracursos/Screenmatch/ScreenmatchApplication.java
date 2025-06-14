package com.aluracursos.Screenmatch;

import com.aluracursos.Screenmatch.model.DatosEpisodio;
import com.aluracursos.Screenmatch.model.DatosSerie;
import com.aluracursos.Screenmatch.model.DatosTemporadas;
import com.aluracursos.Screenmatch.principal.Principal;
import com.aluracursos.Screenmatch.repository.SerieRepository;
import com.aluracursos.Screenmatch.service.ConsumoAPI;
import com.aluracursos.Screenmatch.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication {

	@Autowired
	private SerieRepository repository;
	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

}
