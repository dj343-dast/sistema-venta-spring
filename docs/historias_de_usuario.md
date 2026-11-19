# Historias de usuario - Modulo de inventario y ventas

Roles (los mismos del campo `rol` de la tabla `usuario`):

- **Bodega**: administra el inventario.
- **Vendedor**: atiende clientes y registra ventas.
- **Administrador**: puede hacer todo lo anterior.

## Resumen

| Id | Historia | Rol |
|---|---|---|
| HU-01 | Administrar categorias | Bodega |
| HU-02 | Administrar proveedores | Bodega |
| HU-03 | Administrar productos y su stock | Bodega |
| HU-04 | Administrar clientes | Vendedor |
| HU-05 | Registrar una venta | Vendedor |
| HU-06 | Evitar vender mas de lo que hay en stock | Vendedor |
| HU-07 | Consultar las ventas registradas | Vendedor |
| HU-08 | Recibir mensajes de error claros | Todos |

## HU-01: Administrar categorias

**Como** bodega, **quiero** crear, consultar, editar y eliminar categorias,
**para** clasificar los productos del inventario.

Criterios de aceptacion:

- El nombre de la categoria es obligatorio y no puede repetirse.
- Una categoria que tiene productos no se puede eliminar.
- La lista muestra todas las categorias con su descripcion.

## HU-02: Administrar proveedores

**Como** bodega, **quiero** registrar y mantener los datos de mis proveedores,
**para** saber quien abastece cada producto.

Criterios de aceptacion:

- El nombre de la empresa es obligatorio.
- La empresa, el telefono y el correo no pueden repetirse entre proveedores.
- El correo debe tener un formato valido.
- Un proveedor que tiene productos no se puede eliminar.

## HU-03: Administrar productos y su stock

**Como** bodega, **quiero** registrar productos con su codigo, precios, stock,
categoria y proveedor, **para** controlar el inventario.

Criterios de aceptacion:

- El codigo es obligatorio y no puede repetirse.
- Los precios y el stock no pueden ser negativos.
- Todo producto debe tener una categoria y un proveedor que existan.
- La lista muestra el stock actual de cada producto.
- Un producto que ya se vendio no se puede eliminar.

## HU-04: Administrar clientes

**Como** vendedor, **quiero** registrar y mantener los datos de mis clientes,
**para** asociarles las ventas.

Criterios de aceptacion:

- El nombre y el documento son obligatorios.
- El documento, el correo y el nombre no pueden repetirse entre clientes.
- Un cliente que tiene ventas no se puede eliminar.

## HU-05: Registrar una venta

**Como** vendedor, **quiero** registrar una venta con un cliente y varios
productos, **para** dejar constancia de lo vendido y actualizar el inventario.

Criterios de aceptacion:

- La venta lleva un cliente y al menos un producto con su cantidad (minimo 1).
- El precio de cada linea es el precio de venta del producto en ese momento.
- El total lo calcula el sistema como la suma de los subtotales.
- Al registrar la venta, el stock de cada producto disminuye.
- La pantalla muestra un total estimado mientras se arma la venta.

## HU-06: Evitar vender mas de lo que hay en stock

**Como** vendedor, **quiero** que el sistema rechace una venta cuando un
producto no tiene stock suficiente, **para** no vender lo que no existe.

Criterios de aceptacion:

- Si algun producto no alcanza, se rechaza la venta completa.
- El mensaje indica el producto, el stock disponible y la cantidad pedida.
- Cuando se rechaza, ningun producto cambia de stock (se deshace todo).

## HU-07: Consultar las ventas registradas

**Como** vendedor, **quiero** ver las ventas hechas con su cliente, productos,
total y estado, **para** revisar lo vendido.

Criterios de aceptacion:

- La lista muestra la venta mas reciente primero.
- Cada venta muestra fecha, cliente, productos con cantidad, total y estado.
- Se puede consultar una venta por su numero.

## HU-08: Recibir mensajes de error claros

**Como** usuario del sistema, **quiero** que los errores me expliquen que paso,
**para** corregirlos sin ayuda.

Criterios de aceptacion:

- Un dato invalido se marca en el campo afectado, con su mensaje.
- Un valor repetido, un registro inexistente o un stock insuficiente muestran
  un mensaje especifico y no un error generico.
- Los codigos usados son 400 (datos invalidos), 404 (no existe) y
  409 (conflicto).

## Trazabilidad

| Historia | Clases principales | Pantalla | Prueba |
|---|---|---|---|
| HU-01 | `Categoria`, `CategoriaRepository`, `CategoriaController` | Pestana Categorias | Crear categoria; nombre repetido |
| HU-02 | `Proveedor`, `ProveedorRepository`, `ProveedorController` | Pestana Proveedores | Telefono repetido |
| HU-03 | `Producto`, `ProductoRepository`, `ProductoController` | Pestana Productos | Precio negativo; categoria inexistente; codigo repetido |
| HU-04 | `Cliente`, `ClienteRepository`, `ClienteController` | Pestana Clientes | Documento repetido |
| HU-05 | `Venta`, `DetalleVenta`, `VentaService`, `VentaController` | Pestana Ventas | Venta de 3 unidades |
| HU-06 | `VentaService`, `StockInsuficienteException` | Pestana Ventas | Venta sin stock; venta con dos lineas |
| HU-07 | `VentaController`, `VentaRepository` | Pestana Ventas | Lista de ventas |
| HU-08 | `ManejadorErrores`, `ErrorRespuesta` | Todas las pestanas | Todas las pruebas de error |