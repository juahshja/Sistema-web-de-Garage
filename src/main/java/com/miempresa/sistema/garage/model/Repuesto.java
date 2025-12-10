package com.miempresa.sistema.garage.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "REPUESTO")
public class Repuesto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_repuesto")
    private Long idRepuesto;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "codigo_barras", unique = true, length = 50)
    private String codigoBarras;
    
    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual = 0;
    
    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo = 5;
    
    @Column(name = "precio_costo", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioCosto;
    
    @Column(name = "precio_venta", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioVenta;
    
    @Column(name = "ubicacion_almacen", length = 50)
    private String ubicacionAlmacen;
    
    // Relación: Muchos repuestos provienen de UN proveedor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;
    
    // Constructor vacío (obligatorio para JPA)
    public Repuesto() {
    }
    
    // Constructor con parámetros básicos
    public Repuesto(String nombre, BigDecimal precioCosto, BigDecimal precioVenta, Integer stockActual) {
        this.nombre = nombre;
        this.precioCosto = precioCosto;
        this.precioVenta = precioVenta;
        this.stockActual = stockActual;
    }
    
    // Getters y Setters
    public Long getIdRepuesto() {
        return idRepuesto;
    }
    
    public void setIdRepuesto(Long idRepuesto) {
        this.idRepuesto = idRepuesto;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getCodigoBarras() {
        return codigoBarras;
    }
    
    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }
    
    public Integer getStockActual() {
        return stockActual;
    }
    
    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }
    
    public Integer getStockMinimo() {
        return stockMinimo;
    }
    
    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }
    
    public BigDecimal getPrecioCosto() {
        return precioCosto;
    }
    
    public void setPrecioCosto(BigDecimal precioCosto) {
        this.precioCosto = precioCosto;
    }
    
    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }
    
    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }
    
    public String getUbicacionAlmacen() {
        return ubicacionAlmacen;
    }
    
    public void setUbicacionAlmacen(String ubicacionAlmacen) {
        this.ubicacionAlmacen = ubicacionAlmacen;
    }
    
    public Proveedor getProveedor() {
        return proveedor;
    }
    
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
    
    // Métodos de negocio para inventario
    public void aumentarStock(Integer cantidad) {
        this.stockActual += cantidad;
    }
    
    public void disminuirStock(Integer cantidad) {
        if (this.stockActual >= cantidad) {
            this.stockActual -= cantidad;
        } else {
            throw new IllegalStateException("Stock insuficiente. Disponible: " + this.stockActual);
        }
    }
    
    public boolean necesitaReposicion() {
        return stockActual <= stockMinimo;
    }
    
    public BigDecimal getMargenGanancia() {
        if (precioCosto != null && precioVenta != null && precioCosto.compareTo(BigDecimal.ZERO) > 0) {
            return precioVenta.subtract(precioCosto);
        }
        return BigDecimal.ZERO;
    }
    
    // Método toString para debug
    @Override
    public String toString() {
        return "Repuesto{" +
                "idRepuesto=" + idRepuesto +
                ", nombre='" + nombre + '\'' +
                ", stockActual=" + stockActual +
                ", precioVenta=" + precioVenta +
                '}';
    }
}