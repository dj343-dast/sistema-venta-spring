package com.sistemaventas.sistema_venta_spring.modelo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Venta registrada a un cliente, con sus lineas de detalle.
 * Se mapea a la tabla {@code ventas}.
 */
@Entity
@Table(name = "ventas")
public class Venta {

    /** Identificador generado por MySQL (AUTO_INCREMENT). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ventas")
    private Integer idVenta;

    /** Fecha y hora de la venta. */
    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    /** Suma de los subtotales de todas las lineas. */
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    /** Estado de la venta: completada o anulada. */
    @Column(name = "estado", nullable = false,
            columnDefinition = "enum('completada','anulada')")
    private String estado = "completada";

    /** Cliente al que se le hizo la venta. */
    @ManyToOne
    @JoinColumn(name = "id_clientes", nullable = false)
    private Cliente cliente;

    /** Lineas de la venta; se guardan junto con ella. */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_ventas", nullable = false)
    private List<DetalleVenta> detalles = new ArrayList<>();

    /** Constructor sin argumentos que exige JPA. */
    public Venta() {
    }

    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }
}