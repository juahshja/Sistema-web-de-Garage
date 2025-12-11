// proveedorValidation.js

const rucRegex = /^[0-9]{11}$/;
const telefonoRegex = /^[0-9()+\-\s]{6,20}$/;
const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector("form");
    if (!form) return;

    const razonSocial = form.querySelector("input[name='razonSocial']");
    const ruc = form.querySelector("input[name='ruc']");
    const telefono = form.querySelector("input[name='telefono']");
    const email = form.querySelector("input[name='email']");

    form.addEventListener("submit", (e) => {
        let errores = [];

        if (razonSocial.value.trim().length < 3) {
            errores.push("La razón social debe tener al menos 3 caracteres.");
        }

        if (!rucRegex.test(ruc.value.trim())) {
            errores.push("El RUC debe tener exactamente 11 dígitos.");
        }

        if (telefono.value.trim().length > 0 && !telefonoRegex.test(telefono.value.trim())) {
            errores.push("El teléfono contiene caracteres inválidos.");
        }

        if (email.value.trim().length > 0 && !emailRegex.test(email.value.trim())) {
            errores.push("El email no tiene un formato válido.");
        }

        if (errores.length > 0) {
            e.preventDefault();
            alert("❌ Corrige los siguientes errores:\n\n" + errores.join("\n"));
        }
    });
});
