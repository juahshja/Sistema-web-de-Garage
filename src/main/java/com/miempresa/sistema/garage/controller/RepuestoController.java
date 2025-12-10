package com.miempresa.sistema.garage.controller;

import com.miempresa.sistema.garage.model.Repuesto;
import com.miempresa.sistema.garage.service.RepuestoService;
import com.miempresa.sistema.garage.service.ProveedorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/repuestos")
public class RepuestoController {

    @Autowired
    private RepuestoService repuestoService;

    @Autowired
    private ProveedorService proveedorService;

    // --------------------------------------------------
    // LISTAR REPUESTOS
    // --------------------------------------------------
    @GetMapping
    public String listarRepuestos(Model model) {
        model.addAttribute("repuestos", repuestoService.obtenerTodosRepuestos());
        return "repuesto/repuesto-list"; // <-- tu vista
    }

    // --------------------------------------------------
    // FORMULARIO NUEVO REPUESTO
    // --------------------------------------------------
    @GetMapping("/nuevo")
    public String nuevoRepuesto(Model model) {
        model.addAttribute("repuesto", new Repuesto());
        model.addAttribute("proveedores", proveedorService.obtenerTodosProveedores());
        return "repuesto/repuesto-form";
    }

    // --------------------------------------------------
    // EDITAR REPUESTO
    // --------------------------------------------------
    @GetMapping("/editar/{id}")
    public String editarRepuesto(@PathVariable Long id, Model model) {
        Optional<Repuesto> repuestoOpt = repuestoService.obtenerRepuestoPorId(id);

        if (repuestoOpt.isPresent()) {
            model.addAttribute("repuesto", repuestoOpt.get());
            model.addAttribute("proveedores", proveedorService.obtenerTodosProveedores());
            return "repuesto/repuesto-form";
        }

        return "redirect:/repuestos";
    }

    // --------------------------------------------------
    // GUARDAR REPUESTO (NUEVO O EDITADO)
    // --------------------------------------------------
    @PostMapping("/guardar")
    public String guardarRepuesto(@ModelAttribute Repuesto repuesto) {

        // Si proveedor viene solo con el ID, lo resolvemos
        if (repuesto.getProveedor() != null && repuesto.getProveedor().getIdProveedor() != null) {
            proveedorService.obtenerProveedorPorId(repuesto.getProveedor().getIdProveedor())
                    .ifPresent(repuesto::setProveedor);
        } else {
            repuesto.setProveedor(null);
        }

        repuestoService.guardarRepuesto(repuesto);
        return "redirect:/repuestos";
    }

    // --------------------------------------------------
    // ELIMINAR
    // --------------------------------------------------
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        repuestoService.eliminarRepuesto(id);
        return "redirect:/repuestos";
    }

    // --------------------------------------------------
    // BUSCAR POR NOMBRE
    // --------------------------------------------------
    @GetMapping("/buscar")
    public String buscar(@RequestParam("nombre") String nombre, Model model) {
        model.addAttribute("repuestos", repuestoService.buscarPorNombre(nombre));
        return "repuesto/repuesto-list";
    }

    // --------------------------------------------------
    // FILTRAR POR PROVEEDOR
    // --------------------------------------------------
    @GetMapping("/proveedor/{idProveedor}")
    public String porProveedor(@PathVariable Long idProveedor, Model model) {
        model.addAttribute("repuestos", repuestoService.obtenerRepuestosPorProveedor(idProveedor));
        return "repuesto/repuesto-list";
    }

    // --------------------------------------------------
    // ACTUALIZAR STOCK (AUMENTAR / DISMINUIR)
    // --------------------------------------------------
    @PostMapping("/stock/{id}")
    public String actualizarStock(
            @PathVariable Long id,
            @RequestParam("cantidad") Integer cantidad,
            @RequestParam("accion") String accion) {

        if (accion.equals("aumentar")) {
            repuestoService.aumentarStock(id, cantidad);
        } else if (accion.equals("disminuir")) {
            repuestoService.disminuirStock(id, cantidad);
        }

        return "redirect:/repuestos";
    }
}
