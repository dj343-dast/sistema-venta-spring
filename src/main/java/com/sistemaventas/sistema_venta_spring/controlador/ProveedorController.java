package com.sistemaventas.sistema_venta_spring.controlador;

import com.sistemaventas.sistema_venta_spring.excepcion.RecursoNoEncontradoException;
import com.sistemaventas.sistema_venta_spring.modelo.Proveedor;
import com.sistemaventas.sistema_venta_spring.repositorio.ProveedorRepository;
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
 * Expone por HTTP las operaciones sobre los proveedores del inventario.
 * Todas las direcciones empiezan por /api/proveedores.
 */
@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorRepository proveedorRepository;

    /**
     * Spring entrega aqui el repositorio ya creado (inyeccion de dependencias).
     *
     * @param proveedorRepository acceso a la tabla proveedor
     */
    public ProveedorController(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    /**
     * Devuelve todos los proveedores.
     *
     * @return lista de proveedores en formato JSON
     */
    @GetMapping
    public List<Proveedor> listar() {
        return proveedorRepository.findAll();
    }

    /**
     * Devuelve un proveedor por su id.
     *
     * @param id identificador del proveedor
     * @return el proveedor encontrado
     * @throws RecursoNoEncontradoException si no existe (responde 404)
     */
    @GetMapping("/{id}")
    public Proveedor buscarPorId(@PathVariable Integer id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El proveedor " + id + " no existe"));
    }

    /**
     * Crea un proveedor nuevo.
     *
     * @param proveedor datos enviados en el cuerpo de la peticion (JSON)
     * @return el proveedor guardado, con su id, y el codigo 201
     */
    @PostMapping
    public ResponseEntity<Proveedor> crear(@Valid @RequestBody Proveedor proveedor) {
        proveedor.setIdProveedor(null);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(proveedorRepository.save(proveedor));
    }

    /**
     * Actualiza un proveedor existente.
     *
     * @param id identificador del proveedor a modificar
     * @param datos nuevos datos del proveedor
     * @return el proveedor actualizado
     * @throws RecursoNoEncontradoException si no existe (responde 404)
     */
    @PutMapping("/{id}")
    public Proveedor actualizar(@PathVariable Integer id,
            @Valid @RequestBody Proveedor datos) {
        Proveedor existente = proveedorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El proveedor " + id + " no existe"));
        existente.setNombreEmpresa(datos.getNombreEmpresa());
        existente.setTelefono(datos.getTelefono());
        existente.setCorreo(datos.getCorreo());
        existente.setDireccion(datos.getDireccion());
        return proveedorRepository.save(existente);
    }

    /**
     * Elimina un proveedor. Si tiene productos asociados, la base de datos lo
     * rechaza y el manejador de errores responde 409.
     *
     * @param id identificador del proveedor a borrar
     * @return 204 si se borro
     * @throws RecursoNoEncontradoException si no existe (responde 404)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!proveedorRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("El proveedor " + id + " no existe");
        }
        proveedorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}