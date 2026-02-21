package cibertec.pe;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PedidoMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public Pedido toEntity(PedidoRequest request) {
        Pedido pedido = new Pedido();

        pedido.setFechaRegistro(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setMetodoPago(MetodoPago.EFECTIVO);

        return pedido;
    }

    /**
     * @param entity           La entidad Pedido que queremos convertir a DTO
     * @param nombreCliente    Dato traído de Feign Cliente
     * @param direccion        Dato traído de Feign Cliente
     * @param nombreRepartidor Dato traído de Feign Repartidor
     * @param detalles         La lista de detalles ya convertidos a DTOs
     */
    public PedidoResponse toResponse(Pedido pedido, List<DetallePedidoResponse> detalles) {

        PedidoResponse dto = new PedidoResponse();

        dto.setCliente(pedido.getCliente().getNombre());
        dto.setDireccion(pedido.getCliente().getDireccion());
        dto.setRepartidor(pedido.getRepartidor().getNombre());

        dto.setEstado(pedido.getEstado().name());
        dto.setMetodoPago(pedido.getMetodoPago().name());
        dto.setTotal(pedido.getMontoTotal());

        if (pedido.getFechaRegistro() != null) {
            dto.setFecha(pedido.getFechaRegistro().format(FORMATTER));
        }

        if (detalles != null) {
            dto.setDetalles(detalles.toArray(new DetallePedidoResponse[0]));
        }

        return dto;
    }
}