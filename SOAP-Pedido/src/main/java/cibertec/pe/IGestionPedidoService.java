package cibertec.pe;

import java.util.List;
import java.util.Optional;

import jakarta.jws.WebService;

@WebService
public interface IGestionPedidoService {
	// Cliente
	List<Cliente>		listarClientes();
	Cliente				crearCliente(Cliente cliente);
	Optional<Cliente>	buscarClientePorTelefono(String telefono);
	Cliente				actualizarCliente(Long id, Cliente cliente);
	String 				eliminarCliente(Long id);
	
	//Producto
	List<Producto> listarProductos();
	Producto crearProducto(Producto producto);
	String actualizarProducto(Long id, Producto producto);
	String eliminarProducto(Long id);
	Optional<Producto> buscarProducto(Long id);
	
	//Repartidor 
	List<Repartidor>		listarRepartidores();
	Repartidor				crearRepartidor(Repartidor repartidor);
	Optional<Repartidor>	buscarRepartidor(Long id);
	
	//Pedido
	
    PedidoResponse registrarPedido(PedidoRequest pedido);
    List<PedidoResponse> listarPedidos();
    PedidoResponse buscarPedido(Long id);
    PedidoResponse cambiarEstado(Long id, EstadoPedido nuevoEstado);
    PedidoResponse registrarPagoFinal(Long id, MetodoPago pago);
    List<PedidoResponse> listarPorEstado(EstadoPedido estado);
    List<PedidoResponse> listarPorRepartidor(Long idRepartidor);
    List<PedidoResponse> listarPorCliente(String telefono);
	
}
