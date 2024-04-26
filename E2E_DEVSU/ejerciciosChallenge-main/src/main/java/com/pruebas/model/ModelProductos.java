package com.pruebas.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ModelProductos {
    private final String producto1;
    private final String producto2;
    private final String suma;
    private final String nombre;
    private final String apellido;
    private final String codigoPostal;
}