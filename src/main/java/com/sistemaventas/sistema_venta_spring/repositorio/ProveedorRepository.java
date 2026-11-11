package com.sistemaventas.sistema_venta_spring.repositorio;

import com.sistemaventas.sistema_venta_spring.modelo.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso a datos de la tabla {@code proveedor}.
 */
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
}
