package com.sistemaventas.sistema_venta_spring.servicio;

import com.sistemaventas.sistema_venta_spring.dto.ItemVenta;
import com.sistemaventas.sistema_venta_spring.dto.VentaSolicitud;
import com.sistemaventas.sistema_venta_spring.excepcion.RecursoNoEncontradoException;
import com.sistemaventas.sistema_venta_spring.excepcion.StockInsuficienteException;
import com.sistemaventas.sistema_venta_spring.modelo.Cliente;
import com.sistemaventas.sistema_venta_spring.modelo.DetalleVenta;
import com.sistemaventas.sistema_venta_spring.modelo.Producto;
import com.sistemaventas.sistema_venta_spring.modelo.Venta;
import com.sistemaventas.sistema_venta_spring.repositorio.ClienteRepository;
import com.sistemaventas.sistema_venta_spring.repositorio.ProductoRepository;
import com.sistemaventas.sistema_venta_spring.repositorio.VentaRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Logica de negocio de las ventas: valida existencias, descuenta el
 * inventario y guarda la venta con sus lineas en una sola transaccion.
 */
@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    /**
     * Spring entrega aqui los tres repositorios que necesita el servicio.
     *
     * @param ventaRepository    acceso a la tabla ventas
     * @param clienteRepository  acceso a la tabla clientes
     * @param productoRepository acceso a la tabla productos
     */
    public VentaService(VentaRepository ventaRepository,
            ClienteRepository clienteRepository,
            ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }

    /**
     * Registra una venta y descuenta el stock de cada producto vendido.
     * Si algo falla, la transaccion se deshace y no se guarda nada.
     *
     * @param solicitud cliente y lineas de la venta
     * @return la venta guardada, con sus detalles y su total
     * @throws RecursoNoEncontradoException si el cliente o un producto no existe
     * @throws StockInsuficienteException   si algun producto no tiene stock
     *                                      suficiente
     */
    @Transactional
    public Venta registrar(VentaSolicitud solicitud) {
        Cliente cliente = clienteRepository.findById(solicitud.idCliente())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El cliente " + solicitud.idCliente() + " no existe"));

        Venta venta = new Venta();
        venta.setFecha(LocalDateTime.now());
        venta.setCliente(cliente);

        BigDecimal total = BigDecimal.ZERO;
        for (ItemVenta item : solicitud.items()) {
            Producto producto = productoRepository.findById(item.idProducto())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "El producto " + item.idProducto() + " no existe"));

            if (producto.getStockActual() < item.cantidad()) {
                throw new StockInsuficienteException("Stock insuficiente de '"
                        + producto.getNombre() + "': disponible "
                        + producto.getStockActual() + ", solicitado " + item.cantidad());
            }
            producto.setStockActual(producto.getStockActual() - item.cantidad());

            BigDecimal subtotal = producto.getPrecioVenta()
                    .multiply(BigDecimal.valueOf(item.cantidad()));
            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(item.cantidad());
            detalle.setPrecioUnitario(producto.getPrecioVenta());
            detalle.setSubtotal(subtotal);
            venta.getDetalles().add(detalle);

            total = total.add(subtotal);
        }
        venta.setTotal(total);
        return ventaRepository.save(venta);
    }
}