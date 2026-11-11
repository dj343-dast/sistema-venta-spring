package com.sistemaventas.sistema_venta_spring.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * Datos necesarios para registrar una venta.
 *
 * @param idCliente identificador del cliente que compra
 * @param items     lineas de la venta; debe haber al menos una
 */
public record VentaSolicitud(
        @NotNull(message = "El cliente es obligatorio") Integer idCliente,
        @NotEmpty(message = "La venta debe tener al menos un producto") @Valid List<ItemVenta> items) {
}