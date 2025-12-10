package com.miempresa.sistema.garage.service;

import com.miempresa.sistema.garage.model.DetalleRepuesto;
import com.miempresa.sistema.garage.repository.DetalleRepuestoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class DetalleRepuestoService {

    @Autowired
    private DetalleRepuestoRepository detalleRepuestoRepository;

    public List<DetalleRepuesto> obtenerTodosDetalles() {
        return detalleRepuestoRepository.findAll();
    }

    public DetalleRepuesto guardarDetalle(DetalleRepuesto detalle) {
        return detalleRepuestoRepository.save(detalle);
    }

    public void eliminarDetalle(Long id) {
        detalleRepuestoRepository.deleteById(id);
    }

    public List<DetalleRepuesto> obtenerDetallesDeOrden(Long ordenTrabajoId) {
    return detalleRepuestoRepository.findByOrdenTrabajo_IdOrdenTrabajo(ordenTrabajoId);
}

    public List<DetalleRepuesto> obtenerDetallesDeRepuesto(Long repuestoId) {
    return detalleRepuestoRepository.findByRepuesto_IdRepuesto(repuestoId);
}

public void eliminarDetallesDeOrden(Long ordenTrabajoId) {
    detalleRepuestoRepository.deleteByOrdenTrabajo_IdOrdenTrabajo(ordenTrabajoId);
}

    public Double calcularTotalRepuestosDeOrden(Long ordenTrabajoId) {
        List<DetalleRepuesto> detalles = obtenerDetallesDeOrden(ordenTrabajoId);
        return detalles.stream()
                .mapToDouble(d -> d.getSubtotal().doubleValue())
                .sum();
    }

    public Integer contarRepuestosUtilizadosEnOrden(Long ordenTrabajoId) {
        List<DetalleRepuesto> detalles = obtenerDetallesDeOrden(ordenTrabajoId);
        return detalles.stream()
                .mapToInt(DetalleRepuesto::getCantidad)
                .sum();
    }
}