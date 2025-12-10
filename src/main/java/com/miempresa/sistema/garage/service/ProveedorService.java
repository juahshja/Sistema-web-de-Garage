package com.miempresa.sistema.garage.service;

import com.miempresa.sistema.garage.model.Proveedor;
import com.miempresa.sistema.garage.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    public List<Proveedor> obtenerTodosProveedores() {
        return proveedorRepository.findAll();
    }

    public List<Proveedor> obtenerProveedoresActivos() {
        return proveedorRepository.findByActivo(true);
    }

    public Optional<Proveedor> obtenerProveedorPorId(Long id) {
        return proveedorRepository.findById(id);
    }

    public Proveedor guardarProveedor(Proveedor proveedor) {
        // Validar RUC único
        if (proveedor.getIdProveedor() == null) {
            if (proveedorRepository.existsByRuc(proveedor.getRuc())) {
                throw new RuntimeException("Ya existe un proveedor con RUC: " + proveedor.getRuc());
            }
        } else {
            Optional<Proveedor> proveedorExistente = proveedorRepository.findByRuc(proveedor.getRuc());
            if (proveedorExistente.isPresent() && !proveedorExistente.get().getIdProveedor().equals(proveedor.getIdProveedor())) {
                throw new RuntimeException("El RUC " + proveedor.getRuc() + " ya pertenece a otro proveedor");
            }
        }
        
        return proveedorRepository.save(proveedor);
    }

    public void eliminarProveedor(Long id) {
        proveedorRepository.deleteById(id);
    }

    public Optional<Proveedor> buscarPorRuc(String ruc) {
        return proveedorRepository.findByRuc(ruc);
    }

    public List<Proveedor> buscarPorRazonSocial(String razonSocial) {
        return proveedorRepository.findByRazonSocialContainingIgnoreCase(razonSocial);
    }

    public boolean existePorRuc(String ruc) {
        return proveedorRepository.existsByRuc(ruc);
    }

    public Proveedor desactivarProveedor(Long id) {
        Optional<Proveedor> proveedorOpt = proveedorRepository.findById(id);
        if (proveedorOpt.isPresent()) {
            Proveedor proveedor = proveedorOpt.get();
            proveedor.desactivar();
            return proveedorRepository.save(proveedor);
        }
        throw new RuntimeException("Proveedor no encontrado con ID: " + id);
    }

    public Proveedor activarProveedor(Long id) {
        Optional<Proveedor> proveedorOpt = proveedorRepository.findById(id);
        if (proveedorOpt.isPresent()) {
            Proveedor proveedor = proveedorOpt.get();
            proveedor.activar();
            return proveedorRepository.save(proveedor);
        }
        throw new RuntimeException("Proveedor no encontrado con ID: " + id);
    }

    public long contarProveedoresActivos() {
        return proveedorRepository.findByActivo(true).size();
    }
}