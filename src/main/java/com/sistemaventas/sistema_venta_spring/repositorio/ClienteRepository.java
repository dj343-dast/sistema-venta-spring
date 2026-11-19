package com.sistemaventas.sistema_venta_spring.repositorio;

import com.sistemaventas.sistema_venta_spring.modelo.Cliente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso a datos de la tabla {@code clientes}.
 */
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    /**
     * Busca un cliente por su documento.
     *
     * @param documento numero de documento
     * @return el cliente si existe, o vacio si no
     */
    Optional<Cliente> findByDocumento(String documento);

    /**
     * Busca un cliente por su correo.
     *
     * @param correo correo del cliente
     * @return el cliente si existe, o vacio si no
     */
    Optional<Cliente> findByCorreo(String correo);

    /**
     * Busca un cliente por su nombre.
     *
     * @param nombre nombre del cliente
     * @return el cliente si existe, o vacio si no
     */
    Optional<Cliente> findByNombre(String nombre);
}