package com.miempresa.sistema.garage.controller;

import com.miempresa.sistema.garage.model.Cliente;
import com.miempresa.sistema.garage.model.Vehiculo;
import com.miempresa.sistema.garage.service.ClienteService;
import com.miempresa.sistema.garage.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        return "vehiculo/vehiculo-list"; // Thymeleaf
    }

    // ⭐ FORMULARIO PARA NUEVO VEHÍCULO
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());

        // Para el combo de clientes
        model.addAttribute("clientes", clienteService.obtenerTodosClientes());

        return "vehiculo/vehiculo-form";
    }

    // ⭐ GUARDAR VEHÍCULO
    @PostMapping("/guardar")
    public String guardarVehiculo(
            @ModelAttribute Vehiculo vehiculo,
            @RequestParam Long idCliente,
            Model model
    ) {

        Optional<Cliente> clienteOpt = clienteService.obtenerClientePorId(idCliente);

        if (clienteOpt.isEmpty()) {
            model.addAttribute("error", "Cliente no válido.");
            return "vehiculo/vehiculo-form";
        }

        vehiculo.setCliente(clienteOpt.get());

        try {
            vehiculoService.guardarVehiculo(vehiculo);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("clientes", clienteService.obtenerTodosClientes());
            return "vehiculo/vehiculo-form";
        }

        return "redirect:/vehiculos";
    }

    // ⭐ EDITAR VEHÍCULO
    @GetMapping("/editar/{id}")
    public String editarVehiculo(@PathVariable Long id, Model model) {

        Optional<Vehiculo> vehiculoOpt = vehiculoService.obtenerVehiculoPorId(id);

        if (vehiculoOpt.isEmpty()) {
            return "redirect:/vehiculos?error=notfound";
        }

        model.addAttribute("vehiculo", vehiculoOpt.get());
        model.addAttribute("clientes", clienteService.obtenerTodosClientes());

        return "vehiculo/vehiculo-form";
    }

    // ⭐ ELIMINAR VEHÍCULO
    @GetMapping("/eliminar/{id}")
    public String eliminarVehiculo(@PathVariable Long id) {
        vehiculoService.eliminarVehiculo(id);
        return "redirect:/vehiculos";
    }
}
