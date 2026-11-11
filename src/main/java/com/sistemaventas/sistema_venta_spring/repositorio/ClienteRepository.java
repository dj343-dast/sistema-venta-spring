package com.sistemaventas.sistema_venta_spring.repositorio;

import com.sistemaventas.sistema_venta_spring.modelo.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso a datos de la tabla {@code clientes}.
 */
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
