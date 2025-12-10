package com.miempresa.sistema.garage.service;

import com.miempresa.sistema.garage.model.Repuesto;
import com.miempresa.sistema.garage.repository.RepuestoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RepuestoService {

    @Autowired
    private RepuestoRepository repuestoRepository;

    public List<Repuesto> obtenerTodosRepuestos() {
        return repuestoRepository.findAll();
    }

    public Optional<Repuesto> obtenerRepuestoPorId(Long id) {
        return repuestoRepository.findById(id);
    }

    public Repuesto guardarRepuesto(Repuesto repuesto) {
        // Validar código de barras único
        if (repuesto.getIdRepuesto() == null && repuesto.getCodigoBarras() != null) {
            if (repuestoRepository.existsByCodigoBarras(repuesto.getCodigoBarras())) {
                throw new RuntimeException("Ya existe un repuesto con código de barras: " + repuesto.getCodigoBarras());
            }
        }
        return repuestoRepository.save(repuesto);
    }

    public void eliminarRepuesto(Long id) {
        repuestoRepository.deleteById(id);
    }

    public List<Repuesto> buscarPorNombre(String nombre) {
        return repuestoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Optional<Repuesto> buscarPorCodigoBarras(String codigoBarras) {
        return repuestoRepository.findByCodigoBarras(codigoBarras);
    }

    public List<Repuesto> obtenerRepuestosConStockBajo() {
        return repuestoRepository.findByStockActualLessThanEqual(10); // Stock mínimo 10
    }

    public List<Repuesto> obtenerRepuestosPorProveedor(Long proveedorId) {
    return repuestoRepository.findByProveedor_IdProveedor(proveedorId);
}

    public void aumentarStock(Long repuestoId, Integer cantidad) {
        Optional<Repuesto> repuestoOpt = repuestoRepository.findById(repuestoId);
        if (repuestoOpt.isPresent()) {
            Repuesto repuesto = repuestoOpt.get();
            repuesto.aumentarStock(cantidad);
            repuestoRepository.save(repuesto);
        } else {
            throw new RuntimeException("Repuesto no encontrado con ID: " + repuestoId);
        }
    }

    public void disminuirStock(Long repuestoId, Integer cantidad) {
        Optional<Repuesto> repuestoOpt = repuestoRepository.findById(repuestoId);
        if (repuestoOpt.isPresent()) {
            Repuesto repuesto = repuestoOpt.get();
            repuesto.disminuirStock(cantidad);
            repuestoRepository.save(repuesto);
        } else {
            throw new RuntimeException("Repuesto no encontrado con ID: " + repuestoId);
        }
    }

    public boolean verificarDisponibilidad(Long repuestoId, Integer cantidad) {
        Optional<Repuesto> repuestoOpt = repuestoRepository.findById(repuestoId);
        return repuestoOpt.isPresent() && repuestoOpt.get().getStockActual() >= cantidad;
    }

    public BigDecimal calcularValorTotalInventario() {
        return obtenerTodosRepuestos().stream()
                .map(r -> r.getPrecioCosto().multiply(BigDecimal.valueOf(r.getStockActual())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}