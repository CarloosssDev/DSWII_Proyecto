package cibertec.pe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.jws.WebService;

@WebService
@Component
public class GestionPedidoImplement implements IGestionPedidoService {

	@Autowired
	private IClienteRepository clienteRepo;

	@Autowired
	private IProductoRepository productoRepo;

	@Autowired
	private IRepartidorRepository repartidorRepo;

	@Autowired
	private IPedidoRepository pedidoRepo;

	@Autowired
	private PedidoMapper pedidoMapper;

	@Autowired
	private DetallePedidoMapper detalleMapper;

	@Override
	public List<Cliente> listarClientes() {
		return clienteRepo.findAll();
	}

	@Override
	public Cliente crearCliente(Cliente cliente) {
		if (clienteRepo.findByTelefono(cliente.getTelefono()).isPresent()) {
			throw new RuntimeException("El teléfono " + cliente.getTelefono() + " ya está registrado.");
		}
		return clienteRepo.save(cliente);
	}

	@Override
	public Optional<Cliente> buscarClientePorTelefono(String telefono) {
		return clienteRepo.findByTelefono(telefono);
	}

	@Override
	public Cliente actualizarCliente(Long id, Cliente cliente) {
		return clienteRepo.findById(id).map(existingClient -> {
			Optional<Cliente> clienteConMismoTelef = clienteRepo.findByTelefono(cliente.getTelefono());

			if (clienteConMismoTelef.isPresent() && !clienteConMismoTelef.get().getId().equals(id)) {
				throw new RuntimeException("El teléfono ya pertenece a otro cliente.");
			}

			existingClient.setDireccion(cliente.getDireccion());
			existingClient.setNombre(cliente.getNombre());
			existingClient.setTelefono(cliente.getTelefono());

			return clienteRepo.save(existingClient);
		}).orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
	}

	@Override
	public String eliminarCliente(Long id) {
		Cliente c = clienteRepo.findById(id).orElseThrow(
				() -> new RuntimeException("No se pudo eliminar: El cliente con ID " + id + " no existe."));

		clienteRepo.delete(c);

		return "Cliente con ID " + id + " eliminado correctamente";
	}

	@Override
	public List<Producto> listarProductos() {
		return productoRepo.findAll();
	}

	@Override
	public Producto crearProducto(Producto producto) {
		return productoRepo.save(producto);
	}

	@Override
	public String actualizarProducto(Long id, Producto producto) {
		Optional<Producto> productoOptional = productoRepo.findById(id);
		if (productoOptional.isPresent()) {
			Producto productoExistente = productoOptional.get();
			productoExistente.setNombre(producto.getNombre());
			productoExistente.setPrecio(producto.getPrecio());
			productoRepo.save(productoExistente);
			return "Producto actualizado correctamente";
		} else {
			return "Producto no encontrado";
		}
	}

	@Override
	public String eliminarProducto(Long id) {
		Optional<Producto> productoOptional = productoRepo.findById(id);
		if (productoOptional.isPresent()) {
			productoRepo.deleteById(id);
			return "Producto eliminado correctamente";
		} else {
			return "Producto no encontrado";
		}
	}

	@Override
	public Optional<Producto> buscarProducto(Long id) {
		return productoRepo.findById(id);
	}

	@Override
	public List<Repartidor> listarRepartidores() {
		return repartidorRepo.findAll();
	}

	@Override
	public Repartidor crearRepartidor(Repartidor repartidor) {
		return repartidorRepo.save(repartidor);
	}

	@Override
	public Optional<Repartidor> buscarRepartidor(Long id) {
		return repartidorRepo.findById(id);
	}

	@Override
	public PedidoResponse registrarPedido(PedidoRequest pedido) {
		var datosCliente = buscarClientePorTelefono(pedido.getTelefonoCliente()).get();
		var datosRepartidor = buscarRepartidor(pedido.getIdRepartidor()).get();

		if (datosCliente == null || datosRepartidor == null) {
			new RuntimeException("El cliente o repartidor no existe");
		}

		Pedido nuevoPedido = pedidoMapper.toEntity(pedido);
		nuevoPedido.setCliente(datosCliente);
		nuevoPedido.setRepartidor(datosRepartidor);

		List<DetallePedido> detallesEntity = new ArrayList<>();
		List<DetallePedidoResponse> detallesResponse = new ArrayList<>();
		double montoTotal = 0;

		for (DetallePedidoRequest item : pedido.getDetalles()) {
			var infoProducto = buscarProducto(item.getIdProducto()).get();

			if (infoProducto == null) {
				new RuntimeException("El producto no existe");
			}

			DetallePedido detEntity = detalleMapper.toEntity(item, infoProducto.getPrecio());
			detEntity.setProducto(infoProducto);
			detEntity.setPedido(nuevoPedido);
			detallesEntity.add(detEntity);

			montoTotal += detEntity.getSubTotal();

			detallesResponse.add(detalleMapper.toResponse(detEntity));
		}

		nuevoPedido.setMontoTotal(montoTotal);
		nuevoPedido.setDetalles(detallesEntity);

		Pedido pedidoGuardado = pedidoRepo.save(nuevoPedido);

		return pedidoMapper.toResponse(pedidoGuardado, detallesResponse);
	}

