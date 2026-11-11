package com.sistemaventas.sistema_venta_spring.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Una linea de la venta que llega en la peticion: producto y cantidad.
 *
 * @param idProducto identificador del producto vendido
 * @param cantidad   unidades vendidas
 */
public record ItemVenta(
        @NotNull(message = "El producto es obligatorio") Integer idProducto,
        @NotNull(message = "La cantidad es obligatoria") @Min(value = 1, message = "La cantidad debe ser al menos 1") Integer cantidad) {
}
