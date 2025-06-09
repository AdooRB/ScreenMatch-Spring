package com.aluracursos.Screenmatch.principal;

import com.aluracursos.Screenmatch.model.*;
import com.aluracursos.Screenmatch.repository.SerieRepository;
import com.aluracursos.Screenmatch.service.ConsumoAPI;
import com.aluracursos.Screenmatch.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=1432478b";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosSerie> datosSeries = new ArrayList<>();
    private SerieRepository repositorio;
    private List<Serie> series;

    public Principal(SerieRepository repository) {
        this.repositorio = repository;
    }

    public void muestraMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series 
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    4 - Buscar series por titulo
                    5 - Top 5 mejores series
                    6 - Buscar series por categoría
                    7 - Buscar series por temporadas y evaluación
                    8 - Burcar episodios por titulo
                                   
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case  3:
                    mostrarSeriesBuscadas();
                    break;
                case 4: 
                    buscarSeriesPorTitulo();
                    break;
                case 5:
                    buscaTop5Series();
                    break;
                case 6:
                    buscaSeriesPorCategoria();
                    break;
                case 7:
                    buscaTemporadasYEvaluacion();
                    break;
                case 8:
                    buscarEpisodioPorTitulo();
                    break;
               case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }
    private void buscarEpisodioPorSerie() {
        mostrarSeriesBuscadas();
        System.out.println("Escribe el nombre de la serie deseada");
        var nombreSerie = teclado.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s-> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();

        if (serie.isPresent()){
            var serieEncontrada = serie.get();
            List<DatosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalDeTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(e.numeroEpisodio(),e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);

        }
    }
    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        Serie serie = new Serie(datos);
        repositorio.save(serie);
        //datosSeries.add(datos);
        System.out.println(datos);
    }

    private void mostrarSeriesBuscadas() {
        series = repositorio.findAll();

        //System.out.println(series);
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriesPorTitulo() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        Optional<Serie> serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);

        if (serieBuscada.isPresent()){
            System.out.println("La serie es: " + serieBuscada.get());
        } else {
            System.out.println("Serie no encontrada");
        }
    }

    private void  buscaTop5Series(){
        List<Serie> topSeries = repositorio.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(s ->
                System.out.println("Serie: " + s.getTitulo() + " Evaluación: " + s.getEvaluacion()));
    }

    private void buscaSeriesPorCategoria(){
        System.out.println("Escriba el genero/categoría de la serie que desea buscar");
        var genero = teclado.nextLine();
        var categoria = Categoria.fromEspanol(genero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Las series de la catgoría " + genero);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void buscaTemporadasYEvaluacion(){
        System.out.println("Escirbe la evaluación de la serie");
        var evaluacion = teclado.nextDouble();
        System.out.println("Escribe el número de temporadas de la serie");
        var temporadas = teclado.nextInt();

        // Metodo con Query Deriveds
//        List<Serie> seriesPorTemporadaYEvaluacion = repositorio.findByTotalDeTemporadasLessThanEqualAndEvaluacionGreaterThanEqual(temporadas, evaluacion);

        //Metodo con SQL nativo
//        List<Serie> seriesPorTemporadaYEvaluacion = repositorio.seriePorTemporadaYEvaluacion();

        //Metodo con JPQL
        List<Serie> seriesPorTemporadaYEvaluacion = repositorio.seriePorTemporadaYEvaluacionJPQL(evaluacion,temporadas);

        if (!seriesPorTemporadaYEvaluacion.isEmpty()){
            System.out.println("\nSe encontró con -  EVALUACION >= " + evaluacion + ",  TEMPORADAS <= " + temporadas);
            seriesPorTemporadaYEvaluacion.stream()
                            .sorted(Comparator.comparing(Serie::getEvaluacion).reversed())
                            .forEach(s -> System.out.println("       " + s.getTitulo() + "   (ev.: " + s.getEvaluacion() + " , tmp.: " + s.getTotalDeTemporadas() + ")"));

        }else {
            System.out.println("No se encontró ninguna serie con las características añadidas  -  TEMPORADAS: " + temporadas + " , EVALUACION: " + evaluacion);
        }
    }

    private void buscarEpisodioPorTitulo(){
        System.out.println("Escribe el nombre del episodio que deseas buscar");
        var nombreEpisodio = teclado.nextLine();
        List<Episodio> episodioBuscado = repositorio.espisodiosPorNombre(nombreEpisodio);
        episodioBuscado.forEach( e ->
                System.out.println(String.format("Serie: %s , Episodio: %s , Temporada: %s , Evaluación: %s",
                        e.getSerie().getTitulo(), e.getTitulo(), e.getNumeroEpisodio(), e.getEvaluacion())));
    }
}
