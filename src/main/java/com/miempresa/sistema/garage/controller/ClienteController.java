package com.miempresa.sistema.garage.controller;

import com.miempresa.sistema.garage.model.Cliente;
import com.miempresa.sistema.garage.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // LISTAR CLIENTES
    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("listaClientes", clienteService.obtenerTodosClientes());
         return "cliente/clientes-list";           // ✅ DESPUÉS
     }

    // FORMULARIO PARA CREAR NUEVO CLIENTE
    @GetMapping("/nuevo")
    public String formularioNuevoCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente/cliente-form";            // ✅ DESPUÉS
    }

    // GUARDAR CLIENTE (nuevo o editado)
    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute Cliente cliente, Model model) {
        try {
            clienteService.guardarCliente(cliente);
            return "redirect:/clientes";
        } catch (Exception e) {
            model.addAttribute("cliente", cliente);
            model.addAttribute("errorMensaje", e.getMessage());
        return "cliente/cliente-form";            // ✅ DESPUÉS
        }
    }

    // FORMULARIO PARA EDITAR CLIENTE
    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model) {
        Optional<Cliente> cliente = clienteService.obtenerClientePorId(id);

        if (cliente.isPresent()) {
            model.addAttribute("cliente", cliente.get());
return "cliente/cliente-form";            // ✅ DESPUÉS
        } else {
            return "redirect:/clientes";
        }
    }

    // ELIMINAR CLIENTE
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return "redirect:/clientes";
    }
}
