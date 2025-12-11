// static/ts/app.ts - VERSIÓN FINAL FUNCIONAL

// Importar SOLO los archivos que SÍ tienes
import { ClienteValidaciones } from './validaciones/cliente-validaciones.js';
import { DOMUtils } from './utils/dom-utils.js';

/**
 * Configuración de validaciones para el formulario de cliente
 */
export function configurarValidacionesCliente(): void {
    console.log('🔧 Configurando validaciones para formulario de cliente...');
    
    // Solo ejecutar si estamos en la página de cliente
    if (!document.getElementById('nombre')) {
        return;
    }
    
    // ===================== CONFIGURAR EVENTOS =====================
    
    // Configurar formato automático de teléfono
    DOMUtils.aplicarFormatoTelefono('telefono');
    
    // Validación en tiempo real para cada campo
    configurarValidacionEnTiempoReal('nombre', (valor) => 
        ClienteValidaciones.validarNombre(valor));
    
    configurarValidacionEnTiempoReal('apellidos', (valor) => 
        ClienteValidaciones.validarApellidos(valor));
    
    configurarValidacionEnTiempoReal('dniRuc', (valor) => 
        ClienteValidaciones.validarDocumento(valor));
    
    configurarValidacionEnTiempoReal('correo', (valor) => 
        ClienteValidaciones.validarEmail(valor));
    
    configurarValidacionEnTiempoReal('telefono', (valor) => 
        ClienteValidaciones.validarTelefonoPeruano(valor));
    
    // Validación al enviar el formulario
    const formulario = document.querySelector('form');
    if (formulario) {
        formulario.addEventListener('submit', validarFormularioCompleto);
    }
    
    console.log('✅ Validaciones configuradas correctamente');
}

/**
 * Configura validación en tiempo real para un campo específico
 */
function configurarValidacionEnTiempoReal(
    campoId: string, 
    funcionValidacion: (valor: string) => { valido: boolean; mensaje: string }
): void {
    const campo = document.getElementById(campoId);
    if (!campo) return;
    
    // Eventos para validar
    campo.addEventListener('blur', () => validarCampo(campoId, funcionValidacion));
    campo.addEventListener('input', () => validarCampo(campoId, funcionValidacion));
    
    // Validar al cargar la página (si hay datos precargados)
    setTimeout(() => validarCampo(campoId, funcionValidacion), 100);
}

/**
 * Valida un campo individual y muestra feedback
 */
function validarCampo(
    campoId: string, 
    funcionValidacion: (valor: string) => { valido: boolean; mensaje: string }
): void {
    const campo = document.getElementById(campoId) as HTMLInputElement;
    if (!campo) return;
    
    const valor = campo.value;
    const resultado = funcionValidacion(valor);
    
    if (resultado.valido) {
        DOMUtils.eliminarError(campoId);
        // Mostrar éxito solo para algunos campos
        if (campoId === 'dniRuc' && valor.length > 0) {
            const docResult = ClienteValidaciones.validarDocumento(valor);
            if (docResult.tipo) {
                DOMUtils.mostrarExito(campoId, `✓ ${docResult.tipo} válido`);
            }
        }
    } else {
        DOMUtils.mostrarError(campoId, resultado.mensaje);
        DOMUtils.eliminarExito(campoId);
    }
    
    // Verificar estado general del formulario
    verificarEstadoFormulario();
}

/**
 * Valida todo el formulario antes de enviar
 */
