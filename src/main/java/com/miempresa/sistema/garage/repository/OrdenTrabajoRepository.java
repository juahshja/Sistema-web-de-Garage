package com.miempresa.sistema.garage.repository;

import com.miempresa.sistema.garage.model.OrdenTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdenTrabajoRepository extends JpaRepository<OrdenTrabajo, Long> {
    
    // Buscar órdenes por estado
    List<OrdenTrabajo> findByEstado(String estado);
    
    // Buscar órdenes por vehículo (CORREGIDO)
    List<OrdenTrabajo> findByVehiculo_IdVehiculo(Long idVehiculo);
    
    // Buscar órdenes por cliente (a través del vehículo) (CORREGIDO)
    List<OrdenTrabajo> findByVehiculo_Cliente_IdCliente(Long idCliente);
    
    // Buscar órdenes en un rango de fechas
    List<OrdenTrabajo> findByFechaRecepcionBetween(LocalDateTime inicio, LocalDateTime fin);
    
    // Contar órdenes por estado
    long countByEstado(String estado);
    
    // Buscar órdenes pendientes
    List<OrdenTrabajo> findByEstadoOrderByFechaRecepcionAsc(String estado);
    
    // Últimas 10 órdenes
    List<OrdenTrabajo> findTop10ByOrderByFechaRecepcionDesc();
}