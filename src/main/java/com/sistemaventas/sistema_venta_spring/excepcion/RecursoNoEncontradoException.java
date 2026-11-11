package com.sistemaventas.sistema_venta_spring.excepcion;

/**
 * Se lanza cuando un cliente o un producto referenciado no existe.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    /**
     * Crea la excepcion con el mensaje que se mostrara al usuario.
     *
     * @param mensaje explicacion de lo que no se encontro
     */
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}