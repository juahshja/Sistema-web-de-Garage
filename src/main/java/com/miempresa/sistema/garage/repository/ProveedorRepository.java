package com.miempresa.sistema.garage.repository;

import com.miempresa.sistema.garage.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    Optional<Proveedor> findByRuc(String ruc);
    List<Proveedor> findByRazonSocialContainingIgnoreCase(String razonSocial);
    List<Proveedor> findByActivo(Boolean activo);
    boolean existsByRuc(String ruc);
}