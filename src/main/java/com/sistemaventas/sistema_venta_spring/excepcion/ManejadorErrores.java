package com.sistemaventas.sistema_venta_spring.excepcion;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Convierte las excepciones de la aplicacion en respuestas HTTP con un
 * mensaje claro. Aplica a todos los controladores.
 */
@RestControllerAdvice
public class ManejadorErrores {

    /**
     * Un recurso referenciado no existe (cliente, producto o venta).
     *
     * @param excepcion la excepcion lanzada
     * @return respuesta 404 con el mensaje de la excepcion
     */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorRespuesta> noEncontrado(RecursoNoEncontradoException excepcion) {
        return construir(HttpStatus.NOT_FOUND, excepcion.getMessage(), List.of());
    }

    /**
     * Se pidio mas stock del disponible.
     *
     * @param excepcion la excepcion lanzada
     * @return respuesta 409 con el stock disponible y el solicitado
     */
    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<ErrorRespuesta> stockInsuficiente(StockInsuficienteException excepcion) {
        return construir(HttpStatus.CONFLICT, excepcion.getMessage(), List.of());
    }

    /**
     * Fallo alguna validacion de los datos enviados (@NotBlank, @Min, etc.).
     *
     * @param excepcion la excepcion con la lista de campos invalidos
     * @return respuesta 400 con un detalle por cada campo rechazado
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRespuesta> datosInvalidos(MethodArgumentNotValidException excepcion) {
        List<String> detalles = excepcion.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return construir(HttpStatus.BAD_REQUEST, "Los datos enviados no son validos", detalles);
    }

    /**
     * La base de datos rechazo la operacion: valor unico repetido o registro
     * con datos relacionados.
     *
     * @param excepcion la excepcion lanzada por la base de datos
     * @return respuesta 409 con una explicacion general
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorRespuesta> conflictoDeDatos(DataIntegrityViolationException excepcion) {
        return construir(HttpStatus.CONFLICT,
                "La operacion viola una restriccion de la base de datos: "
                        + "valor repetido o registro con datos relacionados",
                List.of());
    }

    private ResponseEntity<ErrorRespuesta> construir(HttpStatus estado, String mensaje,
            List<String> detalles) {
        return ResponseEntity.status(estado)
                .body(new ErrorRespuesta(estado.value(), mensaje, detalles));
    }
}