/* Conecta el formulario y la tabla con la API REST del sistema.
   Cada módulo se describe en el objeto MODULOS; el resto del código es común. */
const API = "/api/";

const MODULOS = {
  clientes: {
    singular: "cliente", plural: "clientes", id: "idCliente",
    campos: [
      { n: "nombre", t: "Nombre", req: true, max: 100 },
      { n: "documento", t: "Documento", req: true, max: 20 },
      { n: "telefono", t: "Teléfono", max: 20 },
      { n: "correo", t: "Correo", tipo: "email", max: 100 },
      { n: "direccion", t: "Dirección", max: 150 }
    ]
  },
  categorias: {
    singular: "categoría", plural: "categorías", id: "idCategoria",
    campos: [
      { n: "nombre", t: "Nombre", req: true, max: 50 },
      { n: "descripcion", t: "Descripción", max: 200 }
    ]
  },
  proveedores: {
    singular: "proveedor", plural: "proveedores", id: "idProveedor",
    campos: [
      { n: "nombreEmpresa", t: "Empresa", req: true, max: 100 },
      { n: "telefono", t: "Teléfono", max: 20 },
      { n: "correo", t: "Correo", tipo: "email", max: 100 },
      { n: "direccion", t: "Dirección", max: 150 }
    ]
  },
  productos: {
    singular: "producto", plural: "productos", id: "idProducto",
    campos: [
      { n: "codigo", t: "Código", req: true, max: 20 },
      { n: "nombre", t: "Nombre", req: true, max: 100 },
      { n: "descripcion", t: "Descripción", max: 200 },
      { n: "precioCompra", t: "Precio de compra", tipo: "number", req: true },
      { n: "precioVenta", t: "Precio de venta", tipo: "number", req: true },
      { n: "stockActual", t: "Stock actual", tipo: "number", req: true },
      { n: "stockMinimo", t: "Stock mínimo", tipo: "number", req: true },
      { n: "categoria", t: "Categoría", tipo: "categoria", req: true },
      { n: "proveedor", t: "Proveedor", tipo: "proveedor", req: true }
    ]
  }
};

let actual = "clientes";   // módulo que se está viendo
let editandoId = null;     // id del registro que se edita (null = registro nuevo)
let datos = [];            // filas que muestra la tabla

const $ = (id) => document.getElementById(id);

/** Lee un valor anidado: categoria -> nombre de la categoría. */
function valorCelda(fila, c) {
  if (c.tipo === "categoria") return fila.categoria ? fila.categoria.nombre : "";
  if (c.tipo === "proveedor") return fila.proveedor ? fila.proveedor.nombreEmpresa : "";
  return fila[c.n] ?? "";
}

/** Dibuja el formulario del módulo activo. */
async function dibujarFormulario() {
  const m = MODULOS[actual];
  let categorias = [];
  let proveedores = [];
  if (actual === "productos") {
    categorias = await pedir("categorias");
    proveedores = await pedir("proveedores");
  }

  $("campos").innerHTML = m.campos.map((c) => {
    const req = c.req ? ' <span class="req">*</span>' : "";
    let control;
    if (c.tipo === "categoria") {
      control = `<select id="f_${c.n}"><option value="">Seleccione...</option>` +
        categorias.map((k) => `<option value="${k.idCategoria}">${esc(k.nombre)}</option>`).join("") +
        "</select>";
    } else if (c.tipo === "proveedor") {
      control = `<select id="f_${c.n}"><option value="">Seleccione...</option>` +
        proveedores.map((p) => `<option value="${p.idProveedor}">${esc(p.nombreEmpresa)}</option>`).join("") +
        "</select>";
    } else {
      const tipo = c.tipo === "number" ? "number" : (c.tipo || "text");
      const paso = c.tipo === "number" ? ' step="any"' : "";
      control = `<input id="f_${c.n}" type="${tipo === "email" ? "text" : tipo}"${paso}>`;
    }
    return `<div class="campo" id="c_${c.n}"><label for="f_${c.n}">${c.t}${req}</label>${control}<small></small></div>`;
  }).join("");
}

/** Dibuja la cabecera de la tabla y las filas. */
function dibujarTabla() {
  const m = MODULOS[actual];
  $("encabezado").innerHTML = "<tr><th>ID</th>" + m.campos.map((c) => `<th>${c.t}</th>`).join("") + "<th>Acciones</th></tr>";
  const cols = m.campos.length + 2;
  $("filas").innerHTML = datos.length === 0
    ? `<tr><td class="vacio" colspan="${cols}">No hay ${m.plural} registrados</td></tr>`
    : datos.map((f) =>
      `<tr><td>${f[m.id]}</td>` + m.campos.map((c) => `<td>${esc(valorCelda(f, c))}</td>`).join("") +
      `<td><button class="mini editar" data-id="${f[m.id]}">Editar</button>` +
      `<button class="mini borrar" data-id="${f[m.id]}">Eliminar</button></td></tr>`).join("");
}

/** Cambia de módulo y recarga todo. */
async function cambiarModulo(nombre) {
  actual = nombre;
  document.querySelectorAll("#menu button").forEach((b) => b.classList.toggle("activo", b.dataset.ent === nombre));
  await dibujarFormulario();
  await cargar();
  limpiar();
}

