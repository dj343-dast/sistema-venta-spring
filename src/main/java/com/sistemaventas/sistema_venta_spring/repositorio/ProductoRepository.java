package com.sistemaventas.sistema_venta_spring.repositorio;

import com.sistemaventas.sistema_venta_spring.modelo.Producto;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso a datos de la tabla {@code productos}.
 */
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    /**
     * Busca un producto por su codigo interno.
     *
     * @param codigo codigo del producto
     * @return el producto si existe, o vacio si no
     */
    Optional<Producto> findByCodigo(String codigo);
}