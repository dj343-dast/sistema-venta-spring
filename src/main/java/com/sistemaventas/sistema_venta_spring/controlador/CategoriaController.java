package com.sistemaventas.sistema_venta_spring.controlador;

import com.sistemaventas.sistema_venta_spring.modelo.Categoria;
import com.sistemaventas.sistema_venta_spring.repositorio.CategoriaRepository;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expone por HTTP las operaciones sobre las categorias del inventario.
 * Todas las direcciones empiezan por /api/categorias.
 */
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    /**
     * Spring entrega aqui el repositorio ya creado (inyeccion de dependencias).
     *
     * @param categoriaRepository acceso a la tabla categorias
     */
    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }
        /**
     * Devuelve todas las categorias.
     *
     * @return lista de categorias en formato JSON
     */
    @GetMapping
    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    /**
     * Devuelve una categoria por su id.
     *
     * @param id identificador de la categoria
     * @return la categoria (200) o un 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Integer id) {
        return categoriaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}