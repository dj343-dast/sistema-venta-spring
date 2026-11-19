# Documentación de la API REST

Sistema de ventas e inventario. Todos los servicios están bajo la ruta base `http://localhost:8080/api`, reciben y devuelven **JSON** y usan estos métodos HTTP: `GET` (consultar), `POST` (crear), `PUT` (actualizar) y `DELETE` (eliminar).

| Servicio | Ruta base | Operaciones |
|---|---|---|
| Categorías | `/api/categorias` | CRUD completo |
| Proveedores | `/api/proveedores` | CRUD completo |
| Clientes | `/api/clientes` | CRUD completo |
| Productos | `/api/productos` | CRUD completo |
| Ventas | `/api/ventas` | Registrar, listar y consultar |

## Convenciones generales

**Códigos de respuesta**

| Código | Significado | Cuándo ocurre |
|---|---|---|
| 200 | OK | Consulta o actualización exitosa |
| 201 | Created | Registro creado (devuelve el objeto con su id) |
| 204 | No Content | Eliminación exitosa (sin cuerpo) |
| 400 | Bad Request | Los datos enviados no pasan las validaciones |
| 404 | Not Found | El registro con ese id no existe |
| 409 | Conflict | Valor repetido, stock insuficiente o registro con datos relacionados |

**Formato de todos los errores.** Los errores de cualquier servicio devuelven la misma estructura:

```json
{
  "estado": 400,
  "mensaje": "Los datos enviados no son validos",
  "detalles": ["correo: El correo no tiene un formato valido"]
}
```

`detalles` lista el campo y el problema concreto; va vacío cuando el mensaje basta.

**Reglas comunes.** El `id` nunca se envía al crear: lo genera la base de datos. En `PUT` se reemplazan todos los campos editables, así que hay que enviar el objeto completo. Un campo opcional se puede omitir o enviar como `null`.

---

## 1. Categorías — `/api/categorias`

| Método | Ruta | Descripción | Éxito | Errores |
|---|---|---|---|---|
| GET | `/api/categorias` | Lista todas las categorías | 200 | — |
| GET | `/api/categorias/{id}` | Consulta una categoría | 200 | 404 |
| POST | `/api/categorias` | Crea una categoría | 201 | 400, 409 |
| PUT | `/api/categorias/{id}` | Actualiza una categoría | 200 | 400, 404, 409 |
| DELETE | `/api/categorias/{id}` | Elimina una categoría | 204 | 404, 409 |

**Campos**

| Campo | Tipo | Obligatorio | Regla |
|---|---|---|---|
| `nombre` | texto | Sí | Máximo 50 caracteres; **no se puede repetir** |
| `descripcion` | texto | No | Máximo 200 caracteres |

**Ejemplo: crear**

```bash
curl -X POST http://localhost:8080/api/categorias \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Bebidas","descripcion":"Gaseosas, jugos y agua"}'
```

Respuesta `201`:

```json
{ "idCategoria": 1, "nombre": "Bebidas", "descripcion": "Gaseosas, jugos y agua" }
```

Un nombre repetido responde `409`. Eliminar una categoría que ya tiene productos también responde `409`.

---

## 2. Proveedores — `/api/proveedores`

| Método | Ruta | Descripción | Éxito | Errores |
|---|---|---|---|---|
| GET | `/api/proveedores` | Lista los proveedores | 200 | — |
| GET | `/api/proveedores/{id}` | Consulta un proveedor | 200 | 404 |
| POST | `/api/proveedores` | Crea un proveedor | 201 | 400, 409 |
| PUT | `/api/proveedores/{id}` | Actualiza un proveedor | 200 | 400, 404, 409 |
| DELETE | `/api/proveedores/{id}` | Elimina un proveedor | 204 | 404, 409 |

**Campos**

| Campo | Tipo | Obligatorio | Regla |
|---|---|---|---|
| `nombreEmpresa` | texto | Sí | Máximo 100 caracteres; **único** |
| `telefono` | texto | No | Máximo 20 caracteres; **único** |
| `correo` | texto | No | Formato de correo válido; máximo 100; **único** |
| `direccion` | texto | No | Máximo 150 caracteres |

**Ejemplo: crear**

```bash
curl -X POST http://localhost:8080/api/proveedores \
  -H "Content-Type: application/json" \
  -d '{"nombreEmpresa":"Distribuidora Andina","telefono":"3001234567","correo":"ventas@andina.com"}'
```

Respuesta `201`:

```json
{ "idProveedor": 1, "nombreEmpresa": "Distribuidora Andina", "telefono": "3001234567",
  "correo": "ventas@andina.com", "direccion": null }
```

**Ejemplo de conflicto (`409`)**, al repetir el correo:

```json
{
  "estado": 409,
  "mensaje": "Ya existe un registro con ese valor",
  "detalles": ["correo: Ya existe un proveedor con ese correo"]
}
```

---

## 3. Clientes — `/api/clientes`

| Método | Ruta | Descripción | Éxito | Errores |
|---|---|---|---|---|
| GET | `/api/clientes` | Lista los clientes | 200 | — |
| GET | `/api/clientes/{id}` | Consulta un cliente | 200 | 404 |
| POST | `/api/clientes` | Registra un cliente | 201 | 400, 409 |
| PUT | `/api/clientes/{id}` | Actualiza un cliente | 200 | 400, 404, 409 |
| DELETE | `/api/clientes/{id}` | Elimina un cliente | 204 | 404, 409 |

**Campos**

