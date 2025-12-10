package com.miempresa.sistema.garage.controller;

import com.miempresa.sistema.garage.model.Proveedor;
import com.miempresa.sistema.garage.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    // Listar proveedores
    @GetMapping
    public String listarProveedores(Model model) {
        model.addAttribute("proveedores", proveedorService.obtenerTodosProveedores());
        return "proveedor/proveedor-list";
    }

    // Formulario para crear proveedor
    @GetMapping("/nuevo")
    public String nuevoProveedor(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        return "proveedor/proveedor-form";
    }

    // Formulario para editar proveedor
    @GetMapping("/editar/{id}")
    public String editarProveedor(@PathVariable Long id, Model model) {
        Proveedor proveedor = proveedorService.obtenerProveedorPorId(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        model.addAttribute("proveedor", proveedor);
        return "proveedor/proveedor-form";
    }

    // Guardar proveedor
    @PostMapping("/guardar")
    public String guardarProveedor(@ModelAttribute Proveedor proveedor, Model model) {
        try {
            proveedorService.guardarProveedor(proveedor);
            return "redirect:/proveedores";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("proveedor", proveedor);
            return "proveedor/proveedor-form";
        }
    }

    // Eliminar proveedor
    @GetMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Long id) {
        proveedorService.eliminarProveedor(id);
        return "redirect:/proveedores";
    }

    // Activar proveedor
    @GetMapping("/activar/{id}")
    public String activarProveedor(@PathVariable Long id) {
        proveedorService.activarProveedor(id);
        return "redirect:/proveedores";
    }

    // Desactivar proveedor
    @GetMapping("/desactivar/{id}")
    public String desactivarProveedor(@PathVariable Long id) {
        proveedorService.desactivarProveedor(id);
        return "redirect:/proveedores";
    }
}
