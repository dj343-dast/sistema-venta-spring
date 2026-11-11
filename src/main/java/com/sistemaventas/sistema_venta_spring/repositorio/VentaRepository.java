package com.sistemaventas.sistema_venta_spring.repositorio;

import com.sistemaventas.sistema_venta_spring.modelo.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso a datos de la tabla {@code ventas}.
 * Los detalles se guardan por cascada junto con cada venta.
 */
public interface VentaRepository extends JpaRepository<Venta, Integer> {
}