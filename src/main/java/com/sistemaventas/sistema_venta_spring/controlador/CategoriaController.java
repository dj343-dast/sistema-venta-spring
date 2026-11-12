package com.sistemaventas.sistema_venta_spring.controlador;

import com.sistemaventas.sistema_venta_spring.excepcion.RecursoNoEncontradoException;
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
     * @return la categoria encontrada
     * @throws RecursoNoEncontradoException si no existe (responde 404)
     */
    @GetMapping("/{id}")
    public Categoria buscarPorId(@PathVariable Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "La categoria " + id + " no existe"));
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
     * @return la categoria actualizada
     * @throws RecursoNoEncontradoException si no existe (responde 404)
     */
    @PutMapping("/{id}")
    public Categoria actualizar(@PathVariable Integer id,
            @Valid @RequestBody Categoria datos) {
        Categoria existente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "La categoria " + id + " no existe"));
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        return categoriaRepository.save(existente);
    }

    /**
     * Elimina una categoria.
     *
     * @param id identificador de la categoria a borrar
     * @return 204 si se borro
     * @throws RecursoNoEncontradoException si no existe (responde 404)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("La categoria " + id + " no existe");
        }
        categoriaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}