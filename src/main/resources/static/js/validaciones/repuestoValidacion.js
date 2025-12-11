// Validación del formulario de REPUESTO (versión JS)

function validarRepuesto() {
    const nombre = document.querySelector('input[name="nombre"]');
    const codigo = document.querySelector('input[name="codigoBarras"]');
    const precioCosto = document.querySelector('input[name="precioCosto"]');
    const precioVenta = document.querySelector('input[name="precioVenta"]');
    const stockActual = document.querySelector('input[name="stockActual"]');
    const stockMinimo = document.querySelector('input[name="stockMinimo"]');

    let valido = true;

    // Nombre
    if (!nombre.value.trim()) {
        nombre.classList.add("border-red-500", "bg-red-50");
        valido = false;
    } else {
        nombre.classList.remove("border-red-500", "bg-red-50");
    }

    // Código opcional pero numérico si existe
    if (codigo.value.trim() && !/^[0-9]+$/.test(codigo.value)) {
        codigo.classList.add("border-red-500", "bg-red-50");
        valido = false;
    } else {
        codigo.classList.remove("border-red-500", "bg-red-50");
    }

    // Precio costo
    if (Number(precioCosto.value) <= 0) {
        precioCosto.classList.add("border-red-500", "bg-red-50");
        valido = false;
    } else {
        precioCosto.classList.remove("border-red-500", "bg-red-50");
    }

    // Precio venta
    if (Number(precioVenta.value) <= 0 || Number(precioVenta.value) < Number(precioCosto.value)) {
        precioVenta.classList.add("border-red-500", "bg-red-50");
        valido = false;
    } else {
        precioVenta.classList.remove("border-red-500", "bg-red-50");
    }

    // Stock actual
    if (Number(stockActual.value) < 0) {
        stockActual.classList.add("border-red-500", "bg-red-50");
        valido = false;
    } else {
        stockActual.classList.remove("border-red-500", "bg-red-50");
    }

    // Stock mínimo
    if (Number(stockMinimo.value) < 0) {
        stockMinimo.classList.add("border-red-500", "bg-red-50");
        valido = false;
    } else {
        stockMinimo.classList.remove("border-red-500", "bg-red-50");
    }

    return valido;
}

// Lo exportas si lo necesitas
// export { validarRepuesto };
