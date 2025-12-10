package com.miempresa.sistema.garage.repository;

import com.miempresa.sistema.garage.model.Cotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {
    Optional<Cotizacion> findByNumeroCotizacion(String numeroCotizacion);
    List<Cotizacion> findByEstado(String estado);
Optional<Cotizacion> findByOrdenTrabajo_IdOrdenTrabajo(Long idOrdenTrabajo);
    List<Cotizacion> findByFechaEmisionBetween(java.time.LocalDateTime inicio, java.time.LocalDateTime fin);
}