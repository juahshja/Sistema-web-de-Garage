// proveedorValidation.ts

// Expresiones regulares
const rucRegex: RegExp = /^[0-9]{11}$/;
const telefonoRegex: RegExp = /^[0-9()+\-\s]{6,20}$/;
const emailRegex: RegExp = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector("form") as HTMLFormElement | null;
    if (!form) return;

    const razonSocial = form.querySelector("input[name='razonSocial']") as HTMLInputElement;
    const ruc = form.querySelector("input[name='ruc']") as HTMLInputElement;
    const telefono = form.querySelector("input[name='telefono']") as HTMLInputElement;
    const email = form.querySelector("input[name='email']") as HTMLInputElement;

    form.addEventListener("submit", (e: Event) => {
        let errores: string[] = [];

        // Razón Social obligatoria
        if (razonSocial.value.trim().length < 3) {
            errores.push("La razón social debe tener al menos 3 caracteres.");
        }

        // RUC validación estricta
        if (!rucRegex.test(ruc.value.trim())) {
            errores.push("El RUC debe tener exactamente 11 dígitos.");
        }

        // Teléfono opcional pero válido
        if (telefono.value.trim().length > 0 && !telefonoRegex.test(telefono.value.trim())) {
            errores.push("El teléfono contiene caracteres inválidos.");
        }

        // Email opcional pero válido
        if (email.value.trim().length > 0 && !emailRegex.test(email.value.trim())) {
            errores.push("El email no tiene un formato válido.");
        }

        // Mostrar errores si existen
        if (errores.length > 0) {
            e.preventDefault();
            alert("❌ Corrige los siguientes errores:\n\n" + errores.join("\n"));
        }
    });
});
