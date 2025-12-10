package com.miempresa.sistema.garage.repository;

import com.miempresa.sistema.garage.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByDniRuc(String dniRuc);
    List<Cliente> findByApellidosContainingIgnoreCase(String apellidos);
    Optional<Cliente> findByCorreo(String correo);
    boolean existsByDniRuc(String dniRuc);
}