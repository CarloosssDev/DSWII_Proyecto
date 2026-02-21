package cibertec.pe;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_repartidores")
public class Repartidor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombre;

	private String apellido;

	@OneToMany(mappedBy = "repartidor")
	private List<Pedido> pedidos;

	public Repartidor() {
	}

	public Repartidor(String nombre, String apellido, List<Pedido> pedidos) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.pedidos = pedidos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}
}