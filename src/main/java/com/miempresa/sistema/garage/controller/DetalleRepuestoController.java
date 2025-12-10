package com.miempresa.sistema.garage.controller;

import com.miempresa.sistema.garage.model.DetalleRepuesto;
import com.miempresa.sistema.garage.model.OrdenTrabajo;
import com.miempresa.sistema.garage.model.Repuesto;
import com.miempresa.sistema.garage.service.DetalleRepuestoService;
import com.miempresa.sistema.garage.service.OrdenTrabajoService;
import com.miempresa.sistema.garage.service.RepuestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/detalles-repuestos")
public class DetalleRepuestoController {

    @Autowired
    private DetalleRepuestoService detalleRepuestoService;
    
    @Autowired
    private OrdenTrabajoService ordenTrabajoService;
    
    @Autowired
    private RepuestoService repuestoService;

    // 📌 LISTAR TODOS LOS DETALLES
    @GetMapping
    public String listarDetalles(Model model) {
        List<DetalleRepuesto> detalles = detalleRepuestoService.obtenerTodosDetalles();
        model.addAttribute("detalles", detalles);
        return "detalle/list";
    }

    // 📌 FORMULARIO PARA NUEVO DETALLE
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("detalle", new DetalleRepuesto());
        
        // Cargar datos para los selects
        List<OrdenTrabajo> ordenes = ordenTrabajoService.obtenerTodasOrdenes();
        List<Repuesto> repuestos = repuestoService.obtenerTodosRepuestos();
        
        model.addAttribute("ordenes", ordenes);
        model.addAttribute("repuestos", repuestos);
        
        return "detalle/form";
    }

    // 📌 GUARDAR NUEVO DETALLE
    @PostMapping("/guardar")
    public String guardarDetalle(
            @RequestParam Long ordenId,
            @RequestParam Long repuestoId,
            @RequestParam Integer cantidad,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Obtener orden y repuesto
            Optional<OrdenTrabajo> ordenOpt = ordenTrabajoService.obtenerOrdenPorId(ordenId);
            Optional<Repuesto> repuestoOpt = repuestoService.obtenerRepuestoPorId(repuestoId);
            
            if (ordenOpt.isPresent() && repuestoOpt.isPresent()) {
                OrdenTrabajo orden = ordenOpt.get();
                Repuesto repuesto = repuestoOpt.get();
                
                // Crear el detalle
                DetalleRepuesto detalle = new DetalleRepuesto(orden, repuesto, cantidad);
                
                // Guardar usando el servicio
                detalleRepuestoService.guardarDetalle(detalle);
                
                redirectAttributes.addFlashAttribute("mensaje", 
                    "Detalle guardado correctamente");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            } else {
                redirectAttributes.addFlashAttribute("mensaje", 
                    "Orden o repuesto no encontrado");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", 
                "Error al guardar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/detalles-repuestos";
    }

    // 📌 ELIMINAR DETALLE
    @GetMapping("/eliminar/{id}")
    public String eliminarDetalle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            detalleRepuestoService.eliminarDetalle(id);
            redirectAttributes.addFlashAttribute("mensaje", 
                "Detalle eliminado correctamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", 
                "Error al eliminar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/detalles-repuestos";
    }
}