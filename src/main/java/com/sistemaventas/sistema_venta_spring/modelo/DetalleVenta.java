package com.sistemaventas.sistema_venta_spring.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Linea de una venta: un producto, la cantidad vendida y su precio.
 * Se mapea a la tabla {@code detalle_venta}.
 */
@Entity
@Table(name = "detalle_venta")
public class DetalleVenta {

    /** Identificador generado por MySQL (AUTO_INCREMENT). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_venta")
    private Integer idDetalleVenta;

    /** Unidades vendidas del producto. */
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    /** Precio de venta del producto en el momento de la venta. */
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    /** Cantidad por precio unitario; lo calcula el servicio. */
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    /** Producto vendido en esta linea. */
    @NotNull(message = "El producto es obligatorio")
    @ManyToOne
    @JoinColumn(name = "id_productos", nullable = false)
    private Producto producto;

    /** Constructor sin argumentos que exige JPA. */
    public DetalleVenta() {
    }

    public Integer getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public void setIdDetalleVenta(Integer idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}