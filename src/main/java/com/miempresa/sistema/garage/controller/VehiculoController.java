package com.miempresa.sistema.garage.controller;

import com.miempresa.sistema.garage.model.Cliente;
import com.miempresa.sistema.garage.model.Vehiculo;
import com.miempresa.sistema.garage.service.ClienteService;
import com.miempresa.sistema.garage.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private ClienteService clienteService;

    // ⭐ LISTA DE VEHÍCULOS
    @GetMapping
    public String listarVehiculos(Model model) {
        model.addAttribute("vehiculos", vehiculoService.obtenerTodosVehiculos());
        return "vehiculo/vehiculo-list";
    }

    // ⭐ FORMULARIO PARA NUEVO VEHÍCULO
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        model.addAttribute("clientes", clienteService.obtenerTodosClientes());
        return "vehiculo/vehiculo-form";
    }

    // ⭐ GUARDAR VEHÍCULO
    @PostMapping("/guardar")
    public String guardarVehiculo(
            @ModelAttribute Vehiculo vehiculo,
            @RequestParam Long idCliente,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        Optional<Cliente> clienteOpt = clienteService.obtenerClientePorId(idCliente);

        if (clienteOpt.isEmpty()) {
            model.addAttribute("error", "❌ Cliente no válido.");
            model.addAttribute("vehiculo", vehiculo);
            model.addAttribute("clientes", clienteService.obtenerTodosClientes());
            return "vehiculo/vehiculo-form";
        }

        vehiculo.setCliente(clienteOpt.get());

        try {
            vehiculoService.guardarVehiculo(vehiculo);
            
            // Mensaje de éxito
            redirectAttributes.addFlashAttribute("mensaje", "✅ Vehículo guardado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            
            return "redirect:/vehiculos";
            
        } catch (RuntimeException e) {
            model.addAttribute("error", "❌ " + e.getMessage());
            model.addAttribute("vehiculo", vehiculo);
            model.addAttribute("clientes", clienteService.obtenerTodosClientes());
            return "vehiculo/vehiculo-form";
        }
    }

    // ⭐ EDITAR VEHÍCULO
    @GetMapping("/editar/{id}")
    public String editarVehiculo(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Vehiculo> vehiculoOpt = vehiculoService.obtenerVehiculoPorId(id);

        if (vehiculoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "❌ Vehículo no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/vehiculos";
        }

        model.addAttribute("vehiculo", vehiculoOpt.get());
        model.addAttribute("clientes", clienteService.obtenerTodosClientes());

        return "vehiculo/vehiculo-form";
    }

    // ⭐ ELIMINAR VEHÍCULO - ✅ CAMBIADO A POST
    @PostMapping("/eliminar/{id}")
    public String eliminarVehiculo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            vehiculoService.eliminarVehiculo(id);
            redirectAttributes.addFlashAttribute("mensaje", "✅ Vehículo eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "❌ Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/vehiculos";
    }
}