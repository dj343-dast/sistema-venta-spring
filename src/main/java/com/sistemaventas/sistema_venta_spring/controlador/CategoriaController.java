package com.sistemaventas.sistema_venta_spring.controlador;

import com.sistemaventas.sistema_venta_spring.modelo.Categoria;
import com.sistemaventas.sistema_venta_spring.repositorio.CategoriaRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**
     * Crea una categoria nueva.
     *
     * @param categoria datos enviados en el cuerpo de la peticion (JSON)
     * @return la categoria guardada, con su id, y el codigo 201
     */
    @PostMapping
    public ResponseEntity<Categoria> crear(@Valid @RequestBody Categoria categoria) {
        categoria.setIdCategoria(null);
        Categoria guardada = categoriaRepository.save(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    /**
     * Actualiza una categoria existente.
     *
     * @param id    identificador de la categoria a modificar
     * @param datos nuevos datos de la categoria
     * @return la categoria actualizada (200) o un 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable Integer id,
            @Valid @RequestBody Categoria datos) {
        return categoriaRepository.findById(id)
                .map(existente -> {
                    existente.setNombre(datos.getNombre());
                    existente.setDescripcion(datos.getDescripcion());
                    return ResponseEntity.ok(categoriaRepository.save(existente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina una categoria.
     *
     * @param id identificador de la categoria a borrar
     * @return 204 si se borro, o 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!categoriaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categoriaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}