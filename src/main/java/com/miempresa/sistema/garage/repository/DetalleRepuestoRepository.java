package com.miempresa.sistema.garage.repository;

import com.miempresa.sistema.garage.model.DetalleRepuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetalleRepuestoRepository extends JpaRepository<DetalleRepuesto, Long> {
    List<DetalleRepuesto> findByOrdenTrabajo_IdOrdenTrabajo(Long idOrdenTrabajo);
    List<DetalleRepuesto> findByRepuesto_IdRepuesto(Long idRepuesto);
    void deleteByOrdenTrabajo_IdOrdenTrabajo(Long idOrdenTrabajo);
}