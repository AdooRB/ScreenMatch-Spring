package com.aluracursos.Screenmatch.model;

public enum Categoria {
    ACCION("Action"),
    ROMANCE("Romance"),
    Comedia("Comedy"),
    DRAMA("Drama"),
    CRIMEN("Crime");

    private String categoriaOmbd;

    Categoria (String categoriaOmbd){
        this.categoriaOmbd = categoriaOmbd
    }
}
