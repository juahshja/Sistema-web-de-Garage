// ============================================================
// VALIDACIONES PARA VEHÍCULO - JavaScript
// Archivo: src/main/resources/static/js/validaciones/vehiculoValidacion.js
// ============================================================

class VehiculoValidacion {
    
    constructor() {
        // Expresiones regulares
        this.REGEX_PLACA = /^[A-Z0-9]{3}-[A-Z0-9]{3}$/;
        this.REGEX_SOLO_LETRAS_NUMEROS = /^[a-zA-Z0-9\s]+$/;
        this.REGEX_SOLO_LETRAS = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/;
    }

    // ============== VALIDACIÓN DE PLACA ==============
    validarPlaca(placa) {
        placa = placa.trim().toUpperCase();

        if (placa === '') {
            return { isValid: false, message: '❌ La placa es obligatoria' };
        }

        if (placa.length < 6 || placa.length > 10) {
            return { isValid: false, message: '❌ La placa debe tener entre 6 y 10 caracteres' };
        }

        if (!this.REGEX_PLACA.test(placa) && !this.REGEX_SOLO_LETRAS_NUMEROS.test(placa)) {
            return { isValid: false, message: '❌ Formato de placa inválido (Ej: ABC-123)' };
        }

        return { isValid: true, message: '✅ Placa válida' };
    }

    // ============== VALIDACIÓN DE MARCA ==============
    validarMarca(marca) {
        marca = marca.trim();

        if (marca === '') {
            return { isValid: false, message: '❌ La marca es obligatoria' };
        }

        if (marca.length < 2) {
            return { isValid: false, message: '❌ La marca debe tener al menos 2 caracteres' };
        }

        if (marca.length > 50) {
            return { isValid: false, message: '❌ La marca no puede exceder 50 caracteres' };
        }

        if (!this.REGEX_SOLO_LETRAS.test(marca)) {
            return { isValid: false, message: '❌ La marca solo puede contener letras' };
        }

        return { isValid: true, message: '✅ Marca válida' };
    }

    // ============== VALIDACIÓN DE MODELO ==============
    validarModelo(modelo) {
        modelo = modelo.trim();

        if (modelo === '') {
            return { isValid: false, message: '❌ El modelo es obligatorio' };
        }

        if (modelo.length < 2) {
            return { isValid: false, message: '❌ El modelo debe tener al menos 2 caracteres' };
        }

        if (modelo.length > 50) {
            return { isValid: false, message: '❌ El modelo no puede exceder 50 caracteres' };
        }

        if (!this.REGEX_SOLO_LETRAS_NUMEROS.test(modelo)) {
            return { isValid: false, message: '❌ El modelo solo puede contener letras y números' };
        }

        return { isValid: true, message: '✅ Modelo válido' };
    }

    // ============== VALIDACIÓN DE AÑO ==============
    validarAno(ano) {
        const anoNum = typeof ano === 'string' ? parseInt(ano.trim()) : ano;
        const currentYear = new Date().getFullYear();

        if (isNaN(anoNum)) {
            return { isValid: false, message: '❌ El año debe ser un número válido' };
        }

        if (anoNum < 1900) {
            return { isValid: false, message: '❌ El año no puede ser anterior a 1900' };
        }

        if (anoNum > currentYear + 1) {
            return { isValid: false, message: `❌ El año no puede ser mayor a ${currentYear + 1}` };
        }

        return { isValid: true, message: '✅ Año válido' };
    }

    // ============== VALIDACIÓN DE CLIENTE ==============
    validarCliente(clienteId) {
        if (!clienteId || clienteId === '' || clienteId === '0') {
            return { isValid: false, message: '❌ Debe seleccionar un cliente' };
        }

        return { isValid: true, message: '✅ Cliente seleccionado' };
    }

