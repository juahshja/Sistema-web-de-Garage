// ============================================================
// VALIDACIONES PARA CLIENTE - JavaScript
// Archivo: src/main/resources/static/js/validaciones/clienteValidacion.js
// ============================================================

class ClienteValidacion {
    
    constructor() {
        // Expresiones regulares
        this.REGEX_SOLO_LETRAS = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/;
        this.REGEX_SOLO_NUMEROS = /^[0-9]+$/;
        this.REGEX_EMAIL = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        this.REGEX_DNI = /^[0-9]{8}$/;
        this.REGEX_RUC = /^[0-9]{11}$/;
        this.REGEX_TELEFONO = /^[0-9]{9}$/;
    }

    // ============== VALIDACIÓN DE NOMBRE ==============
    validarNombre(nombre) {
        nombre = nombre.trim();

        if (nombre === '') {
            return { isValid: false, message: '❌ El nombre es obligatorio' };
        }

        if (nombre.length < 2) {
            return { isValid: false, message: '❌ El nombre debe tener al menos 2 caracteres' };
        }

        if (nombre.length > 50) {
            return { isValid: false, message: '❌ El nombre no puede exceder 50 caracteres' };
        }

        if (!this.REGEX_SOLO_LETRAS.test(nombre)) {
            return { isValid: false, message: '❌ El nombre solo puede contener letras' };
        }

        return { isValid: true, message: '✅ Nombre válido' };
    }

    // ============== VALIDACIÓN DE APELLIDOS ==============
    validarApellidos(apellidos) {
        apellidos = apellidos.trim();

        if (apellidos === '') {
            return { isValid: false, message: '❌ Los apellidos son obligatorios' };
        }

        if (apellidos.length < 2) {
            return { isValid: false, message: '❌ Los apellidos deben tener al menos 2 caracteres' };
        }

        if (apellidos.length > 100) {
            return { isValid: false, message: '❌ Los apellidos no pueden exceder 100 caracteres' };
        }

        if (!this.REGEX_SOLO_LETRAS.test(apellidos)) {
            return { isValid: false, message: '❌ Los apellidos solo pueden contener letras' };
        }

        return { isValid: true, message: '✅ Apellidos válidos' };
    }

    // ============== VALIDACIÓN DE DNI/RUC ==============
    validarDniRuc(dniRuc) {
        dniRuc = dniRuc.trim();

        if (dniRuc === '') {
            return { isValid: false, message: '❌ El DNI/RUC es obligatorio' };
        }

        if (!this.REGEX_SOLO_NUMEROS.test(dniRuc)) {
            return { isValid: false, message: '❌ El DNI/RUC solo puede contener números' };
        }

        if (this.REGEX_DNI.test(dniRuc)) {
            return { isValid: true, message: '✅ DNI válido (8 dígitos)' };
        }

        if (this.REGEX_RUC.test(dniRuc)) {
            return { isValid: true, message: '✅ RUC válido (11 dígitos)' };
        }

        return { isValid: false, message: '❌ Debe ser un DNI (8 dígitos) o RUC (11 dígitos)' };
    }

    // ============== VALIDACIÓN DE CORREO ==============
    validarCorreo(correo) {
        correo = correo.trim();

        // El correo es opcional, si está vacío es válido
        if (correo === '') {
            return { isValid: true, message: '' };
        }

        if (correo.length > 100) {
            return { isValid: false, message: '❌ El correo no puede exceder 100 caracteres' };
        }

        if (!this.REGEX_EMAIL.test(correo)) {
            return { isValid: false, message: '❌ El formato del correo no es válido' };
        }

        return { isValid: true, message: '✅ Correo válido' };
    }

    // ============== VALIDACIÓN DE TELÉFONO ==============
    validarTelefono(telefono) {
        telefono = telefono.trim();

        // El teléfono es opcional, si está vacío es válido
        if (telefono === '') {
            return { isValid: true, message: '' };
        }

        if (!this.REGEX_SOLO_NUMEROS.test(telefono)) {
            return { isValid: false, message: '❌ El teléfono solo puede contener números' };
        }

        if (!this.REGEX_TELEFONO.test(telefono)) {
            return { isValid: false, message: '❌ El teléfono debe tener exactamente 9 dígitos' };
        }

        return { isValid: true, message: '✅ Teléfono válido' };
    }

