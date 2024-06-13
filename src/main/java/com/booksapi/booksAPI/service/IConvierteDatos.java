package com.booksapi.booksAPI.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
