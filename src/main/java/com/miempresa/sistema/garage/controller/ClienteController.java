package com.miempresa.sistema.garage.controller;

import com.miempresa.sistema.garage.model.Cliente;
import com.miempresa.sistema.garage.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// 🔍 IMPORTS PARA DEBUG
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Map;
// 🔍 FIN DE IMPORTS

import java.util.Optional;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;
    
    // 🔍 INYECTAR JdbcTemplate PARA DEBUG
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // LISTAR CLIENTES
    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("listaClientes", clienteService.obtenerTodosClientes());
        return "cliente/clientes-list";
    }

    // FORMULARIO PARA CREAR NUEVO CLIENTE
    @GetMapping("/nuevo")
    public String formularioNuevoCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente/cliente-form";
    }

    // GUARDAR CLIENTE (nuevo o editado)
    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute("cliente") Cliente cliente, 
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        
        // Log para debug
        System.out.println("🔍 Guardando cliente: " + cliente.getNombre());
        
        try {
            Cliente clienteGuardado = clienteService.guardarCliente(cliente);
            System.out.println("✅ Cliente guardado con ID: " + clienteGuardado.getIdCliente());
            
            // Mensaje de éxito
            redirectAttributes.addFlashAttribute("mensaje", "Cliente guardado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            
            // Redirect específico
            return "redirect:/clientes";
            
        } catch (Exception e) {
            System.err.println("❌ Error al guardar cliente: " + e.getMessage());
            e.printStackTrace();
            
            model.addAttribute("cliente", cliente);
            model.addAttribute("errorMensaje", e.getMessage());
            return "cliente/cliente-form";
        }
    }

    // FORMULARIO PARA EDITAR CLIENTE
    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model) {
        Optional<Cliente> cliente = clienteService.obtenerClientePorId(id);

        if (cliente.isPresent()) {
            model.addAttribute("cliente", cliente.get());
            return "cliente/cliente-form";
        } else {
            return "redirect:/clientes";
        }
    }

    // ✅ ELIMINAR CLIENTE - CAMBIADO A POST
    @PostMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clienteService.eliminarCliente(id);
            redirectAttributes.addFlashAttribute("mensaje", "✅ Cliente eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "❌ Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/clientes";
    }
    
    // 🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍
    // 🔍 MÉTODOS DEBUG - NO AFECTAN LA FUNCIONALIDAD NORMAL
    // 🔍 SE PUEDEN ELIMINAR DESPUÉS DE ENCONTRAR EL PROBLEMA
    // 🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍

    /**
     * MÉTODO DEBUG 1: Para ver dónde se guardan los datos
     * URL: http://localhost:8082/clientes/donde-guarda
     */
    @GetMapping("/donde-guarda")
    @ResponseBody  // Esto hace que devuelva texto/HTML directamente
    public String dondeGuarda() {
        try {
            // Intentar acceder a diferentes nombres de tabla
            String[] posiblesTablas = {
                "CLIENTE", "cliente", "Clientes", "clientes", 
                "cliente_subtio", "tbl_cliente", "tbl_clientes"
            };
            
            StringBuilder result = new StringBuilder();
            result.append("<h2>🔍 BUSCANDO DÓNDE SE GUARDAN LOS DATOS</h2>");
            result.append("<p>Base de datos: garage_clinica</p><hr>");
            
            for (String tabla : posiblesTablas) {
                try {
                    Long count = jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM " + tabla, Long.class
                    );
                    result.append("<p style='color: green; font-weight: bold;'>✅ ")
                          .append(tabla)
                          .append(" → ").append(count).append(" registros</p>");
                    
                    // Si tiene datos, mostrar algunos
                    if (count > 0) {
                        List<Map<String, Object>> datos = jdbcTemplate.queryForList(
                            "SELECT TOP 3 * FROM " + tabla
                        );
                        result.append("<div style='background: #f0f0f0; padding: 10px; margin: 5px;'>");
                        result.append("<strong>Primeros 3 registros:</strong><br>");
                        for (Map<String, Object> fila : datos) {
                            result.append(fila.toString()).append("<br>");
                        }
                        result.append("</div>");
                    }
                } catch (Exception e) {
                    result.append("<p style='color: gray;'>❌ ")
                          .append(tabla)
                          .append(" → No existe</p>");
                }
            }
            
            result.append("<hr><p><strong>📊 Total de clientes según tu servicio:</strong> ")
                  .append(clienteService.contarClientes())
                  .append("</p>");
            
            return result.toString();
            
        } catch (Exception e) {
            return "<h2>❌ Error en debug</h2><pre>" + e.getMessage() + "</pre>";
        }
    }

    /**
     * MÉTODO DEBUG 2: Para ver todas las tablas de la base de datos
     * URL: http://localhost:8082/clientes/tablas-bd
     */
    @GetMapping("/tablas-bd")
    @ResponseBody
    public String verTablasBD() {
        try {
            List<Map<String, Object>> tablas = jdbcTemplate.queryForList(
                "SELECT TABLE_SCHEMA, TABLE_NAME, TABLE_TYPE " +
                "FROM INFORMATION_SCHEMA.TABLES " +
                "WHERE TABLE_TYPE = 'BASE TABLE' " +
                "ORDER BY TABLE_NAME"
            );
            
            StringBuilder result = new StringBuilder();
            result.append("<h2>📋 TABLAS EN LA BASE DE DATOS</h2>");
            result.append("<table border='1' style='border-collapse: collapse;'>");
            result.append("<tr><th>Esquema</th><th>Nombre Tabla</th><th>Tipo</th></tr>");
            
            for (Map<String, Object> tabla : tablas) {
                result.append("<tr>");
                result.append("<td>").append(tabla.get("TABLE_SCHEMA")).append("</td>");
                result.append("<td><strong>").append(tabla.get("TABLE_NAME")).append("</strong></td>");
                result.append("<td>").append(tabla.get("TABLE_TYPE")).append("</td>");
                result.append("</tr>");
            }
            
            result.append("</table>");
            result.append("<p>Total tablas: ").append(tablas.size()).append("</p>");
            
            return result.toString();
            
        } catch (Exception e) {
            return "<h2>❌ Error</h2><pre>" + e.getMessage() + "</pre>";
        }
    }
    
    /**
     * MÉTODO DEBUG 3: Contar clientes desde diferentes fuentes
     * URL: http://localhost:8082/clientes/debug-count
     */
    @GetMapping("/debug-count")
    @ResponseBody
    public String debugCount() {
        try {
            long countService = clienteService.contarClientes();
            long countRepository = clienteService.obtenerTodosClientes().size();
            
            StringBuilder result = new StringBuilder();
            result.append("<h2>📊 CONTEO DE CLIENTES</h2>");
            result.append("<ul>");
            result.append("<li><strong>Desde ClienteService:</strong> ").append(countService).append("</li>");
            result.append("<li><strong>Desde lista obtenida:</strong> ").append(countRepository).append("</li>");
            result.append("</ul>");
            
            // Intentar contar desde SQL directo
            try {
                Long countSQL = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM CLIENTE", Long.class
                );
                result.append("<li><strong>Desde tabla CLIENTE (SQL):</strong> ").append(countSQL).append("</li>");
            } catch (Exception e) {
                result.append("<li><strong>Desde tabla CLIENTE (SQL):</strong> ❌ Tabla no encontrada</li>");
            }
            
            return result.toString();
            
        } catch (Exception e) {
            return "<h2>❌ Error en debug</h2><pre>" + e.getMessage() + "</pre>";
        }
    }
    
    // 🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍
    // 🔍 FIN DE MÉTODOS DEBUG
    // 🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍🔍

}