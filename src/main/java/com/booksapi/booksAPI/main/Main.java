package com.booksapi.booksAPI.main;

import com.booksapi.booksAPI.models.Datos;
import com.booksapi.booksAPI.models.DatosLibro;
import com.booksapi.booksAPI.service.ConsumoAPI;
import com.booksapi.booksAPI.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner scanner = new Scanner(System.in);
    public void mostrarMenu(){
        var json = consumoAPI.obtenerDatos(URL_BASE);
        System.out.println(json);
        var datos = conversor.obtenerDatos(json, Datos.class);
        System.out.println(datos);


        //Top 10 libros
        System.out.println("Top 10 de Libros");
        datos.resultados().stream()
                .sorted(Comparator.comparing(DatosLibro::numeroDescargas).reversed())
                .limit(10)
                .map(l -> l.titulo().toUpperCase())
                .forEach(System.out::println);

        //Buscar por nombre
        System.out.println("Ingrese el nombre del libro que desea buscar");
        var tituloLibro = scanner.nextLine();
        json = consumoAPI.obtenerDatos(URL_BASE+"?search="+tituloLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibro> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();

        if (libroBuscado.isPresent()){
            System.out.println("Libro encontrado ");
            System.out.println(libroBuscado.get());
        }else {
            System.out.println("No se encontro el libro");
        }

        //Trabajando con estadisticas
        DoubleSummaryStatistics est = datos.resultados().stream()
                .filter(d -> d.numeroDescargas()>0)
                .collect(Collectors.summarizingDouble(DatosLibro::numeroDescargas));
        System.out.println("Cantidad media de descargas: " + est.getAverage());
        System.out.println("Cantidad maxima de descargas: " + est.getMax());
        System.out.println("Cantidad minima de descargas: " + est.getMin());
        System.out.println("Cantidad de registros evaluados para calcular las estadisticas: " +est.getCount());
    }
}
