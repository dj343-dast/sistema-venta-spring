package com.sistemaventas.sistema_venta_spring.controlador;

import com.sistemaventas.sistema_venta_spring.dto.VentaSolicitud;
import com.sistemaventas.sistema_venta_spring.excepcion.RecursoNoEncontradoException;
import com.sistemaventas.sistema_venta_spring.modelo.Venta;
import com.sistemaventas.sistema_venta_spring.repositorio.VentaRepository;
import com.sistemaventas.sistema_venta_spring.servicio.VentaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expone por HTTP el registro y la consulta de ventas.
 * Todas las direcciones empiezan por /api/ventas.
 */
@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;
    private final VentaRepository ventaRepository;

    /**
     * Spring entrega aqui el servicio y el repositorio de ventas.
     *
     * @param ventaService    logica para registrar ventas
     * @param ventaRepository acceso a la tabla ventas para las consultas
     */
    public VentaController(VentaService ventaService, VentaRepository ventaRepository) {
        this.ventaService = ventaService;
        this.ventaRepository = ventaRepository;
    }

    /**
     * Registra una venta y descuenta el stock de los productos vendidos.
     *
     * @param solicitud cliente y lineas de la venta
     * @return la venta guardada con su total y el codigo 201
     */
    @PostMapping
    public ResponseEntity<Venta> registrar(@Valid @RequestBody VentaSolicitud solicitud) {
        Venta venta = ventaService.registrar(solicitud);
        return ResponseEntity.status(HttpStatus.CREATED).body(venta);
    }

    /**
     * Devuelve todas las ventas registradas.
     *
     * @return lista de ventas en formato JSON
     */
    @GetMapping
    public List<Venta> listar() {
        return ventaRepository.findAll();
    }

    /**
     * Devuelve una venta por su id.
     *
     * @param id identificador de la venta
     * @return la venta con sus detalles
     * @throws RecursoNoEncontradoException si la venta no existe (responde 404)
     */
    @GetMapping("/{id}")
    public Venta buscarPorId(@PathVariable Integer id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "La venta " + id + " no existe"));
    }
}