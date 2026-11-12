# Sistema de ventas e inventario - Modulo de inventario y ventas

Modulo del sistema de ventas e inventario desarrollado con Spring Boot 4,
Spring Data JPA (Hibernate) y MySQL. Expone una API REST para administrar el
inventario (categorias y productos) y registrar ventas a clientes, descontando
el stock de forma automatica.

## Funcionalidades

- Categorias: crear, consultar, actualizar y eliminar.
- Productos: crear, consultar, actualizar y eliminar. Cada producto pertenece a
  una categoria y a un proveedor, y guarda su precio de compra, su precio de
  venta y su stock.
- Ventas: registrar una venta con varios productos y consultarla.
  - El precio de cada linea se copia del producto en el momento de la venta.
  - El total se calcula en el servidor.
  - El stock de cada producto se descuenta al vender.
  - Si algun producto no tiene stock suficiente, la venta se rechaza completa y
    el inventario no cambia (transaccion).
- Validaciones de los datos de entrada y respuestas de error con un mensaje
  claro.

### Alcance

- Los proveedores y los clientes ya estan mapeados a la base de datos, pero por
  ahora se crean directamente en MySQL (ver "Datos de prueba").
- Las compras (`compras` y `detalle_compras`), los usuarios y la anulacion de
  ventas quedan fuera de este modulo.

## Estructura

Todo el codigo esta en `src/main/java/com/sistemaventas/sistema_venta_spring`:

- `modelo`: entidades JPA que representan las tablas (`Categoria`,
  `Proveedor`, `Producto`, `Cliente`, `Venta`, `DetalleVenta`).
- `repositorio`: interfaces de Spring Data con el acceso a datos de cada tabla.
- `servicio`: logica de negocio. `VentaService` registra la venta y descuenta
  el stock dentro de una transaccion.
- `controlador`: controladores REST (`CategoriaController`,
  `ProductoController`, `VentaController`).
- `dto`: objetos que representan los datos que llegan en la peticion de venta
  (`VentaSolicitud`, `ItemVenta`).
- `excepcion`: excepciones propias y `ManejadorErrores`, que las convierte en
  respuestas HTTP.

Fuera del codigo:

- `base_de_datos.sql`: script con el esquema `mydb` y las 9 tablas.
- `src/main/resources/application.properties`: configuracion general.
- `src/main/resources/application-local.properties.example`: plantilla de la
  conexion a MySQL.

## Requisitos

- JDK 17 o superior (el proyecto se desarrollo con JDK 25).
- MySQL 8.
- Git Bash o una terminal equivalente.
- No hace falta instalar Maven: el proyecto incluye el Maven Wrapper (`mvnw`).

## Configuracion

1. Ejecute `base_de_datos.sql` en MySQL para crear el esquema `mydb` y sus
   tablas. Atencion: el script borra y vuelve a crear las tablas, por lo que
   elimina los datos que ya tengan. Usela en una base de pruebas.
2. Copie `src/main/resources/application-local.properties.example` con el
   nombre `application-local.properties`.
3. Escriba su usuario y su contrasena de MySQL en ese archivo. Ese archivo no se
   sube al repositorio.

La conexion apunta a `jdbc:mysql://localhost:3306/mydb`. La aplicacion usa
`spring.jpa.hibernate.ddl-auto=validate`: al arrancar comprueba que las
entidades coincidan con las tablas, pero nunca las modifica.

## Ejecucion

Desde la raiz del proyecto:

```bash
./mvnw spring-boot:run
```

En PowerShell use `.\mvnw.cmd spring-boot:run`. Cuando aparezca la linea
`Started SistemaVentaSpringApplication`, la API queda disponible en
`http://localhost:8080`. Para detenerla pulse Ctrl + C.

## Datos de prueba

Para probar las ventas hace falta al menos un proveedor y un cliente. Ejecute
en MySQL:

```sql
INSERT INTO mydb.proveedor (nombre_empresa, telefono, correo, direccion)
VALUES ('Distribuidora Central', '3001234567', 'ventas@central.com', 'Calle 10 # 5-20');

INSERT INTO mydb.clientes (nombre, documento, telefono, correo, direccion)
VALUES ('Maria Gomez', '1002003004', '3009876543', 'maria@correo.com', 'Carrera 5 # 8-12');
```

