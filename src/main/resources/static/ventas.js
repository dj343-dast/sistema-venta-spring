/* Pantalla de ventas: arma una venta con varias lineas y la envia a POST /api/ventas.
   Usa $, esc, pedir y aviso de app.js, por eso se carga despues de el. */

let productosVenta = [];   // productos disponibles, para mostrar precio y stock

/** Da formato de pesos colombianos a un numero. */
function formatoPesos(valor) {
  return new Intl.NumberFormat("es-CO", {
    style: "currency", currency: "COP", maximumFractionDigits: 0
  }).format(valor);
}

/** Prepara la pantalla: carga clientes y productos, deja una linea vacia y lista las ventas. */
async function iniciarVentas() {
  const clientes = await pedir("clientes");
  productosVenta = await pedir("productos");

  $("v_cliente").innerHTML = '<option value="">Seleccione...</option>' +
    clientes.map((c) => `<option value="${c.idCliente}">${esc(c.nombre)} (${esc(c.documento)})</option>`).join("");

  $("lineasVenta").innerHTML = "";
  agregarLinea();
  actualizarTotal();
  $("erroresVenta").hidden = true;
  await cargarVentas();
}

/** Agrega una linea: lista de productos, cantidad, subtotal y boton para quitarla. */
function agregarLinea() {
  const opciones = '<option value="">Producto...</option>' +
    productosVenta.map((p) =>
      `<option value="${p.idProducto}">${esc(p.nombre)} (stock ${p.stockActual})</option>`).join("");
  const fila = document.createElement("div");
  fila.className = "linea";
  fila.innerHTML =
    `<select class="l_producto">${opciones}</select>` +
    '<input class="l_cantidad" type="number" min="1" value="1">' +
    '<span class="l_subtotal"></span>' +
    '<button type="button" class="mini borrar l_quitar">Quitar</button>';
  $("lineasVenta").appendChild(fila);
}

/** Calcula el subtotal de cada linea y el total estimado (el servidor calcula el definitivo). */
function actualizarTotal() {
  let total = 0;
  document.querySelectorAll("#lineasVenta .linea").forEach((fila) => {
    const id = fila.querySelector(".l_producto").value;
    const cantidad = Number(fila.querySelector(".l_cantidad").value);
    const producto = productosVenta.find((p) => String(p.idProducto) === id);
    const subtotal = producto && cantidad > 0 ? producto.precioVenta * cantidad : 0;
    fila.querySelector(".l_subtotal").textContent = subtotal > 0 ? formatoPesos(subtotal) : "";
    total += subtotal;
  });
  $("totalVenta").textContent = formatoPesos(total);
}

/** Arma el JSON que espera la API: cliente y lineas (se ignoran las que no tienen producto). */
function leerVenta() {
  const items = [];
  document.querySelectorAll("#lineasVenta .linea").forEach((fila) => {
    const idProducto = fila.querySelector(".l_producto").value;
    if (idProducto !== "") {
      items.push({
        idProducto: Number(idProducto),
        cantidad: Number(fila.querySelector(".l_cantidad").value)
      });
    }
  });
  const cliente = $("v_cliente").value;
  return { idCliente: cliente === "" ? null : Number(cliente), items: items };
}

/** Envia la venta; si sale bien, limpia la pantalla y refresca stock y lista. */
async function registrarVenta(e) {
  e.preventDefault();
  $("erroresVenta").hidden = true;
  const resp = await fetch(API + "ventas", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(leerVenta())
  });
  if (resp.ok) {
    const venta = await resp.json();
    aviso("Venta " + venta.idVenta + " registrada. Total: " + formatoPesos(venta.total), true);
    await iniciarVentas();   // vuelve a cargar productos para mostrar el stock actualizado
  } else {
    const err = await resp.json().catch(() => ({ mensaje: "Error inesperado", detalles: [] }));
    const lista = err.detalles && err.detalles.length
      ? "<ul>" + err.detalles.map((d) => `<li>${esc(d)}</li>`).join("") + "</ul>" : "";
    $("erroresVenta").innerHTML = `<strong>${esc(err.mensaje)}</strong>${lista}`;
    $("erroresVenta").hidden = false;
  }
}

/** Lista las ventas, la mas reciente primero. */
async function cargarVentas() {
  const ventas = await pedir("ventas");
  ventas.sort((a, b) => b.idVenta - a.idVenta);
  $("filasVentas").innerHTML = ventas.length === 0
    ? '<tr><td class="vacio" colspan="6">No hay ventas registradas</td></tr>'
    : ventas.map((v) => {
      const productos = v.detalles.map((d) => `${esc(d.producto.nombre)} x${d.cantidad}`).join(", ");
      return `<tr><td>${v.idVenta}</td><td>${esc(v.fecha.replace("T", " ").substring(0, 16))}</td>` +
        `<td>${esc(v.cliente.nombre)}</td><td>${productos}</td>` +
        `<td>${formatoPesos(v.total)}</td><td>${esc(v.estado)}</td></tr>`;
    }).join("");
}

/* ---- Eventos ---- */
$("btnAgregarLinea").addEventListener("click", () => { agregarLinea(); actualizarTotal(); });
$("lineasVenta").addEventListener("input", actualizarTotal);
$("lineasVenta").addEventListener("click", (e) => {
  if (e.target.classList.contains("l_quitar")) {
    e.target.closest(".linea").remove();
    actualizarTotal();
  }
});
$("formVenta").addEventListener("submit", registrarVenta);