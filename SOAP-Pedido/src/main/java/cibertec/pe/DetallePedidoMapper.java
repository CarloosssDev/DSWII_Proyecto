package cibertec.pe;

import org.springframework.stereotype.Component;

@Component
public class DetallePedidoMapper {
    public DetallePedido toEntity(DetallePedidoRequest request, Double precioReal) {
        DetallePedido entity = new DetallePedido();
        entity.setCantidad(request.getCantidad());
        entity.setPrecioUnitario(precioReal);
        entity.setSubTotal(precioReal * request.getCantidad());
        return entity;
    }

    public DetallePedidoResponse toResponse(DetallePedido dt) {
        DetallePedidoResponse dto = new DetallePedidoResponse();
        dto.setNombreProducto(dt.getProducto().getDescripcion());
        dto.setCantidad(dt.getCantidad());
        dto.setPrecioUnitario(dt.getPrecioUnitario());
        dto.setSubTotal(dt.getSubTotal());
        return dto;
    }
}