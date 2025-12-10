package com.miempresa.sistema.garage.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PROVEEDOR")
public class Proveedor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Long idProveedor;
    
    @Column(name = "razon_social", nullable = false, length = 200)
    private String razonSocial;
    
    @Column(name = "nombre_comercial", length = 200)
    private String nombreComercial;
    
    @Column(name = "ruc", nullable = false, unique = true, length = 11)
    private String ruc;
    
    @Column(name = "direccion", length = 300)
    private String direccion;
    
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "contacto_nombre", length = 100)
    private String contactoNombre;
    
    @Column(name = "contacto_telefono", length = 20)
    private String contactoTelefono;
    
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
    
    // Relación: Un proveedor puede suministrar muchos repuestos
    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Repuesto> repuestos = new ArrayList<>();
    
    // Constructor vacío (obligatorio para JPA)
    public Proveedor() {
    }
    
    // Constructor con parámetros básicos
    public Proveedor(String razonSocial, String ruc, String telefono) {
        this.razonSocial = razonSocial;
        this.ruc = ruc;
        this.telefono = telefono;
        this.activo = true;
    }
    
    // Getters y Setters
    public Long getIdProveedor() {
        return idProveedor;
    }
    
    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }
    
    public String getRazonSocial() {
        return razonSocial;
    }
    
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }
    
    public String getNombreComercial() {
        return nombreComercial;
    }
    
    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }
    
    public String getRuc() {
        return ruc;
    }
    
    public void setRuc(String ruc) {
        this.ruc = ruc;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getContactoNombre() {
        return contactoNombre;
    }
    
    public void setContactoNombre(String contactoNombre) {
        this.contactoNombre = contactoNombre;
    }
    
    public String getContactoTelefono() {
        return contactoTelefono;
    }
    
    public void setContactoTelefono(String contactoTelefono) {
        this.contactoTelefono = contactoTelefono;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    public List<Repuesto> getRepuestos() {
        return repuestos;
    }
    
    public void setRepuestos(List<Repuesto> repuestos) {
        this.repuestos = repuestos;
    }
    
    // Método para agregar repuesto
    public void agregarRepuesto(Repuesto repuesto) {
        repuestos.add(repuesto);
        repuesto.setProveedor(this);
    }
    
    // Método para desactivar proveedor
    public void desactivar() {
        this.activo = false;
    }
    
    // Método para activar proveedor
    public void activar() {
        this.activo = true;
    }
    
    // Método toString para debug
    @Override
    public String toString() {
        return "Proveedor{" +
                "idProveedor=" + idProveedor +
                ", razonSocial='" + razonSocial + '\'' +
                ", ruc='" + ruc + '\'' +
                ", activo=" + activo +
                '}';
    }
}