	@Override
	public List<PedidoResponse> listarPedidos() {
	      List<Pedido> pedidos = pedidoRepo.findAll();
	        List<PedidoResponse> responseList = new ArrayList<>();

	        for (Pedido pedido : pedidos) {
	        	
	            List<DetallePedidoResponse> detallesResponse = new ArrayList<>();
	            for (DetallePedido det : pedido.getDetalles()) {
	                detallesResponse.add(detalleMapper.toResponse(det));
	            }

	            responseList.add(pedidoMapper.toResponse(
	                    pedido,
	                    detallesResponse));
	        }
	        return responseList;
	}

	@Override
	public PedidoResponse buscarPedido(Long id) {
		Pedido pedido = pedidoRepo.findById(id).get();

		if (pedido == null) {
			throw new RuntimeException("Pedido no encontrado");
		}

		List<DetallePedidoResponse> detallesResponse = new ArrayList<>();
		for (DetallePedido det : pedido.getDetalles()) {
			detallesResponse.add(detalleMapper.toResponse(det));
		}
		return pedidoMapper.toResponse(pedido, detallesResponse);
	}

	@Override
	public PedidoResponse cambiarEstado(Long id, EstadoPedido nuevoEstado) {
	       Pedido pedido = pedidoRepo.findById(id).orElse(null);
	        if (pedido == null) {
	            throw new RuntimeException("Pedido no encontrado");
	        }
	        pedido.setEstado(nuevoEstado);
			List<DetallePedidoResponse> detallesResponse = new ArrayList<>();
			for (DetallePedido det : pedido.getDetalles()) {
				detallesResponse.add(detalleMapper.toResponse(det));
			}
	        var pedidoSave = pedidoRepo.save(pedido);
			return pedidoMapper.toResponse(pedidoSave, detallesResponse);
	}

	@Override
	public PedidoResponse registrarPagoFinal(Long id, MetodoPago pago) {
        Pedido pedido = pedidoRepo.findById(id).orElse(null);
        if (pedido == null) {
            throw new RuntimeException("Pedido no encontrado");
        }
        pedido.setMetodoPago(pago);
		List<DetallePedidoResponse> detallesResponse = new ArrayList<>();
		for (DetallePedido det : pedido.getDetalles()) {
			detallesResponse.add(detalleMapper.toResponse(det));
		}
        var pedidoSave = pedidoRepo.save(pedido);
		return pedidoMapper.toResponse(pedidoSave, detallesResponse);	}

	@Override
	public List<PedidoResponse> listarPorEstado(EstadoPedido estado) {
	      List<Pedido> pedidos = pedidoRepo.findByEstado(estado);
	        List<PedidoResponse> responseList = new ArrayList<>();

	        for (Pedido pedido : pedidos) {
	        	
	            List<DetallePedidoResponse> detallesResponse = new ArrayList<>();
	            for (DetallePedido det : pedido.getDetalles()) {
	                detallesResponse.add(detalleMapper.toResponse(det));
	            }

	            responseList.add(pedidoMapper.toResponse(
	                    pedido,
	                    detallesResponse));
	        }
	        return responseList;
	}

	@Override
	public List<PedidoResponse> listarPorRepartidor(Long idRepartidor) {
	      List<Pedido> pedidos = pedidoRepo.findByRepartidorId(idRepartidor);
	        List<PedidoResponse> responseList = new ArrayList<>();

	        for (Pedido pedido : pedidos) {
	        	
	            List<DetallePedidoResponse> detallesResponse = new ArrayList<>();
	            for (DetallePedido det : pedido.getDetalles()) {
	                detallesResponse.add(detalleMapper.toResponse(det));
	            }

	            responseList.add(pedidoMapper.toResponse(
	                    pedido,
	                    detallesResponse));
	        }
	        return responseList;
	}

	@Override
	public List<PedidoResponse> listarPorCliente(String telefono) {
	      List<Pedido> pedidos = pedidoRepo.findByClienteTelefono(telefono);
	        List<PedidoResponse> responseList = new ArrayList<>();

	        for (Pedido pedido : pedidos) {
	        	
	            List<DetallePedidoResponse> detallesResponse = new ArrayList<>();
	            for (DetallePedido det : pedido.getDetalles()) {
	                detallesResponse.add(detalleMapper.toResponse(det));
	            }

	            responseList.add(pedidoMapper.toResponse(
	                    pedido,
	                    detallesResponse));
	        }
	        return responseList;
	}

}
