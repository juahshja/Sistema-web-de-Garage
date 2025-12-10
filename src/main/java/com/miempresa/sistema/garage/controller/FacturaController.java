package com.miempresa.sistema.garage.controller;

import com.miempresa.sistema.garage.model.Factura;
import com.miempresa.sistema.garage.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    // LISTAR FACTURAS
    @GetMapping
    public String listarFacturas(Model model) {
        model.addAttribute("facturas", facturaService.obtenerTodasFacturas());
        return "factura/factura-list";
    }

    // FORMULARIO NUEVA FACTURA
    @GetMapping("/nuevo")
    public String nuevaFactura(Model model) {
        model.addAttribute("factura", new Factura());
        return "factura/factura-form";
    }

    // GUARDAR FACTURA
    @PostMapping("/guardar")
    public String guardarFactura(@ModelAttribute Factura factura, Model model) {
        try {
            factura.setFechaEmision(LocalDateTime.now());
            facturaService.guardarFactura(factura);
            return "redirect:/facturas";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("factura", factura);
            return "factura/factura-form";
        }
    }

    // EDITAR FACTURA
    @GetMapping("/editar/{id}")
    public String editarFactura(@PathVariable Long id, Model model) {
        Factura factura = facturaService.obtenerFacturaPorId(id).orElse(null);
        model.addAttribute("factura", factura);
        return "factura/factura-form";
    }

    // ELIMINAR FACTURA
    @GetMapping("/eliminar/{id}")
    public String eliminarFactura(@PathVariable Long id) {
        facturaService.eliminarFactura(id);
        return "redirect:/facturas";
    }

    // DETALLE FACTURA
    @GetMapping("/detalle/{id}")
    public String detalleFactura(@PathVariable Long id, Model model) {
        Factura factura = facturaService.obtenerFacturaPorId(id).orElse(null);
        model.addAttribute("factura", factura);
        return "factura/factura-detalle";
    }
}