    // ============== VALIDAR FORMULARIO COMPLETO ==============
    validarFormularioCompleto(placa, marca, modelo, ano, clienteId) {
        const resultados = [
            this.validarPlaca(placa),
            this.validarMarca(marca),
            this.validarModelo(modelo),
            this.validarAno(ano),
            this.validarCliente(clienteId)
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
        console.log('🚗 Iniciando validaciones de Vehículo...');

        const form = document.querySelector('form[th\\:action*="/vehiculos/guardar"]');
        if (!form) {
            console.error('❌ No se encontró el formulario de vehículos');
            return;
        }

        // Referencias a los inputs
        const placaInput = document.querySelector('input[th\\:field="*{placa}"]');
        const marcaInput = document.querySelector('input[th\\:field="*{marca}"]');
        const modeloInput = document.querySelector('input[th\\:field="*{modelo}"]');
        const anoInput = document.querySelector('input[th\\:field="*{anoFabricacion}"]');
        const clienteSelect = document.querySelector('select[th\\:field="*{cliente.idCliente}"]');

        // ============== VALIDACIÓN DE PLACA ==============
        if (placaInput) {
            placaInput.addEventListener('input', () => {
                placaInput.value = placaInput.value.toUpperCase();
            });

            placaInput.addEventListener('blur', () => {
                const resultado = this.validarPlaca(placaInput.value);
                if (resultado.isValid) {
                    this.limpiarError('placa');
                } else {
                    this.mostrarError('placa', resultado.message);
                }
            });
        }

        // ============== VALIDACIÓN DE MARCA ==============
        if (marcaInput) {
            marcaInput.addEventListener('blur', () => {
                const resultado = this.validarMarca(marcaInput.value);
                if (resultado.isValid) {
                    this.limpiarError('marca');
                } else {
                    this.mostrarError('marca', resultado.message);
                }
            });

            marcaInput.addEventListener('input', () => {
                if (marcaInput.value.trim().length >= 2) {
                    this.limpiarError('marca');
                }
            });
        }

        // ============== VALIDACIÓN DE MODELO ==============
        if (modeloInput) {
            modeloInput.addEventListener('blur', () => {
                const resultado = this.validarModelo(modeloInput.value);
                if (resultado.isValid) {
                    this.limpiarError('modelo');
                } else {
                    this.mostrarError('modelo', resultado.message);
                }
            });

            modeloInput.addEventListener('input', () => {
                if (modeloInput.value.trim().length >= 2) {
                    this.limpiarError('modelo');
                }
            });
        }

        // ============== VALIDACIÓN DE AÑO ==============
        if (anoInput) {
            anoInput.addEventListener('input', () => {
                anoInput.value = anoInput.value.replace(/[^0-9]/g, '');
            });

            anoInput.addEventListener('blur', () => {
                const resultado = this.validarAno(anoInput.value);
                if (resultado.isValid) {
                    this.limpiarError('anoFabricacion');
                } else {
                    this.mostrarError('anoFabricacion', resultado.message);
                }
            });
        }

        // ============== VALIDACIÓN DE CLIENTE ==============
        if (clienteSelect) {
            clienteSelect.addEventListener('change', () => {
                const resultado = this.validarCliente(clienteSelect.value);
                if (resultado.isValid) {
                    this.limpiarError('cliente');
                } else {
                    this.mostrarError('cliente', resultado.message);
                }
            });
        }

        // ============== VALIDACIÓN AL ENVIAR FORMULARIO ==============
        form.addEventListener('submit', (e) => {
            const placa = placaInput?.value || '';
            const marca = marcaInput?.value || '';
            const modelo = modeloInput?.value || '';
            const ano = anoInput?.value || '';
            const clienteId = clienteSelect?.value || '';

            const esValido = this.validarFormularioCompleto(placa, marca, modelo, ano, clienteId);

            if (esValido) {
                console.log('✅ Formulario de vehículo válido, enviando...');
                
                const btnGuardar = form.querySelector('button[type="submit"]');
                if (btnGuardar) {
                    btnGuardar.disabled = true;
                    btnGuardar.innerHTML = '<i class="fas fa-spinner fa-spin mr-2"></i> Guardando...';
                }
                
                return true;
                
            } else {
                e.preventDefault();
                console.log('❌ Formulario de vehículo inválido');
                
                // Validar y mostrar errores individuales
                const resultados = {
                    placa: this.validarPlaca(placa),
                    marca: this.validarMarca(marca),
                    modelo: this.validarModelo(modelo),
                    anoFabricacion: this.validarAno(ano),
                    cliente: this.validarCliente(clienteId)
                };

                for (const [campo, resultado] of Object.entries(resultados)) {
                    if (!resultado.isValid) {
                        this.mostrarError(campo, resultado.message);
                    }
                }
                
                const primerError = document.querySelector('.border-red-500');
                if (primerError) {
                    primerError.scrollIntoView({ behavior: 'smooth', block: 'center' });
                    primerError.focus();
                }

                alert('⚠️ Por favor corrija los errores del formulario antes de continuar.');
                
                return false;
            }
        });

        console.log('✅ Validaciones de Vehículo inicializadas correctamente');
    }
}

// ============== INICIALIZAR AL CARGAR LA PÁGINA ==============
document.addEventListener('DOMContentLoaded', function() {
    const validador = new VehiculoValidacion();
    validador.inicializar();
});