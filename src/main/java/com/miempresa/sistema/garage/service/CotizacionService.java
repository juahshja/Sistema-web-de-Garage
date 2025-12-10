package com.miempresa.sistema.garage.service;

import com.miempresa.sistema.garage.model.Cotizacion;
import com.miempresa.sistema.garage.repository.CotizacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CotizacionService {

    @Autowired
    private CotizacionRepository cotizacionRepository;

    public List<Cotizacion> obtenerTodasCotizaciones() {
        return cotizacionRepository.findAll();
    }

    public Optional<Cotizacion> obtenerCotizacionPorId(Long id) {
        return cotizacionRepository.findById(id);
    }

    public Cotizacion guardarCotizacion(Cotizacion cotizacion) {
        // Validar número de cotización único
        if (cotizacion.getIdCotizacion() == null && cotizacion.getNumeroCotizacion() != null) {
            if (cotizacionRepository.findByNumeroCotizacion(cotizacion.getNumeroCotizacion()).isPresent()) {
                throw new RuntimeException("Ya existe una cotización con número: " + cotizacion.getNumeroCotizacion());
            }
        }
        return cotizacionRepository.save(cotizacion);
    }

    public void eliminarCotizacion(Long id) {
        cotizacionRepository.deleteById(id);
    }

    public Optional<Cotizacion> buscarPorNumeroCotizacion(String numeroCotizacion) {
        return cotizacionRepository.findByNumeroCotizacion(numeroCotizacion);
    }

    public List<Cotizacion> buscarPorEstado(String estado) {
        return cotizacionRepository.findByEstado(estado);
    }

    public Optional<Cotizacion> obtenerCotizacionDeOrden(Long ordenTrabajoId) {
return cotizacionRepository.findByOrdenTrabajo_IdOrdenTrabajo(ordenTrabajoId);
    }

    public List<Cotizacion> buscarCotizacionesEntreFechas(LocalDateTime inicio, LocalDateTime fin) {
        return cotizacionRepository.findByFechaEmisionBetween(inicio, fin);
    }

    public Cotizacion aprobarCotizacion(Long cotizacionId) {
        Optional<Cotizacion> cotizacionOpt = cotizacionRepository.findById(cotizacionId);
        if (cotizacionOpt.isPresent()) {
            Cotizacion cotizacion = cotizacionOpt.get();
            cotizacion.aprobar();
            return cotizacionRepository.save(cotizacion);
        }
        throw new RuntimeException("Cotización no encontrada con ID: " + cotizacionId);
    }

    public Cotizacion rechazarCotizacion(Long cotizacionId) {
        Optional<Cotizacion> cotizacionOpt = cotizacionRepository.findById(cotizacionId);
        if (cotizacionOpt.isPresent()) {
            Cotizacion cotizacion = cotizacionOpt.get();
            cotizacion.rechazar();
            return cotizacionRepository.save(cotizacion);
        }
        throw new RuntimeException("Cotización no encontrada con ID: " + cotizacionId);
    }

    public List<Cotizacion> obtenerCotizacionesVencidas() {
        List<Cotizacion> todas = cotizacionRepository.findAll();
        return todas.stream()
                .filter(Cotizacion::estaVencida)
                .toList();
    }

    public Cotizacion generarCotizacionParaOrden(Long ordenTrabajoId, String numeroCotizacion) {
        // En implementación real, esto crearía una cotización con datos de la orden
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setNumeroCotizacion(numeroCotizacion);
        // cotizacion.setOrdenTrabajo(orden); // Necesitarías la orden
        return guardarCotizacion(cotizacion);
    }

    public String generarNumeroCotizacionAutomatico() {
        long consecutivo = cotizacionRepository.count() + 1;
        return Cotizacion.generarNumeroCotizacion(consecutivo);
    }
}