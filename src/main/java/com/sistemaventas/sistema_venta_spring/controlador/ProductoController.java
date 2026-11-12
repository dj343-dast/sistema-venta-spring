package com.sistemaventas.sistema_venta_spring.controlador;

import com.sistemaventas.sistema_venta_spring.excepcion.RecursoNoEncontradoException;
import com.sistemaventas.sistema_venta_spring.modelo.Categoria;
import com.sistemaventas.sistema_venta_spring.modelo.Producto;
import com.sistemaventas.sistema_venta_spring.modelo.Proveedor;
import com.sistemaventas.sistema_venta_spring.repositorio.CategoriaRepository;
import com.sistemaventas.sistema_venta_spring.repositorio.ProductoRepository;
import com.sistemaventas.sistema_venta_spring.repositorio.ProveedorRepository;
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
 * Expone por HTTP las operaciones sobre los productos del inventario.
 * Todas las direcciones empiezan por /api/productos.
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;

    /**
     * Spring entrega aqui los tres repositorios que necesita el controlador.
     *
     * @param productoRepository  acceso a la tabla productos
     * @param categoriaRepository acceso a la tabla categorias
     * @param proveedorRepository acceso a la tabla proveedor
     */
    public ProductoController(ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository,
            ProveedorRepository proveedorRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.proveedorRepository = proveedorRepository;
    }

    /**
     * Devuelve todos los productos del inventario.
     *
     * @return lista de productos en formato JSON
     */
    @GetMapping
    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    /**
     * Devuelve un producto por su id.
     *
     * @param id identificador del producto
     * @return el producto con su categoria y su proveedor
     * @throws RecursoNoEncontradoException si el producto no existe (responde 404)
     */
    @GetMapping("/{id}")
    public Producto buscarPorId(@PathVariable Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El producto " + id + " no existe"));
    }

    /**
     * Crea un producto nuevo.
     *
     * @param producto datos del producto enviados en el cuerpo de la peticion
     * @return el producto guardado y el codigo 201
     * @throws RecursoNoEncontradoException si la categoria o el proveedor no
     *                                      existen
     */
    @PostMapping
    public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto) {
        producto.setIdProducto(null);
        asignarRelaciones(producto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productoRepository.save(producto));
    }

    /**
     * Actualiza un producto existente.
     *
     * @param id    identificador del producto a modificar
     * @param datos nuevos datos del producto
     * @return el producto actualizado
     * @throws RecursoNoEncontradoException si el producto, la categoria o el
     *                                      proveedor no existen
     */
    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Integer id,
            @Valid @RequestBody Producto datos) {
        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El producto " + id + " no existe"));
        asignarRelaciones(datos);
        existente.setCodigo(datos.getCodigo());
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        existente.setPrecioCompra(datos.getPrecioCompra());
        existente.setPrecioVenta(datos.getPrecioVenta());
        existente.setStockActual(datos.getStockActual());
        existente.setStockMinimo(datos.getStockMinimo());
        existente.setCategoria(datos.getCategoria());
        existente.setProveedor(datos.getProveedor());
        return productoRepository.save(existente);
    }

    /**
     * Elimina un producto. Si ya tiene ventas asociadas, la base de datos lo
     * rechaza y el manejador de errores responde 409.
     *
     * @param id identificador del producto a borrar
     * @return 204 si se borro
     * @throws RecursoNoEncontradoException si el producto no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!productoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("El producto " + id + " no existe");
        }
        productoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Reemplaza la categoria y el proveedor que llegan solo con su id por los
     * objetos completos de la base de datos.
     *
     * @param producto producto cuyas relaciones se van a resolver
     * @throws RecursoNoEncontradoException si la categoria o el proveedor no
     *                                      existen
     */
    private void asignarRelaciones(Producto producto) {
        Integer idCategoria = producto.getCategoria().getIdCategoria();
        Integer idProveedor = producto.getProveedor().getIdProveedor();

        Categoria categoria = Optional.ofNullable(idCategoria)
                .flatMap(categoriaRepository::findById)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "La categoria " + idCategoria + " no existe"));
        Proveedor proveedor = Optional.ofNullable(idProveedor)
                .flatMap(proveedorRepository::findById)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El proveedor " + idProveedor + " no existe"));

        producto.setCategoria(categoria);
        producto.setProveedor(proveedor);
    }
}