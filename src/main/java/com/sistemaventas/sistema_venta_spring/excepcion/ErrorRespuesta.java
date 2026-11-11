package com.sistemaventas.sistema_venta_spring.excepcion;

import java.util.List;

/**
 * Cuerpo de las respuestas de error de la API.
 *
 * @param estado   codigo HTTP de la respuesta
 * @param mensaje  explicacion general del problema
 * @param detalles lista de problemas concretos, por ejemplo campos invalidos
 */
public record ErrorRespuesta(int estado, String mensaje, List<String> detalles) {
}
