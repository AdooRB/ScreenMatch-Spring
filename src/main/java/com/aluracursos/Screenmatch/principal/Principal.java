package com.aluracursos.Screenmatch.principal;

import com.aluracursos.Screenmatch.model.DatosEpisodio;
import com.aluracursos.Screenmatch.model.DatosSerie;
import com.aluracursos.Screenmatch.model.DatosTemporadas;
import com.aluracursos.Screenmatch.service.ConsumoAPI;
import com.aluracursos.Screenmatch.service.ConvierteDatos;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY =  "&apikey=1432478b";
    private ConvierteDatos conversor = new ConvierteDatos();

    public void muestraMenu(){
        System.out.println("Por favor escribe el nombre de la seria que quieras ingresar: ");

        //Busca los datos generales de la serie
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ","+") + API_KEY);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);

        //Busca los datos de las temporadas
        List<DatosTemporadas> temporadas = new ArrayList<>();
        for (int i = 1; i <= datos.totalDeTemporadas(); i++) {
            json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + "&Season=" + i + API_KEY);
            var datosTemporadas = conversor.obtenerDatos(json,DatosTemporadas.class);
            temporadas.add(datosTemporadas);
        }
        //Mostrar los objetos DatosTemporada
        //temporadas.forEach(System.out::println);

/*
        //Mostrar los episodios por "manera conversional"
        for (int i = 0; i < datos.totalDeTemporadas() ; i++) {
            List<DatosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
            System.out.println("*************** Temporada: " + temporadas.get(i).numero() + "  ******************");
            for (int j = 0; j < episodiosTemporada.size(); j++) {
                System.out.println(episodiosTemporada.get(j).titulo());
            }
        }*/

        //Mostrar los episodios por "función lambda"
            temporadas.forEach(t ->{
                System.out.println("************Temporada " + t.numero() + " ***************");
                t.episodios().forEach(e -> System.out.println(e.titulo()));
            });
    }
}
