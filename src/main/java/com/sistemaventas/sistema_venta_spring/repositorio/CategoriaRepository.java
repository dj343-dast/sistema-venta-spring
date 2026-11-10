package com.sistemaventas.sistema_venta_spring.repositorio;

import com.sistemaventas.sistema_venta_spring.modelo.Categoria;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso a datos de la tabla {@code categorias}.
 * Spring Data genera la implementacion en tiempo de ejecucion.
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    /**
     * Busca una categoria por su nombre exacto.
     *
     * @param nombre nombre de la categoria
     * @return la categoria si existe, o vacio si no
     */
    Optional<Categoria> findByNombre(String nombre);
}