package com.miempresa.sistema.garage.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VEHICULO")
public class Vehiculo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo")
    private Long idVehiculo;
    
    @Column(name = "nro_placa", nullable = false, unique = true, length = 10)
    private String placa;
    
    @Column(name = "marca", nullable = false, length = 50)
    private String marca;
    
    @Column(name = "modelo", nullable = false, length = 50)
    private String modelo;
    
    @Column(name = "ano_fabricacion", nullable = false)
    private Integer anoFabricacion;
    
    // Relación MUCHOS vehículos pertenecen a UN cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;
    
    // Relación: Un vehículo puede tener muchas órdenes de trabajo
    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrdenTrabajo> ordenesTrabajo = new ArrayList<>();
    
    // Constructor vacío (obligatorio para JPA)
    public Vehiculo() {
    }
    
    // Constructor con parámetros
    public Vehiculo(String placa, String marca, String modelo, Integer anoFabricacion, Cliente cliente) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anoFabricacion = anoFabricacion;
        this.cliente = cliente;
    }
    
    // Getters y Setters
    public Long getIdVehiculo() {
        return idVehiculo;
    }
    
    public void setIdVehiculo(Long idVehiculo) {
        this.idVehiculo = idVehiculo;
    }
    
    public String getPlaca() {
        return placa;
    }
    
    public void setPlaca(String placa) {
        this.placa = placa;
    }
    
    public String getMarca() {
        return marca;
    }
    
    public void setMarca(String marca) {
        this.marca = marca;
    }
    
    public String getModelo() {
        return modelo;
    }
    
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    
    public Integer getAnoFabricacion() {
        return anoFabricacion;
    }
    
    public void setAnoFabricacion(Integer anoFabricacion) {
        this.anoFabricacion = anoFabricacion;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public List<OrdenTrabajo> getOrdenesTrabajo() {
        return ordenesTrabajo;
    }
    
    public void setOrdenesTrabajo(List<OrdenTrabajo> ordenesTrabajo) {
        this.ordenesTrabajo = ordenesTrabajo;
    }
    
    // Método toString para debug
    @Override
    public String toString() {
        return "Vehiculo{" +
                "idVehiculo=" + idVehiculo +
                ", placa='" + placa + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", anoFabricacion=" + anoFabricacion +
                ", clienteId=" + (cliente != null ? cliente.getIdCliente() : "null") +
                '}';
    }
}