package com.miempresa.sistema.garage.controller;

import com.miempresa.sistema.garage.model.OrdenTrabajo;
import com.miempresa.sistema.garage.model.Vehiculo;
import com.miempresa.sistema.garage.service.OrdenTrabajoService;
import com.miempresa.sistema.garage.service.VehiculoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ordenes")
public class OrdenTrabajoController {

    @Autowired
    private OrdenTrabajoService ordenTrabajoService;

    @Autowired
    private VehiculoService vehiculoService;

    // LISTAR
    @GetMapping
    public String listarOrdenes(Model model) {
        model.addAttribute("ordenes", ordenTrabajoService.obtenerTodasOrdenes());
        return "orden/orden-list";
    }

    // FORMULARIO NUEVO
    @GetMapping("/nuevo")
    public String nuevaOrden(Model model) {
        model.addAttribute("orden", new OrdenTrabajo());
        model.addAttribute("vehiculos", vehiculoService.obtenerTodosVehiculos());
        return "orden/orden-form";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editarOrden(@PathVariable Long id, Model model) {
        model.addAttribute("orden", ordenTrabajoService.obtenerOrdenPorId(id).orElse(null));
        model.addAttribute("vehiculos", vehiculoService.obtenerTodosVehiculos());
        return "order/orden-form";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardarOrden(OrdenTrabajo orden, @RequestParam Long idVehiculo) {

        Vehiculo vehiculo = vehiculoService.obtenerVehiculoPorId(idVehiculo).orElse(null);
        orden.setVehiculo(vehiculo);

        ordenTrabajoService.guardarOrden(orden);
        return "redirect:/ordenes";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminarOrden(@PathVariable Long id) {
        ordenTrabajoService.eliminarOrden(id);
        return "redirect:/ordenes";
    }
}