async function cargar() {
  datos = await pedir(actual);
  $("tituloLista").textContent = "Lista de " + MODULOS[actual].plural;
  dibujarTabla();
}

function limpiar() {
  editandoId = null;
  $("formulario").reset();
  $("errores").hidden = true;
  document.querySelectorAll(".campo").forEach((d) => { d.classList.remove("invalido"); d.querySelector("small").textContent = ""; });
  $("tituloForm").textContent = "Registrar " + MODULOS[actual].singular;
  $("btnGuardar").textContent = "Guardar";
}

/** Arma el objeto JSON que espera la API a partir del formulario. */
function leerFormulario() {
  const obj = {};
  MODULOS[actual].campos.forEach((c) => {
    const v = $("f_" + c.n).value.trim();
    if (c.tipo === "categoria") obj.categoria = { idCategoria: v === "" ? null : Number(v) };
    else if (c.tipo === "proveedor") obj.proveedor = { idProveedor: v === "" ? null : Number(v) };
    else if (c.tipo === "number") obj[c.n] = v === "" ? null : Number(v);
    else obj[c.n] = v === "" ? null : v;
  });
  return obj;
}

/** Marca en rojo el campo cuyo nombre aparece en el mensaje del servidor. */
function marcarErrores(detalles) {
  detalles.forEach((d) => {
    const nombre = d.split(":")[0].trim();
    const caja = $("c_" + nombre);
    if (caja) { caja.classList.add("invalido"); caja.querySelector("small").textContent = d.substring(d.indexOf(":") + 1).trim(); }
  });
}

async function guardar(e) {
  e.preventDefault();
  limpiarMarcas();
  const url = API + actual + (editandoId ? "/" + editandoId : "");
  const resp = await fetch(url, {
    method: editandoId ? "PUT" : "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(leerFormulario())
  });
  if (resp.ok) {
    aviso("Registro " + (editandoId ? "actualizado" : "guardado") + " correctamente.", true);
    limpiar();
    await cargar();
  } else {
    const err = await resp.json().catch(() => ({ mensaje: "Error inesperado", detalles: [] }));
    const lista = err.detalles && err.detalles.length ? "<ul>" + err.detalles.map((d) => `<li>${esc(d)}</li>`).join("") + "</ul>" : "";
    $("errores").innerHTML = `<strong>${esc(err.mensaje)}</strong>${lista}`;
    $("errores").hidden = false;
    marcarErrores(err.detalles || []);
  }
}

function limpiarMarcas() {
  $("errores").hidden = true;
  document.querySelectorAll(".campo").forEach((d) => { d.classList.remove("invalido"); d.querySelector("small").textContent = ""; });
}

function editar(id) {
  const m = MODULOS[actual];
  const fila = datos.find((f) => String(f[m.id]) === String(id));
  if (!fila) return;
  limpiar();
  editandoId = id;
  m.campos.forEach((c) => {
    let v = fila[c.n];
    if (c.tipo === "categoria") v = fila.categoria ? fila.categoria.idCategoria : "";
    if (c.tipo === "proveedor") v = fila.proveedor ? fila.proveedor.idProveedor : "";
    $("f_" + c.n).value = v ?? "";
  });
  $("tituloForm").textContent = "Editar " + m.singular;
  $("btnGuardar").textContent = "Actualizar";
  window.scrollTo({ top: 0, behavior: "smooth" });
}

async function eliminar(id) {
  if (!confirm("¿Eliminar este registro?")) return;
  const resp = await fetch(API + actual + "/" + id, { method: "DELETE" });
  if (resp.ok) { aviso("Registro eliminado.", true); await cargar(); }
  else {
    const err = await resp.json().catch(() => ({ mensaje: "No se pudo eliminar" }));
    aviso(err.mensaje, false);
  }
}

/** Pide una lista a la API; si falla, avisa en pantalla en vez de quedarse en silencio. */
async function pedir(ruta) {
  const r = await fetch(API + ruta);
  if (!r.ok) {
    aviso("No se pudo cargar " + ruta + " (error " + r.status + ")", false);
    return [];
  }
  return r.json();
}

function aviso(texto, ok) {
  const a = $("aviso");
  a.textContent = texto;
  a.className = "aviso " + (ok ? "ok" : "mal");
  a.hidden = false;
  setTimeout(() => { a.hidden = true; }, 4000);
}

/** Evita que el texto escrito se interprete como HTML (protege de <script>). */
function esc(t) {
  return String(t ?? "").replace(/[&<>"']/g, (c) => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[c]));
}

/* ---- Eventos ---- */
$("menu").addEventListener("click", (e) => { if (e.target.dataset.ent) cambiarModulo(e.target.dataset.ent); });
$("formulario").addEventListener("submit", guardar);
$("btnCancelar").addEventListener("click", limpiar);
$("filas").addEventListener("click", (e) => {
  if (e.target.classList.contains("editar")) editar(e.target.dataset.id);
  if (e.target.classList.contains("borrar")) eliminar(e.target.dataset.id);
});

cambiarModulo("clientes");