package com.miempresa.sistema.garage.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "FACTURA")
public class Factura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_factura")
    private Long idFactura;
    
    @Column(name = "numero_factura", unique = true, nullable = false, length = 20)
    private String numeroFactura;
    
    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;
    
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    @Column(name = "igv", nullable = false, precision = 10, scale = 2)
    private BigDecimal igv;
    
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    
    @Column(name = "estado", nullable = false, length = 20)
    private String estado; // "PENDIENTE", "PAGADA", "ANULADA"
    
    @Column(name = "metodo_pago", length = 30)
    private String metodoPago; // "EFECTIVO", "TARJETA", "TRANSFERENCIA"
    
    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;
    
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
    
    // Relación: Una factura pertenece a UNA orden de trabajo
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orden_trabajo", nullable = false, unique = true)
    private OrdenTrabajo ordenTrabajo;
    
    // Constructor vacío (obligatorio para JPA)
    public Factura() {
        this.fechaEmision = LocalDateTime.now();
        this.estado = "PENDIENTE";
    }
    
    // Constructor con parámetros básicos
    public Factura(String numeroFactura, OrdenTrabajo ordenTrabajo, BigDecimal subtotal) {
        this();
        this.numeroFactura = numeroFactura;
        this.ordenTrabajo = ordenTrabajo;
        this.subtotal = subtotal;
        this.igv = subtotal.multiply(new BigDecimal("0.18")); // 18% IGV Perú
        this.total = subtotal.add(igv);
    }
    
    // Getters y Setters
    public Long getIdFactura() {
        return idFactura;
    }
    
    public void setIdFactura(Long idFactura) {
        this.idFactura = idFactura;
    }
    
    public String getNumeroFactura() {
        return numeroFactura;
    }
    
    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }
    
    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }
    
    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
        // Recalcular IGV y total si se cambia el subtotal
        if (subtotal != null) {
            this.igv = subtotal.multiply(new BigDecimal("0.18"));
            this.total = subtotal.add(igv);
        }
    }
    
    public BigDecimal getIgv() {
        return igv;
    }
    
    public void setIgv(BigDecimal igv) {
        this.igv = igv;
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
    
    public String getMetodoPago() {
        return metodoPago;
    }
    
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
    
    public LocalDateTime getFechaPago() {
        return fechaPago;
    }
    
    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public OrdenTrabajo getOrdenTrabajo() {
        return ordenTrabajo;
    }
    
    public void setOrdenTrabajo(OrdenTrabajo ordenTrabajo) {
        this.ordenTrabajo = ordenTrabajo;
    }
    
    // Métodos de negocio
    public void marcarComoPagada(String metodoPago) {
        this.estado = "PAGADA";
        this.metodoPago = metodoPago;
        this.fechaPago = LocalDateTime.now();
    }
    
    public void anularFactura() {
        this.estado = "ANULADA";
    }
    
    public boolean esPagada() {
        return "PAGADA".equals(estado);
    }
    
    public boolean esAnulada() {
        return "ANULADA".equals(estado);
    }
    
    // Método para generar número de factura automático (ejemplo: FAC-2024-001)
    public static String generarNumeroFactura(Long consecutivo) {
        int año = LocalDateTime.now().getYear();
        String numero = String.format("%03d", consecutivo);
        return "FAC-" + año + "-" + numero;
    }
    
    // Método toString para debug
    @Override
    public String toString() {
        return "Factura{" +
                "idFactura=" + idFactura +
                ", numeroFactura='" + numeroFactura + '\'' +
                ", total=" + total +
                ", estado='" + estado + '\'' +
                ", ordenTrabajoId=" + (ordenTrabajo != null ? ordenTrabajo.getIdOrdenTrabajo() : "null") +
                '}';
    }
}