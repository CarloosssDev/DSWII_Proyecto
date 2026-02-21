package cibertec.pe;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_pedidos")
public class Pedido {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime fechaRegistro = LocalDateTime.now();

	private Double montoTotal;

	@Enumerated(EnumType.STRING)
	private MetodoPago metodoPago = MetodoPago.EFECTIVO;

	@Enumerated(EnumType.STRING)
	private EstadoPedido estado = EstadoPedido.PENDIENTE;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cliente_id")
	@XmlTransient
	private Cliente cliente;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<DetallePedido> detalles;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "repartidor_id")
	@XmlTransient
	private Repartidor repartidor;

	public Pedido() {
	}

	public Pedido(LocalDateTime fechaRegistro, Double montoTotal, MetodoPago metodoPago, EstadoPedido estado,
			Cliente cliente, List<DetallePedido> detalles, Repartidor repartidor) {
		this.fechaRegistro = fechaRegistro;
		this.montoTotal = montoTotal;
		this.metodoPago = metodoPago;
		this.estado = estado;
		this.cliente = cliente;
		this.detalles = detalles;
		this.repartidor = repartidor;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Double getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(Double montoTotal) {
		this.montoTotal = montoTotal;
	}

	public MetodoPago getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(MetodoPago metodoPago) {
		this.metodoPago = metodoPago;
	}

	public EstadoPedido getEstado() {
		return estado;
	}

	public void setEstado(EstadoPedido estado) {
		this.estado = estado;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<DetallePedido> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetallePedido> detalles) {
		this.detalles = detalles;
	}

	public Repartidor getRepartidor() {
		return repartidor;
	}

	public void setRepartidor(Repartidor repartidor) {
		this.repartidor = repartidor;
	}

}