    // ============== VALIDAR FORMULARIO COMPLETO ==============
    validarFormularioCompleto(nombre, apellidos, dniRuc, correo, telefono) {
        const resultados = [
            this.validarNombre(nombre),
            this.validarApellidos(apellidos),
            this.validarDniRuc(dniRuc),
            this.validarCorreo(correo),
            this.validarTelefono(telefono)
        ];

        return resultados.every(resultado => resultado.isValid);
    }

    // ============== MOSTRAR ERROR EN UI ==============
    mostrarError(inputId, mensaje) {
        const input = document.getElementById(inputId);
        const errorElement = document.getElementById(`error-${inputId}`);

        if (input) {
            input.classList.add('border-red-500', 'bg-red-50');
            input.classList.remove('border-green-500', 'bg-green-50');
        }

        if (errorElement) {
            errorElement.textContent = mensaje;
            errorElement.classList.remove('hidden');
            errorElement.classList.add('text-red-600', 'text-xs', 'mt-1', 'flex', 'items-center');
        }
    }

    // ============== LIMPIAR ERROR EN UI ==============
    limpiarError(inputId) {
        const input = document.getElementById(inputId);
        const errorElement = document.getElementById(`error-${inputId}`);

        if (input) {
            input.classList.remove('border-red-500', 'bg-red-50');
            input.classList.add('border-green-500', 'bg-green-50');
        }

        if (errorElement) {
            errorElement.classList.add('hidden');
            errorElement.textContent = '';
        }
    }

