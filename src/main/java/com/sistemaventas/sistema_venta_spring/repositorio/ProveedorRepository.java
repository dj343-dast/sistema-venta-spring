package com.sistemaventas.sistema_venta_spring.repositorio;

import com.sistemaventas.sistema_venta_spring.modelo.Proveedor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso a datos de la tabla {@code proveedor}.
 */
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {

    /**
     * Busca un proveedor por el nombre de su empresa.
     *
     * @param nombreEmpresa nombre de la empresa
     * @return el proveedor si existe, o vacio si no
     */
    Optional<Proveedor> findByNombreEmpresa(String nombreEmpresa);

    /**
     * Busca un proveedor por su telefono.
     *
     * @param telefono telefono de contacto
     * @return el proveedor si existe, o vacio si no
     */
    Optional<Proveedor> findByTelefono(String telefono);

    /**
     * Busca un proveedor por su correo.
     *
     * @param correo correo de contacto
     * @return el proveedor si existe, o vacio si no
     */
    Optional<Proveedor> findByCorreo(String correo);
}