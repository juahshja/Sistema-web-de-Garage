package com.miempresa.sistema.garage.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDEN_TRABAJO")
public class OrdenTrabajo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden_trabajo")
    private Long idOrdenTrabajo;
    
    @Column(name = "fecha_recepcion", nullable = false)
    private LocalDateTime fechaRecepcion;
    
    @Column(name = "fecha_entrega_estimada")
    private LocalDateTime fechaEntregaEstimada;
    
    @Column(name = "fecha_entrega_real")
    private LocalDateTime fechaEntregaReal;
    
    @Column(name = "estado", nullable = false, length = 20)
    private String estado; // "PENDIENTE", "DIAGNOSTICO", "COTIZACION", "APROBADO", "EN_PROCESO", "FINALIZADO", "ENTREGADO"
    
    @Column(name = "descripcion_problema", columnDefinition = "TEXT")
    private String descripcionProblema;
    
    @Column(name = "diagnostico_previo", columnDefinition = "TEXT")
    private String diagnosticoPrevio;
    
    // Relación: Una orden pertenece a UN vehículo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private Vehiculo vehiculo;
    
    // Relación: Una orden puede tener UNA cotización
    @OneToOne(mappedBy = "ordenTrabajo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cotizacion cotizacion;
    
    // Relación: Una orden puede tener UNA factura
    @OneToOne(mappedBy = "ordenTrabajo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Factura factura;
    
    // Relación: Una orden puede usar muchos repuestos (a través de detalle)
    @OneToMany(mappedBy = "ordenTrabajo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleRepuesto> repuestosUtilizados = new ArrayList<>();
    
    // Constructor vacío (obligatorio para JPA)
    public OrdenTrabajo() {
        this.fechaRecepcion = LocalDateTime.now();
        this.estado = "PENDIENTE";
    }
    
    // Constructor con parámetros básicos
    public OrdenTrabajo(Vehiculo vehiculo, String descripcionProblema) {
        this();
        this.vehiculo = vehiculo;
        this.descripcionProblema = descripcionProblema;
    }
    
    // Getters y Setters
    public Long getIdOrdenTrabajo() {
        return idOrdenTrabajo;
    }
    
    public void setIdOrdenTrabajo(Long idOrdenTrabajo) {
        this.idOrdenTrabajo = idOrdenTrabajo;
    }
    
    public LocalDateTime getFechaRecepcion() {
        return fechaRecepcion;
    }
    
    public void setFechaRecepcion(LocalDateTime fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }
    
    public LocalDateTime getFechaEntregaEstimada() {
        return fechaEntregaEstimada;
    }
    
    public void setFechaEntregaEstimada(LocalDateTime fechaEntregaEstimada) {
        this.fechaEntregaEstimada = fechaEntregaEstimada;
    }
    
    public LocalDateTime getFechaEntregaReal() {
        return fechaEntregaReal;
    }
    
    public void setFechaEntregaReal(LocalDateTime fechaEntregaReal) {
        this.fechaEntregaReal = fechaEntregaReal;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getDescripcionProblema() {
        return descripcionProblema;
    }
    
    public void setDescripcionProblema(String descripcionProblema) {
        this.descripcionProblema = descripcionProblema;
    }
    
    public String getDiagnosticoPrevio() {
        return diagnosticoPrevio;
    }
    
    public void setDiagnosticoPrevio(String diagnosticoPrevio) {
        this.diagnosticoPrevio = diagnosticoPrevio;
    }
    
    public Vehiculo getVehiculo() {
        return vehiculo;
    }
    
    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }
    
    public Cotizacion getCotizacion() {
        return cotizacion;
    }
    
    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }
    
    public Factura getFactura() {
        return factura;
    }
    
    public void setFactura(Factura factura) {
        this.factura = factura;
    }
    
    public List<DetalleRepuesto> getRepuestosUtilizados() {
        return repuestosUtilizados;
    }
    
    public void setRepuestosUtilizados(List<DetalleRepuesto> repuestosUtilizados) {
        this.repuestosUtilizados = repuestosUtilizados;
    }
    
    // Método para agregar repuesto a la orden
    public void agregarRepuesto(Repuesto repuesto, Integer cantidad) {
        DetalleRepuesto detalle = new DetalleRepuesto(this, repuesto, cantidad);
        repuestosUtilizados.add(detalle);
    }
    
    // Método para calcular total estimado (si hay cotización)
    public Double getTotalEstimado() {
        if (cotizacion != null) {
            return cotizacion.getTotal().doubleValue();
        }
        return 0.0;
    }
    
    // Métodos para cambiar estado
    public void marcarComoDiagnosticado(String diagnostico) {
        this.diagnosticoPrevio = diagnostico;
        this.estado = "DIAGNOSTICO";
    }
    
    public void marcarComoFinalizado() {
        this.estado = "FINALIZADO";
        this.fechaEntregaReal = LocalDateTime.now();
    }
    
    // Método toString para debug
    @Override
    public String toString() {
        return "OrdenTrabajo{" +
                "idOrdenTrabajo=" + idOrdenTrabajo +
                ", fechaRecepcion=" + fechaRecepcion +
                ", estado='" + estado + '\'' +
                ", vehiculoId=" + (vehiculo != null ? vehiculo.getIdVehiculo() : "null") +
                '}';
    }
}