package com.miempresa.sistema.garage.service;

import com.miempresa.sistema.garage.model.Factura;
import com.miempresa.sistema.garage.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    public List<Factura> obtenerTodasFacturas() {
        return facturaRepository.findAll();
    }

    public Optional<Factura> obtenerFacturaPorId(Long id) {
        return facturaRepository.findById(id);
    }

    public Factura guardarFactura(Factura factura) {
        // Validar número de factura único
        if (factura.getIdFactura() == null) {
            if (facturaRepository.findByNumeroFactura(factura.getNumeroFactura()).isPresent()) {
                throw new RuntimeException("Ya existe una factura con número: " + factura.getNumeroFactura());
            }
        }
        return facturaRepository.save(factura);
    }

    public void eliminarFactura(Long id) {
        facturaRepository.deleteById(id);
    }

    public Optional<Factura> buscarPorNumeroFactura(String numeroFactura) {
        return facturaRepository.findByNumeroFactura(numeroFactura);
    }

    public List<Factura> buscarPorEstado(String estado) {
        return facturaRepository.findByEstado(estado);
    }
public List<Factura> obtenerFacturasDeOrden(Long ordenTrabajoId) {
    return facturaRepository.findByOrdenTrabajo_IdOrdenTrabajo(ordenTrabajoId);
}
    public List<Factura> buscarFacturasEntreFechas(LocalDateTime inicio, LocalDateTime fin) {
        return facturaRepository.findByFechaEmisionBetween(inicio, fin);
    }

    public Factura generarFacturaParaOrden(Long ordenTrabajoId, String numeroFactura, Double subtotal) {
        // En una implementación real, esto usaría OrdenTrabajoService
        // Por ahora creamos una factura básica
        Factura factura = new Factura();
        factura.setNumeroFactura(numeroFactura);
        // factura.setOrdenTrabajo(ordenTrabajo); // Se necesita la orden
        factura.setSubtotal(java.math.BigDecimal.valueOf(subtotal));
        return guardarFactura(factura);
    }

    public Factura marcarFacturaComoPagada(Long facturaId, String metodoPago) {
        Optional<Factura> facturaOpt = facturaRepository.findById(facturaId);
        if (facturaOpt.isPresent()) {
            Factura factura = facturaOpt.get();
            factura.marcarComoPagada(metodoPago);
            return facturaRepository.save(factura);
        }
        throw new RuntimeException("Factura no encontrada con ID: " + facturaId);
    }

    public Factura anularFactura(Long facturaId) {
        Optional<Factura> facturaOpt = facturaRepository.findById(facturaId);
        if (facturaOpt.isPresent()) {
            Factura factura = facturaOpt.get();
            factura.anularFactura();
            return facturaRepository.save(factura);
        }
        throw new RuntimeException("Factura no encontrada con ID: " + facturaId);
    }
}