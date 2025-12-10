package com.miempresa.sistema.garage.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "DETALLE_REPUESTO")
public class DetalleRepuesto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long idDetalle;
    
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
    
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;
    
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    // Relación: Un detalle pertenece a UNA orden de trabajo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orden_trabajo", nullable = false)
    private OrdenTrabajo ordenTrabajo;
    
    // Relación: Un detalle usa UN repuesto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_repuesto", nullable = false)
    private Repuesto repuesto;
    
    // Constructor vacío
    public DetalleRepuesto() {
    }
    
    // Constructor completo
    public DetalleRepuesto(OrdenTrabajo ordenTrabajo, Repuesto repuesto, Integer cantidad) {
        this.ordenTrabajo = ordenTrabajo;
        this.repuesto = repuesto;
        this.cantidad = cantidad;
        this.precioUnitario = repuesto.getPrecioVenta();
        this.subtotal = precioUnitario.multiply(new BigDecimal(cantidad));
    }
    
    // Getters y Setters
    public Long getIdDetalle() {
        return idDetalle;
    }
    
    public void setIdDetalle(Long idDetalle) {
        this.idDetalle = idDetalle;
    }
    
    public Integer getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
        // Recalcular subtotal si cambia la cantidad
        if (precioUnitario != null && cantidad != null) {
            this.subtotal = precioUnitario.multiply(new BigDecimal(cantidad));
        }
    }
    
    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }
    
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
        // Recalcular subtotal si cambia el precio
        if (cantidad != null && precioUnitario != null) {
            this.subtotal = precioUnitario.multiply(new BigDecimal(cantidad));
        }
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    public OrdenTrabajo getOrdenTrabajo() {
        return ordenTrabajo;
    }
    
    public void setOrdenTrabajo(OrdenTrabajo ordenTrabajo) {
        this.ordenTrabajo = ordenTrabajo;
    }
    
    public Repuesto getRepuesto() {
        return repuesto;
    }
    
    public void setRepuesto(Repuesto repuesto) {
        this.repuesto = repuesto;
        // Actualizar precio si cambia el repuesto
        if (repuesto != null) {
            this.precioUnitario = repuesto.getPrecioVenta();
        }
    }
    
    // Método toString
    @Override
    public String toString() {
        return "DetalleRepuesto{" +
                "idDetalle=" + idDetalle +
                ", cantidad=" + cantidad +
                ", subtotal=" + subtotal +
                ", repuestoId=" + (repuesto != null ? repuesto.getIdRepuesto() : "null") +
                '}';
    }
}