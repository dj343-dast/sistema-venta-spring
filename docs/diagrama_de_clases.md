# Diagrama de clases - Modulo de inventario y ventas

## Modelo de dominio

Cada clase es una entidad JPA mapeada a una tabla de la base de datos `mydb`.

```mermaid
classDiagram
    class Categoria {
        -Integer idCategoria
        -String nombre
        -String descripcion
    }
    class Proveedor {
        -Integer idProveedor
        -String nombreEmpresa
        -String telefono
        -String correo
        -String direccion
    }
    class Producto {
        -Integer idProducto
        -String codigo
        -String nombre
        -String descripcion
        -BigDecimal precioCompra
        -BigDecimal precioVenta
        -Integer stockActual
        -Integer stockMinimo
    }
    class Cliente {
        -Integer idCliente
        -String nombre
        -String documento
        -String telefono
        -String correo
        -String direccion
    }
    class Venta {
        -Integer idVenta
        -LocalDateTime fecha
        -BigDecimal total
        -String estado
    }
    class DetalleVenta {
        -Integer idDetalleVenta
        -Integer cantidad
        -BigDecimal precioUnitario
        -BigDecimal subtotal
    }

    Producto "*" --> "1" Categoria : pertenece a
    Producto "*" --> "1" Proveedor : lo abastece
    Venta "*" --> "1" Cliente : se hace a
    Venta "1" *-- "1..*" DetalleVenta : contiene
    DetalleVenta "*" --> "1" Producto : vende
```

Una `Venta` contiene sus `DetalleVenta`: se guardan juntos (cascada) y no
existen por separado.

## Capa de ventas

Muestra como se registra una venta y donde nacen los errores.

```mermaid
classDiagram
    class VentaController {
        +registrar(VentaSolicitud) ResponseEntity
        +listar() List
        +buscarPorId(Integer) Venta
    }
    class VentaService {
        +registrar(VentaSolicitud) Venta
    }
    class VentaSolicitud {
        <<record>>
        Integer idCliente
        List items
    }
    class ItemVenta {
        <<record>>
        Integer idProducto
        Integer cantidad
    }
    class ManejadorErrores
    class StockInsuficienteException
    class RecursoNoEncontradoException

    VentaController ..> VentaService : usa
    VentaController ..> VentaRepository : consulta
    VentaService ..> VentaRepository : guarda
    VentaService ..> ClienteRepository : busca cliente
    VentaService ..> ProductoRepository : busca y descuenta stock
    VentaService ..> VentaSolicitud : recibe
    VentaSolicitud *-- ItemVenta : lineas
    VentaService ..> StockInsuficienteException : lanza
    VentaService ..> RecursoNoEncontradoException : lanza
    ManejadorErrores ..> StockInsuficienteException : responde 409
    ManejadorErrores ..> RecursoNoEncontradoException : responde 404
```

`VentaService.registrar` es transaccional: si lanza una excepcion, se deshace
todo, incluido el descuento de stock.