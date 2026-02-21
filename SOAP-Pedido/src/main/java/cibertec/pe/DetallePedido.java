package cibertec.pe;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "tbl_detalle_pedidos")
public class DetallePedido {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int cantidad;

	private Double precioUnitario;

	private Double subTotal;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pedido_id")
	@XmlTransient
	private Pedido pedido;

	@ManyToOne
	@JoinColumn(name = "producto_id")
	private Producto producto;

	public DetallePedido() {
	}

	public DetallePedido(int cantidad, Double precioUnitario, Double subTotal, Pedido pedido, Producto producto) {
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
		this.subTotal = subTotal;
		this.pedido = pedido;
		this.producto = producto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public Double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(Double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}
}