function validarFormularioCompleto(evento: Event): void {
    console.log('🔄 Validando formulario completo...');
    
    // Obtener valores del formulario
    const formData = {
        nombre: (document.getElementById('nombre') as HTMLInputElement).value,
        apellidos: (document.getElementById('apellidos') as HTMLInputElement).value,
        dniRuc: (document.getElementById('dniRuc') as HTMLInputElement).value,
        correo: (document.getElementById('correo') as HTMLInputElement).value,
        telefono: (document.getElementById('telefono') as HTMLInputElement).value
    };
    
    // Validar todos los campos
    const resultado = ClienteValidaciones.validarFormularioCompleto(formData);
    
    if (!resultado.valido) {
        // Prevenir envío
        evento.preventDefault();
        
        // Mostrar todos los errores
        resultado.errores.forEach(error => {
            DOMUtils.mostrarError(error.campo, error.mensaje);
        });
        
        // Mostrar mensaje general
        mostrarMensajeGlobal('❌ Por favor corrige los errores antes de continuar', 'error');
        
        // Desplazarse al primer error
        if (resultado.errores.length > 0) {
            const primerError = document.getElementById(resultado.errores[0].campo);
            primerError?.scrollIntoView({ behavior: 'smooth', block: 'center' });
            primerError?.focus();
        }
        
        console.log('❌ Formulario inválido:', resultado.errores);
    } else {
        console.log('✅ Formulario válido, enviando...');
        mostrarMensajeGlobal('✅ Validación exitosa, enviando formulario...', 'exito');
    }
}

/**
 * Verifica el estado general del formulario y habilita/deshabilita el botón
 */
function verificarEstadoFormulario(): void {
    // Verificar campos requeridos
    const camposRequeridos = ['nombre', 'apellidos', 'dniRuc'];
    let formularioValido = true;
    
    for (const campoId of camposRequeridos) {
        const campo = document.getElementById(campoId) as HTMLInputElement;
        if (!campo || campo.value.trim() === '') {
            formularioValido = false;
            break;
        }
    }
    
    // También verificar que no haya errores visibles
    const erroresVisibles = document.querySelectorAll('.error-message');
    if (erroresVisibles.length > 0) {
        formularioValido = false;
    }
    
    DOMUtils.toggleBotonEnviar(formularioValido);
}

/**
 * Muestra un mensaje global en la parte superior
 */
function mostrarMensajeGlobal(mensaje: string, tipo: 'exito' | 'error' | 'info'): void {
    // Eliminar mensaje anterior
    const mensajeAnterior = document.getElementById('mensaje-global');
    if (mensajeAnterior) {
        mensajeAnterior.remove();
    }
    
    // Crear nuevo mensaje
    const mensajeDiv = document.createElement('div');
    mensajeDiv.id = 'mensaje-global';
    mensajeDiv.className = `fixed top-4 right-4 z-50 p-4 rounded-lg shadow-lg transform transition-all duration-300 ${
        tipo === 'exito' ? 'bg-green-100 border-green-400 text-green-800' :
        tipo === 'error' ? 'bg-red-100 border-red-400 text-red-800' :
        'bg-blue-100 border-blue-400 text-blue-800'
    } border`;
    
    mensajeDiv.innerHTML = `
        <div class="flex items-center">
            <i class="fas ${
                tipo === 'exito' ? 'fa-check-circle' :
                tipo === 'error' ? 'fa-exclamation-circle' :
                'fa-info-circle'
            } mr-3 text-lg"></i>
            <span>${mensaje}</span>
        </div>
    `;
    
    document.body.appendChild(mensajeDiv);
    
    // Auto-eliminar después de 5 segundos
    setTimeout(() => {
        mensajeDiv.style.opacity = '0';
        mensajeDiv.style.transform = 'translateX(100%)';
        setTimeout(() => mensajeDiv.remove(), 300);
    }, 5000);
}

// ===================== INICIALIZACIÓN =====================

// Iniciar cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    console.log('🚀 GaragePro - Sistema de validaciones cargado');
    
    // Configurar validaciones para cliente
    configurarValidacionesCliente();
    
    // Botón de prueba (tu código original)
    const boton = document.getElementById("btnTest");
    boton?.addEventListener("click", () => {
        alert("¡Funciona! TypeScript está conectado al HTML correctamente 🎉");
    });
});