| Campo | Tipo | Obligatorio | Regla |
|---|---|---|---|
| `nombre` | texto | Sí | Máximo 100 caracteres; **único** |
| `documento` | texto | Sí | Máximo 20 caracteres; **único** |
| `telefono` | texto | No | Máximo 20 caracteres |
| `correo` | texto | No | Formato de correo válido; máximo 100; **único** |
| `direccion` | texto | No | Máximo 150 caracteres |

**Ejemplo: crear**

```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Pedro Perez","documento":"123456","correo":"pedro@correo.com"}'
```

Respuesta `201`:

```json
{ "idCliente": 1, "nombre": "Pedro Perez", "documento": "123456", "telefono": null,
  "correo": "pedro@correo.com", "direccion": null }
```

**Ejemplo de error de validación (`400`)**, con correo mal escrito:

```json
{
  "estado": 400,
  "mensaje": "Los datos enviados no son validos",
  "detalles": ["correo: El correo no tiene un formato valido"]
}
```

Eliminar un cliente que ya tiene ventas responde `409`.

---

## 4. Productos — `/api/productos`

| Método | Ruta | Descripción | Éxito | Errores |
|---|---|---|---|---|
| GET | `/api/productos` | Lista los productos | 200 | — |
| GET | `/api/productos/{id}` | Consulta un producto | 200 | 404 |
| POST | `/api/productos` | Crea un producto | 201 | 400, 404, 409 |
| PUT | `/api/productos/{id}` | Actualiza un producto | 200 | 400, 404, 409 |
| DELETE | `/api/productos/{id}` | Elimina un producto | 204 | 404, 409 |

**Campos**

| Campo | Tipo | Obligatorio | Regla |
|---|---|---|---|
| `codigo` | texto | Sí | Máximo 20 caracteres; **único** |
| `nombre` | texto | Sí | Máximo 100 caracteres |
| `descripcion` | texto | No | Máximo 200 caracteres |
| `precioCompra` | decimal | Sí | No puede ser negativo |
| `precioVenta` | decimal | Sí | No puede ser negativo |
| `stockActual` | entero | Sí | Mínimo 0 |
| `stockMinimo` | entero | Sí | Mínimo 0 |
| `categoria` | objeto | Sí | `{"idCategoria": n}`; la categoría debe existir |
| `proveedor` | objeto | Sí | `{"idProveedor": n}`; el proveedor debe existir |

**Ejemplo: crear**

```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"codigo":"BEB-001","nombre":"Agua 600 ml","precioCompra":900,"precioVenta":1500,
       "stockActual":50,"stockMinimo":10,
       "categoria":{"idCategoria":1},"proveedor":{"idProveedor":1}}'
```

Respuesta `201`:

```json
{
  "idProducto": 1, "codigo": "BEB-001", "nombre": "Agua 600 ml", "descripcion": null,
  "precioCompra": 900.00, "precioVenta": 1500.00, "stockActual": 50, "stockMinimo": 10,
  "categoria": { "idCategoria": 1, "nombre": "Bebidas", "descripcion": "Gaseosas, jugos y agua" },
  "proveedor": { "idProveedor": 1, "nombreEmpresa": "Distribuidora Andina" }
}
```

Si la categoría o el proveedor no existen, responde `404` con el mensaje correspondiente (por ejemplo, `"La categoria 99 no existe"`). Un `codigo` repetido responde `409`.

---

## 5. Ventas — `/api/ventas`

Las ventas se **registran y consultan**, pero no se editan ni se eliminan, para conservar el historial.

| Método | Ruta | Descripción | Éxito | Errores |
|---|---|---|---|---|
| POST | `/api/ventas` | Registra una venta y descuenta el stock | 201 | 400, 404, 409 |
| GET | `/api/ventas` | Lista las ventas | 200 | — |
| GET | `/api/ventas/{id}` | Consulta una venta con sus detalles | 200 | 404 |

**Cuerpo de la petición (`POST`)**

| Campo | Tipo | Obligatorio | Regla |
|---|---|---|---|
| `idCliente` | entero | Sí | El cliente debe existir |
| `items` | lista | Sí | Al menos un producto |
| `items[].idProducto` | entero | Sí | El producto debe existir |
| `items[].cantidad` | entero | Sí | Mínimo 1 |

**Cómo funciona.** El registro es una transacción: si algo falla, no se guarda nada. Por cada línea el sistema comprueba que haya stock suficiente, descuenta las unidades vendidas, toma el **precio de venta desde el servidor** (el cliente no puede enviar precios) y calcula el subtotal y el total de la venta.

**Ejemplo: registrar**

```bash
curl -X POST http://localhost:8080/api/ventas \
  -H "Content-Type: application/json" \
  -d '{"idCliente":1,"items":[{"idProducto":1,"cantidad":3}]}'
```

Respuesta `201`:

```json
{
  "idVenta": 1,
  "fecha": "2026-11-12T20:15:30",
  "total": 4500.00,
  "estado": "completada",
  "cliente": { "idCliente": 1, "nombre": "Pedro Perez" },
  "detalles": [
    { "idDetalleVenta": 1, "cantidad": 3, "precioUnitario": 1500.00,
      "subtotal": 4500.00, "producto": { "idProducto": 1, "nombre": "Agua 600 ml" } }
  ]
}
```

**Errores específicos**

- `404`: el cliente o un producto no existe.
- `409`: stock insuficiente; el mensaje indica lo disponible y lo solicitado.

```json
{
  "estado": 409,
  "mensaje": "Stock insuficiente de 'Agua 600 ml': disponible 50, solicitado 80",
  "detalles": []
}
```

- `400`: falta el cliente, la lista de productos está vacía o una cantidad es menor que 1.