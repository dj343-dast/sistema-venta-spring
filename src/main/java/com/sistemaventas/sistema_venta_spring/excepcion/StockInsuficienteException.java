package com.sistemaventas.sistema_venta_spring.excepcion;

/**
 * Se lanza cuando se intenta vender mas unidades de las que hay en inventario.
 */
public class StockInsuficienteException extends RuntimeException {

    /**
     * Crea la excepcion con el mensaje que se mostrara al usuario.
     *
     * @param mensaje explicacion del stock disponible y del solicitado
     */
    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}