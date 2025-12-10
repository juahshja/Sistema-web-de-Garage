package com.miempresa.sistema.garage.controller;

import com.miempresa.sistema.garage.model.Cotizacion;
import com.miempresa.sistema.garage.model.OrdenTrabajo;
import com.miempresa.sistema.garage.service.CotizacionService;
import com.miempresa.sistema.garage.service.OrdenTrabajoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/cotizaciones")
public class CotizacionController {

    @Autowired
    private CotizacionService cotizacionService;

    @Autowired
    private OrdenTrabajoService ordenTrabajoService;

    // LISTADO GENERAL
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("cotizaciones", cotizacionService.obtenerTodasCotizaciones());
        return "cotizacion/cotizacion-list";
    }

    // FORMULARIO CREAR
    @GetMapping("/nuevo")
    public String nuevo(Model model) {

        Cotizacion cotizacion = new Cotizacion();

        // Generar número automático
        cotizacion.setNumeroCotizacion(cotizacionService.generarNumeroCotizacionAutomatico());

        model.addAttribute("cotizacion", cotizacion);

        // LISTA DE ÓRDENES para seleccionar
        model.addAttribute("ordenes", ordenTrabajoService.obtenerTodasOrdenes());

        return "cotizacion/cotizacion-form";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Optional<Cotizacion> cotOpt = cotizacionService.obtenerCotizacionPorId(id);

        if (cotOpt.isEmpty()) {
            return "redirect:/cotizaciones";
        }

        model.addAttribute("cotizacion", cotOpt.get());
        model.addAttribute("ordenes", ordenTrabajoService.obtenerTodasOrdenes());

        return "cotizacion/cotizacion-form";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Cotizacion cotizacion, @RequestParam Long idOrdenTrabajo, Model model) {

        // Vincular orden de trabajo
        Optional<OrdenTrabajo> ordenOpt = ordenTrabajoService.obtenerOrdenPorId(idOrdenTrabajo);

        if (ordenOpt.isPresent()) {
            cotizacion.setOrdenTrabajo(ordenOpt.get());
        } else {
            model.addAttribute("error", "La orden de trabajo seleccionada no existe.");
            return "cotizacion/cotizacion-form";
        }

        cotizacionService.guardarCotizacion(cotizacion);

        return "redirect:/cotizaciones";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        cotizacionService.eliminarCotizacion(id);
        return "redirect:/cotizaciones";
    }
}
