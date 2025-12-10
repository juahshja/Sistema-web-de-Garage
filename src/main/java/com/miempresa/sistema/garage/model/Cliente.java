package com.miempresa.sistema.garage.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CLIENTE")
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;
    
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
    
    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;
    
    @Column(name = "dni_ruc", nullable = false, unique = true, length = 20)
    private String dniRuc;
    
    @Column(name = "correo", length = 100)
    private String correo;
    
    @Column(name = "telefono", length = 15)
    private String telefono;
    
    // Relación: Un cliente puede tener muchos vehículos
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vehiculo> vehiculos = new ArrayList<>();
    
    // Constructor vacío (obligatorio para JPA)
    public Cliente() {
    }
    
    // Constructor con parámetros
    public Cliente(String nombre, String apellidos, String dniRuc, String correo, String telefono) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dniRuc = dniRuc;
        this.correo = correo;
        this.telefono = telefono;
    }
    
    // Getters y Setters
    public Long getIdCliente() {
        return idCliente;
    }
    
    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getApellidos() {
        return apellidos;
    }
    
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    
    public String getDniRuc() {
        return dniRuc;
    }
    
    public void setDniRuc(String dniRuc) {
        this.dniRuc = dniRuc;
    }
    
    public String getCorreo() {
        return correo;
    }
    
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }
    
    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }
    
    // Método para agregar vehículo
    public void agregarVehiculo(Vehiculo vehiculo) {
        vehiculos.add(vehiculo);
        vehiculo.setCliente(this);
    }
    
    // Método para eliminar vehículo
    public void removerVehiculo(Vehiculo vehiculo) {
        vehiculos.remove(vehiculo);
        vehiculo.setCliente(null);
    }
    
    // Método toString para debug
    @Override
    public String toString() {
        return "Cliente{" +
                "idCliente=" + idCliente +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", dniRuc='" + dniRuc + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}