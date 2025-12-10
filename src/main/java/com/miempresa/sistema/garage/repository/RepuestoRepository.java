package com.miempresa.sistema.garage.repository;

import com.miempresa.sistema.garage.model.Repuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepuestoRepository extends JpaRepository<Repuesto, Long> {
    List<Repuesto> findByNombreContainingIgnoreCase(String nombre);
    Optional<Repuesto> findByCodigoBarras(String codigoBarras);
    List<Repuesto> findByStockActualLessThanEqual(Integer stockMinimo);
List<Repuesto> findByProveedor_IdProveedor(Long idProveedor);
    boolean existsByCodigoBarras(String codigoBarras);
}