Las categorias y los productos se crean desde la API.

## API

| Recurso | Metodo | Direccion | Descripcion |
|---|---|---|---|
| Categorias | GET | `/api/categorias` | Lista las categorias |
| Categorias | GET | `/api/categorias/{id}` | Consulta una categoria |
| Categorias | POST | `/api/categorias` | Crea una categoria |
| Categorias | PUT | `/api/categorias/{id}` | Actualiza una categoria |
| Categorias | DELETE | `/api/categorias/{id}` | Elimina una categoria |
| Productos | GET | `/api/productos` | Lista los productos |
| Productos | GET | `/api/productos/{id}` | Consulta un producto |
| Productos | POST | `/api/productos` | Crea un producto |
| Productos | PUT | `/api/productos/{id}` | Actualiza un producto |
| Productos | DELETE | `/api/productos/{id}` | Elimina un producto |
| Ventas | GET | `/api/ventas` | Lista las ventas |
| Ventas | GET | `/api/ventas/{id}` | Consulta una venta con sus detalles |
| Ventas | POST | `/api/ventas` | Registra una venta y descuenta el stock |

## Ejemplos

### Crear una categoria

```bash
curl -X POST http://localhost:8080/api/categorias \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Bebidas","descripcion":"Productos liquidos"}'
```

### Crear un producto

La categoria y el proveedor se envian solo con su id (ajuste los ids a los que
existan en su base):

```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"codigo":"BEB001","nombre":"Agua 600 ml","descripcion":"Botella de agua","precioCompra":1000,"precioVenta":1500,"stockActual":50,"stockMinimo":10,"categoria":{"idCategoria":1},"proveedor":{"idProveedor":1}}'
```

### Registrar una venta

```bash
curl -X POST http://localhost:8080/api/ventas \
  -H "Content-Type: application/json" \
  -d '{"idCliente":1,"items":[{"idProducto":1,"cantidad":3}]}'
```

La respuesta es `201` con la venta guardada. Estos son los datos principales:

```json
{
  "idVenta": 1,
  "estado": "completada",
  "total": 4500.00,
  "detalles": [
    { "cantidad": 3, "precioUnitario": 1500.00, "subtotal": 4500.00 }
  ]
}
```

Despues de la venta, el `stockActual` del producto baja de 50 a 47.

## Respuestas de error

Todos los errores se devuelven con la misma forma:

```json
{
  "estado": 409,
  "mensaje": "Stock insuficiente de 'Agua 600 ml': disponible 47, solicitado 1000",
  "detalles": []
}
```

| Codigo | Cuando ocurre |
|---|---|
| 400 | Los datos enviados no son validos (campo vacio, precio negativo, venta sin productos). `detalles` indica cada campo rechazado |
| 404 | El cliente, el producto, la categoria, el proveedor o la venta no existen |
| 409 | No hay stock suficiente, el codigo del producto ya existe, o se intenta borrar un registro que tiene datos relacionados (por ejemplo, un producto con ventas) |

## Pruebas realizadas

| Prueba | Resultado |
|---|---|
| Crear una categoria y un producto | 201, con la categoria y el proveedor dentro del producto |
| Producto con precio negativo | 400, con el campo rechazado |
| Producto con una categoria que no existe | 404, con el mensaje |
| Producto con un codigo repetido | 409 |
| Borrar un producto que ya tiene ventas | 409, el producto no se borra |
| Venta de 3 unidades | 201, total correcto y stock descontado |
| Venta con mas unidades que el stock | 409, el stock no cambia |
| Venta con dos lineas donde la segunda no tiene stock | 409, el stock queda como estaba (la transaccion se deshace) |
| Venta con un cliente que no existe | 404 |
| Venta sin productos | 400 |

## Estandares de codificacion

- Clases en UpperCamelCase, metodos y variables en lowerCamelCase, constantes en
  MAYUSCULAS_CON_GUIONES.
- Paquetes en minuscula.
- Cada clase y cada metodo publico llevan comentario Javadoc.
- Los datos de conexion (`application-local.properties`) no se suben al
  repositorio.
- La logica de negocio vive en el servicio y no en los controladores.