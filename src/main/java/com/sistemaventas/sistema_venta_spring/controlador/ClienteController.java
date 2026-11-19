package com.sistemaventas.sistema_venta_spring.controlador;

import com.sistemaventas.sistema_venta_spring.excepcion.RecursoNoEncontradoException;
import com.sistemaventas.sistema_venta_spring.excepcion.ValorDuplicadoException; 
import com.sistemaventas.sistema_venta_spring.modelo.Cliente;
import com.sistemaventas.sistema_venta_spring.repositorio.ClienteRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional; 
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
 * Expone por HTTP las operaciones sobre los clientes.
 * Todas las direcciones empiezan por /api/clientes.
 * Historia de usuario: HU-04 (administrar clientes)
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;

    /**
     * Spring entrega aqui el repositorio ya creado (inyeccion de dependencias).
     *
     * @param clienteRepository acceso a la tabla clientes
     */
    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Devuelve todos los clientes.
     *
     * @return lista de clientes en formato JSON
     */
    @GetMapping
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    /**
     * Devuelve un cliente por su id.
     *
     * @param id identificador del cliente
     * @return el cliente encontrado
     * @throws RecursoNoEncontradoException si el cliente no existe (responde 404)
     */
    @GetMapping("/{id}")
    public Cliente buscarPorId(@PathVariable Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El cliente " + id + " no existe"));
    }

    /**
     * Registra un cliente nuevo.
     *
     * @param cliente datos enviados en el cuerpo de la peticion (JSON)
     * @return el cliente guardado, con su id, y el codigo 201
     * @throws ValorDuplicadoException si el documento, el correo o el nombre ya existen
     */
    @PostMapping
    public ResponseEntity<Cliente> crear(@Valid @RequestBody Cliente cliente) {
        cliente.setIdCliente(null);
        validarUnicos(cliente, null); // NUEVO
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clienteRepository.save(cliente));
    }

    /**
     * Actualiza un cliente existente.
     *
     * @param id    identificador del cliente a modificar
     * @param datos nuevos datos del cliente
     * @return el cliente actualizado
     * @throws RecursoNoEncontradoException si el cliente no existe (responde 404)
     * @throws ValorDuplicadoException si el documento, el correo o el nombre
     *         pertenecen a otro cliente
     */
    @PutMapping("/{id}")
    public Cliente actualizar(@PathVariable Integer id,
            @Valid @RequestBody Cliente datos) {
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El cliente " + id + " no existe"));
        validarUnicos(datos, id); // NUEVO
        existente.setNombre(datos.getNombre());
        existente.setDocumento(datos.getDocumento());
        existente.setTelefono(datos.getTelefono());
        existente.setCorreo(datos.getCorreo());
        existente.setDireccion(datos.getDireccion());
        return clienteRepository.save(existente);
    }

    /**
     * Elimina un cliente. Si ya tiene ventas asociadas, la base de datos lo
     * rechaza y el manejador de errores responde 409.
     *
     * @param id identificador del cliente a borrar
     * @return 204 si se borro
     * @throws RecursoNoEncontradoException si el cliente no existe (responde 404)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("El cliente " + id + " no existe");
        }
        clienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Comprueba que el documento, el correo y el nombre no los tenga otro cliente.
     *
     * @param cliente datos que se quieren guardar
     * @param idActual id del cliente que se edita, o null si es uno nuevo
     * @throws ValorDuplicadoException si algun valor ya pertenece a otro cliente
     */
    private void validarUnicos(Cliente cliente, Integer idActual) { // NUEVO
        if (cliente.getDocumento() != null) {
            verificar("documento", clienteRepository.findByDocumento(cliente.getDocumento()), idActual);
        }
        if (cliente.getCorreo() != null) {
            verificar("correo", clienteRepository.findByCorreo(cliente.getCorreo()), idActual);
        }
        if (cliente.getNombre() != null) {
            verificar("nombre", clienteRepository.findByNombre(cliente.getNombre()), idActual);
        }
    }

    /** Lanza la excepcion si el valor encontrado pertenece a un cliente distinto. */
    private void verificar(String campo, Optional<Cliente> existente, Integer idActual) { // NUEVO
        if (existente.isPresent() && !existente.get().getIdCliente().equals(idActual)) {
            throw new ValorDuplicadoException(campo, "Ya existe un cliente con ese " + campo);
        }
    }
}