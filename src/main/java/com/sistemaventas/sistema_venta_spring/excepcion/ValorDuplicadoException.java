package com.sistemaventas.sistema_venta_spring.excepcion;

/**
 * Se lanza cuando un valor que debe ser unico ya existe en otro registro.
 */
public class ValorDuplicadoException extends RuntimeException {

    private final String campo;

    /**
     * Crea la excepcion indicando el campo repetido y el mensaje para el usuario.
     *
     * @param campo nombre del campo repetido (el mismo que usa el formulario)
     * @param mensaje explicacion del problema
     */
    public ValorDuplicadoException(String campo, String mensaje) {
        super(mensaje);
        this.campo = campo;
    }

    /**
     * Devuelve el campo repetido.
     *
     * @return nombre del campo
     */
    public String getCampo() {
        return campo;
    }
}