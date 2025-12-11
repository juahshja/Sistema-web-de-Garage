package com.miempresa.sistema.garage.controller;

import com.miempresa.sistema.garage.model.Cliente;
import com.miempresa.sistema.garage.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
}