package com.miempresa.sistema.garage.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "COTIZACION")
public class Cotizacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cotizacion")
    private Long idCotizacion;
    
    @Column(name = "numero_cotizacion", unique = true, nullable = false, length = 20)
    private String numeroCotizacion;
    
    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;
    
    @Column(name = "validez_dias", nullable = false)
    private Integer validezDias = 7;
    
    @Column(name = "subtotal_repuestos", precision = 10, scale = 2)
    private BigDecimal subtotalRepuestos = BigDecimal.ZERO;
    
    @Column(name = "subtotal_mano_obra", precision = 10, scale = 2)
    private BigDecimal subtotalManoObra = BigDecimal.ZERO;
    
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    
    @Column(name = "estado", nullable = false, length = 20)
    private String estado; // "PENDIENTE", "APROBADA", "RECHAZADA", "VENCIDA"
    
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
    
    @Column(name = "fecha_aprobacion")
    private LocalDateTime fechaAprobacion;
    
    // Relación: Una cotización pertenece a UNA orden de trabajo
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orden_trabajo", nullable = false, unique = true)
    private OrdenTrabajo ordenTrabajo;
    
    // Constructor vacío
    public Cotizacion() {
        this.fechaEmision = LocalDateTime.now();
        this.estado = "PENDIENTE";
    }
    
    // Constructor con orden
    public Cotizacion(OrdenTrabajo ordenTrabajo) {
        this();
        this.ordenTrabajo = ordenTrabajo;
        calcularTotal();
    }
    
    // Getters y Setters
    public Long getIdCotizacion() {
        return idCotizacion;
    }
    
    public void setIdCotizacion(Long idCotizacion) {
        this.idCotizacion = idCotizacion;
    }
    
    public String getNumeroCotizacion() {
        return numeroCotizacion;
    }
    
    public void setNumeroCotizacion(String numeroCotizacion) {
        this.numeroCotizacion = numeroCotizacion;
    }
    
    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }
    
    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
    
    public Integer getValidezDias() {
        return validezDias;
    }
    
    public void setValidezDias(Integer validezDias) {
        this.validezDias = validezDias;
    }
    
    public BigDecimal getSubtotalRepuestos() {
        return subtotalRepuestos;
    }
    
    public void setSubtotalRepuestos(BigDecimal subtotalRepuestos) {
        this.subtotalRepuestos = subtotalRepuestos;
        calcularTotal();
    }
    
    public BigDecimal getSubtotalManoObra() {
        return subtotalManoObra;
    }
    
    public void setSubtotalManoObra(BigDecimal subtotalManoObra) {
        this.subtotalManoObra = subtotalManoObra;
        calcularTotal();
    }
    
    public BigDecimal getTotal() {
        return total;
    }
    
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public LocalDateTime getFechaAprobacion() {
        return fechaAprobacion;
    }
    
    public void setFechaAprobacion(LocalDateTime fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }
    
    public OrdenTrabajo getOrdenTrabajo() {
        return ordenTrabajo;
    }
    
    public void setOrdenTrabajo(OrdenTrabajo ordenTrabajo) {
        this.ordenTrabajo = ordenTrabajo;
    }
    
    // Métodos de negocio
    private void calcularTotal() {
        this.total = subtotalRepuestos.add(subtotalManoObra);
    }
    
    public void agregarRepuesto(Repuesto repuesto, Integer cantidad) {
        BigDecimal costoRepuesto = repuesto.getPrecioVenta().multiply(new BigDecimal(cantidad));
        this.subtotalRepuestos = this.subtotalRepuestos.add(costoRepuesto);
        calcularTotal();
    }
    
    public void agregarManoObra(BigDecimal costoManoObra) {
        this.subtotalManoObra = this.subtotalManoObra.add(costoManoObra);
        calcularTotal();
    }
    
    public void aprobar() {
        this.estado = "APROBADA";
        this.fechaAprobacion = LocalDateTime.now();
    }
    
    public void rechazar() {
        this.estado = "RECHAZADA";
    }
    
    public boolean estaVencida() {
        if (fechaEmision == null || validezDias == null) return false;
        LocalDateTime fechaVencimiento = fechaEmision.plusDays(validezDias);
        return LocalDateTime.now().isAfter(fechaVencimiento);
    }
    
    public boolean esAprobada() {
        return "APROBADA".equals(estado);
    }
    
    // Método para generar número de cotización
    public static String generarNumeroCotizacion(Long consecutivo) {
        int año = LocalDateTime.now().getYear();
        String numero = String.format("%03d", consecutivo);
        return "COT-" + año + "-" + numero;
    }
    
    // Método toString
    @Override
    public String toString() {
        return "Cotizacion{" +
                "idCotizacion=" + idCotizacion +
                ", numeroCotizacion='" + numeroCotizacion + '\'' +
                ", total=" + total +
                ", estado='" + estado + '\'' +
                ", ordenTrabajoId=" + (ordenTrabajo != null ? ordenTrabajo.getIdOrdenTrabajo() : "null") +
                '}';
    }
}