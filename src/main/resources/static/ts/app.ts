// app.ts - Simple y funcional
console.log('🚀 Sistema Garage - TypeScript cargado');

const APP_CONFIG = {
    nombre: 'Sistema Garage',
    version: '1.0.0',
    puerto: 8082
};

console.log(`✅ ${APP_CONFIG.nombre} v${APP_CONFIG.version} iniciado`);

// Funciones globales básicas
function mostrarMensaje(mensaje: string, tipo: 'success' | 'error' | 'info' = 'info'): void {
    console.log(`[${tipo.toUpperCase()}] ${mensaje}`);
}

// Exportar
export { APP_CONFIG, mostrarMensaje };