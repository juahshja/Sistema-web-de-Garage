package com.miempresa.sistema.garage.repository;

import com.miempresa.sistema.garage.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    Optional<Vehiculo> findByPlaca(String placa);
    List<Vehiculo> findByMarcaContainingIgnoreCase(String marca);
List<Vehiculo> findByCliente_IdCliente(Long idCliente);
    boolean existsByPlaca(String placa);
    long countByMarca(String marca);
    
}