    // ============== INICIALIZAR VALIDACIONES ==============
    inicializar() {
        console.log('🚀 Iniciando validaciones de Cliente...');

        const form = document.getElementById('formCliente');
        if (!form) {
            console.error('❌ No se encontró el formulario con id="formCliente"');
            return;
        }

        // Referencias a los inputs
        const nombreInput = document.getElementById('nombre');
        const apellidosInput = document.getElementById('apellidos');
        const dniRucInput = document.getElementById('dniRuc');
        const correoInput = document.getElementById('correo');
        const telefonoInput = document.getElementById('telefono');
        const tipoDocumento = document.getElementById('tipo-documento');

        // ============== VALIDACIÓN DE NOMBRE ==============
        if (nombreInput) {
            nombreInput.addEventListener('blur', () => {
                const resultado = this.validarNombre(nombreInput.value);
                if (resultado.isValid) {
                    this.limpiarError('nombre');
                } else {
                    this.mostrarError('nombre', resultado.message);
                }
            });

            nombreInput.addEventListener('input', () => {
                if (nombreInput.value.trim().length >= 2) {
                    this.limpiarError('nombre');
                }
            });
        }

        // ============== VALIDACIÓN DE APELLIDOS ==============
        if (apellidosInput) {
            apellidosInput.addEventListener('blur', () => {
                const resultado = this.validarApellidos(apellidosInput.value);
                if (resultado.isValid) {
                    this.limpiarError('apellidos');
                } else {
                    this.mostrarError('apellidos', resultado.message);
                }
            });

            apellidosInput.addEventListener('input', () => {
                if (apellidosInput.value.trim().length >= 2) {
                    this.limpiarError('apellidos');
                }
            });
        }

        // ============== VALIDACIÓN DE DNI/RUC ==============
        if (dniRucInput) {
            // Solo permitir números
            dniRucInput.addEventListener('input', () => {
                dniRucInput.value = dniRucInput.value.replace(/[^0-9]/g, '');

                // Actualizar tipo de documento
                const longitud = dniRucInput.value.length;
                if (tipoDocumento) {
                    if (longitud === 8) {
                        tipoDocumento.textContent = '✅ DNI válido (8 dígitos)';
                        tipoDocumento.className = 'text-xs text-green-600 font-medium';
                    } else if (longitud === 11) {
                        tipoDocumento.textContent = '✅ RUC válido (11 dígitos)';
                        tipoDocumento.className = 'text-xs text-green-600 font-medium';
                    } else {
                        tipoDocumento.textContent = 'DNI: 8 dígitos | RUC: 11 dígitos';
                        tipoDocumento.className = 'text-xs text-gray-500';
                    }
                }
            });

            dniRucInput.addEventListener('blur', () => {
                const resultado = this.validarDniRuc(dniRucInput.value);
                if (resultado.isValid) {
                    this.limpiarError('dniRuc');
                } else {
                    this.mostrarError('dniRuc', resultado.message);
                }
            });
        }

        // ============== VALIDACIÓN DE CORREO ==============
        if (correoInput) {
            correoInput.addEventListener('blur', () => {
                const resultado = this.validarCorreo(correoInput.value);
                if (resultado.isValid) {
                    this.limpiarError('correo');
                } else {
                    this.mostrarError('correo', resultado.message);
                }
            });
        }

        // ============== VALIDACIÓN DE TELÉFONO ==============
        if (telefonoInput) {
            // Solo permitir números
            telefonoInput.addEventListener('input', () => {
                telefonoInput.value = telefonoInput.value.replace(/[^0-9]/g, '');
            });

            telefonoInput.addEventListener('blur', () => {
                const resultado = this.validarTelefono(telefonoInput.value);
                if (resultado.isValid) {
                    this.limpiarError('telefono');
                } else {
                    this.mostrarError('telefono', resultado.message);
                }
            });
        }

        // ============== VALIDACIÓN AL ENVIAR FORMULARIO ==============
        form.addEventListener('submit', (e) => {
            const nombre = nombreInput?.value || '';
            const apellidos = apellidosInput?.value || '';
            const dniRuc = dniRucInput?.value || '';
            const correo = correoInput?.value || '';
            const telefono = telefonoInput?.value || '';

            // Validar todos los campos
            const resultadoNombre = this.validarNombre(nombre);
            const resultadoApellidos = this.validarApellidos(apellidos);
            const resultadoDniRuc = this.validarDniRuc(dniRuc);
            const resultadoCorreo = this.validarCorreo(correo);
            const resultadoTelefono = this.validarTelefono(telefono);

            // Si todo es válido, permitir envío
            const esValido = this.validarFormularioCompleto(nombre, apellidos, dniRuc, correo, telefono);

            if (esValido) {
                console.log('✅ Formulario válido, enviando...');
                
                // Deshabilitar botón para evitar doble envío
                const btnGuardar = document.getElementById('btnGuardar');
                if (btnGuardar) {
                    btnGuardar.disabled = true;
                    btnGuardar.innerHTML = '<i class="fas fa-spinner fa-spin mr-2"></i> Guardando...';
                }
                
                // ✅ PERMITIR QUE EL FORMULARIO SE ENVÍE NORMALMENTE
                return true;
                
            } else {
                // ❌ PREVENIR ENVÍO SOLO SI HAY ERRORES
                e.preventDefault();
                console.log('❌ Formulario inválido');
                
                // Mostrar errores
                if (!resultadoNombre.isValid) {
                    this.mostrarError('nombre', resultadoNombre.message);
                } else {
                    this.limpiarError('nombre');
                }

                if (!resultadoApellidos.isValid) {
                    this.mostrarError('apellidos', resultadoApellidos.message);
                } else {
                    this.limpiarError('apellidos');
                }

                if (!resultadoDniRuc.isValid) {
                    this.mostrarError('dniRuc', resultadoDniRuc.message);
                } else {
                    this.limpiarError('dniRuc');
                }

                if (!resultadoCorreo.isValid) {
                    this.mostrarError('correo', resultadoCorreo.message);
                } else {
                    this.limpiarError('correo');
                }

                if (!resultadoTelefono.isValid) {
                    this.mostrarError('telefono', resultadoTelefono.message);
                } else {
                    this.limpiarError('telefono');
                }
                
                // Scroll al primer error
                const primerError = document.querySelector('.border-red-500');
                if (primerError) {
                    primerError.scrollIntoView({ behavior: 'smooth', block: 'center' });
                    primerError.focus();
                }

                // Mostrar alerta
                alert('⚠️ Por favor corrija los errores del formulario antes de continuar.');
                
                return false;
            }
        });

        console.log('✅ Validaciones de Cliente inicializadas correctamente');
    }
}

// ============== INICIALIZAR AL CARGAR LA PÁGINA ==============
document.addEventListener('DOMContentLoaded', function() {
    const validador = new ClienteValidacion();
    validador.inicializar();
});