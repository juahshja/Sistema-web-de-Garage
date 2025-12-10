package com.miempresa.sistema.garage.service;

import com.miempresa.sistema.garage.model.*;
import com.miempresa.sistema.garage.repository.OrdenTrabajoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrdenTrabajoService {

    @Autowired
    private OrdenTrabajoRepository ordenTrabajoRepository;

    public List<OrdenTrabajo> obtenerTodasOrdenes() {
        return ordenTrabajoRepository.findAll();
    }

    public Optional<OrdenTrabajo> obtenerOrdenPorId(Long id) {
        return ordenTrabajoRepository.findById(id);
    }

    public OrdenTrabajo guardarOrden(OrdenTrabajo orden) {
        return ordenTrabajoRepository.save(orden);
    }

    public void eliminarOrden(Long id) {
        ordenTrabajoRepository.deleteById(id);
    }

    public List<OrdenTrabajo> buscarPorEstado(String estado) {
        return ordenTrabajoRepository.findByEstado(estado);
    }

   public List<OrdenTrabajo> obtenerOrdenesDeVehiculo(Long vehiculoId) {
    return ordenTrabajoRepository.findByVehiculo_IdVehiculo(vehiculoId);
}

public List<OrdenTrabajo> obtenerOrdenesDeCliente(Long clienteId) {
    return ordenTrabajoRepository.findByVehiculo_Cliente_IdCliente(clienteId);
}

    public List<OrdenTrabajo> obtenerUltimas10Ordenes() {
        return ordenTrabajoRepository.findTop10ByOrderByFechaRecepcionDesc();
    }

    public long contarOrdenesPorEstado(String estado) {
        return ordenTrabajoRepository.countByEstado(estado);
    }

    public OrdenTrabajo crearOrdenParaVehiculo(Vehiculo vehiculo, String descripcionProblema) {
        OrdenTrabajo orden = new OrdenTrabajo(vehiculo, descripcionProblema);
        return ordenTrabajoRepository.save(orden);
    }

    public OrdenTrabajo actualizarDiagnostico(Long ordenId, String diagnostico) {
        Optional<OrdenTrabajo> ordenOpt = ordenTrabajoRepository.findById(ordenId);
        if (ordenOpt.isPresent()) {
            OrdenTrabajo orden = ordenOpt.get();
            orden.marcarComoDiagnosticado(diagnostico);
            return ordenTrabajoRepository.save(orden);
        }
        throw new RuntimeException("Orden no encontrada con ID: " + ordenId);
    }

    public OrdenTrabajo finalizarOrden(Long ordenId) {
        Optional<OrdenTrabajo> ordenOpt = ordenTrabajoRepository.findById(ordenId);
        if (ordenOpt.isPresent()) {
            OrdenTrabajo orden = ordenOpt.get();
            orden.marcarComoFinalizado();
            return ordenTrabajoRepository.save(orden);
        }
        throw new RuntimeException("Orden no encontrada con ID: " + ordenId);
    }
}