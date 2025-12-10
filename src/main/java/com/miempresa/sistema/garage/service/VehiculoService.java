package com.miempresa.sistema.garage.service;

import com.miempresa.sistema.garage.model.Vehiculo;
import com.miempresa.sistema.garage.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VehiculoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    // Obtener todos los vehículos
    public List<Vehiculo> obtenerTodosVehiculos() {
        return vehiculoRepository.findAll();
    }

    // Obtener vehículo por ID
    public Optional<Vehiculo> obtenerVehiculoPorId(Long id) {
        return vehiculoRepository.findById(id);
    }

    // Guardar vehículo
    public Vehiculo guardarVehiculo(Vehiculo vehiculo) {
        // Validar que la placa no exista (excepto si es el mismo vehículo)
        if (vehiculo.getIdVehiculo() == null) {
            // Nuevo vehículo: verificar que placa no exista
            if (vehiculoRepository.existsByPlaca(vehiculo.getPlaca())) {
                throw new RuntimeException("Ya existe un vehículo con placa: " + vehiculo.getPlaca());
            }
        } else {
            // Vehículo existente: verificar que placa no esté usada por otro vehículo
            Optional<Vehiculo> vehiculoExistente = vehiculoRepository.findByPlaca(vehiculo.getPlaca());
            if (vehiculoExistente.isPresent() && !vehiculoExistente.get().getIdVehiculo().equals(vehiculo.getIdVehiculo())) {
                throw new RuntimeException("La placa " + vehiculo.getPlaca() + " ya pertenece a otro vehículo");
            }
        }
        
        return vehiculoRepository.save(vehiculo);
    }

    // Eliminar vehículo
    public void eliminarVehiculo(Long id) {
        if (!vehiculoRepository.existsById(id)) {
            throw new RuntimeException("No se encontró el vehículo con ID: " + id);
        }
        vehiculoRepository.deleteById(id);
    }

    // Buscar vehículo por placa
    public Optional<Vehiculo> buscarPorPlaca(String placa) {
        return vehiculoRepository.findByPlaca(placa);
    }

    // Buscar vehículos por marca
    public List<Vehiculo> buscarPorMarca(String marca) {
        return vehiculoRepository.findByMarcaContainingIgnoreCase(marca);
    }

    // Buscar vehículos de un cliente
   public List<Vehiculo> obtenerVehiculosDeCliente(Long clienteId) {
    return vehiculoRepository.findByCliente_IdCliente(clienteId);
}
    // Verificar si existe un vehículo por placa
    public boolean existePorPlaca(String placa) {
        return vehiculoRepository.existsByPlaca(placa);
    }

    // Contar vehículos por marca
    public long contarPorMarca(String marca) {
        return vehiculoRepository.countByMarca(marca);
    }
}