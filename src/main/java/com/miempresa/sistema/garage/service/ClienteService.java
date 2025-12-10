package com.miempresa.sistema.garage.service;

import com.miempresa.sistema.garage.model.Cliente;
import com.miempresa.sistema.garage.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // Obtener todos los clientes
    public List<Cliente> obtenerTodosClientes() {
        return clienteRepository.findAll();
    }

    // Obtener cliente por ID
    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    // Guardar cliente (crear o actualizar)
    public Cliente guardarCliente(Cliente cliente) {
        // Validar que el DNI/RUC no exista (excepto si es el mismo cliente)
        if (cliente.getIdCliente() == null) {
            // Nuevo cliente: verificar que DNI no exista
            if (clienteRepository.existsByDniRuc(cliente.getDniRuc())) {
                throw new RuntimeException("Ya existe un cliente con DNI/RUC: " + cliente.getDniRuc());
            }
        } else {
            // Cliente existente: verificar que DNI no esté usado por otro cliente
            Optional<Cliente> clienteExistente = clienteRepository.findByDniRuc(cliente.getDniRuc());
            if (clienteExistente.isPresent() && !clienteExistente.get().getIdCliente().equals(cliente.getIdCliente())) {
                throw new RuntimeException("El DNI/RUC " + cliente.getDniRuc() + " ya pertenece a otro cliente");
            }
        }
        
        return clienteRepository.save(cliente);
    }

    // Eliminar cliente por ID
    public void eliminarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("No se encontró el cliente con ID: " + id);
        }
        clienteRepository.deleteById(id);
    }

    // Buscar cliente por DNI/RUC
    public Optional<Cliente> buscarPorDniRuc(String dniRuc) {
        return clienteRepository.findByDniRuc(dniRuc);
    }

    // Buscar clientes por apellido
    public List<Cliente> buscarPorApellido(String apellido) {
        return clienteRepository.findByApellidosContainingIgnoreCase(apellido);
    }

    // Buscar cliente por email
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByCorreo(email);
    }

    // Verificar si existe un cliente por DNI/RUC
    public boolean existePorDniRuc(String dniRuc) {
        return clienteRepository.existsByDniRuc(dniRuc);
    }

    // Contar total de clientes
    public long contarClientes() {
        return clienteRepository.count();
    }

    // Obtener cliente con sus vehículos
    public Optional<Cliente> obtenerClienteConVehiculos(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()) {
            // Forzar carga de vehículos (LAZY loading)
            cliente.get().getVehiculos().size();
        }
        return cliente;
    }
}