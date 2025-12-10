package com.miempresa.sistema.garage.repository;

import com.miempresa.sistema.garage.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    Optional<Factura> findByNumeroFactura(String numeroFactura);
    List<Factura> findByEstado(String estado);
    List<Factura> findByOrdenTrabajo_IdOrdenTrabajo(Long idOrdenTrabajo); // ← CAMBIADO
    List<Factura> findByFechaEmisionBetween(java.time.LocalDateTime inicio, java.time.LocalDateTime fin);
    Optional<Factura> findByOrdenTrabajo_IdOrdenTrabajoAndEstado(Long idOrdenTrabajo, String estado); // ← CAMBIADO
}